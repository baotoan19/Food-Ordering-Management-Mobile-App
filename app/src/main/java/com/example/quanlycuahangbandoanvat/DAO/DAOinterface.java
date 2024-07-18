package com.example.quanlycuahangbandoanvat.DAO;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallback;

import java.util.ArrayList;

public interface DAOinterface<T> {
    public void insert(T t , final CRUDCallback callback);

    public void update(T t, final CRUDCallback callback);

    public void delete(String t, final CRUDCallback callback);

//    public ArrayList<T> selectAll(final OnDataLoadedCallback listener);

    public T selectById(String t);
}