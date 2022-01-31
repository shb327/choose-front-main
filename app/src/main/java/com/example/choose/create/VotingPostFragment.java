package com.example.choose.create;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.choose.R;
import com.example.choose.api.PostController;
import com.example.choose.dto.CreateTextPostRequestDTO;
import com.example.choose.dto.CreateVotingPostRequestDTO;
import com.example.choose.dto.PostDTO;
import com.example.choose.home.HomeActivity;
import com.example.choose.retrofit.RetrofitUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VotingPostFragment extends Fragment {

    AutoCompleteTextView textIn;
    AutoCompleteTextView textInTwo;
    TextView errorText;
    Button buttonAdd;
    LinearLayout block;
    LinearLayout bigBlock;
    Button button;
    PostController postController = RetrofitUtils.getInstance().getRetrofit().create(PostController.class);
    ArrayAdapter<String> adapter;

    public VotingPostFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate =  inflater.inflate(R.layout.fragment_voting_post, container, false);

        final int[] counter = {1};

        adapter = new ArrayAdapter<String>(inflate.getContext(), android.R.layout.simple_dropdown_item_1line);
        textIn = (AutoCompleteTextView) inflate.findViewById(R.id.textIn);
        textInTwo = (AutoCompleteTextView) inflate.findViewById(R.id.textInTwo);
        textIn.setAdapter(adapter);
        errorText = inflate.findViewById(R.id.errorText);
        button = inflate.findViewById(R.id.sendBtn);
        buttonAdd = (Button) inflate.findViewById(R.id.add);
        bigBlock = (LinearLayout) inflate.findViewById(R.id.options);
        block = (LinearLayout) inflate.findViewById(R.id.container);

        TextInputEditText titleText = inflate.findViewById(R.id.titleTxt);
        TextInputLayout titleLayout = inflate.findViewById(R.id.titleLayout);
        List<String> options = new ArrayList<>();

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
                    titleLayout.setError("Character Limit Exceeded");
                    titleText.setTextColor(Color.parseColor("#F75010"));
                }
                else {
                    titleLayout.setErrorEnabled(false);
                    titleLayout.setError(null);
                    titleText.setTextColor(Color.parseColor("#68B2A0"));
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    titleLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                }
            }
        });

        textIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (textIn.getRight() - textIn.getCompoundDrawables()[2].getBounds().width())) {
                        textIn.getText().clear();
                        return true;
                    }
                }
                return false;
            }
        });

        textInTwo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (textInTwo.getRight() - textInTwo.getCompoundDrawables()[2].getBounds().width())) {
                        textInTwo.getText().clear();
                        return true;
                    }
                }
                return false;
            }
        });

        textIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                errorText.setTextColor(Color.parseColor("#FFFFFF"));
                bigBlock.setBackgroundResource(R.drawable.upload);
            }
        });

        textInTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                errorText.setTextColor(Color.parseColor("#FFFFFF"));
                bigBlock.setBackgroundResource(R.drawable.upload);
            }
        });

        ViewGroup finalContainer = block;
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(counter[0] < 9) {
                    counter[0]++;
                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.row, null);
                    AutoCompleteTextView textOut = (AutoCompleteTextView) addView.findViewById(R.id.textout);
                    textOut.setAdapter(adapter);
                    textOut.setText(textIn.getText().toString());
                    textOut.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (event.getRawX() >= (textOut.getRight() - textOut.getCompoundDrawables()[2].getBounds().width())) {
                                    ((LinearLayout) addView.getParent()).removeView(addView);
                                    counter[0]--;
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    textOut.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            errorText.setTextColor(Color.parseColor("#FFFFFF"));
                            bigBlock.setBackgroundResource(R.drawable.upload);
                        }
                    });
                    textOut.setText("");
                    finalContainer.addView(addView);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((titleText.getText().length() == 0) && (textIn.getText().length() == 0) && (textInTwo.getText().length() == 0)) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("The title cannot be empty");
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    titleText.setTextColor(Color.parseColor("#F75010"));
                    errorText.setTextColor(Color.parseColor("#F75010"));
                    errorText.setText("At Least Two Options should be Filled");
                    bigBlock.setBackgroundResource(R.drawable.upload_red);
                    return;
                } else if ((titleText.getText().length() == 0) && (textIn.getText().length() == 0)) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("The title cannot be empty");
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    titleText.setTextColor(Color.parseColor("#F75010"));
                    errorText.setTextColor(Color.parseColor("#F75010"));
                    errorText.setText("At Least Two Options should be Filled");
                    bigBlock.setBackgroundResource(R.drawable.upload_red);
                    return;
                } else if ((titleText.getText().length() == 0) && (textInTwo.getText().length() == 0)) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("The title cannot be empty");
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    titleText.setTextColor(Color.parseColor("#F75010"));
                    errorText.setTextColor(Color.parseColor("#F75010"));
                    errorText.setText("At Least Two Options should be Filled");
                    bigBlock.setBackgroundResource(R.drawable.upload_red);
                    return;
                }else if (titleText.getText().length() == 0) {
                    titleLayout.setErrorEnabled(true);
                    titleLayout.setError("The title cannot be empty");
                    titleLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                    titleText.setTextColor(Color.parseColor("#F75010"));
                    return;
                } else if (titleLayout.isErrorEnabled()) { return;
                } else if (textIn.getText().length() == 0) {
                    errorText.setTextColor(Color.parseColor("#F75010"));
                    errorText.setText("At Least Two Options should be Filled");
                    bigBlock.setBackgroundResource(R.drawable.upload_red);
                    return;
                } else if (textInTwo.getText().length() == 0) {
                    errorText.setTextColor(Color.parseColor("#F75010"));
                    errorText.setText("At Least Two Options should be Filled");
                    bigBlock.setBackgroundResource(R.drawable.upload_red);
                    return;
                }

                options.add(String.valueOf(textIn.getText()));
                options.add(String.valueOf(textInTwo.getText()));
                boolean extraFieldsFilled = true;
                for (int i = 0; i < finalContainer.getChildCount(); i++) {
                    View tmp = finalContainer.getChildAt(i);
                    AutoCompleteTextView tmpText = tmp.findViewById(R.id.textout);
                    if(tmpText.getText().length() == 0) extraFieldsFilled = false;
                    options.add(String.valueOf(tmpText.getText()));
                }
                if(extraFieldsFilled){
                    postController.createVotingPost(new CreateVotingPostRequestDTO(options,titleText.getText().toString()))
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
                }else{
                    options.clear();
                    errorText.setTextColor(Color.parseColor("#F75010"));
                    bigBlock.setBackgroundResource(R.drawable.upload_red);
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