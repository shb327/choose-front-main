package com.example.choose.login;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.choose.R;
import com.example.choose.registration.FirstStepFragment;
import com.example.choose.registration.Registration;
import com.example.choose.retrofit.RetrofitUtils;
import com.example.choose.api.LoginController;
import com.example.choose.home.HomeActivity;
import com.example.choose.start.StartingActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText password;
    TextInputLayout passwordLayout;
    TextInputLayout emailLayout;
    TextInputEditText email;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signup = (TextView)findViewById(R.id.signup_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Registration.class));
            }
        });

        Button btn = findViewById(R.id.login_button);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        passwordLayout = findViewById(R.id.passwordLayout);
        emailLayout = findViewById(R.id.emailLayout);

        setTextWatcher();
        txt = findViewById(R.id.textView9);

        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        RetrofitUtils utils = RetrofitUtils.getInstance();

        LoginController controller = RetrofitUtils.getInstance().getRetrofit().create(LoginController.class);
        btn.setOnClickListener(v -> {
            if(email.getText().length() == 0 || password.getText().length() == 0){
                email.setTextColor(Color.parseColor("#F75010"));
                email.setHintTextColor(Color.parseColor("#F75010"));
                email.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_round_highlight_off_24, 0);
                password.setTextColor(Color.parseColor("#F75010"));
                password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_round_highlight_off_24, 0);
                password.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                passwordLayout.setEndIconTintList(ColorStateList.valueOf(Color.parseColor("#F75010")));
                txt.setText("Please input your data");
                txt.setTextColor(Color.parseColor("#F75010"));
                txt.startAnimation(shake);
                emailLayout.startAnimation(shake);
                passwordLayout.startAnimation(shake);
                return;
            }
            else {
                controller.login(email.getText().toString(), password.getText().toString())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.i("post", response.raw().request().headers().toString());
                                if (response.code() == 200 || response.code() == 302) {
                                    utils.login(email.getText().toString(), password.getText().toString());
                                    utils.updateRetrofit();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                } else {
                                    email.setTextColor(Color.parseColor("#F75010"));
                                    email.setHintTextColor(Color.parseColor("#F75010"));
                                    email.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_round_highlight_off_24, 0);
                                    password.setTextColor(Color.parseColor("#F75010"));
                                    password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_round_highlight_off_24, 0);
                                    passwordLayout.setEndIconTintList(ColorStateList.valueOf(Color.parseColor("#F75010")));
                                    passwordLayout.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                                    passwordLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#F75010")));
                                    txt.setTextColor(Color.parseColor("#F75010"));
                                    txt.startAnimation(shake);
                                    txt.setText("No matching records");
                                    emailLayout.startAnimation(shake);
                                    passwordLayout.startAnimation(shake);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Login Error", t.getMessage(), t);
                            }
                        });
            }
        });
    }

    public void setTextWatcher(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#56C596")));
                password.setTextColor(Color.parseColor("#56C596"));
                password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                passwordLayout.setEndIconTintList(ColorStateList.valueOf(Color.parseColor("#56C596")));
                email.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                email.setTextColor(Color.parseColor("#56C596"));
                email.setHintTextColor(Color.parseColor("#56C596"));
                txt.setText(null);
            }
        };
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }

}