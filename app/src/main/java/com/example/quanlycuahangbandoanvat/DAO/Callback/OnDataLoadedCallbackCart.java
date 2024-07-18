package com.example.quanlycuahangbandoanvat.DAO.Callback;

import com.example.quanlycuahangbandoanvat.DTO.Cart;

import java.util.ArrayList;

public interface OnDataLoadedCallbackCart {
    void onDataLoaded(ArrayList<Cart> t);
    void onDataLoadedSigle(Cart cart);
    void onError(String errorMessage);
}
