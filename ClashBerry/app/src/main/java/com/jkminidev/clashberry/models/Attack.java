package com.jkminidev.clashberry.models;

import com.google.gson.annotations.SerializedName;

public class Attack {
    @SerializedName("defenderTag")
    private String defenderTag;
    
    @SerializedName("stars")
    private int stars;
    
    @SerializedName("destructionPercentage")
    private double destructionPercentage;

    // Constructors
    public Attack() {}

    public Attack(String defenderTag, int stars, double destructionPercentage) {
        this.defenderTag = defenderTag;
        this.stars = stars;
        this.destructionPercentage = destructionPercentage;
    }

    // Getters and Setters
    public String getDefenderTag() {
        return defenderTag;
    }

    public void setDefenderTag(String defenderTag) {
        this.defenderTag = defenderTag;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public double getDestructionPercentage() {
        return destructionPercentage;
    }

    public void setDestructionPercentage(double destructionPercentage) {
        this.destructionPercentage = destructionPercentage;
    }
}