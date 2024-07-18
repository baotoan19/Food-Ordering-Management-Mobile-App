package com.example.quanlycuahangbandoanvat.GUI.MenuFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterListViewFood;
import com.example.quanlycuahangbandoanvat.BUS.FoodBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DAO.FoodDAO;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.DetailFoodActivity.DetailFoodActivity;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Combo2_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Combo2_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Combo2_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Combo2_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Combo2_Fragment newInstance(String param1, String param2) {
        Combo2_Fragment fragment = new Combo2_Fragment();
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
        return inflater.inflate(R.layout.fragment_combo2_, container, false);
    }

    // khai báo biến
    ListView listViewCombo2Food;
    FoodBUS foodBUS = new FoodBUS();
    CustomAdapterListViewFood customAdapterFood;
    FoodDAO foodDAO = new FoodDAO();
    ArrayList<Food> listFood = new ArrayList<>();
    ArrayList<Food> listFoodByCategory = new ArrayList<>();
    int selectedPosition = -1;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ánh xạ ID
        listViewCombo2Food = (view).findViewById(R.id.listViewCombo2Food);

        // innit array list Food
        foodDAO.selectAll(new OnDataLoadedCallbackFood() {
            @Override
            public void onDataLoaded(ArrayList<Food> Foods) {
                listFood.addAll(Foods);
                foodBUS = new FoodBUS(listFood);

                // init listview
                listFoodByCategory.addAll(foodBUS.getFoodByCategory("CxnMH5jmmCKZwJtAYHHl"));
                customAdapterFood = new CustomAdapterListViewFood(getContext(), R.layout.layout_food_item, listFoodByCategory);
                listViewCombo2Food.setAdapter(customAdapterFood);
            }
            @Override
            public void onError(String errorMessage) {

            }


        });
        listViewCombo2Food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy item được click
                Food selectedFood = (Food) parent.getItemAtPosition(position);

                Intent intent = new Intent(getContext(), DetailFoodActivity.class);
                intent.putExtra("SelectedFood", selectedFood);
                startActivity(intent);
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
                foodBUS = new FoodBUS(listFood);
                //foodBUS.setListFood(listFood);
            }

            @Override
            public void onError(String errorMessage) {

            }

        });
    }
}