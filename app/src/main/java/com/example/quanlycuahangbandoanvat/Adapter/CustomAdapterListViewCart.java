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

import com.bumptech.glide.Glide;
import com.example.quanlycuahangbandoanvat.BUS.FoodBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DAO.CartDetailDAO;
import com.example.quanlycuahangbandoanvat.DAO.FoodDAO;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.GUI.Interface.OnCartChangedListener;
import com.example.quanlycuahangbandoanvat.Helper.Formatter;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

public class CustomAdapterListViewCart extends RecyclerView.Adapter<CustomAdapterListViewCart.ViewHolder> {

    private Context context;
    private ArrayList<CartDetail> listCartDetail;
    private FoodBUS foodBUS;
    private CartDAO cartDAO = new CartDAO();
    private FoodDAO foodDAO = new FoodDAO();
    private OnCartChangedListener onCartChangedListener;
    private CartDetailDAO cartDetailDAO = new CartDetailDAO();

    public CustomAdapterListViewCart(Context context, ArrayList<CartDetail> listCartDetail, OnCartChangedListener listener) {
        this.context = context;
        this.listCartDetail = listCartDetail;
        this.onCartChangedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartDetail cartDetail = listCartDetail.get(position);
        String currentCart_ID = getCartIDFromSharedReferences();
        String currentCus_ID = getCustomerIDFromSharedReferences();
        foodDAO.selectAlls(new OnDataLoadedCallbackFood() {
            @Override
            public void onDataLoaded(ArrayList<Food> t) {
                foodBUS = new FoodBUS(t);
                Food food = foodBUS.getByFoodID(cartDetail.getFood_ID());
                if (!food.getFood_ID().equals("")) {
                    String urlImage = food.getFood_Image();
                    Glide.with(context)
                            .load(urlImage)
                            .placeholder(R.drawable.logokfc3)
                            .error(R.drawable.image_error)
                            .into(holder.imageView);
                    holder.txtNameFood.setText(food.getFood_Name());
                    cartDetail.setPrice(food.getFood_Price() * cartDetail.getQuantity());
                    holder.txtTotalFoodItem.setText(Formatter.FormatVND(cartDetail.getPrice()));
                    holder.txtQuantity.setText(String.valueOf(cartDetail.getQuantity()));
                    if (cartDetail.getQuantity() <= 1){
                        holder.btnDeleteQuantity.setEnabled(false);
                        holder.btnDeleteQuantity.setAlpha(0.5f);
                    }
                } else {
                    holder.txtNameFood.setText("Unknown Food");
                    holder.txtTotalFoodItem.setText("");
                    holder.imageView.setImageResource(R.drawable.image_error);
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });

        // Set up click listeners for buttons
        holder.btnRemoveFoodItemInCart.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Xác nhận xóa món ăn").setMessage("Bạn có chắc muốn xóa món ăn này ?")
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cartDetailDAO.delete(cartDetail.getCartDetail_ID(), new CRUDCallback() {
                                @Override
                                public void onCRUDComplete(int result) {
                                    if (result == 1){
                                        if (onCartChangedListener != null) {
                                            onCartChangedListener.onCartChanged();
                                        }
                                        Toast.makeText(view.getContext(),"Xóa món ăn thành công",Toast.LENGTH_SHORT).show();
                                        listCartDetail.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }
                            });
                            updateCartTotal(currentCart_ID,currentCus_ID);
                        }
                    }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        });

        holder.btnAddQuantity.setOnClickListener(view -> {
            Food food = foodBUS.getByFoodID(cartDetail.getFood_ID());
            int currentQuantity = cartDetail.getQuantity();
            currentQuantity++;
            if(currentQuantity > 1){
                holder.btnDeleteQuantity.setEnabled(true);
                holder.btnDeleteQuantity.setAlpha(1f);
            }
            cartDetail.setQuantity(currentQuantity);
            cartDetail.setPrice(currentQuantity * food.getFood_Price());
            cartDetailDAO.update(cartDetail, new CRUDCallback() {
                @Override
                public void onCRUDComplete(int result) {
                    if (result ==  1){
                        holder.txtTotalFoodItem.setText(Formatter.FormatVND(cartDetail.getPrice()));
                        holder.txtQuantity.setText(String.valueOf(cartDetail.getQuantity())); // Cập nhật số lượng
                        if (onCartChangedListener != null) {
                            onCartChangedListener.onCartChanged();
                        }

                    }
                }
            });
            updateCartTotal(currentCart_ID,currentCart_ID);
        });

        holder.btnDeleteQuantity.setOnClickListener(view -> {
            Food food = foodBUS.getByFoodID(cartDetail.getFood_ID());
            int currentQuantity = cartDetail.getQuantity();
            if (currentQuantity > 1){
                currentQuantity--;
                if (currentQuantity <= 1){
                    holder.btnDeleteQuantity.setEnabled(false);
                    holder.btnDeleteQuantity.setAlpha(0.5f);
                }
                cartDetail.setQuantity(currentQuantity);
                cartDetail.setPrice(currentQuantity * food.getFood_Price());
                cartDetailDAO.update(cartDetail, new CRUDCallback() {
                    @Override
                    public void onCRUDComplete(int result) {
                        if (result == 1){
                            holder.txtTotalFoodItem.setText(Formatter.FormatVND(cartDetail.getPrice()));
                            holder.txtQuantity.setText(String.valueOf(cartDetail.getQuantity())); // Cập nhật số lượng
                            if (onCartChangedListener != null) {
                                onCartChangedListener.onCartChanged();
                            }
                        }
                    }
                });
                updateCartTotal(currentCart_ID,currentCus_ID);
            }
        });

        if (onCartChangedListener != null) {
            onCartChangedListener.onCartChanged();
        }
    }

    private void updateCartTotal(String cartID, String customerID) {
        cartDetailDAO.selectAllByCartID(cartID, new OnDataLoadedCallbackCartDetail() {
            @Override
            public void onDataLoaded(ArrayList<CartDetail> cartDetails) {
                double totalCartPrice = 0;
                for (CartDetail cartDetail : cartDetails) {
                    totalCartPrice += cartDetail.getPrice();
                }
                Cart cart = new Cart(cartID, customerID, totalCartPrice,false);
                cartDAO.update(cart, new CRUDCallback() {
                    @Override
                    public void onCRUDComplete(int result) {
                        if (result == 1) {
                        } else {
                        }
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                Toast.makeText(context, "Lỗi khi tính tổng tiền giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCustomerIDFromSharedReferences() {
        SharedPreferences sharedPref = context.getSharedPreferences("GUI.MainActivity", Context.MODE_PRIVATE);
        return sharedPref.getString("current_customer_id", "");
    }

    private String getCartIDFromSharedReferences() {
        SharedPreferences sharedPref = context.getSharedPreferences("GUI.MainActivity", Context.MODE_PRIVATE);
        return sharedPref.getString("current_cart_id", "");
    }

    @Override
    public int getItemCount() {
        return listCartDetail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView txtNameFood;
        public TextView txtTotalFoodItem;
        public TextView txtQuantity;
        public TextView btnRemoveFoodItemInCart;
        public ImageView btnDeleteQuantity;
        public ImageView btnAddQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageItemCart);
            txtNameFood = itemView.findViewById(R.id.txtFoodName);
            txtTotalFoodItem = itemView.findViewById(R.id.txtTotalFoodItem);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnRemoveFoodItemInCart = itemView.findViewById(R.id.btnRemoveFood);
            btnDeleteQuantity = itemView.findViewById(R.id.btnDeleteQuantity);
            btnAddQuantity = itemView.findViewById(R.id.btnAddQuantity);
        }
    }
}

