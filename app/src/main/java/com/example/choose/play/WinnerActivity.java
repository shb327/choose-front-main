package com.example.choose.play;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.choose.R;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.home.HomeActivity;
import com.example.choose.post.DownloadImageTask;
import com.example.choose.post.PostDisplay;

public class WinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winner_activity);

        Bundle extras = getIntent().getExtras();
        Button button = findViewById(R.id.back);

        TextView data = findViewById(R.id.data);
        ImageView imageWinner = findViewById(R.id.imageWinner);
        TextView stats = findViewById(R.id.stats);
        data.setText(extras.getString("name") + " is the Winner!");

        System.out.println(extras.getInt("id"));
        new DownloadImageTask(imageWinner).execute(extras.getString("url"));

        button.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        });

        stats.setOnClickListener(v -> {
            Intent i = new Intent(WinnerActivity.this, PlayOffStatsActivity.class);
            i.putExtra("id", extras.getInt("id"));
            i.putExtra("from", "CLose");
            startActivity(i);
        });
    }
}