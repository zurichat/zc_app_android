package com.zurichat.app.models;

public class Participant {
    private String username;
    private String status;
    private int profile_photo;

    public Participant(String username, String status, int profile_photo) {
        this.username = username;
        this.status = status;
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public int getProfile_photo() {
        return profile_photo;
    }
}
