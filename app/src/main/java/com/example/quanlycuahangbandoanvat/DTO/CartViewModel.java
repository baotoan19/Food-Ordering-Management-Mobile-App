package com.example.quanlycuahangbandoanvat.DTO;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CartViewModel extends ViewModel {
    private Cart cart;
    private final MutableLiveData<Cart> cartLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> cartQuantityLiveData = new MutableLiveData<>(0);
    public LiveData<Cart> getCart() {
        return cartLiveData;
    }
    public void setCart(Cart cart) {
        cartLiveData.setValue(cart);
    }
    public LiveData<Integer> getCartQuantity() {
        return cartQuantityLiveData;
    }
    public void updateCartQuantity(int quantity) {
        cartQuantityLiveData.setValue(quantity);
    }
}