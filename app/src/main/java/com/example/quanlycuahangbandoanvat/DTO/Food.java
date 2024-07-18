package com.example.quanlycuahangbandoanvat.DTO;

import java.io.Serializable;

public class Food implements Serializable {
    private String Food_ID;
    private String Food_Name;
    private String Food_Description;
    private double Food_Price;
    private String Food_Status;
    private String Food_Promotion_ID;
    private String Food_Category_ID;
    private int Food_Quantity_Purchased;
    private boolean Food_isDeleted;
    private String Food_Image;

    public Food() {
    }

    public Food(String food_ID, String food_Name, String food_Description, double food_Price, String food_Status, String food_Promotion_ID, String food_Category_ID, int food_Quantity_Purchased, boolean food_isDeleted, String food_Image) {
        Food_ID = food_ID;
        Food_Name = food_Name;
        Food_Description = food_Description;
        Food_Price = food_Price;
        Food_Status = food_Status;
        Food_Promotion_ID = food_Promotion_ID;
        Food_Category_ID = food_Category_ID;
        Food_Quantity_Purchased = food_Quantity_Purchased;
        Food_isDeleted = food_isDeleted;
        Food_Image = food_Image;
    }

    public String getFood_ID() {
        return Food_ID;
    }

    public void setFood_ID(String food_ID) {
        Food_ID = food_ID;
    }

    public String getFood_Name() {
        return Food_Name;
    }

    public void setFood_Name(String food_Name) {
        Food_Name = food_Name;
    }

    public String getFood_Description() {
        return Food_Description;
    }

    public void setFood_Description(String food_Description) {
        Food_Description = food_Description;
    }

    public double getFood_Price() {
        return Food_Price;
    }

    public void setFood_Price(double food_Price) {
        Food_Price = food_Price;
    }

    public String getFood_Status() {
        return Food_Status;
    }

    public void setFood_Status(String food_Status) {
        Food_Status = food_Status;
    }

    public String getFood_Promotion_ID() {
        return Food_Promotion_ID;
    }

    public void setFood_Promotion_ID(String food_Promotion_ID) {
        Food_Promotion_ID = food_Promotion_ID;
    }

    public String getFood_Category_ID() {
        return Food_Category_ID;
    }

    public void setFood_Category_ID(String food_Category_ID) {
        Food_Category_ID = food_Category_ID;
    }

    public int getFood_Quantity_Purchased() {
        return Food_Quantity_Purchased;
    }

    public void setFood_Quantity_Purchased(int food_Quantity_Purchased) {
        Food_Quantity_Purchased = food_Quantity_Purchased;
    }

    public boolean isFood_isDeleted() {
        return Food_isDeleted;
    }

    public void setFood_isDeleted(boolean food_isDeleted) {
        Food_isDeleted = food_isDeleted;
    }

    public String getFood_Image() {
        return Food_Image;
    }

    public void setFood_Image(String food_Image) {
        Food_Image = food_Image;
    }
}
