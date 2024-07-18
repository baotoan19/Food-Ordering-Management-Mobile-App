package com.example.quanlycuahangbandoanvat.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlycuahangbandoanvat.BUS.CartBUS;
import com.example.quanlycuahangbandoanvat.BUS.CartDetailBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DAO.CartDetailDAO;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

public class CustomAdapterRecycleViewFood extends RecyclerView.Adapter<CustomAdapterRecycleViewFood.MyViewHolder> {
    ArrayList<Food> arrayListFood = new ArrayList<>();
    private Context context; // Lưu trữ context của Activity

    CartDetailBUS cartDetailBUS = new CartDetailBUS();
    CartDAO cartDAO = new CartDAO();
    CartDetailDAO cartDetailDAO = new CartDetailDAO();
    CartBUS cartBUS = new CartBUS();

    public CustomAdapterRecycleViewFood(ArrayList<Food> arrayListFood, Context context) {
        this.arrayListFood = arrayListFood;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_food_horizontal_item,
                parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Food food = arrayListFood.get(position);
        holder.tvName.setText(food.getFood_Name());
        holder.tvPrice.setText(String.valueOf(food.getFood_Price()));
        holder.tvDes.setText(String.valueOf(food.getFood_Description()));
        //holder.imageViewFood.setImageResource(food.getFood_Image());

        String urlImage = food.getFood_Image();

        Glide.with(holder.itemView.getContext())
                .load(urlImage)
                .placeholder(R.drawable.logokfc3) // Hình ảnh hiển thị trong khi tải
                .error(R.drawable.image_error) // Hình ảnh hiển thị nếu tải thất bại
                .into(holder.imageViewFood);



        if(getCustomerIDFromSharedReferences().isEmpty()) {
            holder.btnAddFood.setEnabled(false);
            holder.btnAddFood.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray100));
        }

        holder.btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentCartID = getCartIDFromSharedReferences();
                String currentCusID = getCustomerIDFromSharedReferences();
                if (currentCartID.isEmpty()) {
                    createNewCart(currentCusID, food);
                } else {
                    updateCart(currentCartID,currentCusID,food);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListFood.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvDes;
        ImageView imageViewFood;
        Button btnAddFood;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvHorFoodName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvHorFoodPrice);
            tvDes = (TextView) itemView.findViewById(R.id.tvHorFoodDescription);
            imageViewFood = (ImageView) itemView.findViewById(R.id.imgHorFood);
            btnAddFood = (Button) itemView.findViewById(R.id.btnHorFoodAddToCart);
        }
    }


    private void updateCart(String cartID, String customerID, Food food) {
        cartDetailDAO.selectAllByCartID(cartID, new OnDataLoadedCallbackCartDetail() {
            @Override
            public void onDataLoaded(ArrayList<CartDetail> t) {
                cartDetailBUS = new CartDetailBUS(t);
                CartDetail existingCartDetail = cartDetailBUS.getCartDetailByFoodID(cartID, food.getFood_ID());
                if (existingCartDetail == null) {
                    int defaultQuantity = 1;
                    CartDetail newCartDetail = new CartDetail(null, cartID, food.getFood_ID(), defaultQuantity, food.getFood_Price());
                    cartDetailDAO.insert(newCartDetail, new CRUDCallback() {
                        @Override
                        public void onCRUDComplete(int result) {
                            if (result == 1) {
                                updateCartTotal(cartID, customerID);
                                Toast.makeText(context, "Đã thêm " + food.getFood_Name() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    int newQuantity = existingCartDetail.getQuantity() + 1;
                    existingCartDetail.setQuantity(newQuantity);
                    existingCartDetail.setPrice(newQuantity * food.getFood_Price());
                    cartDetailDAO.update(existingCartDetail, new CRUDCallback() {
                        @Override
                        public void onCRUDComplete(int result) {
                            if (result == 1) {
                                updateCartTotal(cartID, customerID);
                                Toast.makeText(context, "Đã cập nhật số lượng " + food.getFood_Name() + " trong giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi
                Toast.makeText(context, "Lỗi tải dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createNewCart(String customerID, Food food) {
        Cart cart = new Cart(null, customerID, 0.0, false);
        cartDAO.insert(cart, new CRUDCallback() {
            @Override
            public void onCRUDComplete(int result) {
                if (result == 1) {
                    setCartIDToSharedReferences(cart.getCart_ID());
                    updateCartTotal(cart.getCart_ID(),customerID);
                    // Thêm CartDetail sau khi tạo Cart thành công
                    int defaultQuantity = 1;
                    CartDetail newCartDetail = new CartDetail(null, cart.getCart_ID(), food.getFood_ID(), defaultQuantity, food.getFood_Price());
                    cartDetailDAO.insert(newCartDetail, new CRUDCallback() {
                        @Override
                        public void onCRUDComplete(int result) {
                            if (result == 1) {
                                Toast.makeText(context, "Đã thêm " + food.getFood_Name() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    private void updateCartTotal(String cartID, String customerID) {
        cartDAO.selectAll(new OnDataLoadedCallbackCart() {
            @Override
            public void onDataLoaded(ArrayList<Cart> t) {
                cartBUS = new CartBUS(t);
                Cart cart = cartBUS.getByCartIDs(cartID);
                if (!cart.getPayment()){
                    cartDetailDAO.selectAllByCartID(cartID, new OnDataLoadedCallbackCartDetail() {
                        @Override
                        public void onDataLoaded(ArrayList<CartDetail> cartDetails) {
                            double totalCartPrice = 0;
                            for (CartDetail cartDetail : cartDetails) {
                                totalCartPrice += cartDetail.getPrice();
                            }
                            Cart cart = new Cart(cartID, customerID, totalCartPrice, false);
                            cartDAO.update(cart, new CRUDCallback() {
                                @Override
                                public void onCRUDComplete(int result) {
                                    if (result == 1) {
                                        // Cập nhật tổng tiền giỏ hàng thành công
                                    } else {
                                        Toast.makeText(context, "Lỗi khi tính tổng tiền giỏ hàng", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Xử lý lỗi
                            Toast.makeText(context, "Lỗi khi tính tổng tiền giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onDataLoadedSigle(Cart cart) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
    private void setCartIDToSharedReferences(String value) {
        SharedPreferences sharedPref = this.context.getSharedPreferences("GUI.MainActivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("current_cart_id", value);
        editor.apply();
    }

    private String getCustomerIDFromSharedReferences() {
        SharedPreferences sharedPref = this.context.getSharedPreferences("GUI.MainActivity", Context.MODE_PRIVATE);
        return sharedPref.getString("current_customer_id", "");
    }

    private String getCartIDFromSharedReferences() {
        SharedPreferences sharedPref = this.context.getSharedPreferences("GUI.MainActivity", MODE_PRIVATE);
        return sharedPref.getString("current_cart_id", "");
    }

}