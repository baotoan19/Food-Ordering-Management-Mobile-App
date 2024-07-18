package com.example.quanlycuahangbandoanvat.DTO;

public class CartDetail {
    private String CartDetail_ID;
    private String Cart_ID;
    private String Food_ID;
    private int Quantity;
    private double Price;

    public String getCartDetail_ID() {
        return CartDetail_ID;
    }

    public void setCartDetail_ID(String cartDetail_ID) {
        CartDetail_ID = cartDetail_ID;
    }

    public String getCart_ID() {
        return Cart_ID;
    }

    public void setCart_ID(String cart_ID) {
        Cart_ID = cart_ID;
    }

    public String getFood_ID() {
        return Food_ID;
    }

    public void setFood_ID(String food_ID) {
        Food_ID = food_ID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public CartDetail(String cartDetail_ID, String cart_ID, String food_ID, int quantity, double price) {
        CartDetail_ID = cartDetail_ID;
        Cart_ID = cart_ID;
        Food_ID = food_ID;
        Quantity = quantity;
        Price = price;
    }

    public CartDetail(){

    }


}
