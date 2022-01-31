package com.example.choose.play;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.community.CommunityDisplay;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.dto.PlayOffPostDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.home.HomeActivity;
import com.example.choose.post.PostDisplay;
import com.example.choose.recyclers.ClickListener;
import com.example.choose.recyclers.PlayOffAdapter;
import com.example.choose.retrofit.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayOffStatsActivity extends AppCompatActivity {
    Integer id;
    String from;
    PostDTO post;
    private RecyclerView recyclerView;
    PostController postController;
    PlayOffAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_off_stats);

        Bundle extras = getIntent().getExtras();
        adapter = new PlayOffAdapter(new ClickListener() {
            @Override
            public void onPositionClicked(int position) { }

            @Override
            public void onLongClicked(int position) { }
        });
        Button button = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.stats_recycle_view);
        id = extras.getInt("id");
        from = extras.getString("from");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        postController = RetrofitUtils
                .getInstance()
                .getRetrofit()
                .create(PostController.class);
        postController.getPost(id).enqueue(new Callback<PostDTO>() {
            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                post = response.body();
                adapter.localDataSet.addAll(((PlayOffPostDTO) response.body()).getOptions());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {
                Log.e("getPost", t.getMessage(), t);
            }
        });

        button.setOnClickListener(v -> {
            if(from.equals("CommunityDisplay")) {
                Intent i = new Intent(this, PostDisplay.class);
                i.putExtra("from", "CommunityDisplay");
                CommunityDTO communityDTO = (CommunityDTO) extras.getSerializable("community");
                i.putExtra("community", communityDTO);
                i.putExtra("post", post);
                startActivity(i);
            } else if(from.equals("CommunityDisplayCF")) {
                Intent i = new Intent(this, PostDisplay.class);
                i.putExtra("from", "CommunityDisplayCF");
                CommunityDTO communityDTO = (CommunityDTO) extras.getSerializable("community");
                System.out.println(communityDTO);
                i.putExtra("community", communityDTO);
                i.putExtra("post", post);
                startActivity(i);
            }else if(from.equals("Feed")) {
                Intent i = new Intent(this, PostDisplay.class);
                i.putExtra("from", "Feed");
                i.putExtra("post", post);
                startActivity(i);
            } else {
                Intent i = new Intent(this, PostDisplay.class);
                i.putExtra("from", "HomeActivity");
                i.putExtra("post", post);
                startActivity(i);
            }
        });
    }
}