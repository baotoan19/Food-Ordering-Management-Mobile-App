package com.example.quanlycuahangbandoanvat.DTO;

import java.io.Serializable;

public class Customer  implements Serializable {
    private String Cus_ID;
    private String Cus_Name;
    private String Cus_Email;
    private String Cus_Address;
    private String Cus_Sex;
    private String Cus_Password;
    private String Cus_Phone;

    public Customer() {
    }

    public Customer(String Cus_ID, String Cus_Name, String Cus_Email, String Cus_Address, String Cus_Sex, String Cus_Password, String Cus_Phone) {
        this.Cus_ID = Cus_ID;
        this.Cus_Name = Cus_Name;
        this.Cus_Email = Cus_Email;
        this.Cus_Address = Cus_Address;
        this.Cus_Sex = Cus_Sex;
        this.Cus_Password = Cus_Password;
        this.Cus_Phone = Cus_Phone;
    }


    public String getCus_ID() {
        return Cus_ID;
    }

    public void setCus_ID(String Cus_ID) {
        this.Cus_ID = Cus_ID;
    }

    public String getCus_Name() {
        return Cus_Name;
    }

    public void setCus_Name(String Cus_Name) {
        this.Cus_Name = Cus_Name;
    }

    public String getCus_Email() {
        return Cus_Email;
    }

    public void setCus_Email(String Cus_Email) {
        this.Cus_Email = Cus_Email;
    }

    public String getCus_Address() {
        return Cus_Address;
    }

    public void setCus_Address(String Cus_Address) {
        this.Cus_Address = Cus_Address;
    }

    public String getCus_Sex() {
        return Cus_Sex;
    }

    public void setCus_Sex(String Cus_Sex) {
        this.Cus_Sex = Cus_Sex;
    }

    public String getCus_Password() {
        return Cus_Password;
    }

    public void setCus_Password(String Cus_Password) {
        this.Cus_Password = Cus_Password;
    }

    public String getCus_Phone() {
        return Cus_Phone;
    }

    public void setCus_Phone(String Cus_Phone) {
        this.Cus_Phone = Cus_Phone;
    }
}
