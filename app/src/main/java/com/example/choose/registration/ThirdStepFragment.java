package com.example.choose.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.choose.R;
import com.example.choose.login.LoginActivity;
import com.example.choose.retrofit.RetrofitUtils;
import com.example.choose.api.LoginController;
import com.example.choose.api.RegistrationController;
import com.example.choose.dto.RegistrationConfirmationDTO;
import com.example.choose.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThirdStepFragment extends Fragment {

    TextInputLayout cardOne;
    TextInputEditText one;

    TextInputLayout cardTwo;
    TextInputEditText two;

    TextInputLayout cardThree;
    TextInputEditText three;

    TextInputLayout cardFour;
    TextInputEditText four;

    TextView error;

    RegistrationController registrationController;

    public ThirdStepFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_three, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardOne = view.findViewById(R.id.field1CardView);
        one = view.findViewById(R.id.field1);

        cardTwo = view.findViewById(R.id.field2CardView);
        two = view.findViewById(R.id.field2);

        cardThree = view.findViewById(R.id.field3CardView);
        three = view.findViewById(R.id.field3);

        cardFour = view.findViewById(R.id.field4CardView);
        four = view.findViewById(R.id.field4);

        error = view.findViewById(R.id.errortext);

        /* Assign the TextViews in the array in the order in which you want to shift focus */
        TextInputEditText[] fields = {one, two, three, four};

        for (TextInputEditText currfield : fields) {
            currfield.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nextEdittext().requestFocus();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    one.setTextColor(Color.parseColor("#56C596"));

                    two.setTextColor(Color.parseColor("#56C596"));

                    three.setTextColor(Color.parseColor("#56C596"));

                    four.setTextColor(Color.parseColor("#56C596"));

                    cardOne.setErrorEnabled(false);
                    one.setTextColor(Color.parseColor("#56C596"));
                    cardTwo.setErrorEnabled(false);
                    two.setTextColor(Color.parseColor("#56C596"));
                    cardThree.setErrorEnabled(false);
                    three.setTextColor(Color.parseColor("#56C596"));
                    cardFour.setErrorEnabled(false);
                    four.setTextColor(Color.parseColor("#56C596"));

                    error.setTextColor(Color.WHITE);
                    error.setText("Please double-check your verification code");
                }

                public TextInputEditText nextEdittext() {
                    int i;
                    for (i = 0; i < fields.length - 1; i++) {
                        if (fields[i] == currfield)
                            return fields[i + 1];
                    }
                    return fields[i];
                }
            });
        }
    }

    public void send(ViewPager viewPager, LoginController controller, String username, String password, Context context){
        RetrofitUtils utils = RetrofitUtils.getInstance();
        registrationController = utils.getRetrofit().create(RegistrationController.class);
        registrationController.confirm(
                new RegistrationConfirmationDTO(one.getText().toString() + two.getText().toString() +
                        three.getText().toString() + four.getText().toString())).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (one.getText().length() == 0 || two.getText().length() == 0 || three.getText().length() == 0 || four.getText().length() == 0) {
                    cardOne.setErrorEnabled(true);
                    one.setTextColor(Color.parseColor("#F75010"));

                    cardTwo.setErrorEnabled(true);
                    two.setTextColor(Color.parseColor("#F75010"));

                    cardThree.setErrorEnabled(true);
                    three.setTextColor(Color.parseColor("#F75010"));

                    cardFour.setErrorEnabled(true);
                    four.setTextColor(Color.parseColor("#F75010"));

                    error.setText("Please provide some input for all four fields");
                    error.setTextColor(Color.parseColor("#F75010"));

                    return;
                }else if(response.code() == 500){
                    cardOne.setErrorEnabled(true);
                    one.setTextColor(Color.parseColor("#F75010"));

                    cardTwo.setErrorEnabled(true);
                    two.setTextColor(Color.parseColor("#F75010"));

                    cardThree.setErrorEnabled(true);
                    three.setTextColor(Color.parseColor("#F75010"));

                    cardFour.setErrorEnabled(true);
                    four.setTextColor(Color.parseColor("#F75010"));

                    error.setText("Please, double-check your verification code");
                    error.setTextColor(Color.parseColor("#F75010"));

                    return;
                }
                controller.login(username, password)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                utils.login(username, password);
                                utils.updateRetrofit();
                                startActivity(new Intent(context, HomeActivity.class));

                                Log.i("Login", response.raw().request().headers().toString());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Login Error", t.getMessage(), t);
                            }
                        });
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                cardOne.setErrorEnabled(true);
                cardOne.setError("1");
                one.setTextColor(Color.parseColor("#F75010"));
                cardTwo.setErrorEnabled(true);
                cardTwo.setError("2");
                two.setTextColor(Color.parseColor("#F75010"));
                cardThree.setErrorEnabled(true);
                cardThree.setError("3");
                three.setTextColor(Color.parseColor("#F75010"));
                cardFour.setErrorEnabled(true);
                cardFour.setError("4");
                four.setTextColor(Color.parseColor("#F75010"));
                error.setTextColor(Color.parseColor("#F75010"));
            }
        });
    }

    public boolean isComplete() {
        return !cardOne.isErrorEnabled() && !cardTwo.isErrorEnabled() && !cardThree.isErrorEnabled() && !cardFour.isErrorEnabled();
    }
}