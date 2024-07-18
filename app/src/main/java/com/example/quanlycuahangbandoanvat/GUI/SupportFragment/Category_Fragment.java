package com.example.quanlycuahangbandoanvat.GUI.SupportFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterGridViewCategory;
import com.example.quanlycuahangbandoanvat.DTO.OptionCategory;
import com.example.quanlycuahangbandoanvat.GUI.MainFragment.Menu;
import com.example.quanlycuahangbandoanvat.GUI.MainFragment.Register;
import com.example.quanlycuahangbandoanvat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Category_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Category_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Category_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Category_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Category_Fragment newInstance(String param1, String param2) {
        Category_Fragment fragment = new Category_Fragment();
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
        return inflater.inflate(R.layout.fragment_category_, container, false);

    }
    GridView gridViewCategory;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<OptionCategory> categories_details = getListData();
        gridViewCategory = view.findViewById(R.id.GridViewCategory);
        gridViewCategory.setAdapter(new CustomAdapterGridViewCategory(requireContext(), categories_details));
        // When the user clicks on the GridItem
        gridViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = gridViewCategory.getItemAtPosition(position);
                OptionCategory OptionCategory = (OptionCategory) o;
//                Toast.makeText(getContext(), "Selected :"
//                        + " " + OptionCategory, Toast.LENGTH_LONG).show();
                switchToMenuFragment();
            }
        });
    }

    private List<OptionCategory> getListData() {
        List<OptionCategory> list = new ArrayList<OptionCategory>();
        OptionCategory mm = new OptionCategory(R.drawable.mon_moi, "Món mới");
        OptionCategory c1 = new OptionCategory(R.drawable.combo_1nguoi, "Combo 1 người");
        OptionCategory cn = new OptionCategory(R.drawable.combo_nhom, "Combo nhóm");
        OptionCategory gr = new OptionCategory(R.drawable.ga_ran, "Gà rán - Gà quay");
        OptionCategory bg = new OptionCategory(R.drawable.burger_com_mi_y, "Burger - Cơm");
        OptionCategory nh = new OptionCategory(R.drawable.mon_an_nhe, "Thức ăn nhẹ");
        OptionCategory tu = new OptionCategory(R.drawable.do_uong, "Đồ uống");
        list.add(mm);
        list.add(c1);
        list.add(cn);
        list.add(gr);
        list.add(bg);
        list.add(nh);
        list.add(tu);
        return list;
    }
    private void loadFragmentMenu() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutMainActivity, new Menu());
        fragmentTransaction.commit();
    }
    private void switchToMenuFragment() {
        FrameLayout frameLayoutMainActivity = requireActivity().findViewById(R.id.FrameLayoutMainActivity);
        frameLayoutMainActivity.removeAllViews();
        frameLayoutMainActivity.addView(LayoutInflater.from(getContext()).inflate(R.layout.fragment_menu, frameLayoutMainActivity, false));
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_menu);
    }
}