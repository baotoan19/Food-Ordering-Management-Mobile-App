package com.example.quanlycuahangbandoanvat.BUS;

import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Food;

import java.util.ArrayList;

public class CartDetailBUS {
    private ArrayList<CartDetail> listCartDetail = new ArrayList<>();

    public ArrayList<CartDetail> getListCart() {
        return listCartDetail;
    }

    public void setListCartDetail(ArrayList<CartDetail> listCartDetail) {
        this.listCartDetail = listCartDetail;
    }

    public CartDetailBUS(ArrayList<CartDetail> listCartDetail) {
        this.listCartDetail = listCartDetail;
    }

    public CartDetailBUS() {

    }

    public ArrayList<CartDetail> getCartDetailByCart(String cart_id) {
        ArrayList<CartDetail> result = new ArrayList<>();
        for(CartDetail cartDetail : this.listCartDetail) {
            if(cartDetail.getCart_ID().equals(cart_id)) {
                result.add(cartDetail);
            }
        }
        return result;
    }

    public CartDetail getCartDetailByFoodID(String cart_id, String food_id) {
        if (cart_id.isEmpty() || food_id.isEmpty()) {
            return null;
        }
        for (CartDetail cartDetail : this.listCartDetail) {
            if (cart_id.equals(cartDetail.getCart_ID()) && food_id.equals(cartDetail.getFood_ID())) {
                return cartDetail;
            }
        }
        return null;
    }

}