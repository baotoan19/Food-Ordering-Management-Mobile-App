package com.example.quanlycuahangbandoanvat.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.quanlycuahangbandoanvat.GUI.Admin.OrderFragment.CancelOrder;
import com.example.quanlycuahangbandoanvat.GUI.Admin.OrderFragment.CurrentOrder;
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.AllFood_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.ChickenFried_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.Combo1_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.Combo2_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.Combo3_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.Food_And_Drink;

public class MyViewPageAdapter_AdminOrder extends FragmentStatePagerAdapter {
    public MyViewPageAdapter_AdminOrder(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new CurrentOrder();
            case 1:
                return  new CancelOrder();

            default: return new CurrentOrder();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Đơn hiện có";
            case  1:
                return "Đã hủy";

            default:
                return "Đơn hiện có";
        }
    }
}
