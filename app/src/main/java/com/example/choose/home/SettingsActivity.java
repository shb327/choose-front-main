package com.example.choose.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.choose.R;
import com.example.choose.api.CommunityController;
import com.example.choose.community.CommunityDisplay;
import com.example.choose.community.EditCommunityActivity;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.recyclers.ClickListener;
import com.example.choose.recyclers.DeleteCommunityAdapter;
import com.example.choose.recyclers.EditCommunityAdapter;
import com.example.choose.retrofit.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private final DeleteCommunityAdapter deleteAdapter = new DeleteCommunityAdapter(new ClickListener() {
        @Override
        public void onPositionClicked(int position) { }

        @Override
        public void onLongClicked(int position) { }
    });

    private final EditCommunityAdapter editAdapter = new EditCommunityAdapter(new ClickListener() {
        @Override
        public void onPositionClicked(int position) {
            Intent i = new Intent(SettingsActivity.this, EditCommunityActivity.class);
            i.putExtra("community", editAdapter.localDataSet.get(position));
            startActivity(i);
        }

        @Override
        public void onLongClicked(int position) { }
    });

    private RecyclerView recyclerView;
    CommunityController communityController;

    public SettingsActivity() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        communityController = RetrofitUtils.getInstance().getRetrofit().create(CommunityController.class);
        Bundle extras = getIntent().getExtras();
        String from = extras.getString("from");
        TextView textView = findViewById(R.id.action);
        recyclerView = findViewById(R.id.settings_recycle_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(SettingsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        if(from.equals("Delete")){
            communityController
                    .getUserOwning()
                    .enqueue(new Callback<List<CommunityDTO>>() {
                        @Override
                        public void onResponse(Call<List<CommunityDTO>> call, Response<List<CommunityDTO>> response) {
                            if (response.isSuccessful()) {
                                deleteAdapter.localDataSet.addAll(response.body());
                                deleteAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("community", String.valueOf(response.code()));
                                Log.e("community", response.raw().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CommunityDTO>> call, Throwable t) {
                            Log.e("community", t.getMessage(), t);
                        }
                    });
            recyclerView.setAdapter(deleteAdapter);
        }else{
            communityController
                    .getUserOwning()
                    .enqueue(new Callback<List<CommunityDTO>>() {
                        @Override
                        public void onResponse(Call<List<CommunityDTO>> call, Response<List<CommunityDTO>> response) {
                            if (response.isSuccessful()) {
                                editAdapter.localDataSet.addAll(response.body());
                                editAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("community", String.valueOf(response.code()));
                                Log.e("community", response.raw().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CommunityDTO>> call, Throwable t) {
                            Log.e("community", t.getMessage(), t);
                        }
                    });
            recyclerView.setAdapter(editAdapter);
            textView.setText("Edit Community");
        }

        communityController
                .getUserOwning()
                .enqueue(new Callback<List<CommunityDTO>>() {
                    @Override
                    public void onResponse(Call<List<CommunityDTO>> call, Response<List<CommunityDTO>> response) {
                        if (response.isSuccessful()) {
                            deleteAdapter.localDataSet.clear();
                            deleteAdapter.localDataSet.addAll(response.body());
                            deleteAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("community", String.valueOf(response.code()));
                            Log.e("community", response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CommunityDTO>> call, Throwable t) {
                        Log.e("community", t.getMessage(), t);
                    }
                });
        Button button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this, HomeActivity.class);
                i.putExtra("fragment", 3);
                startActivity(i);
            }
        });
    }
}