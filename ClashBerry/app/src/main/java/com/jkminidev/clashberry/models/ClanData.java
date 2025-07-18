package com.jkminidev.clashberry.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ClanData {
    @SerializedName("tag")
    private String tag;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("badge")
    private String badge;
    
    @SerializedName("stars")
    private int stars;
    
    @SerializedName("attacks")
    private int attacks;
    
    @SerializedName("destructionPercentage")
    private double destructionPercentage;
    
    @SerializedName("members")
    private List<Member> members;

    // Constructors
    public ClanData() {}

    public ClanData(String tag, String name, String badge, int stars, int attacks, 
                   double destructionPercentage, List<Member> members) {
        this.tag = tag;
        this.name = name;
        this.badge = badge;
        this.stars = stars;
        this.attacks = attacks;
        this.destructionPercentage = destructionPercentage;
        this.members = members;
    }

    // Getters and Setters
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getAttacks() {
        return attacks;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public double getDestructionPercentage() {
        return destructionPercentage;
    }

    public void setDestructionPercentage(double destructionPercentage) {
        this.destructionPercentage = destructionPercentage;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}