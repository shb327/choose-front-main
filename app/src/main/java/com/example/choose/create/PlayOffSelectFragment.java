package com.example.choose.create;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.PlayOffOptionDTO;
import com.example.choose.dto.PlayOffRequestOptionDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.home.HomeActivity;
import com.example.choose.recyclers.MyListItem;
import com.example.choose.recyclers.MyRecyclerViewAdapter;
import com.example.choose.retrofit.RetrofitUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.function.IntToDoubleFunction;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayOffSelectFragment extends Fragment {

    private MyRecyclerViewAdapter adapter;
    ArrayList<MyListItem> myListItems;
    private RecyclerView items;
    PostController postController;

    public PlayOffSelectFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        myListItems.get(adapter.getUploadPosition()).setMediaUri(uri);
        adapter.notifyDataSetChanged();
//        res.setImageURI(uri);
//        File file = FileUtils.getFile(getContext(), uri);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("uri"), file);
//        body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//        uploaded = true;
//        block.setClickable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate =  inflater.inflate(R.layout.fragment_play_off_select, container, false);
        items = (RecyclerView) inflate.findViewById(R.id.recycler);
        myListItems = MyListItem.createContactsList(getArguments().getInt("choice"));
        adapter = new MyRecyclerViewAdapter(myListItems, this);
        items.setAdapter(adapter);
        items.setLayoutManager(new LinearLayoutManager(inflate.getContext()));

        return inflate;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void upload(){
        postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);

        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), "Title");

        Gson gson = new Gson();

        RequestBody options = RequestBody
                .create(MediaType.parse("application/json"), gson.toJson(myListItems
                        .stream()
                        .map(MyListItem::getTitle)
                        .map(PlayOffRequestOptionDTO::new)
                        .collect(Collectors.toList())));

        postController.createPlayoffPost(title,options,
                myListItems.stream()
                        .map(item -> {
                            File file = FileUtils.getFile(getContext(), item.getMediaUri());
                            RequestBody requestFile = RequestBody.create(MediaType.parse("uri"), file);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                            return body;
                        })
                        .collect(Collectors.toList()))
                .enqueue(new Callback<PostDTO>() {
                    @Override
                    public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                        Intent i = new Intent(getContext(), HomeActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<PostDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
