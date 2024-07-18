package com.example.quanlycuahangbandoanvat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.Helper.Formatter;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

public class CustomAdapterListViewFoodPopular extends ArrayAdapter<Food> {
    Context context;
    int layoutItem;
    ArrayList<Food> listFood;
    public CustomAdapterListViewFoodPopular(@NonNull Context context, int layoutItem, @NonNull ArrayList<Food> listFood) {
        super(context, layoutItem, listFood);
        this.context = context;
        this.layoutItem = layoutItem;
        this.listFood = listFood;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Food food = listFood.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutItem, null);
        }
        TextView kindfood = convertView.findViewById(R.id.kindfood);
        kindfood.setText(food.getFood_Description());
        TextView namefood = convertView.findViewById(R.id.namefood);
        namefood.setText(food.getFood_Name());
        TextView pricefood = convertView.findViewById(R.id.pricefood);
        pricefood.setText(Formatter.FormatVND(food.getFood_Price()));

        ImageView imageView = convertView.findViewById(R.id.imgfoodpopular);
        String urlImage = food.getFood_Image();

        Glide.with(getContext())
                .load(urlImage)
                .placeholder(R.drawable.logokfc3)
                .error(R.drawable.image_error)
                .into(imageView);

        return convertView;
    }
}
