package com.example.choose.create;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.choose.R;
import com.example.choose.retrofit.RetrofitUtils;
import com.example.choose.api.PostController;
import com.example.choose.dto.CreateTextPostRequestDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextPostFragment extends Fragment {

    public TextPostFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextInputEditText titleText;
    TextInputEditText descriptionText;
    TextInputLayout titleLayout;
    TextInputLayout descriptionLayout;
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View inflate =  inflater.inflate(R.layout.fragment_text_post, container, false);
       PostController postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);

       button = inflate.findViewById(R.id.sendBtn);
       titleText = inflate.findViewById(R.id.titleTxt);
       descriptionText = inflate.findViewById(R.id.contentTxt);
       titleLayout = inflate.findViewById(R.id.titleLayout);
       descriptionLayout = inflate.findViewById(R.id.contentLayout);
       titleLayout.setErrorEnabled(true);
       descriptionLayout.setErrorEnabled(true);
       titleLayout.setCounterTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));

       titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (titleText.getText().length() == 21) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("Character Limit Exceeded");
                    titleText.setTextColor(Color.parseColor("#F75010"));

                } else {
                    titleLayout.setErrorEnabled(false);
                    titleLayout.setError(null);
                    titleText.setTextColor(Color.parseColor("#68B2A0"));
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    titleLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                }
            }
       });


       descriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (descriptionText.getText().length() > 256) {
                    descriptionLayout.setErrorEnabled(true);
                    descriptionLayout.setError("Character limit exceeded");
                    descriptionText.setTextColor(Color.parseColor("#F75010"));
                } else {
                    descriptionLayout.setErrorEnabled(false);
                    descriptionLayout.setError(null);
                    descriptionText.setTextColor(Color.parseColor("#68B2A0"));
                    descriptionLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    descriptionLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    descriptionLayout.setCounterTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                }
            }
       });

       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((titleText.getText().length() == 0) && (descriptionText.getText().length() == 0)) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("The title cannot be empty");
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    titleText.setTextColor(Color.parseColor("#F75010"));
                    descriptionLayout.setErrorEnabled(true);
                    descriptionLayout.setError("The description cannot be empty");
                    descriptionLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    descriptionText.setTextColor(Color.parseColor("#F75010"));
                    descriptionLayout.setCounterTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    return;
                } else if (titleText.getText().length() == 0) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("The title cannot be empty");
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    titleText.setTextColor(Color.parseColor("#F75010"));
                    return;
                } else if (descriptionText.getText().length() == 0) {
                    descriptionLayout.setErrorEnabled(true);
                    descriptionLayout.setError("The description cannot be empty");
                    descriptionLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    descriptionText.setTextColor(Color.parseColor("#F75010"));
                    descriptionLayout.setCounterTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    return;
                } else if (descriptionLayout.isErrorEnabled()) { return;
                } else if (titleLayout.isErrorEnabled()) { return;
                }

                postController.createTextPost(new CreateTextPostRequestDTO(
                        descriptionText.getText().toString(),
                        titleText.getText().toString()))
                        .enqueue(new Callback<PostDTO>() {
                            @Override
                            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                                startActivity(new Intent(inflate.getContext(), HomeActivity.class));
                            }

                            @Override
                            public void onFailure(Call<PostDTO> call, Throwable t) {
                                Toast.makeText(inflate.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
       });

       Button btn = inflate.findViewById(R.id.closeBtn);
       btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseType.close();
            }
       });
       return inflate;
    }
}