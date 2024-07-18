package com.example.quanlycuahangbandoanvat.BUS;

import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallback;
import com.example.quanlycuahangbandoanvat.DAO.UserDAO;
import com.example.quanlycuahangbandoanvat.DTO.User;

import java.util.ArrayList;

public class UserBUS {
    private ArrayList<User> listUser = new ArrayList<>();

    public ArrayList<User> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    public UserBUS(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    public UserBUS() {

    }
    public ArrayList<User> getAll() {
        return this.listUser;
    }


    public User getByIndex(int index) {
        return this.listUser.get(index);
    }

    public User getByUserID(String id) {
        int vitri = -1;
        int i = 0;
        while (i <= this.listUser.size() && vitri == -1) {
            if (this.listUser.get(i).getId().equals(id)) {
                vitri = i;
            } else {
                i++;
            }
        }
        return this.listUser.get(vitri);
    }

    public int getIndexByUserID(String id) {
        int i = 0;
        int vitri = -1;
        while (i < this.listUser.size() && vitri == -1) {
            if (listUser.get(i).getId().equals(id)) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public ArrayList<User> search(String text) {
        text = text.toLowerCase();
        ArrayList<User> result = new ArrayList<>();
        for (User i : this.listUser) {
            if (i.getName().toLowerCase().contains(text)) {
                result.add(i);
            }
        }
        return result;
    }
}
