package com.example.quanlycuahangbandoanvat.GUI.CartFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.quanlycuahangbandoanvat.GUI.MainFragment.Menu;
import com.example.quanlycuahangbandoanvat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartEmptyFragment extends Fragment {

    Button btnStartBuyFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_empty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStartBuyFood = view.findViewById(R.id.btnStartBuyFood);
        btnStartBuyFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMenuFragment();
            }
        });
    }

    private void switchToMenuFragment() {
        FrameLayout frameLayoutMainActivity = requireActivity().findViewById(R.id.FrameLayoutMainActivity);
        frameLayoutMainActivity.removeAllViews();
        frameLayoutMainActivity.addView(LayoutInflater.from(getContext()).inflate(R.layout.fragment_menu, frameLayoutMainActivity, false));

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_menu);
    }
}