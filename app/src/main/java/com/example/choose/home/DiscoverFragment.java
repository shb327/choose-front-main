package com.example.choose.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.CommunityController;
import com.example.choose.community.CommunityDisplay;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.recyclers.ClickListener;
import com.example.choose.recyclers.DiscoverCommunityAdapter;
import com.example.choose.retrofit.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFragment extends Fragment {

    public DiscoverFragment() { }

    private DiscoverCommunityAdapter adapter;

    private RecyclerView recyclerView;
    int visibleItemCount;
    int totalItemCount;
    private boolean loading;
    int pastVisibleItems;
    int page;
    CommunityController communityController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DiscoverCommunityAdapter(new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                Intent i = new Intent(getContext(), CommunityDisplay.class);
                i.putExtra("from", "DiscoverFragment");
                i.putExtra("community", adapter.localDataSet.get(position));
                startActivity(i);
            }

            @Override
            public void onLongClicked(int position) {

            }
        });
        page = 0;
        loading = true;
        communityController = RetrofitUtils
                .getInstance()
                .getRetrofit()
                .create(CommunityController.class);
        communityController
                .getDiscoverCommunity(page++, 10)
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
        View inflate = inflater.inflate(R.layout.fragment_discover, container, false);
        recyclerView = inflate.findViewById(R.id.discover_recycle_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);
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
                            communityController
                                    .getDiscoverCommunity(page++, 10)
                                    .enqueue(new Callback<List<CommunityDTO>>() {
                                        @Override
                                        public void onResponse(Call<List<CommunityDTO>> call, Response<List<CommunityDTO>> response) {
                                            if (response.isSuccessful()) {
                                                adapter.localDataSet.addAll(response.body());
                                                adapter.notifyDataSetChanged();
                                                loading = true;
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
                    }
                }
            }
        });
        return inflate;
    }
}