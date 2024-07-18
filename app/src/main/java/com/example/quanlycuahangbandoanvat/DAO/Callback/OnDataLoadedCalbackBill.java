package com.example.quanlycuahangbandoanvat.DAO.Callback;

import com.example.quanlycuahangbandoanvat.DTO.Bill;


import java.util.ArrayList;

public interface OnDataLoadedCalbackBill {
    void onDataLoaded(ArrayList<Bill> t);
    void onError(String errorMessage);
}
