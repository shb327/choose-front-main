package com.example.choose.recyclers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.dto.PlayOffOptionDTO;
import java.util.ArrayList;
import java.util.List;

public class PlayOffAdapter extends RecyclerView.Adapter<PlayOffAdapter.ViewHolder>{
    public List<PlayOffOptionDTO> localDataSet = new ArrayList<>();
    private final ClickListener listener;

    public PlayOffAdapter( ClickListener listener) {
        this.listener = listener;
    }

    public List<PlayOffOptionDTO> getLocalDataSet() {
        return localDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.stat_row_item, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        PlayOffOptionDTO option = localDataSet.get(position);
        viewHolder.getName().setText(option.getTitle());
        viewHolder.getNumber().setText("Votes: " + option.getVotedUsers());
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
        private TextView number;
        private TextView name;

        public ViewHolder(View view, ClickListener listener) {
            super(view);
            number = view.findViewById(R.id.counter);
            name = view.findViewById(R.id.optionTitle);
        }

        public TextView getNumber() {
            return number;
        }

        public TextView getName() {
            return name;
        }

        @Override
        public void onClick(View v) { }

        @Override
        public boolean onLongClick(View v) {
            //TODO:Options
            return true;
        }
    }
}