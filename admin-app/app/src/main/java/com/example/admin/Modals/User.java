package com.example.admin.Modals;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("token")
    private String token;
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("passes")
    private List<String> passes;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private String address;
    @SerializedName("zone")
    private  String zone;

    public User() {
    }

    public User(String token, String id, String name, String email, String password, List<String> passes, String phone) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.passes = passes;
        this.phone = phone;
        this.address = address;
        this.zone = zone;
    }

    public User(String name, String email, String password, List<String> passes, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passes = passes;
        this.phone = phone;
        this.address = address;
        this.zone = zone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getPasses() {
        return passes;
    }

    public void setPasses(List<String> passes) {
        this.passes = passes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
