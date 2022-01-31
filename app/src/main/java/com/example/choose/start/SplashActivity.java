package com.example.choose.start;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.choose.R;
import com.example.choose.retrofit.RetrofitUtils;
import com.example.choose.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RetrofitUtils.getInstance().setAccountManager(AccountManager.get(this));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(RetrofitUtils.getInstance().hasLogin()){
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, StartingActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}