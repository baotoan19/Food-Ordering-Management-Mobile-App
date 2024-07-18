package com.example.quanlycuahangbandoanvat.DTO;

public class Role {
    private String Role_ID;
    private String Role_Name;

    public Role() {
    }

    public Role(String role_ID, String role_Name) {
        Role_ID = role_ID;
        Role_Name = role_Name;
    }

    public String getRole_ID() {
        return Role_ID;
    }

    public void setRole_ID(String role_ID) {
        Role_ID = role_ID;
    }

    public String getRole_Name() {
        return Role_Name;
    }

    public void setRole_Name(String role_Name) {
        Role_Name = role_Name;
    }
}
