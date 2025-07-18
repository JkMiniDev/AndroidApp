package com.jkminidev.clashberry.models;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("error")
    private String error;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("clan")
    private ClanInfo clan;

    // Constructors
    public ErrorResponse() {}

    public ErrorResponse(String error, String message, ClanInfo clan) {
        this.error = error;
        this.message = message;
        this.clan = clan;
    }

    // Getters and Setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClanInfo getClan() {
        return clan;
    }

    public void setClan(ClanInfo clan) {
        this.clan = clan;
    }
}