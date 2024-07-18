package com.example.quanlycuahangbandoanvat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlycuahangbandoanvat.BUS.NotificationBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.NotificationDAO;
import com.example.quanlycuahangbandoanvat.DTO.Notification;
import com.example.quanlycuahangbandoanvat.GUI.Admin.NotificationFragment.NotificationAdminFragment;
import com.example.quanlycuahangbandoanvat.GUI.MainFragment.Option;
import com.example.quanlycuahangbandoanvat.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CustomAdapterFragmentNotificationForAdmin extends ArrayAdapter {
    Context context; // ngữ cảnh
    int layoutItem; // layout item
    ArrayList<Notification> listNotification = new ArrayList<>(); // dữ liệu


    public CustomAdapterFragmentNotificationForAdmin(@NonNull Context context, int layoutItem, @NonNull  ArrayList<Notification> listNotification) {
        super(context, layoutItem, listNotification);
        this.context=context;
        this.layoutItem = layoutItem;
        this.listNotification=listNotification;
    }

    NotificationDAO notificationDAO = new NotificationDAO();
    NotificationBUS notificationBUS = new NotificationBUS();

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Notification notification = listNotification.get(position);
        String notificationID = notification.getNotification_ID();
        Option option = Option.newInstance("", "");
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(layoutItem,null);
        }

        TextView tvNotificationContent = (TextView) convertView.findViewById(R.id.tvNotificationContent);
        tvNotificationContent.setText(notification.getNotification_Content());

        TextView tvNotificationTimeAgo = (TextView) convertView.findViewById(R.id.tvNotificationTimeAgo);
        long millisecondsUntilEnd = System.currentTimeMillis() - notification.getNotification_Date().getTime();
        int hours = (int) (millisecondsUntilEnd / (1000.0 * 3600.0));
        tvNotificationTimeAgo.setText(String.valueOf(hours) + " hours ago");

        String status;
        if(notification.getNotification_Status() == true){
            status = "Seen";
        }
        else {
            status = "Unnoticed";
        }
        TextView tvNotificationStatus = (TextView) convertView.findViewById(R.id.tvNotificationStatus);
        tvNotificationStatus.setText(status);

        // Đăng ký context menu cho ImageView
        ImageView imageViewNotificationOption = (ImageView) convertView.findViewById(R.id.imageViewNotificationOption);
        imageViewNotificationOption.setOnClickListener(view -> {
            // Đăng ký context menu khi ImageView được nhấn
            imageViewNotificationOption.showContextMenu();
        });
        imageViewNotificationOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, v);

                // Gắn menu từ XML vào PopupMenu
                popupMenu.getMenuInflater().inflate(R.menu.menu_notification_options, popupMenu.getMenu());

                // Sử dụng reflection để buộc hiển thị biểu tượng
                try {
                    Field popup = PopupMenu.class.getDeclaredField("mPopup");
                    popup.setAccessible(true);
                    Object menuPopupHelper = popup.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_mark_as_read:
                                    notificationDAO.updateNotificationStatus(notificationID, true, new CRUDCallback() {
                                        @Override
                                        public void onCRUDComplete(int result) {
                                            Toast.makeText(getContext(), "Mark successfully", Toast.LENGTH_SHORT).show();
                                            loadFragmentOption();
                                        }
                                    });
                                return true;
                            case R.id.action_delete_notification:
                                notificationDAO.delete(notificationID, new CRUDCallback() {
                                    @Override
                                    public void onCRUDComplete(int result) {
                                        Toast.makeText(getContext(), "Delete notification successfully", Toast.LENGTH_SHORT).show();
                                        loadFragmentOption();
                                    }
                                });
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                // Hiển thị menu context
                popupMenu.show();
            }
        });
        return convertView;
    }
    private void loadFragmentOption() {
        FragmentManager fragmentManager = null;
        if (context instanceof FragmentActivity) {
            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        } else if (context instanceof AppCompatActivity) {
            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        }

        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FrameLayoutAdminActivity, new NotificationAdminFragment());
            fragmentTransaction.commit();
        } else {
            throw new IllegalStateException("Context does not support FragmentManager");
        }
    }
}
