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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.PostDTO;
import com.example.choose.home.HomeActivity;
import com.example.choose.retrofit.RetrofitUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagePostFragment extends Fragment {

    public ImagePostFragment() { }

    TextInputEditText titleText;
    TextInputEditText descriptionText;
    TextInputLayout titleLayout;
    TextInputLayout descriptionLayout;
    ImageView res;
    Button button;
    Button gallery;
    Button camera;
    ImageView block;
    TextView textView;
    TextView errorText;
    MultipartBody.Part body;
    boolean uploaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        res.setImageURI(uri);
        File file = FileUtils.getFile(getContext(), uri);
        RequestBody requestFile = RequestBody.create(MediaType.parse("uri"), file);
        body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        uploaded = true;
        gallery.setClickable(false);
        camera.setClickable(false);
        block.setClickable(false);
        errorText.setTextColor(Color.parseColor("#ffffff"));
        block.setImageResource(R.drawable.upload);
        textView.setText("Very Good Image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        uploaded = false;
        View inflate =  inflater.inflate(R.layout.fragment_image_post, container, false);
        button = inflate.findViewById(R.id.sendBtn);
        PostController postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);

        descriptionText = inflate.findViewById(R.id.descriptionTxt);
        descriptionLayout = inflate.findViewById(R.id.descriptionLayout);
        titleText = inflate.findViewById(R.id.titleTxt);
        titleLayout = inflate.findViewById(R.id.titleLayout);

        res = inflate.findViewById(R.id.imageView3);
        gallery = inflate.findViewById(R.id.gallery);
        camera = inflate.findViewById(R.id.camera);
        block = inflate.findViewById(R.id.block);
        textView = inflate.findViewById(R.id.textView);
        errorText = inflate.findViewById(R.id.errorText);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ImagePostFragment.this).crop().compress(1024)
                        .maxResultSize(1080, 1080).start();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ImagePostFragment.this).galleryOnly().crop().compress(1024)
                        .maxResultSize(1080, 1080).start();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ImagePostFragment.this).cameraOnly().crop().compress(1024)
                        .maxResultSize(1080, 1080).start();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((titleText.getText().length() == 0) && (descriptionText.getText().length() == 0) && !uploaded) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("The title cannot be empty");
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    titleText.setTextColor(Color.parseColor("#F75010"));
                    descriptionLayout.setErrorEnabled(true);
                    descriptionLayout.setError("The description cannot be empty");
                    descriptionLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    descriptionText.setTextColor(Color.parseColor("#F75010"));
                    descriptionLayout.setCounterTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    errorText.setTextColor(Color.parseColor("#F75010"));
                    block.setImageResource(R.drawable.upload_red);
                    return;
                } else if ((titleText.getText().length() == 0) && (descriptionText.getText().length() == 0)) {
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
                }else if (!uploaded) {
                    errorText.setTextColor(Color.parseColor("#F75010"));
                    block.setImageResource(R.drawable.upload_red);
                    return;
                } else if (descriptionLayout.isErrorEnabled()) { return;
                } else if (titleLayout.isErrorEnabled()) { return;
                }

                postController.createImagePost(titleText.getText().toString(),
                        descriptionText.getText().toString(), body)
                        .enqueue(new Callback<PostDTO>() {
                        @Override
                        public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                            startActivity(new Intent(inflate.getContext(), HomeActivity.class));
                        }

                        @Override
                        public void onFailure(Call<PostDTO> call, Throwable t) {
                            Toast.makeText(inflate.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                });;
            }
        });

        descriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(descriptionText.getText().length()>256){
                    descriptionLayout.setErrorEnabled(true);
                    descriptionLayout.setError("Character limit exceeded");
                    descriptionText.setTextColor(Color.parseColor("#F75010"));
                    descriptionLayout.setCounterTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                } else {
                    descriptionLayout.setErrorEnabled(false);
                    descriptionLayout.setError(null);
                    descriptionText.setTextColor(Color.parseColor("#68B2A0"));
                    descriptionLayout.setCounterTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    descriptionLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    descriptionLayout.setCounterTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                }
            }
        });

        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(titleText.getText().length()==20){
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("Character limit exceeded");
                    titleText.setTextColor(Color.parseColor("#F75010"));
                }
                else {
                    titleLayout.setErrorEnabled(false);
                    titleLayout.setError(null);
                    titleText.setTextColor(Color.parseColor("#68B2A0"));
                    titleLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                }
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