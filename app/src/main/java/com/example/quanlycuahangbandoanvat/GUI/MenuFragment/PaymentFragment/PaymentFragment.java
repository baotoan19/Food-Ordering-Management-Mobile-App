package com.example.quanlycuahangbandoanvat.GUI.MenuFragment.PaymentFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterRecyclerViewFinalOrder;
import com.example.quanlycuahangbandoanvat.BUS.CartBUS;
import com.example.quanlycuahangbandoanvat.BUS.CustomerBUS;
import com.example.quanlycuahangbandoanvat.DAO.BillDAO;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCustomer;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DAO.CartDetailDAO;
import com.example.quanlycuahangbandoanvat.DAO.CustomerDAO;
import com.example.quanlycuahangbandoanvat.DAO.NotificationDAO;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.Notification;
import com.example.quanlycuahangbandoanvat.GUI.MainFragment.Account;
import com.example.quanlycuahangbandoanvat.R;
import com.google.firebase.Timestamp;

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PaymentFragment extends Fragment {
    RecyclerView recyclerViewOrderFinal;
    Button btnOder;
    TextView txtTotalPayment,txtCusName,txtCusPhone,txtCusEmail,txtCusDiaChi;
    CustomAdapterRecyclerViewFinalOrder customAdapterRecyclerViewFinalOrder;
    ArrayList<CartDetail> listCartDetail = new ArrayList<>();

    CustomerDAO customerDAO = new CustomerDAO();
    CartDAO cartDAO = new CartDAO();
    CartDetailDAO cartDetailDAO = new CartDetailDAO();
    BillDAO billDAO = new BillDAO();
    NotificationDAO notificationDAO  = new NotificationDAO();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        addControls(view);

        //Show thông tin của khách hàng ở đây
        String current_cusID = getCustomerIDFromSharedReferences();
        customerDAO.selectAll(new OnDataLoadedCallbackCustomer() {
            @Override
            public void onDataLoaded(ArrayList<Customer> t) {
                CustomerBUS customerBUS = new CustomerBUS(t);
                Customer customer = customerBUS.getByCustomerID(current_cusID);
                txtCusName.setText(customer.getCus_Name());
                txtCusPhone.setText(customer.getCus_Phone());
                txtCusEmail.setText(customer.getCus_Email());
                txtCusDiaChi.setText(customer.getCus_Address());
            }
            @Override
            public void onError(String errorMessage) {

            }
        });

        //Show thông tin tóm tắt đơn hàng
        String current_cartID = getCartIDFromSharedReferences();
        cartDetailDAO.selectAllByCartID(current_cartID, new OnDataLoadedCallbackCartDetail() {
            @Override
            public void onDataLoaded(ArrayList<CartDetail> t) {
                listCartDetail = t;
                customAdapterRecyclerViewFinalOrder = new CustomAdapterRecyclerViewFinalOrder(getContext(),listCartDetail);
                recyclerViewOrderFinal.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewOrderFinal.setAdapter(customAdapterRecyclerViewFinalOrder);
            }
            @Override
            public void onError(String errorMessage) {

            }
        });

        //Tổng tiền final ở đây
        double totalPaymentFinal = getArguments() != null ? getArguments().getDouble("totalPaymentFinal", 0) : 0;
        txtTotalPayment.setText(NumberFormat.getInstance(new Locale("vi", "VN")).format(totalPaymentFinal) + "đ");

        //Xử lý lấy mã giảm giá ở đây
        String appliedPromotionID = getArguments() != null ? getArguments().getString("appliedPromotionID","") : "";

        //Xử lý sự kiện đặt hàng ở đây
        addEvent(totalPaymentFinal,appliedPromotionID);


        return view;
    }

    private void addControls(View view){
        recyclerViewOrderFinal = view.findViewById(R.id.recyclerViewOrderfinal);
        btnOder = view.findViewById(R.id.btnDatHang);
        txtCusName = view.findViewById(R.id.txtOderCusName);
        txtCusPhone = view.findViewById(R.id.txtOrderCusPhone);
        txtCusEmail = view.findViewById(R.id.txtOrderCusEmail);
        txtCusDiaChi = view.findViewById(R.id.txtOrderCusDiaChi);
        txtTotalPayment = view.findViewById(R.id.txtTotalPayMent);
    }

    private void addEvent(double totalPayment, String current_promotionID){
        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Xử lý thêm bill tại đây
               String current_cartID = getCartIDFromSharedReferences();
               String current_cusID = getCustomerIDFromSharedReferences();
               Bill bill = new Bill(null,current_cartID,current_cusID,current_promotionID,new Timestamp(new Date()), new Timestamp(new Date()),"Chờ xác nhận",totalPayment);
               billDAO.insert(bill, new CRUDCallback() {
                   @Override
                   public void onCRUDComplete(int result) {
                       if (result == 1){
                           Toast.makeText(getContext(),"Bạn đã đặt hàng thành công !",Toast.LENGTH_SHORT).show();

                           // Xử lý notification tới Admin ở đây
                           String bill_ID = bill.getBill_ID();
                           String receiver_ID = "XUdl4K3PDIo56n6t16OR";
                           Notification notification = new Notification(null,bill_ID,receiver_ID,current_cusID,new Date(),false,"Đơn hàng " + bill.getBill_ID() + " đang chờ xác nhận!");
                           notificationDAO.insert(notification, new CRUDCallback() {
                               @Override
                               public void onCRUDComplete(int result) {
                                   if (result == 1){
                                       cartDAO.selectByIds(current_cartID, new OnDataLoadedCallbackCart() {
                                           @Override
                                           public void onDataLoaded(ArrayList<Cart> t) {

                                           }
                                           @Override
                                           public void onDataLoadedSigle(Cart cart) {
                                               cart = new Cart(cart.getCart_ID(),cart.getCus_ID(),cart.getTotal_Cart(),true);
                                               cartDAO.update(cart, new CRUDCallback() {
                                                   @Override
                                                   public void onCRUDComplete(int result) {
                                                       if (result == 1){
                                                           setCartIDToSharedReferences("");
                                                           loadFragment(new Account());
                                                       }
                                                   }
                                               });
                                           }
                                           @Override
                                           public void onError(String errorMessage) {

                                           }
                                       });
                                   }
                               }
                           });


                       }
                   }
               });
            }
        });
    }

    private String getCustomerIDFromSharedReferences() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("current_customer_id", "");
    }

    private String getCartIDFromSharedReferences() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("current_cart_id", "");
    }

    private void setCartIDToSharedReferences(String value) {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("current_cart_id", value);
        editor.apply();
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutMainActivity, fragment);
        fragmentTransaction.commit();
    }
}