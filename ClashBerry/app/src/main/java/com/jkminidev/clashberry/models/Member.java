package com.jkminidev.clashberry.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Member {
    @SerializedName("tag")
    private String tag;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("townhallLevel")
    private int townhallLevel;
    
    @SerializedName("thEmoji")
    private String thEmoji;
    
    @SerializedName("mapPosition")
    private int mapPosition;
    
    @SerializedName("attacks")
    private List<Attack> attacks;
    
    @SerializedName("attacksUsed")
    private int attacksUsed;
    
    @SerializedName("opponentAttacks")
    private int opponentAttacks;

    // Constructors
    public Member() {}

    public Member(String tag, String name, int townhallLevel, String thEmoji, 
                 int mapPosition, List<Attack> attacks, int attacksUsed, int opponentAttacks) {
        this.tag = tag;
        this.name = name;
        this.townhallLevel = townhallLevel;
        this.thEmoji = thEmoji;
        this.mapPosition = mapPosition;
        this.attacks = attacks;
        this.attacksUsed = attacksUsed;
        this.opponentAttacks = opponentAttacks;
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

    public int getTownhallLevel() {
        return townhallLevel;
    }

    public void setTownhallLevel(int townhallLevel) {
        this.townhallLevel = townhallLevel;
    }

    public String getThEmoji() {
        return thEmoji;
    }

    public void setThEmoji(String thEmoji) {
        this.thEmoji = thEmoji;
    }

    public int getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition = mapPosition;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }

    public int getAttacksUsed() {
        return attacksUsed;
    }

    public void setAttacksUsed(int attacksUsed) {
        this.attacksUsed = attacksUsed;
    }

    public int getOpponentAttacks() {
        return opponentAttacks;
    }

    public void setOpponentAttacks(int opponentAttacks) {
        this.opponentAttacks = opponentAttacks;
    }
}