package com.example.quanlycuahangbandoanvat.DAO.Callback;

import com.example.quanlycuahangbandoanvat.DTO.CartDetail;

import java.util.ArrayList;

public interface OnDataLoadedCallbackCartDetail {
    void onDataLoaded(ArrayList<CartDetail> t);
    void onError(String errorMessage);
}
