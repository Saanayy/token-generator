package com.example.token.Modals;

import com.google.gson.annotations.SerializedName;

public class PassGenerationResult {

    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private Integer status;

    public PassGenerationResult() {
    }


    public PassGenerationResult(String id, Integer status) {
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
