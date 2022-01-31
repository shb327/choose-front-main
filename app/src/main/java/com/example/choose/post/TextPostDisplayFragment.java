package com.example.choose.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.choose.R;

public class TextPostDisplayFragment extends Fragment {

    public TextPostDisplayFragment() { }

    String description;

    public TextPostDisplayFragment(String description) {
        this.description = description;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_text_post_display, container, false);
        TextView desc = inflate.findViewById(R.id.desc);
        desc.setText(description);
        return inflate;
    }
}
