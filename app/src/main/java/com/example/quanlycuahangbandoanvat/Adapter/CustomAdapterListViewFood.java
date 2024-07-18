package com.example.quanlycuahangbandoanvat.Adapter;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;

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
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.DetailFoodActivity.DetailFoodActivity;
import com.example.quanlycuahangbandoanvat.Helper.Formatter;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

public class CustomAdapterListViewFood extends ArrayAdapter<Food> {
    Context context;
    int layoutItem;
    ArrayList<Food> listFood;
    CartBUS cartBUS = new CartBUS();
    CartDetailBUS cartDetailBUS = new CartDetailBUS();
    CartDAO cartDAO = new CartDAO();
    CartDetailDAO cartDetailDAO = new CartDetailDAO();
    ListView  listViewAllFood;

    public CustomAdapterListViewFood(@NonNull Context context, int layoutItem, @NonNull ArrayList<Food> listFood) {
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

        ImageView imageView = convertView.findViewById(R.id.imgFood);
        String urlImage = food.getFood_Image();

        Glide.with(getContext())
                .load(urlImage)
                .placeholder(R.drawable.logokfc3)
                .error(R.drawable.image_error)
                .into(imageView);

        TextView tvName = convertView.findViewById(R.id.tvFoodName);
        tvName.setText(food.getFood_Name());

        TextView tvPrice = convertView.findViewById(R.id.tvFoodPrice);
        String priceFormat = Formatter.FormatVND(food.getFood_Price());
        tvPrice.setText(priceFormat);

        TextView tvDes = convertView.findViewById(R.id.tvFoodDescription);
        tvDes.setText(String.valueOf(food.getFood_Description()));

        Button btnAddFoodToCart = convertView.findViewById(R.id.btnFoodAddToCart);
        if(getCustomerIDFromSharedPreferences().isEmpty()) {
            btnAddFoodToCart.setEnabled(false);
            btnAddFoodToCart.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray100));
        }



        btnAddFoodToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentCartID = getCartIDFromSharedPreferences();
                String currentCusID = getCustomerIDFromSharedPreferences();

                if (currentCartID.isEmpty()) {
                    createNewCart(currentCusID, food);
                } else {
                    updateCart(currentCartID,currentCusID,food);
                }
            }
        });

        return convertView;
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
                                Toast.makeText(getContext(), "Đã thêm " + food.getFood_Name() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Đã cập nhật số lượng " + food.getFood_Name() + " trong giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi
                Toast.makeText(getContext(), "Lỗi tải dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Đã thêm " + food.getFood_Name() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    // Function to update the cart total
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

    private String getCustomerIDFromSharedPreferences() {
        SharedPreferences sharedPref = this.context.getSharedPreferences("GUI.MainActivity", Context.MODE_PRIVATE);
        return sharedPref.getString("current_customer_id", "");
    }

    private String getCartIDFromSharedPreferences() {
        SharedPreferences sharedPref = this.context.getSharedPreferences("GUI.MainActivity", MODE_PRIVATE);
        return sharedPref.getString("current_cart_id", "");
    }

    private void setCartIDToSharedReferences(String value) {
        SharedPreferences sharedPref = this.context.getSharedPreferences("GUI.MainActivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("current_cart_id", value);
        editor.apply();
    }
}