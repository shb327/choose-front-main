package com.example.choose.create;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.choose.R;

public class PlayOffPostFragment extends Fragment {

    private PlayOffSelectFragment playOffSelectFragment;

    public PlayOffPostFragment() { }

    private int anInt;
    private boolean secondFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate =  inflater.inflate(R.layout.fragment_play_off_post, container, false);
        displayFirstView();
        playOffSelectFragment = new PlayOffSelectFragment();
        anInt = 8;
        secondFragment = false;

        Button btn = inflate.findViewById(R.id.closeBtn);
        Button sendBtn = inflate.findViewById(R.id.sendBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseType.close();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(!secondFragment){
                    return;
                }
                playOffSelectFragment.upload();
            }
        });

        View playOffContainer = inflate.findViewById(R.id.off_container);


        playOffContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayOffChooseFragment.isReady()){
                    setAnInt(PlayOffChooseFragment.getNumber());
                    displaySecondView();
                }
            }
        });
        return inflate;
    }

    public void showFragment(Fragment fragment, int position) {
        FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.off_container, fragment, fragment.getClass().getName());
        mTransaction.commit();
    }

    public void displayFirstView() {
        showFragment(new PlayOffChooseFragment(),0);
    }

    public void displaySecondView() {
        Bundle bundle = new Bundle();
        secondFragment = true;
        bundle.putInt("choice", PlayOffChooseFragment.getNumber());
        bundle.putString("title", PlayOffChooseFragment.getTitle());
        playOffSelectFragment.setArguments(bundle);
        showFragment(playOffSelectFragment, 1);
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }
}