package com.example.quanlycuahangbandoanvat.DTO;

import java.util.Date;

public class Rating {
    private String Rating_ID;
    private String Bill_ID;
    private String Rating_comment;
    private Date Rating_date;

    public Rating() {
    }

    public Rating(String rating_ID, String bill_ID, String rating_comment, Date rating_date) {
        Rating_ID = rating_ID;
        Bill_ID = bill_ID;
        Rating_comment = rating_comment;
        Rating_date = rating_date;
    }

    public String getRating_ID() {
        return Rating_ID;
    }

    public void setRating_ID(String rating_ID) {
        Rating_ID = rating_ID;
    }

    public String getBill_ID() {
        return Bill_ID;
    }

    public void setBill_ID(String bill_ID) {
        Bill_ID = bill_ID;
    }

    public String getRating_comment() {
        return Rating_comment;
    }

    public void setRating_comment(String rating_comment) {
        Rating_comment = rating_comment;
    }

    public Date getRating_date() {
        return Rating_date;
    }

    public void setRating_date(Date rating_date) {
        Rating_date = rating_date;
    }
}
