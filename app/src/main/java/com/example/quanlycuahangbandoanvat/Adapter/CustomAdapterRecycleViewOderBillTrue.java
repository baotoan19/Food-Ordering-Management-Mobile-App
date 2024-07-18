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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangbandoanvat.DAO.BillDAO;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.CartDetailDAO;
import com.example.quanlycuahangbandoanvat.DAO.FoodDAO;
import com.example.quanlycuahangbandoanvat.DAO.NotificationDAO;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.example.quanlycuahangbandoanvat.DTO.Notification;
import com.example.quanlycuahangbandoanvat.GUI.AccountFragment.AccountOrder_Fragment;
import com.example.quanlycuahangbandoanvat.GUI.MainFragment.Account;
import com.example.quanlycuahangbandoanvat.R;

import com.google.firebase.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomAdapterRecycleViewOderBillTrue extends RecyclerView.Adapter<CustomAdapterRecycleViewOderBillTrue.ViewHolder> {
    private Context context;
    private ArrayList<Bill> listBill;
    private FoodDAO foodDAO = new FoodDAO();
    private CartDetailDAO cartDetailDAO = new CartDetailDAO();
    private ArrayList<CartDetail> listCartDetail = new ArrayList<>();
    BillDAO billDAO = new BillDAO();
    NotificationDAO notificationDAO = new NotificationDAO();
    AlertDialog dialog;

    public CustomAdapterRecycleViewOderBillTrue(Context context, ArrayList<Bill> listBill) {
        this.context = context;
        this.listBill = listBill;
    }

    @NonNull
    @Override
    public CustomAdapterRecycleViewOderBillTrue.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recyitem_oder_istrue, parent, false);
        return new CustomAdapterRecycleViewOderBillTrue.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = listBill.get(position);
        holder.txtIdBillOder.setText(bill.getBill_ID());

        Date orderDate = bill.getOrder_Date().toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(orderDate);
        holder.txtDateOder.setText(formattedDate);

        holder.txtBillStatus.setText(bill.getBill_Status());

        // Set màu cho bill status
        if (bill.getBill_Status().equals("Chờ xác nhận")) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
        } else if (bill.getBill_Status().equals("Đã xác nhận")){
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.green));
        }else{
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.btnHuy));
        }

        holder.imageBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBillDetailsDialog(bill);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBill.size();
    }

    private void showBillDetailsDialog(Bill bill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_details_dialogs_cart, null);

        TextView txtDialogBillID = dialogView.findViewById(R.id.txtDetailBillID);
        TextView txtDialogCusID = dialogView.findViewById(R.id.txtDetailCusID);
        TextView txtDialogOrderDate = dialogView.findViewById(R.id.txtDetailOrderDate);
        TextView txtDialogDeliveryDate = dialogView.findViewById(R.id.txtDetailDeliveryDate);
        TextView txtDialogTotalBill = dialogView.findViewById(R.id.txtDetailTotalBill);
        TextView txtDialogBillStatus = dialogView.findViewById(R.id.txtDetailBillStatus);
        RecyclerView recyclerViewDetailBill = dialogView.findViewById(R.id.recylerViewDetails);
        Button btnDatLai = dialogView.findViewById(R.id.btnDatLaiDon);
        Button btnHuyDon = dialogView.findViewById(R.id.btnHuyDon);
        ImageView btnClose = dialogView.findViewById(R.id.imageBtnClose);

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

        btnDatLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bill billNew = new Bill(null, bill.getCart_ID(), bill.getCus_ID(), bill.getFood_Promotion_ID(), new Timestamp(new Date()), new Timestamp(new Date()), "Chờ xác nhận", bill.getTotal_Bill());

                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(context);
                confirmBuilder.setTitle("Xác nhận đặt lại đơn hàng");
                confirmBuilder.setMessage("Bạn có chắc muốn đặt lại đơn hàng này?");
                confirmBuilder.setPositiveButton("Đặt lại", (dialog, which) -> {
                    billDAO.insert(billNew, new CRUDCallback() {
                        @Override
                        public void onCRUDComplete(int result) {
                            if (result == 1) {
                                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(context);
                                infoBuilder.setTitle("Thông báo");
                                infoBuilder.setMessage("Đơn hàng đã được đặt lại thành công !");

                                String receder_ID = "XUdl4K3PDIo56n6t16OR";
                                Notification notification = new Notification(null,billNew.getBill_ID(),receder_ID,bill.getCus_ID(),new Date(),false,"Đơn hàng " + bill.getBill_ID() + " đang chờ xác nhận !" );
                                notificationDAO.insert(notification, new CRUDCallback() {
                                    @Override
                                    public void onCRUDComplete(int result) {
                                        if (result == 1){

                                        }
                                    }
                                });

                                infoBuilder.setPositiveButton("OK", (dialog, which) -> {
                                    dialog.dismiss();
                                    loadFragment(new Account());
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
            }
        });

        btnHuyDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bill.getBill_ID() != null && bill.getBill_Status().equals("Chờ xác nhận")) {
                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(context);
                    confirmBuilder.setTitle("Xác nhận hủy đơn hàng");
                    confirmBuilder.setMessage("Bạn có chắc muốn hủy đơn hàng này?");
                    confirmBuilder.setPositiveButton("Hủy", (dialog, which) -> {
                        Bill billUpdate = new Bill(bill.getBill_ID(), bill.getCart_ID(), bill.getCus_ID(), bill.getFood_Promotion_ID(), bill.getOrder_Date(), new Timestamp(new Date()), "Đơn đã hủy", bill.getTotal_Bill());
                        billDAO.update(billUpdate, new CRUDCallback() {
                            @Override
                            public void onCRUDComplete(int result) {
                                if (result == 1) {
                                    AlertDialog.Builder infoBuilder = new AlertDialog.Builder(context);
                                    infoBuilder.setTitle("Thông báo");
                                    infoBuilder.setMessage("Đơn hàng đã được hủy thành công !");
                                    String receder_ID = "XUdl4K3PDIo56n6t16OR";
                                    Notification notification = new Notification(null,bill.getBill_ID(),receder_ID,bill.getCus_ID(),new Date(),false,"Khách Hàng " + bill.getCus_ID() + " vừa hủy đơn hàng " + bill.getCart_ID());
                                    notificationDAO.insert(notification, new CRUDCallback() {
                                        @Override
                                        public void onCRUDComplete(int result) {
                                            if (result == 1){

                                            }
                                        }
                                    });
                                    txtDialogBillStatus.setText(billUpdate.getBill_Status());
                                    infoBuilder.setPositiveButton("OK", (dialog, which) -> {
                                        dialog.dismiss();
                                        loadFragment(new Account());
                                    });
                                    infoBuilder.show();
                                }
                            }
                        });
                    });
                    confirmBuilder.setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    confirmBuilder.show(); // Show the confirmation dialog
                } else if (bill.getBill_ID() != null && bill.getBill_Status().equals("Đã xác nhận")) {
                    AlertDialog.Builder infoBuilder = new AlertDialog.Builder(context);
                    infoBuilder.setTitle("Thông báo");
                    infoBuilder.setMessage("Đơn hàng đã được xác nhận và không thể hủy vui lòng liên hệ tới holine của cửa hàng!");
                    infoBuilder.setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    infoBuilder.show();
                }else{
                    Toast.makeText(context,"Đơn hàng này đã được hủy !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtIdBillOder, txtDateOder, txtBillStatus;
        ImageView imageBtnDetails;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdBillOder = itemView.findViewById(R.id.txtIDBill);
            txtDateOder = itemView.findViewById(R.id.txtOderDate);
            txtBillStatus = itemView.findViewById(R.id.txtBillStatus);
            imageBtnDetails = itemView.findViewById(R.id.imageBtnDetails);
            cardView = itemView.findViewById(R.id.cartView1);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutMainActivity, fragment);
        fragmentTransaction.commit();
    }

}