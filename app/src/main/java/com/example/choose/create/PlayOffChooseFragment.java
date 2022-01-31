package com.example.choose.create;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choose.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PlayOffChooseFragment extends Fragment {

    public PlayOffChooseFragment() { }

    private TextInputEditText titleTxt;

    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rb6;

    public static boolean isReady;
    public static int number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate =  inflater.inflate(R.layout.fragment_play_off_choose, container, false);

        titleTxt = inflate.findViewById(R.id.titleTxt);
        TextInputLayout titleLayout = inflate.findViewById(R.id.titleLayout);

        isReady = false;

        Button continueBtn = inflate.findViewById(R.id.continueBtn);
        View view = getActivity().findViewById(R.id.off_container);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((titleTxt.getText().length() == 0)) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("The title cannot be empty");
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    titleTxt.setTextColor(Color.parseColor("#F75010"));
                    return;
                } else if (titleLayout.isErrorEnabled()) {
                    return;
                }
                if(rb1.isChecked()){
                    setNumber(8);
                }else if(rb2.isChecked()){
                    setNumber(16);
                }else if(rb3.isChecked()){
                    setNumber(32);
                }else if(rb4.isChecked()){
                    setNumber(64);
                }else if(rb5.isChecked()){
                    setNumber(128);
                }else if(rb6.isChecked()){
                    setNumber(256);
                }
                setReady(true);
                view.callOnClick();
            }
        });

        rb1 = inflate.findViewById(R.id.eight);
        rb2 = inflate.findViewById(R.id.sixteen);
        rb3 = inflate.findViewById(R.id.thirtyTwo);
        rb4 = inflate.findViewById(R.id.sixtyFour);
        rb5 = inflate.findViewById(R.id.hundredTwentyEight);
        rb6 = inflate.findViewById(R.id.twoHundredFiftySix);

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb2.setChecked(false);
                rb1.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb1.setChecked(false);
                rb5.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb1.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                rb1.setChecked(false);
            }
        });

        titleTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(titleTxt.getText().length()==20){
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("Character limit exceeded");
                    titleTxt.setTextColor(Color.parseColor("#F75010"));
                }
                else {
                    titleLayout.setErrorEnabled(false);
                    titleLayout.setError(null);
                    titleTxt.setTextColor(Color.parseColor("#68B2A0"));
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                }
            }
        });
        return inflate;
    }

    public static void setReady(boolean switchActivity) {
        PlayOffChooseFragment.isReady = switchActivity;
    }

    public static boolean isReady() {
        return isReady;
    }

    public static int getNumber() {
        return number;
    }

    public static void setNumber(int number) {
        PlayOffChooseFragment.number = number;
    }
}