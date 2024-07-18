package com.example.quanlycuahangbandoanvat.DTO;

import java.util.Date;

public class Notification {
    private String Notification_ID;
    private String Bill_ID;
    private String Receiver_ID;
    private String Sender_ID;
    private Date Notification_Date;
    private Boolean Notification_Status;
    private String Notification_Content;

    public Notification(){

    }

    public Notification(String notification_ID, String bill_ID, String receiver_ID, String sender_ID, Date notification_Date, Boolean notification_Status, String notification_Content) {
        Notification_ID = notification_ID;
        Bill_ID = bill_ID;
        Receiver_ID = receiver_ID;
        Sender_ID = sender_ID;
        Notification_Date = notification_Date;
        Notification_Status = notification_Status;
        Notification_Content = notification_Content;
    }

    public String getNotification_ID() {
        return Notification_ID;
    }

    public void setNotification_ID(String notification_ID) {
        Notification_ID = notification_ID;
    }

    public String getBill_ID() {
        return Bill_ID;
    }

    public void setBill_ID(String bill_ID) {
        Bill_ID = bill_ID;
    }

    public String getReceiver_ID() {
        return Receiver_ID;
    }

    public void setReceiver_ID(String receiver_ID) {
        Receiver_ID = receiver_ID;
    }

    public String getSender_ID() {
        return Sender_ID;
    }

    public void setSender_ID(String sender_ID) {
        Sender_ID = sender_ID;
    }

    public Date getNotification_Date() {
        return Notification_Date;
    }

    public void setNotification_Date(Date notification_Date) {
        Notification_Date = notification_Date;
    }

    public Boolean getNotification_Status() {
        return Notification_Status;
    }

    public void setNotification_Status(Boolean notification_Status) {
        Notification_Status = notification_Status;
    }

    public String getNotification_Content() {
        return Notification_Content;
    }

    public void setNotification_Content(String notification_Content) {
        Notification_Content = notification_Content;
    }

}
