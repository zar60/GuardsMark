package com.example.guardsmark;

import android.graphics.Bitmap;

public class UserInfoData {
    private String name;
    private double distance;
    private Bitmap profile_pic;

    public UserInfoData() {
    }

    public UserInfoData(String name, double distance, Bitmap profile_pic) {
        this.name = name;
        this.distance = distance;
        this.profile_pic = profile_pic;
    }
    public UserInfoData(String name, double distance) {
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Bitmap getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(Bitmap profile_pic) {
        this.profile_pic = profile_pic;
    }
}
