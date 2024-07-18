package com.example.quanlycuahangbandoanvat.DTO;


import java.util.Date;

public class Promotion {
    private String Food_Promotion_ID;
    private String Promotion_Name;
    private Date Date_Start;
    private Date Date_End;
    private boolean IsDeleted;
    private int Discount_Amount;
    private  double Total_min;

    public Promotion() {
    }

    public Promotion(String food_Promotion_ID, String promotion_Name, Date date_Start, Date date_End, boolean IsDeleted, int discount_Amount, double Total_min) {
        Food_Promotion_ID = food_Promotion_ID;
        Promotion_Name = promotion_Name;
        Date_Start = date_Start;
        Date_End = date_End;
        this.IsDeleted = IsDeleted;
        Discount_Amount = discount_Amount;
        this.Total_min = Total_min;
    }

    public double getTotal_min() {
        return Total_min;
    }

    public void setTotal_min(double total_min) {
        Total_min = total_min;
    }

    public String getFood_Promotion_ID() {
        return Food_Promotion_ID;
    }

    public void setFood_Promotion_ID(String food_Promotion_ID) {
        Food_Promotion_ID = food_Promotion_ID;
    }

    public String getPromotion_Name() {
        return Promotion_Name;
    }

    public void setPromotion_Name(String promotion_Name) {
        Promotion_Name = promotion_Name;
    }

    public Date getDate_Start() {
        return Date_Start;
    }

    public void setDate_Start(Date date_Start) {
        Date_Start = date_Start;
    }

    public Date getDate_End() {
        return Date_End;
    }

    public void setDate_End(Date date_End) {
        Date_End = date_End;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public double getDiscount_Amount() {
        return Discount_Amount;
    }

    public void setDiscount_Amount(int discount_Amount) {
        Discount_Amount = discount_Amount;
    }
}
