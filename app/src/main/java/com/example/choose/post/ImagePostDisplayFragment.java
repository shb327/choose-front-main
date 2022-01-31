package com.example.choose.post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.choose.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImagePostDisplayFragment extends Fragment {
    String description;
    String url;

    public ImagePostDisplayFragment() { }

    public ImagePostDisplayFragment(String description, String url) {
        this.description = description;
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_image_post_display, container, false);
        TextView desc = inflate.findViewById(R.id.desc);
        desc.setText(description);
        new DownloadImageTask((ImageView) inflate.findViewById(R.id.image)).execute(url);
        return inflate;
    }
}