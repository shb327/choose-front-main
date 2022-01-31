package com.example.choose.recyclers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<MyListItem> list;
    private Fragment fragment;
    private Integer uploadPosition;

    public MyRecyclerViewAdapter(List<MyListItem> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    public Integer getUploadPosition() {
        return uploadPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.play_off_row, parent, false);
        return new ViewHolder(contactView, contactView.findViewById(R.id.block), contactView.findViewById(R.id.optionTitle));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyListItem listItem = list.get(position);
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(fragment).crop().compress(1024).maxResultSize(1080, 1080).start();
                uploadPosition = holder.getLayoutPosition();
            }
        });
        if(listItem.mediaUri != null){
            holder.getImageView().setImageURI(listItem.mediaUri);
        }
        holder.getTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listItem.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView, ImageView imageView, TextView textView) {
            super(itemView);
            this.imageView = imageView;
            this.textView = textView;
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
