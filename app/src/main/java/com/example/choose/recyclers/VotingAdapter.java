package com.example.choose.recyclers;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.VotingOptionDTO;
import com.example.choose.dto.VotingPostDTO;
import com.example.choose.retrofit.RetrofitUtils;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VotingAdapter extends RecyclerView.Adapter<VotingAdapter.ViewHolder>{
    public List<VotingOptionDTO> localDataSet = new ArrayList<>();
    public int overall;
    PostController postController;
    int postId;
    boolean voteTaken = false;
    private final ClickListener listener;
    Activity activity;

    public VotingAdapter(Activity activity, int postId, Supplier<VotingPostDTO> supplier) {
        this.activity = activity;
        this.listener = new ClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPositionClicked(int position) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        VotingPostDTO postDTO = supplier.get();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                localDataSet.clear();
                                overall = 0;
                                for (VotingOptionDTO votingOptionDTO: postDTO.getOptions())
                                    overall += votingOptionDTO.getVotedUsers();
                                localDataSet.addAll(postDTO.getOptions());
                                notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onLongClicked(int position) {

            }
        };
        this.postId = postId;
    }

    public List<VotingOptionDTO> getLocalDataSet() {
        return localDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.voting_row, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        VotingOptionDTO option = localDataSet.get(position);
        if(!option.getVoted()) viewHolder.getCircle().setVisibility(View.GONE);
        else voteTaken = true;
        String votedData = calculate(option.getVotedUsers(), overall) + " %";
        viewHolder.getNumber().setText(votedData);
        viewHolder.getBar().setProgress(calculate(option.getVotedUsers(), overall) + 1);
        viewHolder.getName().setText(option.getTitle());
        viewHolder.setId(option.getId().intValue());
        viewHolder.setPostId(postId);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private WeakReference<ClickListener> listenerRef;
        int postId;
        int id;
        private TextView number;
        private TextView name;
        private ImageView circle;
        private LinearProgressIndicator bar;

        public ViewHolder(View view, ClickListener listener) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            number = view.findViewById(R.id.percent);
            name = view.findViewById(R.id.name);
            circle = view.findViewById(R.id.circle);
            bar = view.findViewById(R.id.bar);
            view.setOnClickListener(this);
        }

        public TextView getNumber() {
            return number;
        }

        public TextView getName() {
            return name;
        }

        public ImageView getCircle() {
            return circle;
        }

        public LinearProgressIndicator getBar() {
            return bar;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }

        @Override
        public void onClick(View v) {
            if(!voteTaken){
                postController = RetrofitUtils
                        .getInstance()
                        .getRetrofit()
                        .create(PostController.class);
                postController
                        .voteForPost(postId, id)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                circle.setVisibility(View.VISIBLE);
                                Log.i("Vote: " + id, response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Vote Error", t.getMessage(), t);
                            }
                        });
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }
    }

    public int calculate(int votes, int goal){
        if (goal==0) return 0;
        return (votes * 100)/goal;
    }
}
