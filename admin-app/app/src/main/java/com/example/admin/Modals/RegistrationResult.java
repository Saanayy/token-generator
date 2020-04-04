package com.example.admin.Modals;

import com.google.gson.annotations.SerializedName;

public class RegistrationResult {

    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private Integer status;

    public RegistrationResult() {
    }


    public RegistrationResult(String id, Integer status) {
        super();
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}