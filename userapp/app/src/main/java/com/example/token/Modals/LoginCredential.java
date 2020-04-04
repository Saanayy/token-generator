package com.example.token.Modals;

import com.google.gson.annotations.SerializedName;

public class LoginCredential {

    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;

    public LoginCredential(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginCredential() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
