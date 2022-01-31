package com.example.choose.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginController {
    @POST("/login")
    Call<Void> login (@Query("username") String username, @Query("password") String password);
}
