package com.example.quanlycuahangbandoanvat.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangbandoanvat.BUS.FoodBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DAO.FoodDAO;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapterRecyclerViewFinalOrder extends RecyclerView.Adapter<CustomAdapterRecyclerViewFinalOrder.ViewHolder> {

    private Context context;
    private ArrayList<CartDetail> listCartDetail;

    FoodDAO foodDAO = new FoodDAO();

    public CustomAdapterRecyclerViewFinalOrder(Context context, ArrayList<CartDetail> listCartDetail) {
        this.context = context;
        this.listCartDetail = listCartDetail;
    }

    @NonNull
    @Override
    public CustomAdapterRecyclerViewFinalOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_oder_final, parent, false);
        return new CustomAdapterRecyclerViewFinalOrder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartDetail cartDetail = listCartDetail.get(position);
        String food_id = cartDetail.getFood_ID();
        foodDAO.selectAll(new OnDataLoadedCallbackFood() {
            @Override
            public void onDataLoaded(ArrayList<Food> t) {
                FoodBUS foodBUS = new FoodBUS(t);
                Food food = foodBUS.getByFoodID(food_id);
                if (food != null) {
                    holder.txtQuantityAndFood.setText(cartDetail.getQuantity() + " x " + food.getFood_Name());
                } else {
                    holder.txtQuantityAndFood.setText(cartDetail.getQuantity() + " x (Food not found)");
                }
                holder.txtTotalOneFood.setText(String.format("%.0f", cartDetail.getPrice()));
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
        holder.txtQuantityAndFood.setText(cartDetail.getQuantity() + " x " + cartDetail.getFood_ID());
        holder.txtTotalOneFood.setText(String.format("%.0f", cartDetail.getPrice()));
    }

    @Override
    public int getItemCount() {
        return listCartDetail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtQuantityAndFood;
        public TextView txtTotalOneFood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuantityAndFood = itemView.findViewById(R.id.txtSoLuongAndNameFoodCart);
            txtTotalOneFood = itemView.findViewById(R.id.txtTotalOneFood);
        }
    }
}