package com.hbt.yiqing.entity;

import java.util.LinkedList;
import java.util.List;

public class YQInfo {
    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    private String continent;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getNowConfirm() {
        return nowConfirm;
    }

    public void setNowConfirm(int nowConfirm) {
        this.nowConfirm = nowConfirm;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public int getSuspect() {
        return suspect;
    }

    public void setSuspect(int suspect) {
        this.suspect = suspect;
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        this.heal = heal;
    }

    public List<YQInfo> getChildren() {
        return children;
    }

    public void setChildren(List<YQInfo> children) {
        this.children = children;
    }

    private String name;
    private int confirm;
    private int nowConfirm;
    private int dead;
    private int suspect;
    private int heal;
    private List<YQInfo> children = new LinkedList<>();

}
