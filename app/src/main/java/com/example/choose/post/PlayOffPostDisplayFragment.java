package com.example.choose.post;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.community.CommunityDisplay;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.dto.PetitionPostDTO;
import com.example.choose.dto.PlayOffOptionDTO;
import com.example.choose.dto.PlayOffPostDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.home.HomeActivity;
import com.example.choose.play.PlayOffPlayActivity;
import com.example.choose.play.PlayOffStatsActivity;
import com.example.choose.play.WinnerActivity;
import com.example.choose.retrofit.RetrofitUtils;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.shb327.playoff.Match;
import com.shb327.playoff.Participant;
import com.shb327.playoff.Tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayOffPostDisplayFragment extends Fragment {
    Integer id;
    String from;
    CommunityDTO community;
    PostDTO post;
    PostController postController;

    public PlayOffPostDisplayFragment() { }

    public PlayOffPostDisplayFragment(Integer id, String from) {
        this.id = id;
        this.from = from;
        community = null;
    }

    public PlayOffPostDisplayFragment(Integer id, String from, CommunityDTO community) {
        this.id = id;
        this.from = from;
        this.community = community;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_play_off_post_display, container, false);
        Button stats = inflate.findViewById(R.id.stats);
        Button play = inflate.findViewById(R.id.play);
        postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);

        postController.getPost(id).enqueue(new Callback<PostDTO>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                post = response.body();
                List<PlayOffOptionDTO> options = new ArrayList<>(((PlayOffPostDTO) post).getOptions());
                for (PlayOffOptionDTO option: options) {
                    if(option.getVoted()){
                        play.setClickable(false);
                        play.setText("Played");
                        play.setBackgroundColor(Color.parseColor("#329D9C"));
                        play.setTextColor(Color.WHITE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {
                Log.e("getPost", t.getMessage(), t);
            }
        });

        stats.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), PlayOffStatsActivity.class);
            i.putExtra("id", id);
            i.putExtra("from", from);
            if(community != null) i.putExtra("community", community);
            startActivity(i);
        });

        play.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), PlayOffPlayActivity.class);
            i.putExtra("id", id);
            i.putExtra("from", from);
            if(community != null) i.putExtra("community", community);
            startActivity(i);
        });
        return inflate;
    }
}