package com.example.quanlycuahangbandoanvat.GUI.MenuFragment.DetailFoodActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.quanlycuahangbandoanvat.Helper.Formatter;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DetailFoodActivity extends AppCompatActivity {

    private ImageView imageView_Back,imgdetailfood;

    private TextView namedetailfood, pricedetailfood, motadetail;
    TextView themcart;


    ArrayList<Food> listFood;
    CartBUS cartBUS = new CartBUS();
    CartDetailBUS cartDetailBUS = new CartDetailBUS();
    CartDAO cartDAO = new CartDAO();
    CartDetailDAO cartDetailDAO = new CartDetailDAO();
    Food food = new Food();

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        // Gắn ID
        imageView_Back = findViewById(R.id.imageView_Back);
       imgdetailfood=findViewById(R.id.imgdetailfood);
        namedetailfood = findViewById(R.id.namedetailfood);
        pricedetailfood = findViewById(R.id.pricedetailfood);
        motadetail = findViewById(R.id.motadetail);
        themcart=findViewById(R.id.themcart);

        // check customer
        if(getCustomerIDFromSharedPreferences().isEmpty()) {
            themcart.setEnabled(false);
            themcart.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray100));
        }

        // Lấy thông tin chi tiết của Food
        Intent intent = getIntent();
        Food selectedFood = (Food) intent.getSerializableExtra("SelectedFood");
        if (selectedFood != null) {
            namedetailfood.setText(selectedFood.getFood_Name());
            String priceFormat = Formatter.FormatVND(selectedFood.getFood_Price());
            pricedetailfood.setText(priceFormat);
            motadetail.setText(selectedFood.getFood_Description());
            String urlImage = selectedFood.getFood_Image();

            Glide.with(DetailFoodActivity.this)
                    .load(urlImage)
                    .placeholder(R.drawable.logokfc3)
                    .error(R.drawable.image_error)
                    .into(imgdetailfood);
        }
        // Sự kiện click vào nút back để quay lại trang trước
        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        themcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentCartID = getCartIDFromSharedPreferences();
                String currentCusID = getCustomerIDFromSharedPreferences();
                if (currentCartID.isEmpty()) {
                    createNewCart(currentCusID, selectedFood);
                } else {
                    updateCart(currentCartID,currentCusID,selectedFood);
                }
            }
        });

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
                                Toast.makeText(getApplicationContext(), "Đã thêm " + food.getFood_Name() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), "Đã cập nhật số lượng " + food.getFood_Name() + " trong giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi
                Toast.makeText(getApplicationContext(), "Lỗi tải dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), "Đã thêm " + food.getFood_Name() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getApplicationContext(), "Lỗi khi tính tổng tiền giỏ hàng", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Xử lý lỗi
                            Toast.makeText(getApplicationContext(), "Lỗi khi tính tổng tiền giỏ hàng", Toast.LENGTH_SHORT).show();
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
        SharedPreferences sharedPref = getSharedPreferences("GUI.MainActivity", Context.MODE_PRIVATE);
        return sharedPref.getString("current_customer_id", "");
    }

    private String getCartIDFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("GUI.MainActivity", MODE_PRIVATE);
        return sharedPref.getString("current_cart_id", "");
    }

    private void setCartIDToSharedReferences(String value) {
        SharedPreferences sharedPref = getSharedPreferences("GUI.MainActivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("current_cart_id", value);
        editor.apply();
    }

}