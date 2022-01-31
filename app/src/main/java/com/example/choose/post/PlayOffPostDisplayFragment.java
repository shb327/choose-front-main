package com.example.choose.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.community.CommunityDisplay;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.dto.PetitionPostDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.home.HomeActivity;
import com.example.choose.play.PlayOffPlayActivity;
import com.example.choose.play.PlayOffStatsActivity;
import com.example.choose.retrofit.RetrofitUtils;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayOffPostDisplayFragment extends Fragment {
    Integer id;
    String from;
    CommunityDTO community;

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