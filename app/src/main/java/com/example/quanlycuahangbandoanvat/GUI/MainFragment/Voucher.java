package com.example.quanlycuahangbandoanvat.GUI.MainFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterListViewFood;
import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterListViewPromotion;
import com.example.quanlycuahangbandoanvat.BUS.FoodBUS;
import com.example.quanlycuahangbandoanvat.BUS.PromotionBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackPromotion;
import com.example.quanlycuahangbandoanvat.DAO.PromotionDAO;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.DTO.Promotion;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Voucher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Voucher extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Voucher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Voucher.
     */
    // TODO: Rename and change types and number of parameters
    public static Voucher newInstance(String param1, String param2) {
        Voucher fragment = new Voucher();
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
        return inflater.inflate(R.layout.fragment_voucher, container, false);

    }

    // khai báo biến
    ListView listViewSale;
    PromotionBUS promotionBUS ;
    ArrayList<Promotion> promotionsIsFalse=new ArrayList<>();
    CustomAdapterListViewPromotion customAdapterPromotion;
    PromotionDAO promotionDAO = new PromotionDAO();
    ArrayList<Promotion> listPromotion = new ArrayList<>();
    int selectedPosition = -1;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ánh xạ ID
        listViewSale = (view).findViewById(R.id.listViewSale);

        // innit array list Food
        promotionDAO.selectAll(new OnDataLoadedCallbackPromotion() {
            @Override
            public void onDataLoaded(ArrayList<Promotion> promotions) {
                listPromotion.addAll(promotions);
                promotionBUS = new PromotionBUS(listPromotion);

                customAdapterPromotion = new CustomAdapterListViewPromotion(getContext(), R.layout.layout_item_sale,  promotionBUS.getListPromotionAvailablity());
                listViewSale.setAdapter(customAdapterPromotion);
                customAdapterPromotion.notifyDataSetChanged();
            }

            @Override
            public void onDataLoadedSingle(Promotion t) {

            }

            @Override
            public void onError(String errorMessage) {
                Log.e("TAG", "Error in selectAll: " + errorMessage);
            }


        });
    }
}