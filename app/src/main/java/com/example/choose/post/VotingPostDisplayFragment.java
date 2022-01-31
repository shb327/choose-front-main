package com.example.choose.post;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.PetitionPostDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.dto.VotingOptionDTO;
import com.example.choose.dto.VotingPostDTO;
import com.example.choose.recyclers.ClickListener;
import com.example.choose.recyclers.VotingAdapter;
import com.example.choose.retrofit.RetrofitUtils;

import java.io.IOException;
import java.util.function.Supplier;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VotingPostDisplayFragment extends Fragment {
    Integer id;
    private RecyclerView recyclerView;
    PostController postController;

    public VotingPostDisplayFragment() { }

    public VotingPostDisplayFragment(Integer id) {
        this.id = id;
    }

    private VotingAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_voting_post_display, container, false);
        postController = RetrofitUtils
                .getInstance()
                .getRetrofit()
                .create(PostController.class);
        adapter = new VotingAdapter(this.getActivity(), id, new Supplier<VotingPostDTO>() {
            @Override
            public VotingPostDTO get() {
                postController = RetrofitUtils
                        .getInstance()
                        .getRetrofit()
                        .create(PostController.class);
                try {
                    return ((VotingPostDTO) postController.getPost(id).execute().body());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        recyclerView = inflate.findViewById(R.id.options_recycle_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        postController.getPost(id).enqueue(new Callback<PostDTO>() {
            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                for (VotingOptionDTO votingOptionDTO: (((VotingPostDTO) response.body()).getOptions()))
                    adapter.overall += votingOptionDTO.getVotedUsers();
                adapter.localDataSet.addAll(((VotingPostDTO) response.body()).getOptions());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {
                Log.e("getPost", t.getMessage(), t);
            }
        });
        return inflate;
    }

    public void refresh(int id){
        postController
                .getPost(id).enqueue(new Callback<PostDTO>() {
            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                adapter.localDataSet.clear();
                adapter.overall = 0;
                for (VotingOptionDTO votingOptionDTO: (((VotingPostDTO) response.body()).getOptions()))
                    adapter.overall += votingOptionDTO.getVotedUsers();
                adapter.localDataSet.addAll(((VotingPostDTO) response.body()).getOptions());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {
                Log.e("getPost", t.getMessage(), t);
            }
        });
    }
}
