package com.example.quanlycuahangbandoanvat.DAO.Callback;

import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.Food;

import java.util.ArrayList;

public interface OnDataLoadedCallbackCustomer {
    void onDataLoaded(ArrayList<Customer> t);
    void onError(String errorMessage);
}
