package com.example.quanlycuahangbandoanvat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlycuahangbandoanvat.BUS.FoodBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DAO.FoodDAO;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapterRecyclerViewBillDetails extends RecyclerView.Adapter<CustomAdapterRecyclerViewBillDetails.ViewHolder> {

    private Context context;
    private ArrayList<CartDetail> listCartDetail;
    FoodDAO foodDAO = new FoodDAO();

    public CustomAdapterRecyclerViewBillDetails(Context context, ArrayList<CartDetail> listCartDetail) {
        this.context = context;
        this.listCartDetail = listCartDetail;
    }

    @NonNull
    @Override
    public CustomAdapterRecyclerViewBillDetails.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_detailfood_bill, parent, false);
        return new CustomAdapterRecyclerViewBillDetails.ViewHolder(view);
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
                    holder.txtQuantityAndNameFood.setText(cartDetail.getQuantity() + " x " + food.getFood_Name());
                } else {
                    holder.txtQuantityAndNameFood.setText(cartDetail.getQuantity() + " x (Food not found)");
                }
                Glide.with(context)
                        .load(food.getFood_Image())
                        .placeholder(R.drawable.logokfc3)
                        .error(R.drawable.image_error)
                        .into(holder.imageDetailFood);
                String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(food.getFood_Price());
                holder.txtPriceDetailFood.setText(formattedPrice);

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listCartDetail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtQuantityAndNameFood, txtPriceDetailFood;
        ImageView imageDetailFood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuantityAndNameFood = itemView.findViewById(R.id.txtNameAndQuantityDetailFood);
            txtPriceDetailFood = itemView.findViewById(R.id.txtPriceDetailFood);
            imageDetailFood = itemView.findViewById(R.id.imageDetailsFood);
        }
    }
}
