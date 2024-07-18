package com.example.quanlycuahangbandoanvat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangbandoanvat.BUS.CustomerBUS;
import com.example.quanlycuahangbandoanvat.DAO.BillDAO;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCustomer;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DAO.CartDetailDAO;
import com.example.quanlycuahangbandoanvat.DAO.CustomerDAO;
import com.example.quanlycuahangbandoanvat.DAO.NotificationDAO;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdminCustomAdapterRecyclerViewShowCancelDetailOrder extends RecyclerView.Adapter<AdminCustomAdapterRecyclerViewShowCancelDetailOrder.ViewHolder> {

    private Context context;
    private ArrayList<Bill> listBill;
    CustomerDAO customerDAO = new CustomerDAO();
    CartDAO cartDAO = new CartDAO();
    CartDetailDAO cartDetailDAO = new CartDetailDAO();
    BillDAO billDAO = new BillDAO();
    NotificationDAO notificationDAO = new NotificationDAO();
    AlertDialog dialog;

    public AdminCustomAdapterRecyclerViewShowCancelDetailOrder(Context context, ArrayList<Bill> listBill) {
        this.context = context;
        this.listBill = listBill;
    }

    @NonNull
    @Override
    public AdminCustomAdapterRecyclerViewShowCancelDetailOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_admin_custom_item_show_cancel_order, parent, false);
        return new AdminCustomAdapterRecyclerViewShowCancelDetailOrder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = listBill.get(position);

        holder.txtCcIDBill.setText(bill.getBill_ID());
        Date orderDate = bill.getOrder_Date().toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(orderDate);
        holder.txtCcOderDate.setText(formattedDate);
        holder.txtCcBillStatus.setText(bill.getBill_Status());

        customerDAO.selectAll(new OnDataLoadedCallbackCustomer() {
            @Override
            public void onDataLoaded(ArrayList<Customer> t) {
                CustomerBUS customerBUS = new CustomerBUS(t);
                Customer cus = customerBUS.getByCustomerID(bill.getCus_ID());
                if (cus != null) {
                    holder.txtCcTenKH.setText(cus.getCus_Name());
                }
            }
            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });

        holder.imageCcBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBillCurrentOrderDialog(bill);
            }
        });
    }

    private void showBillCurrentOrderDialog(Bill bill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_admin_custom_detail_cancel_oder, null);

        TextView txtDialogBillID = dialogView.findViewById(R.id.txtCancelBillID);
        TextView txtDialogCusID = dialogView.findViewById(R.id.txtCancelCusID);
        TextView txtDialogOrderDate = dialogView.findViewById(R.id.txtCancelOrderDate);
        TextView txtDialogDeliveryDate = dialogView.findViewById(R.id.txtCancelDeliveryDate);
        TextView txtDialogTotalBill = dialogView.findViewById(R.id.txtCancelTotalBill);
        TextView txtDialogBillStatus = dialogView.findViewById(R.id.txtCancelBillStatus);
        RecyclerView recyclerViewDetailBill = dialogView.findViewById(R.id.recylerViewCancelBills);
        Button btnHuyXacNhanDon = dialogView.findViewById(R.id.btnCancelDonHangHuy);
        ImageView btnClose = dialogView.findViewById(R.id.imageCancelBtnClose);

        txtDialogBillID.setText(bill.getBill_ID());
        txtDialogCusID.setText(bill.getCus_ID());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date orderDate = bill.getOrder_Date().toDate();
        String formattedOrderDate = sdf.format(orderDate);
        txtDialogOrderDate.setText(formattedOrderDate);
        Date deliveryDate = bill.getDelivery_Order().toDate();
        String formattedDeliveryDate = sdf.format(deliveryDate);
        txtDialogDeliveryDate.setText(formattedDeliveryDate);
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        txtDialogTotalBill.setText(currencyFormatter.format(bill.getTotal_Bill()));
        txtDialogBillStatus.setText(bill.getBill_Status());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewDetailBill.setLayoutManager(linearLayoutManager);
        cartDetailDAO.selectAllByCartID(bill.getCart_ID(), new OnDataLoadedCallbackCartDetail() {
            @Override
            public void onDataLoaded(ArrayList<CartDetail> cartDetails) {
                CustomAdapterRecyclerViewBillDetails adapter = new CustomAdapterRecyclerViewBillDetails(context, cartDetails);
                recyclerViewDetailBill.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return listBill.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCcIDBill, txtCcTenKH, txtCcOderDate, txtCcBillStatus;
        ImageView imageCcBtnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCcIDBill = itemView.findViewById(R.id.txtCcIDBill);
            txtCcTenKH = itemView.findViewById(R.id.txtCcTenKH);
            txtCcOderDate = itemView.findViewById(R.id.txtCcOderDate);
            txtCcBillStatus = itemView.findViewById(R.id.txtCcBillStatus);
            imageCcBtnDetails = itemView.findViewById(R.id.imageCcBtnDetails);
        }
    }
}

