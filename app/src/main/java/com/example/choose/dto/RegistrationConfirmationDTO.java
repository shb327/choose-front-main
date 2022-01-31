package com.example.choose.dto;

public class RegistrationConfirmationDTO {
    String code;

    public RegistrationConfirmationDTO(String code) {
        this.code = code;
    }

    public RegistrationConfirmationDTO() { }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
