package com.example.quanlycuahangbandoanvat.DTO;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private String Cart_ID;
    private String Cus_ID;
    private Double Total_Cart;
    private Boolean IsPayment;

    public Cart(){

    }

    public Cart(String cart_ID, String cus_ID, Double total_Cart, Boolean isPayment) {
        Cart_ID = cart_ID;
        Cus_ID = cus_ID;
        Total_Cart = total_Cart;
        IsPayment = isPayment;
    }

    public String getCart_ID() {
        return Cart_ID;
    }

    public void setCart_ID(String cart_ID) {
        Cart_ID = cart_ID;
    }

    public String getCus_ID() {
        return Cus_ID;
    }

    public void setCus_ID(String cus_ID) {
        Cus_ID = cus_ID;
    }

    public Double getTotal_Cart() {
        return Total_Cart;
    }

    public void setTotal_Cart(Double total_Cart) {
        Total_Cart = total_Cart;
    }

    public Boolean getPayment() {
        return IsPayment;
    }

    public void setPayment(Boolean payment) {
        IsPayment = payment;
    }
}