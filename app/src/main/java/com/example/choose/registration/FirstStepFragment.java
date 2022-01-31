package com.example.choose.registration;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.choose.R;
import com.example.choose.retrofit.RetrofitUtils;
import com.example.choose.api.RegistrationController;
import com.example.choose.dto.RegistrationUsernameDTO;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstStepFragment extends Fragment {

    public FirstStepFragment() { }

    TextInputLayout username;
    TextInputEditText field;

    TextInputLayout password;
    TextInputEditText hidden;

    TextInputLayout confirm;
    TextInputEditText second;

    RegistrationController registrationController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.outlinedTextField1);
        field = view.findViewById(R.id.username);

        password = view.findViewById(R.id.outlinedTextField2);
        hidden = view.findViewById(R.id.hidden);

        confirm = view.findViewById(R.id.outlinedTextField3);
        second = view.findViewById(R.id.second);

        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(field.getText().length()== 20){
                    username.setErrorEnabled(true);
                    username.setError("Character Limit Exceeded");
                    field.setTextColor(Color.parseColor("#F75010"));
                }else if(!field.getText().toString().matches("^[a-z0-9._-]{3,}$")){
                    username.setErrorEnabled(true);
                    username.setError("Invalid Username");
                    field.setTextColor(Color.parseColor("#F75010"));
                }else {
                    username.setErrorEnabled(false);
                    username.setError(null);
                    username.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    field.setTextColor(Color.parseColor("#56C596"));
                }
            }
        });
        hidden.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!hidden.getText().toString().matches("^(?=.*?[A-Z])(?=(.*[a-z])+)(?=(.*[\\d])+)(?=(.*[\\W])+)(?!.*\\s).{8,}$")){
                    password.setErrorEnabled(true);
                    password.setError("Invalid Password");
                    hidden.setTextColor(Color.parseColor("#F75010"));
                }else {
                    password.setErrorEnabled(false);
                    password.setError(null);
                    password.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    hidden.setTextColor(Color.parseColor("#56C596"));
                }
            }
        });
        second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!second.getText().toString().equals(hidden.getText().toString())){
                    confirm.setErrorEnabled(true);
                    confirm.setError("Passwords Don't Match");
                    second.setTextColor(Color.parseColor("#F75010"));
                }else if(!second.getText().toString().matches("^(?=.*?[A-Z])(?=(.*[a-z])+)(?=(.*[\\d])+)(?=(.*[\\W])+)(?!.*\\s).{8,}$")){
                    confirm.setErrorEnabled(true);
                    confirm.setError("Invalid Password");
                    second.setTextColor(Color.parseColor("#F75010"));
                }else {
                    confirm.setErrorEnabled(false);
                    confirm.setError(null);
                    confirm.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#68B2A0")));
                    second.setTextColor(Color.parseColor("#56C596"));
                }
            }
        });
    }

    public void send(ViewPager viewPager){
        RetrofitUtils utils = RetrofitUtils.getInstance();
        registrationController = utils.getRetrofit().create(RegistrationController.class);
        registrationController.username(
                new RegistrationUsernameDTO(field.getText().toString(), hidden.getText().toString()))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (field.getText().length() == 0) {
                            username.setErrorEnabled(true);
                            username.setError("Username Can't be Empty");
                            username.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                            field.setTextColor(Color.parseColor("#F75010"));
                            return;
                        } else if (hidden.getText().length() == 0) {
                            password.setErrorEnabled(true);
                            password.setError("Please Input Password");
                            password.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                            hidden.setTextColor(Color.parseColor("#F75010"));
                            return;
                        } else if (second.getText().length() == 0) {
                            confirm.setErrorEnabled(true);
                            confirm.setError("Please Repeat Your Password");
                            confirm.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                            second.setTextColor(Color.parseColor("#F75010"));
                            return;
                        }else if (response.code() == 400) {
                            username.setErrorEnabled(true);
                            username.setError("Username Exists");
                            field.setTextColor(Color.parseColor("#F75010"));
                            return;
                        }
                        utils.login(field.getText().toString(), hidden.getText().toString());
                        utils.updateRetrofit();
                        getActivity().runOnUiThread(() -> {
                            viewPager.setCurrentItem(1);
                        });
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                        username.setErrorEnabled(true);
                        username.setError("");
                        field.setTextColor(Color.parseColor("#F75010"));
                        password.setErrorEnabled(true);
                        password.setError("");
                        hidden.setTextColor(Color.parseColor("#F75010"));
                        confirm.setErrorEnabled(true);
                        confirm.setError("Something Went Wrong");
                        second.setTextColor(Color.parseColor("#F75010"));
                    }
                });
    }

    public boolean isComplete() {
        return !username.isErrorEnabled() && !password.isErrorEnabled() && !confirm.isErrorEnabled();
    }

    public String getUsername(){
        return field.getText().toString();
    }

    public String getPassword(){
        return hidden.getText().toString();
    }
}