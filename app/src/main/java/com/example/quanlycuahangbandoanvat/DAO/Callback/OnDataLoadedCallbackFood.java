package com.example.quanlycuahangbandoanvat.DAO.Callback;

import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.DTO.User;

import java.util.ArrayList;

public interface OnDataLoadedCallbackFood {
    void onDataLoaded(ArrayList<Food> t);
    void onError(String errorMessage);
}
