package com.example.quanlycuahangbandoanvat.BUS;

import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.DTO.Promotion;

import java.util.ArrayList;
import java.util.Collections;

public class PromotionBUS {
    private ArrayList<Promotion> listPromotion = new ArrayList<>();

    public ArrayList<Promotion> getListPromotion() {
        return listPromotion;
    }

    public void setListPromotion(ArrayList<Promotion> listPromotion) {
        this.listPromotion = listPromotion;
    }

    public PromotionBUS(ArrayList<Promotion> listPromotion) {

        this.listPromotion = listPromotion;
    }

    public PromotionBUS() {

    }
    public ArrayList<Promotion> getAll() {
        return this.listPromotion;
    }

    public Promotion getByIndex(int index) {
        return this.listPromotion.get(index);
    }

    public Promotion getByFoodID(String id) {
        int vitri = -1;
        int i = 0;
        while (i <= this.listPromotion.size() && vitri == -1) {
            if (this.listPromotion.get(i).getFood_Promotion_ID().equals(id)) {
                vitri = i;
            } else {
                i++;
            }
        }
        return this.listPromotion.get(vitri);
    }

    public int getIndexByPromotionID(String id) {
        int i = 0;
        int vitri = -1;
        while (i < this.listPromotion.size() && vitri == -1) {
            if (listPromotion.get(i).getFood_Promotion_ID().equals(id)) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public ArrayList<Promotion> getListPromotionAvailablity() {
        ArrayList<Promotion> newPromotion=new ArrayList<>();
        for (Promotion Item : this.listPromotion)
        {
            long millisecondsUntilEnd = Item.getDate_End().getTime() - System.currentTimeMillis();
            if (millisecondsUntilEnd > 0 && Item.getDate_Start().getTime() <= System.currentTimeMillis())
            {
                newPromotion.add(Item);
            }
        }
        return  newPromotion;
    }
}
