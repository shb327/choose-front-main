package com.example.choose.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.CommunityController;
import com.example.choose.community.CommunityDisplay;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.recyclers.ClickListener;
import com.example.choose.recyclers.UserCommunityAdapter;
import com.example.choose.retrofit.RetrofitUtils;

import java.util.List;

public class CommunitiesFragment extends Fragment {

    private final UserCommunityAdapter adapter = new UserCommunityAdapter(new ClickListener() {
        @Override
        public void onPositionClicked(int position) {
            Intent i = new Intent(getContext(), CommunityDisplay.class);
            i.putExtra("from", "CommunitiesFragment");
            i.putExtra("community", adapter.localDataSet.get(position));
            startActivity(i);
        }

        @Override
        public void onLongClicked(int position) {
            //TODO:Options
        }
    });

    private RecyclerView recyclerView;
    CommunityController communityController;

    public CommunitiesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communityController = RetrofitUtils
                .getInstance()
                .getRetrofit()
                .create(CommunityController.class);
        communityController
                .getUserCommunities()
                .enqueue(new Callback<List<CommunityDTO>>() {
                @Override
                public void onResponse(Call<List<CommunityDTO>> call, Response<List<CommunityDTO>> response) {
                    if (response.isSuccessful()) {
                        adapter.localDataSet.clear();
                        adapter.localDataSet.addAll(response.body());
                        adapter.notifyDataSetChanged();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communities, container, false);
        recyclerView = view.findViewById(R.id.communities_recycle_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}