package com.example.choose.post;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.PetitionPostDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.retrofit.RetrofitUtils;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetitionPostDisplayFragment extends Fragment {
    Integer id;
    String description;
    String url;
    Integer goal;
    Integer votedCount;

    public PetitionPostDisplayFragment() { }

    public PetitionPostDisplayFragment(Integer id, String description, String url) {
        this.id = id;
        this.description = description;
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        goal = 0;
        votedCount = 0;
        View inflate = inflater.inflate(R.layout.fragment_petition_post_display, container, false);
        TextView desc = inflate.findViewById(R.id.desc);
        TextView signs = inflate.findViewById(R.id.counter);
        ImageView imageView = inflate.findViewById(R.id.image);
        LinearProgressIndicator bar = inflate.findViewById(R.id.bar);
        desc.setText(description);
        if(url != null)
            new DownloadImageTask(imageView).execute(url);
        else
            imageView.setVisibility(View.GONE);

        PostController postController = RetrofitUtils
                .getInstance()
                .getRetrofit()
                .create(PostController.class);
        postController
                .getPost(id).enqueue(new Callback<PostDTO>() {
                @Override
                public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                    setGoal(((PetitionPostDTO) response.body()).getGoal());
                    setVotedCount(((PetitionPostDTO) response.body()).getVotedCount());
                    if (goal == null){
                        if(votedCount > 10){
                            signs.setText("Gathered " + votedCount + " signs out of 100");
                            bar.setProgress(calculate(votedCount, 100));
                        }else if(votedCount > 100){
                            signs.setText("Gathered " + votedCount + " signs out of 1000");
                            bar.setProgress(calculate(votedCount, 1000));
                        }else{
                            signs.setText("Gathered " + votedCount + " signs out of 10");
                            bar.setProgress(calculate(votedCount, 10));
                        }
                    }else{
                        if(votedCount > goal){
                            int res = votedCount - goal;
                            signs.setText("Gathered " + votedCount + " signs, " + res + " behind the goal.");
                            bar.setProgress(100);
                        }else if(votedCount == goal){
                            signs.setText("Goal of " + goal + " signs is reached!");
                            bar.setProgress(100);
                        }else{
                            signs.setText("Gathered " + votedCount + " signs out of " + goal);
                            bar.setProgress(calculate(votedCount, goal));
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostDTO> call, Throwable t) {
                    Log.e("getPost", t.getMessage(), t);
                }
        });
        return inflate;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public void setVotedCount(Integer votedCount) {
        this.votedCount = votedCount;
    }

    public int calculate(int votes, int goal){
        return (votes * 100)/goal;
    }
}