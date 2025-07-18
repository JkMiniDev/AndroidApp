package com.jkminidev.clashberry.models;

import com.google.gson.annotations.SerializedName;

public class ClanInfo {
    @SerializedName("tag")
    private String tag;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("badge")
    private String badge;
    
    @SerializedName("level")
    private int level;
    
    @SerializedName("members")
    private int members;
    
    @SerializedName("isWarLogPublic")
    private boolean isWarLogPublic;

    // Constructors
    public ClanInfo() {}

    public ClanInfo(String tag, String name, String badge, int level, int members, boolean isWarLogPublic) {
        this.tag = tag;
        this.name = name;
        this.badge = badge;
        this.level = level;
        this.members = members;
        this.isWarLogPublic = isWarLogPublic;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public boolean isWarLogPublic() {
        return isWarLogPublic;
    }

    public void setWarLogPublic(boolean warLogPublic) {
        isWarLogPublic = warLogPublic;
    }
}