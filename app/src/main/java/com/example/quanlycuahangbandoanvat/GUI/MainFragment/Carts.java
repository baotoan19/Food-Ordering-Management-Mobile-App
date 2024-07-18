package com.example.quanlycuahangbandoanvat.GUI.MainFragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterListViewCart;
import com.example.quanlycuahangbandoanvat.BUS.BillBUS;
import com.example.quanlycuahangbandoanvat.BUS.CartBUS;
import com.example.quanlycuahangbandoanvat.BUS.CartDetailBUS;
import com.example.quanlycuahangbandoanvat.DAO.BillDAO;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCalbackBill;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackPromotion;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DAO.CartDetailDAO;
import com.example.quanlycuahangbandoanvat.DAO.PromotionDAO;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Promotion;
import com.example.quanlycuahangbandoanvat.GUI.CartFragment.CartEmptyFragment;
import com.example.quanlycuahangbandoanvat.GUI.Interface.OnCartChangedListener;
import com.example.quanlycuahangbandoanvat.GUI.MenuFragment.PaymentFragment.PaymentFragment;
import com.example.quanlycuahangbandoanvat.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Carts extends Fragment implements OnCartChangedListener {

    private RecyclerView recyclerViewListViewCart;
    private CustomAdapterListViewCart recyclerViewCartAdapter;
    private ArrayList<CartDetail> listCartDetail;
    private EditText edtMaGiamGia;
    private Button btnApplyPromotion, btnThanhToan;
    private TextView total_Oder, total_Payment, total_Delivery, total_Promotion;
    CartDetailDAO cartDetailDAO = new CartDetailDAO();
    CartDAO cartDAO = new CartDAO();
    BillDAO billDAO = new BillDAO();
    PromotionDAO promotionDAO = new PromotionDAO();
    private double totalPaymentFinal = 0;
    private String appliedPromotionID = ""; // Store the applied promotion ID

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();

        // Load cart details and update total payment
        String cart_ID = getCartIDFromSharedPreferences();
        if (!cart_ID.isEmpty()) {
            loadCartDetails(cart_ID);
        } else {
            loadFragment(new CartEmptyFragment());
        }

        addEvent();
    }

    private void initViews(View view) {
        recyclerViewListViewCart = view.findViewById(R.id.recyclerViewItemCart);
        edtMaGiamGia = view.findViewById(R.id.edtMaGiamGia);
        btnApplyPromotion = view.findViewById(R.id.btnApplyGiamGia);
        total_Oder = view.findViewById(R.id.txtTotalDHang);
        total_Payment = view.findViewById(R.id.txtTotalBill);
        total_Delivery = view.findViewById(R.id.txtPhiGHang);
        total_Promotion = view.findViewById(R.id.txtPromotion);
        btnThanhToan = view.findViewById(R.id.btnThanhToan);
    }

    private void addEvent() {
        btnApplyPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promotion_ID = edtMaGiamGia.getText().toString();
                applyPromotion(promotion_ID);
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass totalPaymentFinal and appliedPromotionID to PaymentFragment
                Bundle args = new Bundle();
                args.putDouble("totalPaymentFinal", totalPaymentFinal);
                args.putString("appliedPromotionID", appliedPromotionID);
                PaymentFragment paymentFragment = new PaymentFragment();
                paymentFragment.setArguments(args);
                loadFragment(paymentFragment);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerViewListViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        listCartDetail = new ArrayList<>();
        recyclerViewCartAdapter = new CustomAdapterListViewCart(getContext(), listCartDetail, this);
        recyclerViewListViewCart.setAdapter(recyclerViewCartAdapter);
    }

    private void loadCartDetails(String cart_ID) {
        cartDAO.selectAll(new OnDataLoadedCallbackCart() {
            @Override
            public void onDataLoaded(ArrayList<Cart> t) {
                CartBUS cartBUS = new CartBUS(t);
                Cart cart = cartBUS.getByCartIDs(cart_ID);
                cartDetailDAO.selectAll(new OnDataLoadedCallbackCartDetail() {
                    @Override
                    public void onDataLoaded(ArrayList<CartDetail> cartDetails) {
                        CartDetailBUS cartDetailBUS = new CartDetailBUS(cartDetails);
                        listCartDetail.clear();
                        listCartDetail.addAll(cartDetailBUS.getCartDetailByCart(cart_ID));

                        if (listCartDetail.isEmpty()) {
                            loadFragment(new CartEmptyFragment());
                        } else {
                            recyclerViewCartAdapter.notifyDataSetChanged();
                            updateTotalPayment(cart_ID);
                        }
                    }
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), "Error loading cart details", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onDataLoadedSigle(Cart cart) {
                // Not used in this context
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Error loading cart", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateTotalPayment(String cart_ID) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeVN);
        cartDAO = new CartDAO();
        cartDAO.selectByIds(cart_ID, new OnDataLoadedCallbackCart() {
            @Override
            public void onDataLoaded(ArrayList<Cart> t) {

            }

            @Override
            public void onDataLoadedSigle(Cart cart) {
                if (cart != null) {
                    double deliveryFee = cart.getTotal_Cart() < 300000 ? 30000 : 0;

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            total_Oder.setText(numberFormat.format(cart.getTotal_Cart()));
                            total_Delivery.setText(numberFormat.format(deliveryFee));
                            String totalDeliveryText = total_Delivery.getText().toString();
                            totalDeliveryText = totalDeliveryText.replaceAll("[^0-9]", "");
                            double totalDelivery = Double.parseDouble(totalDeliveryText);
                            totalPaymentFinal = cart.getTotal_Cart() + totalDelivery - (Double.parseDouble(total_Promotion.getText().toString().replaceAll("[^0-9]", ""))); // Correct calculation for totalPaymentFinal
                            total_Payment.setText(numberFormat.format(totalPaymentFinal));
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Cart not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void applyPromotion(String promotion_ID) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeVN);
        String current_cusID = getCustomerIDFromSharedReferences();

        billDAO.selectAll(new OnDataLoadedCalbackBill() {
            @Override
            public void onDataLoaded(ArrayList<Bill> bills) {
                BillBUS billBUS = new BillBUS(bills);
                for (Bill bill : bills) {
                    if (bill.getCus_ID().equals(current_cusID) &&
                            bill.getFood_Promotion_ID().equals(promotion_ID)) {
                        Toast.makeText(getContext(), "Bạn đã sử dụng mã giảm giá này cho một hóa đơn khác !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                promotionDAO.selectByIds(promotion_ID, new OnDataLoadedCallbackPromotion() {
                    @Override
                    public void onDataLoaded(ArrayList<Promotion> t) {
                    }

                    @Override
                    public void onDataLoadedSingle(Promotion promotion) {
                        if (promotion != null) {
                            String totalOrderText = total_Oder.getText().toString();
                            totalOrderText = totalOrderText.replaceAll("[^0-9]", "");
                            double totalOrder = Double.parseDouble(totalOrderText);
                            if (totalOrder < promotion.getTotal_min()) {
                                Toast.makeText(getContext(), "Tổng đơn hàng chưa đủ điều kiện áp dụng khuyến mãi !", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            double totalDelivery = Double.parseDouble(total_Delivery.getText().toString().replaceAll("[^0-9.]", ""));
                            double discountAmount = promotion.getDiscount_Amount();
                            double total_Payments = totalOrder - totalDelivery - discountAmount;
                            total_Promotion.setText(numberFormat.format(discountAmount));
                            total_Payment.setText(numberFormat.format(total_Payments));
                            totalPaymentFinal = total_Payments;
                            appliedPromotionID = promotion_ID;

                            Toast.makeText(getContext(), "Áp dụng khuyến mãi thành công !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Mã khuyến mãi không hợp lệ !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
    }

    private String getCartIDFromSharedPreferences() {
        SharedPreferences sharedPref = requireActivity().getPreferences(MODE_PRIVATE);
        return sharedPref.getString("current_cart_id", "");
    }

    private String getCustomerIDFromSharedReferences() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("current_customer_id", "");
    }

    @Override
    public void onCartChanged() {
        String cart_ID = getCartIDFromSharedPreferences();
        if (!cart_ID.isEmpty()) {
            updateTotalPayment(cart_ID);
        } else {
            Toast.makeText(getContext(), "Cart ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutMainActivity, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}


