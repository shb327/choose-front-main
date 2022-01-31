package com.example.choose.home;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.community.CreateCommunityActivity;
import com.example.choose.dto.GetFeedRequestDTO;
import com.example.choose.dto.GetFeedResponseDTO;
import com.example.choose.post.PostDisplay;
import com.example.choose.recyclers.ClickListener;
import com.example.choose.recyclers.FeedAdapter;
import com.example.choose.retrofit.RetrofitUtils;
import com.example.choose.start.StartingActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    public FeedFragment() { }

    private FeedAdapter adapter;

    private RecyclerView recyclerView;
    PostController postController;
    private DrawerLayout drawer;

    int visibleItemCount;
    int totalItemCount;
    int pastVisibleItems;

    int page;

    private boolean loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FeedAdapter(new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                Intent i = new Intent(getContext(), PostDisplay.class);
                i.putExtra("post", adapter.localDataSet.get(position));
                i.putExtra("from", "Feed");
                startActivity(i);
            }

            @Override
            public void onLongClicked(int position) { }
        }, this.getActivity());
        postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
        loading = true;
        page = 0;
        postController
                .getFeed(new GetFeedRequestDTO(page++,10))
                .enqueue(new Callback<GetFeedResponseDTO>() {
                    @Override
                    public void onResponse(Call<GetFeedResponseDTO> call, Response<GetFeedResponseDTO> response) {
                        if (response.isSuccessful()) {
                            adapter.localDataSet.clear();
                            adapter.localDataSet.addAll(response.body().getPosts());
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("post", String.valueOf(response.code()));
                            Log.e("post", response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetFeedResponseDTO> call, Throwable t) {
                        Log.e("post", t.getMessage(), t);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = inflate.findViewById(R.id.feed_recycle_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        MaterialToolbar toolbar = inflate.findViewById(R.id.topAppBar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(inflate.getContext(), R.style.MontserratBoldTextAppearance);

        drawer = inflate.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.main_green));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = inflate.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.username);
        String accountName = RetrofitUtils.getInstance().getUsername();
        if(accountName.contains("@"))
            for (int i = 0; i < accountName.length(); i++)
                if (accountName.charAt(i) == '@') accountName = accountName.substring(0,i);
        name.setText(accountName);
        setNavigationViewListener(inflate);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false;
                            postController
                                    .getFeed(new GetFeedRequestDTO(page++,10))
                                    .enqueue(new Callback<GetFeedResponseDTO>() {
                                                 @Override
                                                 public void onResponse(Call<GetFeedResponseDTO> call, Response<GetFeedResponseDTO> response) {
                                                     if (response.isSuccessful()) {
                                                         adapter.localDataSet.addAll(response.body().getPosts());
                                                         adapter.notifyDataSetChanged();
                                                         loading = true;
                                                     } else {
                                                         Log.e("post", String.valueOf(response.code()));
                                                         Log.e("post", response.raw().toString());
                                                     }
                                                 }

                                                 @Override
                                                 public void onFailure(Call<GetFeedResponseDTO> call, Throwable t) {
                                                     Log.e("post", t.getMessage(), t);
                                                 }
                                             }
                                    );
                        }
                    }
                }
            }
        });
        return inflate;
    }

    private void setNavigationViewListener(View view) {
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i = new Intent(getContext(), SettingsActivity.class);
        switch (item.getItemId()) {
            case R.id.delete:
                i.putExtra("from", "Delete");
                startActivity(i);
                break;
            case  R.id.communities:
                i.putExtra("from", "Edit");
                startActivity(i);
                break;
            case R.id.create:
                Intent intent = new Intent(getContext(), CreateCommunityActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                Intent logoutIntent = new Intent(getContext(), StartingActivity.class);
                RetrofitUtils.getInstance().deleteAccountManager(AccountManager.get(getContext()));
                startActivity(logoutIntent);
                break;
        }
        return true;
    }
}