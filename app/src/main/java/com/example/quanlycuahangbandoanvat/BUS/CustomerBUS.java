package com.example.quanlycuahangbandoanvat.BUS;

import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.Customer;

import java.util.ArrayList;

public class CustomerBUS {
    private ArrayList<Customer> listCustomer = new ArrayList<>();

    public ArrayList<Customer> getListCustomer() {
        return listCustomer;
    }

    public void setListCustomer(ArrayList<Customer> listCustomer) {
        this.listCustomer = listCustomer;
    }

    public CustomerBUS(ArrayList<Customer> listCustomer) {
        this.listCustomer = listCustomer;
    }

    public CustomerBUS() {

    }
    public ArrayList<Customer> getAll() {
        return this.listCustomer;
    }

    public Customer getByIndex(int index) {
        return this.listCustomer.get(index);
    }

    public Customer getByCustomerID(String id) {
        int vitri = -1;
        int i = 0;
        while (i < this.listCustomer.size() && vitri == -1) {
            if (this.listCustomer.get(i).getCus_ID().equals(id)) {
                vitri = i;
            } else {
                i++;
            }
        }
        if (vitri != -1) {
            return this.listCustomer.get(vitri);
        } else {
            return null;
        }
    }


    public int getIndexByCustomerID(String id) {
        int i = 0;
        int vitri = -1;
        while (i < this.listCustomer.size() && vitri == -1) {
            if (listCustomer.get(i).getCus_ID().equals(id)) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public ArrayList<Customer> search(String text) {
        text = text.toLowerCase();
        ArrayList<Customer> result = new ArrayList<>();
        for (Customer i : this.listCustomer) {
            if (i.getCus_Name().toLowerCase().contains(text)) {
                result.add(i);
            }
        }
        return result;
    }
    public Customer getCustomerByEmailPassword(String email, String password) {
        for(Customer i : this.listCustomer) {
            if(i.getCus_Email().equals(email) && i.getCus_Password().equals(password)) {
                return i;
            }
        }
        return null;
    }
}
