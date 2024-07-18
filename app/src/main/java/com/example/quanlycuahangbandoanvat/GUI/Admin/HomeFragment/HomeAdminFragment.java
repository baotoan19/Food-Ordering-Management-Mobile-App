package com.example.quanlycuahangbandoanvat.GUI.Admin.HomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterListViewFood;
import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterListViewFoodPopular;
import com.example.quanlycuahangbandoanvat.BUS.BillBUS;
import com.example.quanlycuahangbandoanvat.BUS.FoodBUS;
import com.example.quanlycuahangbandoanvat.DAO.BillDAO;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCalbackBill;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DAO.FoodDAO;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.Helper.Formatter;
import com.example.quanlycuahangbandoanvat.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeAdminFragment extends Fragment {

    ListView listViewFoodPoplular;
    FoodBUS foodBUS = new FoodBUS();
    CustomAdapterListViewFoodPopular customAdapterFood;
    FoodDAO foodDAO = new FoodDAO();
    ArrayList<Food> listFood = new ArrayList<>();
    TextView tv,SumOfOrdered,SumOfOrder,SumOfTurnOver,SumOfFood;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeAdminFragment newInstance(String param1, String param2) {
        HomeAdminFragment fragment = new HomeAdminFragment();
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
        return inflater.inflate(R.layout.fragment_home_admin, container, false);
    }

    BillDAO billDAO = new BillDAO();
    BillBUS billBUS = new BillBUS();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ánh xạ ID
        listViewFoodPoplular = (view).findViewById(R.id.listfoodpopular);
        SumOfOrdered=view.findViewById(R.id.SumOfOrdered);
        SumOfOrder=view.findViewById(R.id.SumOfOrder);
        SumOfTurnOver=view.findViewById(R.id.SumOfTurnOver);
        SumOfFood=view.findViewById(R.id.SumOfFood);

        // innit array list Food
        foodDAO.selectAll(new OnDataLoadedCallbackFood() {
            @Override
            public void onDataLoaded(ArrayList<Food> Foods) {
                listFood.addAll(Foods);
                foodBUS = new FoodBUS(Foods);
                // tổng món ăn
                int sumOfFood = foodBUS.getTotalFood();
                SumOfFood.setText(String.valueOf(sumOfFood));


                // hiển thị danh sách
                ArrayList<Food> listFoodPopular = foodBUS.getRandomFoods(10);
                // init list view
                customAdapterFood = new CustomAdapterListViewFoodPopular(getContext(), R.layout.layout_foodpopular, listFoodPopular);
                listViewFoodPoplular.setAdapter(customAdapterFood);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        // init bill information
        billDAO.selectAll(new OnDataLoadedCalbackBill() {
            @Override
            public void onDataLoaded(ArrayList<Bill> t) {
                billBUS = new BillBUS(t);

                int totalBill = billBUS.getTotalBill();
                int totalUnconfirmedBill = billBUS.getUnconfirmedBill();
                double revenue = billBUS.getRevenueBill();

                SumOfOrdered.setText(String.valueOf(totalBill));
                SumOfOrder.setText(String.valueOf(totalUnconfirmedBill));
                SumOfTurnOver.setText(Formatter.FormatVND(revenue));
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
    public void loadArrayListFood(){
        foodDAO.selectAll(new OnDataLoadedCallbackFood() {
            @Override
            public void onDataLoaded(ArrayList<Food> Foods) {
                listFood.clear();
                listFood.addAll(Foods);
                customAdapterFood.notifyDataSetChanged();
                foodBUS.setListFood(listFood);
            }

            @Override
            public void onError(String errorMessage) {

            }

        });
    }
}