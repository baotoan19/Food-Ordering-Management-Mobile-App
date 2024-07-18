package com.example.quanlycuahangbandoanvat.BUS;

import com.example.quanlycuahangbandoanvat.DTO.Notification;
import com.example.quanlycuahangbandoanvat.Helper.NotificationDateComparator;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationBUS {
    private ArrayList<Notification> listNotification = new ArrayList<>();

    public ArrayList<Notification> getListNotification() {
        return listNotification;
    }

    public void setListNotification(ArrayList<Notification> listNotification) {
        this.listNotification = listNotification;
    }

    public NotificationBUS(ArrayList<Notification> listNotification) {

        this.listNotification = listNotification;
    }

    public NotificationBUS() {

    }
    public ArrayList<Notification> getAll() {
        return this.listNotification;
    }

    public Notification getByIndex(int index) {
        return this.listNotification.get(index);
    }

    public int getIndexByNotificationID(String id) {
        int i = 0;
        int vitri = -1;
        while (i < this.listNotification.size() && vitri == -1) {
            if (listNotification.get(i).getNotification_ID().equals(id)) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }
//    public ArrayList<Notification> getListNotificationByCustomerID(String id) {
//        ArrayList<Notification> result = new ArrayList<>();
//        for(Notification item: this.listNotification) {
//            if(item.getReceiver_ID().equals(id)) {
//                result.add(item);
//            }
//        }
//        return result;
//    }
    public ArrayList<Notification> getListNotificationByCustomerID(String id) {
        ArrayList<Notification> result = new ArrayList<>();
        for(Notification item: this.listNotification) {
            if(item.getReceiver_ID().equals(id)) {
                result.add(item);
            }
        }
        // sắp xếp theo ngày gần nhất
        Collections.sort(result, new NotificationDateComparator());
        return result;
    }
    public boolean isExistUnnoticedNotificationByCustomerID(String id) {
        // thông báo chưa đọc
        for(Notification item: this.listNotification) {
            if(item.getReceiver_ID().equals(id)) {
                if(item.getNotification_Status() == false) {
                    return  true;
                }
            }
        }
        return false;
    }
}
