package com.example.choose.registration;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.choose.R;
import com.example.choose.retrofit.RetrofitUtils;
import com.example.choose.api.RegistrationController;
import com.example.choose.dto.RegistrationEmailDTO;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondStepFragment extends Fragment {

    TextInputLayout email;
    TextInputEditText field;

    TextInputLayout first;
    TextInputEditText name;

    TextInputLayout last;
    TextInputEditText surname;

    RegistrationController registrationController;

    public SecondStepFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.outlinedTextField1);
        field = view.findViewById(R.id.email);

        first = view.findViewById(R.id.outlinedTextField2);
        name = view.findViewById(R.id.first);

        last = view.findViewById(R.id.outlinedTextField3);
        surname = view.findViewById(R.id.last);

        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!field.getText().toString().matches("^[a-zA-Z0-9_!#$%&’*+\\=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
                    email.setErrorEnabled(true);
                    email.setError("Invalid Email Adress");
                    field.setTextColor(Color.parseColor("#F75010"));
                }else if(field.getText().length()== 60){
                    email.setErrorEnabled(true);
                    email.setError("Character Limit Exceeded");
                    field.setTextColor(Color.parseColor("#F75010"));
                }else {
                    email.setErrorEnabled(false);
                    email.setError(null);
                    field.setTextColor(Color.parseColor("#56C596"));
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!name.getText().toString().matches("^[a-zA-Z0-9._-]{3,}$")){
                    first.setErrorEnabled(true);
                    first.setError("Invalid Input");
                    name.setTextColor(Color.parseColor("#F75010"));
                }else if(name.getText().length()== 20){
                    first.setErrorEnabled(true);
                    first.setError("Character Limit Exceeded");
                    name.setTextColor(Color.parseColor("#F75010"));
                }else {
                    first.setErrorEnabled(false);
                    first.setError(null);
                    name.setTextColor(Color.parseColor("#56C596"));
                }
            }
        });

        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!name.getText().toString().matches("^[a-zA-Z0-9._-]{3,}$")){
                    last.setErrorEnabled(true);
                    last.setError("Invalid Input");
                    surname.setTextColor(Color.parseColor("#F75010"));
                }else if(name.getText().length()== 20){
                    last.setErrorEnabled(true);
                    last.setError("Character Limit Exceeded");
                    surname.setTextColor(Color.parseColor("#F75010"));
                }else {
                    last.setErrorEnabled(false);
                    last.setError(null);
                    surname.setTextColor(Color.parseColor("#56C596"));
                }
            }
        });
    }

    public void send(ViewPager viewPager, Button button){
        RetrofitUtils utils = RetrofitUtils.getInstance();
        registrationController = utils.getRetrofit().create(RegistrationController.class);
        registrationController.email(
                new RegistrationEmailDTO(
                        field.getText().toString(),
                        name.getText().toString(),
                        surname.getText().toString()))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (field.getText().length() == 0) {
                            email.setErrorEnabled(true);
                            email.setError("Email Can't be Empty");
                            email.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                            field.setTextColor(Color.parseColor("#F75010"));
                            return;
                        } else if (name.getText().length() == 0) {
                            first.setErrorEnabled(true);
                            first.setError("Name Can't be Empty");
                            first.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                            name.setTextColor(Color.parseColor("#F75010"));
                            return;
                        } else if (surname.getText().length() == 0) {
                            last.setErrorEnabled(true);
                            last.setError("Name Can't be Empty");
                            last.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                            surname.setTextColor(Color.parseColor("#F75010"));
                            return;
                        }
                        getActivity().runOnUiThread(() -> viewPager.setCurrentItem(2));
                        button.setText("Finish");
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                        email.setErrorEnabled(true);
                        email.setError("");
                        field.setTextColor(Color.parseColor("#F75010"));
                        first.setErrorEnabled(true);
                        first.setError("");
                        name.setTextColor(Color.parseColor("#F75010"));
                        last.setErrorEnabled(true);
                        last.setError("Something Went Wrong");
                        surname.setTextColor(Color.parseColor("#F75010"));
                    }
                });
    }

    public boolean isComplete() {
        return !email.isErrorEnabled() && !first.isErrorEnabled() && !last.isErrorEnabled();
    }
}