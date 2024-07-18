package com.example.quanlycuahangbandoanvat.Helper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

    public static String FormatVND(double vnd) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(vnd) + "đ";
    }
    public static String FormatTime(Date thoigian) {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm");
        return formatDate.format(thoigian);
    }
}
