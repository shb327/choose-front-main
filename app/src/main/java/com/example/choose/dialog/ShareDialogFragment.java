package com.example.choose.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choose.R;
import com.example.choose.api.CommunityController;
import com.example.choose.dto.CommunityDTO;
import com.example.choose.recyclers.ClickListener;
import com.example.choose.recyclers.DialogCommunityAdapter;
import com.example.choose.retrofit.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareDialogFragment extends DialogFragment {

    private String TAG = "ShareDialog";
    public interface OnInputListener{
        void sendInput(String input);
    }
    public OnInputListener mOnInputListener;
    private RecyclerView recyclerView;
    CommunityController communityController;
    Button button;
    int page;
    int postId;

    public ShareDialogFragment(int postId) {
        this.postId = postId;
    }

    private final DialogCommunityAdapter adapter = new DialogCommunityAdapter(new ClickListener() {
        @Override
        public void onPositionClicked(int position) {
            communityController = RetrofitUtils
                    .getInstance()
                    .getRetrofit()
                    .create(CommunityController.class);
            communityController
                    .addPost(adapter.localDataSet.get(position).getId().intValue(), postId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(getContext(), "Congrats, post was send!", Toast.LENGTH_SHORT).show();
                            Log.i("Send", response.raw().request().headers().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Send Error", t.getMessage(), t);
                        }
                    });
        }

        @Override
        public void onLongClicked(int position) {
            //TODO:Options
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 0;
        communityController = RetrofitUtils
                .getInstance()
                .getRetrofit()
                .create(CommunityController.class);
        communityController
                .getUserCommunities()
                .enqueue(new Callback<List<CommunityDTO>>() {
                    @Override
                    public void onResponse(Call<List<CommunityDTO>> call, Response<List<CommunityDTO>> response) {
                        if (response.isSuccessful()) {
                            adapter.localDataSet.clear();
                            adapter.localDataSet.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("community", String.valueOf(response.code()));
                            Log.e("community", response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CommunityDTO>> call, Throwable t) {
                        Log.e("community", t.getMessage(), t);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_dialog, container, false);

        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        recyclerView = view.findViewById(R.id.dialog_recycle_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}