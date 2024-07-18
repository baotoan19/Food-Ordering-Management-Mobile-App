package com.example.quanlycuahangbandoanvat.DAO.Callback;

import com.example.quanlycuahangbandoanvat.DTO.Notification;

import java.util.ArrayList;

public interface OnDataLoadedCallbackNotification {
    void onDataLoaded(ArrayList<Notification> t);
    void onDataLoadedSingle(Notification t);
    void onError(String errorMessage);
}
