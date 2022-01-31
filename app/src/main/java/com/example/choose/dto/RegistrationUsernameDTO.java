package com.example.choose.dto;

public class RegistrationUsernameDTO {
    private String username;
    private String password;

    public RegistrationUsernameDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //JSON Init
    public RegistrationUsernameDTO() { }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
