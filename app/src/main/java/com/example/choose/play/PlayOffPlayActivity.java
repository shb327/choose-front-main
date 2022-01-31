package com.example.choose.play;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.dto.PlayOffOptionDTO;
import com.example.choose.dto.PlayOffPostDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.post.DownloadImageTask;
import com.example.choose.post.PostDisplay;
import com.example.choose.retrofit.RetrofitUtils;
import com.shb327.playoff.Match;
import com.shb327.playoff.Participant;
import com.shb327.playoff.Tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayOffPlayActivity extends AppCompatActivity {
    Integer id;
    String from;
    PostDTO post;
    TextView name;
    TextView data;
    TextView optionOneName;
    TextView optionTwoName;
    ImageView optionOneImage;
    ImageView optionTwoImage;
    ImageView up;
    ImageView down;
    PostController postController;
    AtomicInteger optionsCounter;
    AtomicInteger eliminationsCounter;
    AtomicReference<Match<PlayOffOptionDTO>> reference = new AtomicReference<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_off_play);
        Bundle extras = getIntent().getExtras();

        name = findViewById(R.id.name);
        Button button = findViewById(R.id.back);
        data = findViewById(R.id.data);
        optionOneName = findViewById(R.id.textOptionOne);
        optionTwoName = findViewById(R.id.textOptionTwo);
        optionOneImage = findViewById(R.id.imageOptionOne);
        optionTwoImage = findViewById(R.id.imageOptionTwo);
        up = findViewById(R.id.up);
        down = findViewById(R.id.down);
        id = extras.getInt("id");
        from = extras.getString("from");

        eliminationsCounter = new AtomicInteger(0);
        optionsCounter = new AtomicInteger(0);

        postController = RetrofitUtils
                .getInstance()
                .getRetrofit()
                .create(PostController.class);

        postController.getPost(id).enqueue(new Callback<PostDTO>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                post = response.body();
                List<PlayOffOptionDTO> start = new ArrayList<>();
                start.addAll(((PlayOffPostDTO) post).getOptions());

                eliminationsCounter = new AtomicInteger(0);
                optionsCounter.addAndGet(start.size());

                Tournament<PlayOffOptionDTO> tournament = new Tournament<>(start.stream()
                        .map(Participant::new)
                        .collect(Collectors.toList()));

                Match<PlayOffOptionDTO> match = tournament.nextMatch();
                nextMatch(match);
                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Match<PlayOffOptionDTO> playMatch = reference.get();
                        playMatch.win(true);
                        if (tournament.hasWinner()){
                            Intent i = new Intent(PlayOffPlayActivity.this, WinnerActivity.class);
                            i.putExtra("url", tournament.getWinner().getValue().getMedia());
                            i.putExtra("name", tournament.getWinner().getValue().getTitle());
                            i.putExtra("id", id);
                            startActivity(i);
                            return;
                        }
                        nextMatch(tournament.nextMatch());
                    }
                });

                optionOneImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Match<PlayOffOptionDTO> playMatch = reference.get();
                        playMatch.win(true);
                        if (tournament.hasWinner()){
                            Intent i = new Intent(PlayOffPlayActivity.this, WinnerActivity.class);
                            i.putExtra("url", tournament.getWinner().getValue().getMedia());
                            i.putExtra("name", tournament.getWinner().getValue().getTitle());
                            i.putExtra("id", id);
                            startActivity(i);
                            return;
                        }
                        nextMatch(tournament.nextMatch());
                    }
                });

                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Match<PlayOffOptionDTO> playMatch = reference.get();
                        playMatch.win(false);
                        if (tournament.hasWinner()){
                            Intent i = new Intent(PlayOffPlayActivity.this, WinnerActivity.class);
                            i.putExtra("url", tournament.getWinner().getValue().getMedia());
                            i.putExtra("name", tournament.getWinner().getValue().getTitle());
                            i.putExtra("id", id);
                            startActivity(i);
                            return;
                        }
                        nextMatch(tournament.nextMatch());
                    }
                });

                optionTwoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Match<PlayOffOptionDTO> playMatch = reference.get();
                        playMatch.win(false);
                        if (tournament.hasWinner()){
                            Intent i = new Intent(PlayOffPlayActivity.this, WinnerActivity.class);
                            i.putExtra("url", tournament.getWinner().getValue().getMedia());
                            i.putExtra("name", tournament.getWinner().getValue().getTitle());
                            i.putExtra("id", id);
                            startActivity(i);
                            return;
                        }
                        nextMatch(tournament.nextMatch());
                    }
                });
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
            }else if(from.equals("CommunityDisplayCF")) {
                Intent i = new Intent(this, PostDisplay.class);
                i.putExtra("from", "CommunityDisplayCF");
                CommunityDTO communityDTO = (CommunityDTO) extras.getSerializable("community");
                i.putExtra("community", communityDTO);
                i.putExtra("post", post);
                startActivity(i);
            }else if(from.equals("Feed")) {
                Intent i = new Intent(this, PostDisplay.class);
                i.putExtra("from", "Feed");
                i.putExtra("post", post);
                startActivity(i);
            }else {
                Intent i = new Intent(this, PostDisplay.class);
                i.putExtra("from", "HomeActivity");
                i.putExtra("post", post);
                startActivity(i);
            }
        });
    }

    public void nextMatch(Match<PlayOffOptionDTO> match){
        optionOneName.setText(match.getFirst().getValue().getTitle());
        optionTwoName.setText(match.getSecond().getValue().getTitle());
        new DownloadImageTask(optionOneImage).execute(match.getFirst().getValue().getMedia());
        new DownloadImageTask(optionTwoImage).execute(match.getSecond().getValue().getMedia());
        if(optionsCounter.intValue() > 128){
            name.setText("Play-Off   1/128");
        }else if(optionsCounter.intValue() > 64){
            name.setText("Play-Off   1/64");
        }else if(optionsCounter.intValue() > 32){
            name.setText("Play-Off   1/32");
        }else if(optionsCounter.intValue() > 16){
            name.setText("Play-Off   1/16");
        }else if(optionsCounter.intValue() > 8){
            name.setText("Play-Off   1/8");
        }else if(optionsCounter.intValue() > 4){
            name.setText("Play-Off   1/4");
        }else if(optionsCounter.intValue() > 2){
            name.setText("Play-Off   1/2");
        }else{
            name.setText("Play-Off   Final");
        }
        data.setText("Eliminated: " + eliminationsCounter.getAndAdd(1) + "; Left: " + optionsCounter.getAndAdd(-1));
        reference.set(match);
    }
}