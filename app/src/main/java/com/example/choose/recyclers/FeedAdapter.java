package com.example.choose.recyclers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.ImagePostDTO;
import com.example.choose.dto.LikeStatus;
import com.example.choose.dto.PetitionPostDTO;
import com.example.choose.dto.PlayOffOptionDTO;
import com.example.choose.dto.PlayOffPostDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.dto.PostType;
import com.example.choose.dto.TextPostDTO;
import com.example.choose.dto.VotingOptionDTO;
import com.example.choose.dto.VotingPostDTO;
import com.example.choose.play.PlayOffPlayActivity;
import com.example.choose.post.DownloadImageTask;
import com.example.choose.retrofit.RetrofitUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public List<PostDTO> localDataSet = new ArrayList<>();
    private final ClickListener listener;
    PostController postController;
    Activity activity;

    public FeedAdapter(ClickListener listener, Activity activity) {
        this.listener = listener;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case 1:
                View textView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.text_post_feed_row_item, parent, false);
                return new TextPostViewHolder(textView, listener);
            case 2:
                View imageView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.image_post_feed_row_item, parent, false);
                return new ImagePostViewHolder(imageView, listener);
            case 3:
                View petitionView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.petition_post_feed_row_item, parent, false);
                return new PetitionPostViewHolder(petitionView, listener);
            case 4:
                View votingView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.voting_post_feed_row_item, parent, false);
                return new VotingPostViewHolder(votingView, listener);
            case 5:
                View playOffView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.play_off_post_feed_row_item, parent, false);
                return new PlayOffPostViewHolder(playOffView, listener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        PostDTO post = localDataSet.get(position);
        PostType type = post.getType();
        switch (type){
            default:
            case TEXT:
                return 1;
            case IMAGE:
                return 2;
            case PETITION:
                return 3;
            case VOTE:
                return 4;
            case PLAYOFF:
                return 5;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        PostDTO post = localDataSet.get(position);
        switch (holder.getItemViewType()) {
            default:
            case 1:
                TextPostViewHolder textPostViewHolder = ((TextPostViewHolder) holder);
                textPostViewHolder.getTitle().setText(post.getTitle());
                textPostViewHolder.getAuthor().setText("by " + post.getAuthorUsername());
                textPostViewHolder.getLikeCount().setText(String.valueOf(post.getLikesCount().intValue()));
                if(post.getLikeStatus() != null){
                    if(post.getLikeStatus().equals(LikeStatus.LIKE)){
                        textPostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        textPostViewHolder.getLike().setImageResource(R.drawable.fnh);
                        textPostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    }else {
                        textPostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        textPostViewHolder.getDislike().setImageResource(R.drawable.fbh);
                        textPostViewHolder.getLike().setImageResource(R.drawable.onh);
                    }
                }else{
                    textPostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    textPostViewHolder.getLike().setImageResource(R.drawable.onh);
                }
                TextPostDTO textPostDTO = ((TextPostDTO) post);
                textPostViewHolder.getDescription().setText(textPostDTO.getContent());
                textPostViewHolder.setPostDTO(post);
                break;
            case 2:
                ImagePostViewHolder imagePostViewHolder = ((ImagePostViewHolder) holder);
                imagePostViewHolder.getTitle().setText(post.getTitle());
                imagePostViewHolder.getAuthor().setText("by " + post.getAuthorUsername());
                imagePostViewHolder.getLikeCount().setText(String.valueOf(post.getLikesCount().intValue()));
                if(post.getLikeStatus() != null){
                    if(post.getLikeStatus().equals(LikeStatus.LIKE)){
                        imagePostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        imagePostViewHolder.getLike().setImageResource(R.drawable.fnh);
                        imagePostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    }else {
                        imagePostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        imagePostViewHolder.getDislike().setImageResource(R.drawable.fbh);
                        imagePostViewHolder.getLike().setImageResource(R.drawable.onh);
                    }
                }else{
                    imagePostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    imagePostViewHolder.getLike().setImageResource(R.drawable.onh);
                }
                ImagePostDTO imagePostDTO = ((ImagePostDTO) post);
                imagePostViewHolder.getDescription().setText(imagePostDTO.getDescription());
                imagePostViewHolder.setPostDTO(post);
                new DownloadImageTask(imagePostViewHolder.getImage()).execute(imagePostDTO.getUrl());
                break;
            case 3:
                PetitionPostViewHolder petitionPostViewHolder = ((PetitionPostViewHolder) holder);
                petitionPostViewHolder.getTitle().setText(post.getTitle());
                petitionPostViewHolder.getAuthor().setText("by " + post.getAuthorUsername());
                petitionPostViewHolder.getLikeCount().setText(String.valueOf(post.getLikesCount().intValue()));
                if(post.getLikeStatus() != null){
                    if(post.getLikeStatus().equals(LikeStatus.LIKE)){
                        petitionPostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        petitionPostViewHolder.getLike().setImageResource(R.drawable.fnh);
                        petitionPostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    }else {
                        petitionPostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        petitionPostViewHolder.getDislike().setImageResource(R.drawable.fbh);
                        petitionPostViewHolder.getLike().setImageResource(R.drawable.onh);
                    }
                }else{
                    petitionPostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    petitionPostViewHolder.getLike().setImageResource(R.drawable.onh);
                }
                PetitionPostDTO petitionPostDTO = ((PetitionPostDTO) post);
                petitionPostViewHolder.getDescription().setText(petitionPostDTO.getDescription());
                petitionPostViewHolder.setPostDTO(post);
                if(petitionPostDTO.getMediaUrl() != null) {
                    new DownloadImageTask(petitionPostViewHolder.getImage()).execute(petitionPostDTO.getMediaUrl());
                }else{
                    petitionPostViewHolder.getImage().setVisibility(View.GONE);
                }
                if(petitionPostDTO.getVoted()){
                    petitionPostViewHolder.getButton().setText("signed");
                    petitionPostViewHolder.getButton().setBackgroundColor(Color.parseColor("#329D9C"));
                    petitionPostViewHolder.getButton().setTextColor(Color.WHITE);
                    petitionPostViewHolder.getButton().setClickable(false);
                }
                break;
            case 4:
                VotingPostViewHolder votingPostViewHolder = ((VotingPostViewHolder) holder);
                votingPostViewHolder.getTitle().setText(post.getTitle());
                votingPostViewHolder.getAuthor().setText("by " + post.getAuthorUsername());
                votingPostViewHolder.getLikeCount().setText(String.valueOf(post.getLikesCount().intValue()));
                votingPostViewHolder.setPostDTO(post);
                if(post.getLikeStatus() != null){
                    if(post.getLikeStatus().equals(LikeStatus.LIKE)){
                        votingPostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        votingPostViewHolder.getLike().setImageResource(R.drawable.fnh);
                        votingPostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    }else {
                        votingPostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        votingPostViewHolder.getDislike().setImageResource(R.drawable.fbh);
                        votingPostViewHolder.getLike().setImageResource(R.drawable.onh);
                    }
                }else{
                    votingPostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    votingPostViewHolder.getLike().setImageResource(R.drawable.onh);
                }
                VotingAdapter adapter;
                postController = RetrofitUtils
                        .getInstance()
                        .getRetrofit()
                        .create(PostController.class);
                adapter = new VotingAdapter(activity, post.getId().intValue(), new Supplier<VotingPostDTO>() {
                    @Override
                    public VotingPostDTO get() {
                        postController = RetrofitUtils
                                .getInstance()
                                .getRetrofit()
                                .create(PostController.class);
                        try {
                            return ((VotingPostDTO) postController.getPost(post.getId().intValue()).execute().body());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
                votingPostViewHolder.getRecyclerView().setLayoutManager(votingPostViewHolder.mLayoutManager);
                votingPostViewHolder.getRecyclerView().setAdapter(adapter);
                postController
                        .getPost(post.getId().intValue()).enqueue(new Callback<PostDTO>() {
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
                break;
            case 5:
                PlayOffPostViewHolder playOffPostViewHolder = ((PlayOffPostViewHolder) holder);
                playOffPostViewHolder.getTitle().setText(post.getTitle());
                playOffPostViewHolder.setPostDTO(post);
                playOffPostViewHolder.getAuthor().setText("by " + post.getAuthorUsername());
                playOffPostViewHolder.getLikeCount().setText(String.valueOf(post.getLikesCount().intValue()));
                if(post.getLikeStatus() != null){
                    if(post.getLikeStatus().equals(LikeStatus.LIKE)){
                        playOffPostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        playOffPostViewHolder.getLike().setImageResource(R.drawable.fnh);
                        playOffPostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    }else {
                        playOffPostViewHolder.getLikeCount().setTextColor(Color.parseColor("#205072"));
                        playOffPostViewHolder.getDislike().setImageResource(R.drawable.fbh);
                        playOffPostViewHolder.getLike().setImageResource(R.drawable.onh);
                    }
                }else{
                    playOffPostViewHolder.getDislike().setImageResource(R.drawable.obh);
                    playOffPostViewHolder.getLike().setImageResource(R.drawable.onh);
                }
                PlayOffPostDTO playOffPostDTO = ((PlayOffPostDTO) post);
                List<PlayOffOptionDTO> options = new ArrayList<>(playOffPostDTO.getOptions());
                for (PlayOffOptionDTO option: options) {
                    if(option.getVoted()){
                        playOffPostViewHolder.getPlay().setText("Played");
                        playOffPostViewHolder.getPlay().setBackgroundColor(Color.parseColor("#329D9C"));
                        playOffPostViewHolder.getPlay().setTextColor(Color.WHITE);
                        playOffPostViewHolder.getPlay().setClickable(false);
                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class TextPostViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private WeakReference<ClickListener> listenerRef;
        private final TextView title;
        private final TextView author;
        private final TextView description;
        private TextView likeCount;
        private final ImageView like;
        private final ImageView dislike;
        PostDTO postDTO;
        PostController postController;

        public TextPostViewHolder(View view, ClickListener listener) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            author = view.findViewById(R.id.author);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            likeCount = view.findViewById(R.id.likeCount);
            like = view.findViewById(R.id.imageView4);
            dislike = view.findViewById(R.id.imageView3);
            view.setOnClickListener(this);
            like.setOnClickListener(this);
            dislike.setOnClickListener(this);
        }

        public void setPostDTO(PostDTO postDTO) {
            this.postDTO = postDTO;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getLikeCount() {
            return likeCount;
        }

        public ImageView getLike() {
            return like;
        }

        public ImageView getDislike() {
            return dislike;
        }

        @Override
        public void onClick(View v) {
            postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
            if (v.getId() == like.getId()) {
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.LIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(null);
                                Log.i("Unlike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                like.setImageResource(R.drawable.fnh);
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(LikeStatus.LIKE);
                                Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp + 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            like.setImageResource(R.drawable.fnh);
                            postDTO.setLikeStatus(LikeStatus.LIKE);
                            Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Like Error", t.getMessage(), t);
                        }
                    });
                }
            }else if (v.getId() == dislike.getId()){
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.DISLIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(null);
                                Log.i("Undislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Undislike Error ", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                dislike.setImageResource(R.drawable.fbh);
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(LikeStatus.DISLIKE);
                                Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Dislike Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp - 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            dislike.setImageResource(R.drawable.fbh);
                            postDTO.setLikeStatus(LikeStatus.DISLIKE);
                            Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Dislike Error", t.getMessage(), t);
                        }
                    });
                }
            }else{
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }
    }

    public static class ImagePostViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        private WeakReference<ClickListener> listenerRef;
        private final TextView title;
        private final TextView description;
        private final TextView author;
        private final ImageView image;
        private TextView likeCount;
        private final ImageView like;
        private final ImageView dislike;
        PostDTO postDTO;
        PostController postController;

        public ImagePostViewHolder(View view, ClickListener listener) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            description = view.findViewById(R.id.description);
            image = view.findViewById(R.id.image);
            likeCount = view.findViewById(R.id.likeCount);
            like = view.findViewById(R.id.imageView4);
            dislike = view.findViewById(R.id.imageView3);
            view.setOnClickListener(this);
            like.setOnClickListener(this);
            dislike.setOnClickListener(this);
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getLikeCount() {
            return likeCount;
        }

        public ImageView getLike() {
            return like;
        }

        public ImageView getDislike() {
            return dislike;
        }

        public void setPostDTO(PostDTO postDTO) {
            this.postDTO = postDTO;
        }

        @Override
        public void onClick(View v) {
            postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
            if (v.getId() == like.getId()) {
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.LIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(null);
                                Log.i("Unlike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                like.setImageResource(R.drawable.fnh);
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(LikeStatus.LIKE);
                                Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp + 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            like.setImageResource(R.drawable.fnh);
                            postDTO.setLikeStatus(LikeStatus.LIKE);
                            Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Like Error", t.getMessage(), t);
                        }
                    });
                }
            }else if (v.getId() == dislike.getId()){
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.DISLIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(null);
                                Log.i("Undislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Undislike Error ", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                dislike.setImageResource(R.drawable.fbh);
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(LikeStatus.DISLIKE);
                                Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Dislike Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp - 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            dislike.setImageResource(R.drawable.fbh);
                            postDTO.setLikeStatus(LikeStatus.DISLIKE);
                            Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Dislike Error", t.getMessage(), t);
                        }
                    });
                }
            }else{
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }
    }

    public static class PetitionPostViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        private WeakReference<ClickListener> listenerRef;
        private final TextView title;
        private final TextView description;
        private final TextView author;
        private final ImageView image;
        private TextView likeCount;
        private final ImageView like;
        private final ImageView dislike;
        Button button;
        PostDTO postDTO;
        PostController postController;

        public PetitionPostViewHolder(View view, ClickListener listener) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            description = view.findViewById(R.id.description);
            image = view.findViewById(R.id.image);
            button = view.findViewById(R.id.signature);
            likeCount = view.findViewById(R.id.likeCount);
            like = view.findViewById(R.id.imageView4);
            dislike = view.findViewById(R.id.imageView3);
            button.setOnClickListener(this);
            like.setOnClickListener(this);
            dislike.setOnClickListener(this);
            view.setOnClickListener(this);
        }
        public TextView getDescription() {
            return description;
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getAuthor() {
            return author;
        }

        public Button getButton() {
            return button;
        }

        public TextView getLikeCount() {
            return likeCount;
        }

        public ImageView getLike() {
            return like;
        }

        public ImageView getDislike() {
            return dislike;
        }

        public void setPostDTO(PostDTO postDTO) {
            this.postDTO = postDTO;
        }

        @Override
        public void onClick(View v) {
            postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
            if (v.getId() == button.getId()) {
                postController
                        .voteForPost(postDTO.getId().intValue(), null)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                button.setText("signed");
                                button.setBackgroundColor(Color.parseColor("#329D9C"));
                                button.setTextColor(Color.WHITE);
                                button.setClickable(false);
                                Log.i("Sign", response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Sign Error", t.getMessage(), t);
                            }
                        });
            }else if (v.getId() == like.getId()) {
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.LIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(null);
                                Log.i("Unlike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                like.setImageResource(R.drawable.fnh);
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(LikeStatus.LIKE);
                                Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp + 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            like.setImageResource(R.drawable.fnh);
                            postDTO.setLikeStatus(LikeStatus.LIKE);
                            Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Like Error", t.getMessage(), t);
                        }
                    });
                }
            }else if (v.getId() == dislike.getId()){
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.DISLIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(null);
                                Log.i("Undislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Undislike Error ", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                dislike.setImageResource(R.drawable.fbh);
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(LikeStatus.DISLIKE);
                                Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Dislike Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp - 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            dislike.setImageResource(R.drawable.fbh);
                            postDTO.setLikeStatus(LikeStatus.DISLIKE);
                            Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Dislike Error", t.getMessage(), t);
                        }
                    });
                }
            }else {
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }
    }

    public static class VotingPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private WeakReference<ClickListener> listenerRef;
        private final TextView title;
        private final TextView author;
        private TextView likeCount;
        private final ImageView like;
        private final ImageView dislike;
        PostDTO postDTO;
        PostController postController;
        RecyclerView recyclerView;
        LinearLayoutManager mLayoutManager;

        public VotingPostViewHolder(View view, ClickListener listener) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            recyclerView = view.findViewById(R.id.options_recycle_view);
            mLayoutManager = new LinearLayoutManager(view.getContext());
            likeCount = view.findViewById(R.id.likeCount);
            like = view.findViewById(R.id.imageView4);
            dislike = view.findViewById(R.id.imageView3);
            like.setOnClickListener(this);
            dislike.setOnClickListener(this);
            recyclerView.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getAuthor() {
            return author;
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public LinearLayoutManager getLayoutManager() {
            return mLayoutManager;
        }

        public TextView getLikeCount() {
            return likeCount;
        }

        public ImageView getLike() {
            return like;
        }

        public ImageView getDislike() {
            return dislike;
        }

        public void setPostDTO(PostDTO postDTO) {
            this.postDTO = postDTO;
        }

        @Override
        public void onClick(View v) {
            postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
            if (v.getId() == like.getId()) {
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.LIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(null);
                                Log.i("Unlike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                like.setImageResource(R.drawable.fnh);
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(LikeStatus.LIKE);
                                Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp + 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            like.setImageResource(R.drawable.fnh);
                            postDTO.setLikeStatus(LikeStatus.LIKE);
                            Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Like Error", t.getMessage(), t);
                        }
                    });
                }
            }else if (v.getId() == dislike.getId()){
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.DISLIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(null);
                                Log.i("Undislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Undislike Error ", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                dislike.setImageResource(R.drawable.fbh);
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(LikeStatus.DISLIKE);
                                Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Dislike Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp - 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            dislike.setImageResource(R.drawable.fbh);
                            postDTO.setLikeStatus(LikeStatus.DISLIKE);
                            Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Dislike Error", t.getMessage(), t);
                        }
                    });
                }
            }else{
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }
    }

    public static class PlayOffPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private WeakReference<ClickListener> listenerRef;
        private final TextView title;
        private final TextView author;
        private TextView likeCount;
        private final ImageView like;
        private final ImageView dislike;
        private final Button play;
        PostDTO postDTO;
        PostController postController;

        public PlayOffPostViewHolder(View view, ClickListener listener) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            play =  view.findViewById(R.id.play);
            likeCount = view.findViewById(R.id.likeCount);
            like = view.findViewById(R.id.imageView4);
            dislike = view.findViewById(R.id.imageView3);
            play.setOnClickListener(this);
            like.setOnClickListener(this);
            dislike.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getLikeCount() {
            return likeCount;
        }

        public ImageView getLike() {
            return like;
        }

        public ImageView getDislike() {
            return dislike;
        }

        public Button getPlay() { return play; }

        public void setPostDTO(PostDTO postDTO) {
            this.postDTO = postDTO;
        }

        @Override
        public void onClick(View v) {
            postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
            if (v.getId() == play.getId()){
//                Intent i = new Intent(v.getContext(), PlayOffPlayActivity.class);
//                i.putExtra("id", postDTO.getId());
//                i.putExtra("from", "Feed");
//                v.getContext().startActivity(i);
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }else if (v.getId() == like.getId()) {
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.LIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(null);
                                Log.i("Unlike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                like.setImageResource(R.drawable.fnh);
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(LikeStatus.LIKE);
                                Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Like Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.LIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp + 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            like.setImageResource(R.drawable.fnh);
                            postDTO.setLikeStatus(LikeStatus.LIKE);
                            Log.i("Like: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Like Error", t.getMessage(), t);
                        }
                    });
                }
            }else if (v.getId() == dislike.getId()){
                if(postDTO.getLikeStatus() != null) {
                    if ((postDTO.getLikeStatus().equals(LikeStatus.DISLIKE))) {
                        postController.like(postDTO.getId().intValue(), LikeStatus.UNSET).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp + 1));
                                likeCount.setTextColor(Color.parseColor("#329D9C"));
                                dislike.setImageResource(R.drawable.obh);
                                postDTO.setLikeStatus(null);
                                Log.i("Undislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Undislike Error ", t.getMessage(), t);
                            }
                        });
                    }else{
                        postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int tmp = Integer.parseInt(likeCount.getText().toString());
                                likeCount.setText(String.valueOf(tmp - 2));
                                likeCount.setTextColor(Color.parseColor("#205072"));
                                dislike.setImageResource(R.drawable.fbh);
                                like.setImageResource(R.drawable.onh);
                                postDTO.setLikeStatus(LikeStatus.DISLIKE);
                                Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Dislike Error", t.getMessage(), t);
                            }
                        });
                    }
                }else{
                    postController.like(postDTO.getId().intValue(), LikeStatus.DISLIKE).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int tmp = Integer.parseInt(likeCount.getText().toString());
                            likeCount.setText(String.valueOf(tmp - 1));
                            likeCount.setTextColor(Color.parseColor("#205072"));
                            dislike.setImageResource(R.drawable.fbh);
                            postDTO.setLikeStatus(LikeStatus.DISLIKE);
                            Log.i("Dislike: " + postDTO.getId().intValue(), response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Dislike Error", t.getMessage(), t);
                        }
                    });
                }
            }else{
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }
    }
}