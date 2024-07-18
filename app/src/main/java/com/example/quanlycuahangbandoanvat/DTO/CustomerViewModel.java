package com.example.quanlycuahangbandoanvat.DTO;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomerViewModel extends ViewModel {
    private Customer customer;
    private final MutableLiveData<Customer> customerLiveData = new MutableLiveData<>();
    public LiveData<Customer> getCustomer() {
        return customerLiveData;
    }

    public void setCustomer(Customer customer) {
        customerLiveData.setValue(customer);
    }
}
