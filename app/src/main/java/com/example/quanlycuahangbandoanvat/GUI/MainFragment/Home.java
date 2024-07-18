package com.example.quanlycuahangbandoanvat.GUI.MainFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;


import com.example.quanlycuahangbandoanvat.BUS.CartBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.GUI.SupportFragment.BannerCarousel_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.SupportFragment.Category_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.SupportFragment.FavouriteFood_Fragment;
import com.example.quanlycuahangbandoanvat.Helper.SwipeListener;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //

    CartDAO cartDAO = new CartDAO();
    CartBUS cartBUS = new CartBUS();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // load fragment banner
        loadAllFragment(R.id.FrameLayoutBanner, new BannerCarousel_Fragment());
        // load fragment category
        loadAllFragment(R.id.FrameLayoutMenu, new Category_Fragment());
        // load fragment favourite food
        loadAllFragment(R.id.FrameLayoutFood, new FavouriteFood_Fragment());

    }

    private void loadAllFragment(int rid, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(rid, fragment);
        fragmentTransaction.commit();
    }
    private String getCustomerIDFromSharedReferences(){
        SharedPreferences sharedPref =  requireActivity().getPreferences(Context.MODE_PRIVATE);
        String currentCustomerID = sharedPref.getString("current_customer_id", "");
        return currentCustomerID;
    }
}