package com.example.quanlycuahangbandoanvat.DTO;

public class Category {
    private String Food_Category_ID;
    private String Category_Name;

    public Category() {
    }

    public Category(String food_Category_ID, String category_Name) {
        Food_Category_ID = food_Category_ID;
        Category_Name = category_Name;
    }

    public String getFood_Category_ID() {
        return Food_Category_ID;
    }

    public void setFood_Category_ID(String food_Category_ID) {
        Food_Category_ID = food_Category_ID;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }
}
