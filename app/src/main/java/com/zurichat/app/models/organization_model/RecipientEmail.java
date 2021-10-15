package com.zurichat.app.models.organization_model;

public class RecipientEmail {
    private int email_img;
    private String email;
    private int delete_btn;

    public RecipientEmail(String email) {
        this.email = email;
    }

    public int getEmail_img() {
        return email_img;
    }

    public String getEmail() {
        return email;
    }

    public int getDelete_btn() {
        return delete_btn;
    }
}
