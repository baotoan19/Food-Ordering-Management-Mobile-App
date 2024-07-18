package com.example.quanlycuahangbandoanvat.GUI.Admin.OrderFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlycuahangbandoanvat.Adapter.MyViewPageAdapter_AdminOrder;
import com.example.quanlycuahangbandoanvat.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderAdminFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public OrderAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderAdminFragment newInstance(String param1, String param2) {
        OrderAdminFragment fragment = new OrderAdminFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order_admin, container, false);
        tabLayout=view.findViewById(R.id.tabLayout_AdminOrder);
        viewPager=view.findViewById(R.id.AdminOrder_ViewPage);
        MyViewPageAdapter_AdminOrder adapter_adminOrder=new MyViewPageAdapter_AdminOrder(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter_adminOrder);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}