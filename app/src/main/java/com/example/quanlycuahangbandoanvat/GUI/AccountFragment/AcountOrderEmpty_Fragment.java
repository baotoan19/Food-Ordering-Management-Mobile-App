package com.example.quanlycuahangbandoanvat.GUI.AccountFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.quanlycuahangbandoanvat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcountOrderEmpty_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcountOrderEmpty_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AcountOrderEmpty_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcountOrderEmpty_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcountOrderEmpty_Fragment newInstance(String param1, String param2) {
        AcountOrderEmpty_Fragment fragment = new AcountOrderEmpty_Fragment();
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
        View view =  inflater.inflate(R.layout.fragment_acount_order_empty, container, false);

        Button btnDatHangEmpty = view.findViewById(R.id.btnStartBuyFoodAccOder);
        btnDatHangEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMenuFragment();
            }
        });

        return view;
    }

    private void switchToMenuFragment() {
        FrameLayout frameLayoutMainActivity = requireActivity().findViewById(R.id.FrameLayoutMainActivity);
        frameLayoutMainActivity.removeAllViews();
        frameLayoutMainActivity.addView(LayoutInflater.from(getContext()).inflate(R.layout.fragment_menu, frameLayoutMainActivity, false));
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_menu);
    }
}