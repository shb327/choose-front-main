package com.example.choose.api;

import android.text.PrecomputedText;

import com.example.choose.dto.GetFeedRequestDTO;
import com.example.choose.dto.RegistrationConfirmationDTO;
import com.example.choose.dto.RegistrationEmailDTO;
import com.example.choose.dto.RegistrationUsernameDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegistrationController {
    @POST("/api/registration/username")
    Call<Void> username(@Body RegistrationUsernameDTO registrationUsernameDTO);

    @POST("/api/registration/email")
    Call<Void> email(@Body RegistrationEmailDTO registrationEmailDTO);

    @POST("/api/registration/confirm")
    Call<Void> confirm(@Body RegistrationConfirmationDTO registrationEmailDTO);
}
