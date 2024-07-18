package com.example.quanlycuahangbandoanvat.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.quanlycuahangbandoanvat.GUI.AccountFragment.AccountDetail_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.AccountFragment.AccountOrder_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.AccountFragment.DeleteAccount_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.AccountFragment.ResetPassword_Fragment;


public class CustomAdapterFragmentAccount extends FragmentPagerAdapter {
    private int numberPage;

    public CustomAdapterFragmentAccount(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numberPage = behavior;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String cate = "";
        switch (position) {
            case 0: {
                AccountOrder_Fragment fg = new AccountOrder_Fragment();
                return fg;
            }
            case 1: {
                AccountDetail_Fragment fg = new AccountDetail_Fragment();
                return fg;
            }
            case 2: {
                ResetPassword_Fragment fg = new ResetPassword_Fragment();
                return fg;
            }
            case 3: {
                DeleteAccount_Fragment fg = new DeleteAccount_Fragment();
                return fg;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.numberPage;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "Đơn hàng đã đặt";
            }
            case 1: {
                return "Chi tiết tài khoản";
            }
            case 2: {
                return "Đặt lại mật khẩu";
            }
            case 3: {
                return "Xóa tài khoản";
            }
        }
        return "";
    }

}
