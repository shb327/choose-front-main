package com.example.choose.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.dto.GetCommunityFeedRequestDTO;
import com.example.choose.dto.GetCommunityFeedResponseDTO;
import com.example.choose.home.HomeActivity;
import com.example.choose.post.PostDisplay;
import com.example.choose.recyclers.ClickListener;
import com.example.choose.recyclers.FeedAdapter;
import com.example.choose.retrofit.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityDisplay extends AppCompatActivity {
    PostController postController;
    CommunityDTO community;
    Bundle extras;
    String from;

    FeedAdapter adapter = new FeedAdapter(new ClickListener() {
        @Override
        public void onPositionClicked(int position) {
            Intent i = new Intent(CommunityDisplay.this, PostDisplay.class);
            i.putExtra("post", adapter.localDataSet.get(position));
            if(from.equals("CommunitiesFragment")){ i.putExtra("from", "CommunityDisplayCF"); }
            else i.putExtra("from", "CommunityDisplay");
            i.putExtra("community", community);
            startActivity(i);
        }

        @Override
        public void onLongClicked(int position) {
            //ToDo: Options
        }
    }, this);
    private RecyclerView recyclerView;

    int page;
    String step;
    private boolean loading;
    int visibleItemCount;
    int totalItemCount;
    int pastVisibleItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_display);
        Button button = findViewById(R.id.sendBtn);
        extras = getIntent().getExtras();
        from = extras.getString("from");
        community = (CommunityDTO) extras.getSerializable("community");

        TextView name = findViewById(R.id.name);
        TextView username = findViewById(R.id.username);
        TextView description = findViewById(R.id.description);
        recyclerView = findViewById(R.id.communities_recycle_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        name.setText(community.getName());
        username.setText(community.getUsername());
        description.setText(community.getDescription());

        postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
        loading = true;
        page = 0;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        postController
                .getCommunityFeed(new GetCommunityFeedRequestDTO(page++, 10, community.getId().intValue()))
                .enqueue(new Callback<GetCommunityFeedResponseDTO>() {
                    @Override
                    public void onResponse(Call<GetCommunityFeedResponseDTO> call, Response<GetCommunityFeedResponseDTO> response) {
                        if (response.isSuccessful()) {
                            adapter.localDataSet.addAll(response.body().getPosts());
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("post", String.valueOf(response.code()));
                            Log.e("post", response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCommunityFeedResponseDTO> call, Throwable t) {
                        Log.e("post", t.getMessage(), t);
                    }
                });


        System.out.println(from);
        button.setOnClickListener(v -> {
            Intent i = new Intent(CommunityDisplay.this, HomeActivity.class);
            if(from.equals("CommunitiesFragment")) {
                i.putExtra("fragment", 2);
            }else {
                i.putExtra("fragment", 1);
            }
            startActivity(i);
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
                                    .getCommunityFeed(new GetCommunityFeedRequestDTO(page++,10, community.getId().intValue()))
                                    .enqueue(new Callback<GetCommunityFeedResponseDTO>() {
                                                 @Override
                                                 public void onResponse(Call<GetCommunityFeedResponseDTO> call, Response<GetCommunityFeedResponseDTO> response) {
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
                                                 public void onFailure(Call<GetCommunityFeedResponseDTO> call, Throwable t) {
                                                     Log.e("post", t.getMessage(), t);
                                                 }
                                             }
                                    );
                        }
                    }
                }
            }
        });

    }
}