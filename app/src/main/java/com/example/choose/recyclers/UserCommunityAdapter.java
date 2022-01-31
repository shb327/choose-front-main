package com.example.choose.recyclers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.CommunityController;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.retrofit.RetrofitUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommunityAdapter extends RecyclerView.Adapter<UserCommunityAdapter.ViewHolder>{

    public List<CommunityDTO> localDataSet = new ArrayList<>();
    private final ClickListener listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.community_row, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    public UserCommunityAdapter(List<CommunityDTO> localDataSet, ClickListener listener) {
        this.localDataSet = localDataSet;
        this.listener = listener;
    }

    public UserCommunityAdapter(ClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CommunityDTO communityDTO = localDataSet.get(position);
        viewHolder.setCommunityDTO(communityDTO);
        viewHolder.getName().setText(communityDTO.getName());
        viewHolder.getUsername().setText(communityDTO.getUsername());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void removeAt(int position) {
        localDataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, localDataSet.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private WeakReference<ClickListener> listenerRef;
        private final TextView name;
        private final TextView username;
        Button button;
        CommunityDTO communityDTO;
        CommunityController communityController;

        public ViewHolder(View view, ClickListener listener) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            name = view.findViewById(R.id.name);
            username = view.findViewById(R.id.username);
            button = view.findViewById(R.id.leave);
            view.setOnClickListener(this);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == button.getId()) {
                communityController = RetrofitUtils
                        .getInstance()
                        .getRetrofit()
                        .create(CommunityController.class);
                communityController
                        .leaveCommunity(communityDTO.getId().intValue())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                removeAt(getAdapterPosition());
                                Toast.makeText(v.getContext(), "Community: " +
                                        communityDTO.getName() +
                                        ", is unfollowed!", Toast.LENGTH_SHORT).show();
                                Log.i("Leave", response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Leave Error", t.getMessage(), t);
                            }
                        });
            } else {
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }

        public void setCommunityDTO(CommunityDTO communityDTO) {
            this.communityDTO = communityDTO;
        }

        public TextView getName() {
            return name;
        }

        public TextView getUsername() {
            return username;
        }

        public Button getButton() { return button; }
    }
}