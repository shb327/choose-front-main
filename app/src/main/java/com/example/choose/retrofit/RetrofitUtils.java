package com.example.choose.retrofit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;

import com.example.choose.dto.PostDTO;
import com.example.choose.dto.PostDTODeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static final String TYPE_ACCOUNT = "com.example.choose";
    private static final RetrofitUtils INSTANCE = new RetrofitUtils();
    private Retrofit retrofit;
    private AccountManager accountManager;

    private RetrofitUtils() { }

    public static RetrofitUtils getInstance() {
        return INSTANCE;
    }

    public Retrofit getRetrofit() {
        if (retrofit == null) { updateRetrofit(); }
        return retrofit;
    }

    public void updateRetrofit() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PostDTO.class, new PostDTODeserializer()).create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://choose.teheidoma.com/")
                .addConverterFactory(gsonConverterFactory)
                .client(new OkHttpClient.Builder()
                        .followRedirects(false)
                        .addInterceptor(chain -> {
                            Request.Builder builder = chain
                                    .request()
                                    .newBuilder();
                            if (hasLogin()) {
                                builder.addHeader("Authorization", getAuthHeader());
                            }
                            return chain.proceed(builder.build());
                        }).build())
                .build();
    }

    @SuppressLint("NewApi")
    private String getAuthHeader() {
        Account account = accountManager.getAccountsByType(TYPE_ACCOUNT)[0];
        String value = account.name + ":" + accountManager.getPassword(account);
        String str = Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.US_ASCII));
        return "Basic " + str;
    }

    public boolean hasLogin() {
        return accountManager.getAccountsByType(TYPE_ACCOUNT).length > 0;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void deleteAccountManager(AccountManager accountManager) {
        accountManager.removeAccount(
                accountManager.getAccountsByType(TYPE_ACCOUNT)[0], null, null);
    }

    public void login(String login, String password) {
        Account account = new Account(login, TYPE_ACCOUNT);
        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setAuthToken(account, TYPE_ACCOUNT, null);
    }

    public String getUsername(){
        Account account = accountManager.getAccountsByType(TYPE_ACCOUNT)[0];
        return account.name;
    }
}
