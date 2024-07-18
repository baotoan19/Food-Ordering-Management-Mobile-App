package com.example.quanlycuahangbandoanvat.GUI.MainFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterFragmentAccount;
import com.example.quanlycuahangbandoanvat.BUS.CustomerBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCustomer;
import com.example.quanlycuahangbandoanvat.DAO.CustomerDAO;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.CustomerViewModel;
import com.example.quanlycuahangbandoanvat.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
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

    private CustomerViewModel customerViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get ViewModel instance using ViewModelProvider
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

        // Data from SharedReferences
        String currentCustomerID = getCustomerIDFromSharedReferences();
        if(!currentCustomerID.isEmpty()) {
            customerDAO.selectAll(new OnDataLoadedCallbackCustomer() {
                @Override
                public void onDataLoaded(ArrayList<Customer> t) {
                    customerArrayList.addAll(t);
                    customerBUS = new CustomerBUS(customerArrayList);
                    Customer customer = customerBUS.getByCustomerID(currentCustomerID);
                    if (customer != null) {
                        String cusName = customer.getCus_Name();
                        tvYourName.setText(cusName);
                    } else {
                        tvYourName.setText("Loading...");
                    }
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView tvYourName;
    CustomerDAO customerDAO = new CustomerDAO();
    CustomerBUS customerBUS = new CustomerBUS();
    ArrayList<Customer> customerArrayList = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Tabview ViewPager
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        tvYourName = view.findViewById(R.id.tvYourName);

        // FRAGMENT
        FragmentManager manager = getChildFragmentManager();
        CustomAdapterFragmentAccount adapter = new CustomAdapterFragmentAccount(manager, 4);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private boolean isUserLoggedIn() {
        boolean result = false;
        String currentCustomerID = getCustomerIDFromSharedReferences();
        Customer customer = customerViewModel.getCustomer().getValue();
        if(!currentCustomerID.isEmpty() || customer != null) {
            result = true;
        }
        else {
            result = false;
        }
        return result;
    }
    private String getCustomerIDFromSharedReferences(){
        SharedPreferences sharedPref =  requireActivity().getPreferences(Context.MODE_PRIVATE);
        String currentCustomerID = sharedPref.getString("current_customer_id", "");
        return currentCustomerID;
    }
}