package com.example.quanlycuahangbandoanvat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.quanlycuahangbandoanvat.DTO.User;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

public class CustomAdapterListViewUser extends ArrayAdapter {
    Context context; // ngữ cảnh
    int layoutItem; // layout item
    ArrayList<User> listUser = new ArrayList<>(); // dữ liệu


    public CustomAdapterListViewUser(@NonNull Context context, int layoutItem, @NonNull  ArrayList<User> listUser) {
        super(context, layoutItem, listUser);
        this.context=context;
        this.layoutItem = layoutItem;
        this.listUser=listUser;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = listUser.get(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(layoutItem,null);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(user.getName());

        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        tvEmail.setText(user.getEmail());

        TextView tvAge = (TextView) convertView.findViewById(R.id.tvAge);
        tvAge.setText(String.valueOf(user.getAge()));

        return convertView;
    }
}
