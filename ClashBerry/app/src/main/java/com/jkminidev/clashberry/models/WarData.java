package com.jkminidev.clashberry.models;

import com.google.gson.annotations.SerializedName;

public class WarData {
    @SerializedName("state")
    private String state;
    
    @SerializedName("teamSize")
    private int teamSize;
    
    @SerializedName("warType")
    private String warType;
    
    @SerializedName("cwlRound")
    private Integer cwlRound;
    
    @SerializedName("timeRemaining")
    private String timeRemaining;
    
    @SerializedName("timeLabel")
    private String timeLabel;
    
    @SerializedName("clan")
    private ClanData clan;
    
    @SerializedName("opponent")
    private ClanData opponent;

    // Constructors
    public WarData() {}

    public WarData(String state, int teamSize, String warType, Integer cwlRound, 
                   String timeRemaining, String timeLabel, ClanData clan, ClanData opponent) {
        this.state = state;
        this.teamSize = teamSize;
        this.warType = warType;
        this.cwlRound = cwlRound;
        this.timeRemaining = timeRemaining;
        this.timeLabel = timeLabel;
        this.clan = clan;
        this.opponent = opponent;
    }

    // Getters and Setters
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public String getWarType() {
        return warType;
    }

    public void setWarType(String warType) {
        this.warType = warType;
    }

    public Integer getCwlRound() {
        return cwlRound;
    }

    public void setCwlRound(Integer cwlRound) {
        this.cwlRound = cwlRound;
    }

    public String getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(String timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String timeLabel) {
        this.timeLabel = timeLabel;
    }

    public ClanData getClan() {
        return clan;
    }

    public void setClan(ClanData clan) {
        this.clan = clan;
    }

    public ClanData getOpponent() {
        return opponent;
    }

    public void setOpponent(ClanData opponent) {
        this.opponent = opponent;
    }
}