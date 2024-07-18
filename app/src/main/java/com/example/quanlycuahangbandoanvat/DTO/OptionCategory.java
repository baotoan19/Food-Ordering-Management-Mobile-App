package com.example.quanlycuahangbandoanvat.DTO;

public class OptionCategory {
    private int image;
    private String title;

    public OptionCategory() {
    }

    public OptionCategory(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
