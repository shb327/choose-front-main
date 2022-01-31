package com.example.choose.recyclers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.CommunityController;
import com.example.choose.api.PostController;
import com.example.choose.dialog.ShareDialogFragment;
import com.example.choose.dto.PostDTO;
import com.example.choose.dto.PostType;
import com.example.choose.retrofit.RetrofitUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    public List<PostDTO> localDataSet = new ArrayList<>();
    private final ClickListener listener;

    public PostAdapter(List<PostDTO> localDataSet, ClickListener listener) {
        this.localDataSet = localDataSet;
        this.listener = listener;
    }

    public PostAdapter(ClickListener listener) {
        this.listener = listener;
    }

    public List<PostDTO> getLocalDataSet() {
        return localDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        PostDTO post = localDataSet.get(position);
        PostType type = post.getType();
        viewHolder.getTextView().setText(post.getTitle());
        switch (type){
            case IMAGE:
                viewHolder.getTextType().setText("Image Post");
                break;
            case PETITION:
                viewHolder.getTextType().setText("Petition");
                break;
            case VOTE:
                viewHolder.getTextType().setText("Voting Post");
                break;
            case PLAYOFF:
                viewHolder.getTextType().setText("Play-Off Post");
                break;
            default:
            case TEXT:
                viewHolder.getTextType().setText("Text Post");
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView textView;
        private final TextView textType;
        private WeakReference<ClickListener> listenerRef;
        Button share;
        Button delete;
        CommunityController communityController;
        PostController postController;

        public ViewHolder(View view, ClickListener listener) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            textView = view.findViewById(R.id.textView5);
            textType = view.findViewById(R.id.textViewTitle);
            share = view.findViewById(R.id.share);
            delete = view.findViewById(R.id.delete);
            view.setOnClickListener(this);
            share.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == share.getId()) {
                ShareDialogFragment dialog = new ShareDialogFragment(localDataSet.get(getAdapterPosition()).getId().intValue());
                dialog.show(FragmentManager.findFragment(v).getFragmentManager(), "MyCustomDialog");
            }else if (v.getId() == delete.getId()) {
                postController = RetrofitUtils
                        .getInstance()
                        .getRetrofit()
                        .create(PostController.class);
                postController
                        .deletePost(localDataSet.get(getAdapterPosition()).getId().intValue())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int position = getAdapterPosition();
                                Toast.makeText(v.getContext(),
                                        "Post: " + localDataSet.get(position).getTitle() + ", is deleted!", Toast.LENGTH_SHORT).show();
                                removeAt(position);
                                Log.i("Delete", response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Delete Error", t.getMessage(), t);
                            }
                        });
            }else {
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        public void removeAt(int position) {
            localDataSet.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, localDataSet.size());
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }

        public TextView getTextType() {
            return textType;
        }

        public TextView getTextView() {
            return textView;
        }

    }
}
