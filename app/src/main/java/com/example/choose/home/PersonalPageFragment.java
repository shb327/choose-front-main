package com.example.choose.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.choose.recyclers.PostAdapter;
import com.example.choose.retrofit.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalPageFragment extends Fragment {
    private TextView welcome;
    private RecyclerView recyclerView;
    PostController postController;

    int visibleItemCount;
    int totalItemCount;
    int pastVisibleItems;
    int page;
    private boolean loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
        loading = true;
        page = 0;
        postController
                .getSelfFeed(new GetFeedRequestDTO(page++,10))
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
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView = view.findViewById(R.id.content_recycle_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        Button manage = view.findViewById(R.id.manage);
        Button create = view.findViewById(R.id.create);

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SettingsActivity.class);
                i.putExtra("from", "Edit");
                startActivity(i);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateCommunityActivity.class));
            }
        });

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
                                    .getSelfFeed(new GetFeedRequestDTO(page++,10))
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welcome = view.findViewById(R.id.welcome);
        String name = RetrofitUtils.getInstance().getUsername();
        if(name.contains("@"))
            for (int i = 0; i < name.length(); i++)
                if (name.charAt(i) == '@') name = name.substring(0,i);
        welcome.setText("Welcome on Board " + name + "!");
    }
    private final PostAdapter adapter = new PostAdapter(new ClickListener() {
        @Override
        public void onPositionClicked(int position) {
            Intent i = new Intent(getContext(), PostDisplay.class);
            i.putExtra("post", adapter.localDataSet.get(position));
            i.putExtra("from", "PersonalPageFragment");
            startActivity(i);
        }

        @Override
        public void onLongClicked(int position) { }
    });
}