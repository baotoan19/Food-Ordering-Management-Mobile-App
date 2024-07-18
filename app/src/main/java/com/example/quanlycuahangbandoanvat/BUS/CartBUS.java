package com.example.quanlycuahangbandoanvat.BUS;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class CartBUS {
    private ArrayList<Cart> listCart = new ArrayList<>();

    public ArrayList<Cart> getListCart() {
        return listCart;
    }

    public void setListCart(ArrayList<Cart> listCart) {
        this.listCart = listCart;
    }

    public CartBUS(ArrayList<Cart> listCart) {
        this.listCart = listCart;
    }

    public CartBUS() {

    }

    public Cart getTotalOrderByCartID(String cart_id){
        Cart cart = new Cart();
        if (!cart_id.equals("")){
            for(Cart carts : this.listCart) {
                if(carts.getCart_ID().equals(cart_id)) {
                    cart = carts;
                }
            }
        }
        return cart;
    }

    public Cart getCartByCustomerIDs(String id) {
        int vitri = -1;
        for (int i = 0; i < this.listCart.size(); i++) {
            if (this.listCart.get(i).getCus_ID().equals(id)) {
                vitri = i;
                break;
            }
        }
        if (vitri != -1) {
            return this.listCart.get(vitri);
        } else {
            return null;
        }
    }

    public Cart getByCartIDs(String id) {
        int vitri = -1;
        for (int i = 0; i < this.listCart.size(); i++) {
            if (this.listCart.get(i).getCart_ID().equals(id)) {
                vitri = i;
                break;
            }
        }
        if (vitri != -1) {
            return this.listCart.get(vitri);
        } else {
            return null;
        }
    }

}