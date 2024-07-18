package com.example.quanlycuahangbandoanvat.Helper;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static Boolean isEmpty(String input) {
        if (input == null) {
            return true;
        }
        return input.equals("");
    }

    public static Boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(\\+84|0)\\d{9,10}$";
        Pattern pattern = Pattern.compile(regex);
        if(phoneNumber == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\(\\)])[A-Za-z\\d@$!%*?&\\(\\)]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public static boolean isNumber(String num) {
        boolean result = true;
        if (num == null) return false;
        try {
            long k = Long.parseLong(num);
            if(k < 0) {
                result = false;
            }
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }
    public static boolean isDoubleNumber(String num) {
        boolean result = true;
        if (num == null) return false;
        try {
            double d = Double.parseDouble(num);
            if(d < 0) {
                result = false;
            }
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }
    public static boolean isValidMonth(int month) {
        return (month >= 1 && month <= 12);
    }
    public static boolean isValidYear(int year) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        return (year <= currentYear);
    }
}
