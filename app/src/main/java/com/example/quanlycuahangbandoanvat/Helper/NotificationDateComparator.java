package com.example.quanlycuahangbandoanvat.Helper;

import com.example.quanlycuahangbandoanvat.DTO.Notification;

import java.util.Comparator;

public class NotificationDateComparator implements Comparator<Notification> {
    @Override
    public int compare(Notification n1, Notification n2) {
        // Sắp xếp từ ngày gần nhất đến xa nhất
        return n2.getNotification_Date().compareTo(n1.getNotification_Date());
    }
}