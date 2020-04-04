package com.example.token.Modals;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("id")
    User user;
    @SerializedName("status")
    int status;

    public LoginResult() {
    }

    public LoginResult(User user, int status) {
        this.user = user;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
