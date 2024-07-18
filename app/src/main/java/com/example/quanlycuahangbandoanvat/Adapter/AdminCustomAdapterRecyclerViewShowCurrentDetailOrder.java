package com.example.quanlycuahangbandoanvat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangbandoanvat.BUS.CustomerBUS;
import com.example.quanlycuahangbandoanvat.DAO.BillDAO;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCustomer;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DAO.CartDetailDAO;
import com.example.quanlycuahangbandoanvat.DAO.CustomerDAO;
import com.example.quanlycuahangbandoanvat.DAO.NotificationDAO;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.Notification;
import com.example.quanlycuahangbandoanvat.R;
import com.google.firebase.Timestamp;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdminCustomAdapterRecyclerViewShowCurrentDetailOrder extends RecyclerView.Adapter<AdminCustomAdapterRecyclerViewShowCurrentDetailOrder.ViewHolder> {

    private Context context;
    private ArrayList<Bill> listBill;
    CustomerDAO customerDAO = new CustomerDAO();
    CartDAO cartDAO = new CartDAO();
    CartDetailDAO cartDetailDAO = new CartDetailDAO();
    BillDAO billDAO = new BillDAO();
    NotificationDAO notificationDAO = new NotificationDAO();
    AlertDialog dialog;

    public AdminCustomAdapterRecyclerViewShowCurrentDetailOrder(Context context, ArrayList<Bill> listBill) {
        this.context = context;
        this.listBill = listBill;
    }

    @NonNull
    @Override
    public AdminCustomAdapterRecyclerViewShowCurrentDetailOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_admin_custom_item_show_current_order, parent, false);
        return new AdminCustomAdapterRecyclerViewShowCurrentDetailOrder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = listBill.get(position);

        holder.txtCurrIDBill.setText(bill.getBill_ID());
        Date orderDate = bill.getOrder_Date().toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(orderDate);
        holder.txtCurrOderDate.setText(formattedDate);
        holder.txtCurrBillStatus.setText(bill.getBill_Status());

        customerDAO.selectAll(new OnDataLoadedCallbackCustomer() {
            @Override
            public void onDataLoaded(ArrayList<Customer> t) {
                CustomerBUS customerBUS = new CustomerBUS(t);
                Customer cus = customerBUS.getByCustomerID(bill.getCus_ID());
                if (cus != null) {
                    holder.txtCurrTenKH.setText(cus.getCus_Name());
                }
            }
            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });

        holder.imageCurrBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBillCurrentOrderDialog(bill);
            }
        });
    }

    private void showBillCurrentOrderDialog(Bill bill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_admin_custom_detail_current_oder, null);

        TextView txtDialogBillID = dialogView.findViewById(R.id.txtCurrentBillID);
        TextView txtDialogCusID = dialogView.findViewById(R.id.txtCurrentCusID);
        TextView txtDialogOrderDate = dialogView.findViewById(R.id.txtCurrentOrderDate);
        TextView txtDialogDeliveryDate = dialogView.findViewById(R.id.txtCurrentDeliveryDate);
        TextView txtDialogTotalBill = dialogView.findViewById(R.id.txtCurrentTotalBill);
        TextView txtDialogBillStatus = dialogView.findViewById(R.id.txtCurrentBillStatus);
        RecyclerView recyclerViewDetailBill = dialogView.findViewById(R.id.recylerViewCurrentBill);
        Button btnXacNhan = dialogView.findViewById(R.id.btnXacNhanDon);
        ImageView btnClose = dialogView.findViewById(R.id.imageCurrentBtnClose);

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

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bill.getBill_ID() != null && bill.getBill_Status().equals("Chờ xác nhận")) {
                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(context);
                    confirmBuilder.setTitle("Xác nhận đơn hàng cho khách");
                    confirmBuilder.setMessage("Bạn có chắc muốn xác nhận đơn hàng này?");
                    confirmBuilder.setPositiveButton("Xác Nhận", (dialog, which) -> {
                        Bill billUpdate = new Bill(bill.getBill_ID(), bill.getCart_ID(), bill.getCus_ID(), bill.getFood_Promotion_ID(), bill.getOrder_Date(), new Timestamp(new Date()), "Đã xác nhận", bill.getTotal_Bill());
                        billDAO.update(billUpdate, new CRUDCallback() {
                            @Override
                            public void onCRUDComplete(int result) {
                                if (result == 1) {
                                    AlertDialog.Builder infoBuilder = new AlertDialog.Builder(context);
                                    infoBuilder.setTitle("Thông báo");
                                    infoBuilder.setMessage("Đơn hàng đã được xác nhận thành công !");

                                    String sender_ID = "XUdl4K3PDIo56n6t16OR";
                                    Notification notification = new Notification(null,bill.getBill_ID(),bill.getCus_ID(),sender_ID,new Date(),false,"Bạn có một đơn hàng " + bill.getBill_ID() + " đã được xác nhận"  );
                                    notificationDAO.insert(notification, new CRUDCallback() {
                                        @Override
                                        public void onCRUDComplete(int result) {
                                            if (result == 1){
                                                Toast.makeText(context,"Đã gửi thông báo đến khách hàng ",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    txtDialogBillStatus.setText(billUpdate.getBill_Status());
                                    infoBuilder.setPositiveButton("OK", (dialog, which) -> {
                                        dialog.dismiss();
                                    });
                                    infoBuilder.show();
                                }
                            }
                        });
                    });
                    confirmBuilder.setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    confirmBuilder.show();
                }else{
                    Toast.makeText(context,"Lỗi xác nhận đơn hàng !",Toast.LENGTH_SHORT).show();
                }
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
        TextView txtCurrIDBill, txtCurrTenKH, txtCurrOderDate, txtCurrBillStatus;
        ImageView imageCurrBtnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCurrIDBill = itemView.findViewById(R.id.txtCurrIDBill);
            txtCurrTenKH = itemView.findViewById(R.id.txtCurrTenKH);
            txtCurrOderDate = itemView.findViewById(R.id.txtCurrOderDate);
            txtCurrBillStatus = itemView.findViewById(R.id.txtCurrBillStatus);
            imageCurrBtnDetails = itemView.findViewById(R.id.imageCurrBtnDetails);
        }
    }
}

