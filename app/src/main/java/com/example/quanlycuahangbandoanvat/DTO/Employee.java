package com.example.quanlycuahangbandoanvat.DTO;

import java.util.Date;

public class Employee {
    private String Empl_ID;
    private String Empl_name;
    private String Empl_Address;
    private String Empl_Email;
    private String Empl_Password;
    private String Empl_Phone;
    private Date Date_work;
    private String Role_ID;

    public Employee() {
    }

    public Employee(String empl_ID, String empl_name, String empl_Address, String empl_Email, String empl_Password, String empl_Phone, Date date_work, String role_ID) {
        Empl_ID = empl_ID;
        Empl_name = empl_name;
        Empl_Address = empl_Address;
        Empl_Email = empl_Email;
        Empl_Password = empl_Password;
        Empl_Phone = empl_Phone;
        Date_work = date_work;
        Role_ID = role_ID;
    }

    public String getEmpl_ID() {
        return Empl_ID;
    }

    public void setEmpl_ID(String empl_ID) {
        Empl_ID = empl_ID;
    }

    public String getEmpl_name() {
        return Empl_name;
    }

    public void setEmpl_name(String empl_name) {
        Empl_name = empl_name;
    }

    public String getEmpl_Address() {
        return Empl_Address;
    }

    public void setEmpl_Address(String empl_Address) {
        Empl_Address = empl_Address;
    }

    public String getEmpl_Email() {
        return Empl_Email;
    }

    public void setEmpl_Email(String empl_Email) {
        Empl_Email = empl_Email;
    }

    public String getEmpl_Password() {
        return Empl_Password;
    }

    public void setEmpl_Password(String empl_Password) {
        Empl_Password = empl_Password;
    }

    public String getEmpl_Phone() {
        return Empl_Phone;
    }

    public void setEmpl_Phone(String empl_Phone) {
        Empl_Phone = empl_Phone;
    }

    public Date getDate_work() {
        return Date_work;
    }

    public void setDate_work(Date date_work) {
        Date_work = date_work;
    }

    public String getRole_ID() {
        return Role_ID;
    }

    public void setRole_ID(String role_ID) {
        Role_ID = role_ID;
    }
}
