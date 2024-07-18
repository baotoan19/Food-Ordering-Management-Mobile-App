package com.example.quanlycuahangbandoanvat.DAO.Callback;


import com.example.quanlycuahangbandoanvat.DTO.User;

import java.util.ArrayList;

public interface OnDataLoadedCallback {
    void onDataLoaded(ArrayList<User> t);
    void onError(String errorMessage);
}
