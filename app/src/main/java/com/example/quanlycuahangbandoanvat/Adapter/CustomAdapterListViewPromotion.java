package com.example.quanlycuahangbandoanvat.Adapter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlycuahangbandoanvat.DTO.Promotion;
import com.example.quanlycuahangbandoanvat.Helper.Formatter;
import com.example.quanlycuahangbandoanvat.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CustomAdapterListViewPromotion extends ArrayAdapter {
    Context context; // ngữ cảnh
    int layoutItem; // layout item
    ArrayList<Promotion> listPromotion = new ArrayList<>(); // dữ liệu


    public CustomAdapterListViewPromotion(@NonNull Context context, int layoutItem, @NonNull ArrayList<Promotion> listPromotion) {
        super(context, layoutItem, listPromotion);
        this.context = context;
        this.layoutItem = layoutItem;
        this.listPromotion = listPromotion;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Promotion promotion = listPromotion.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutItem, null);
        }
        Button btnPromotionCopy=convertView.findViewById(R.id.btnPromotionCopy);

        btnPromotionCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("promotion code label", promotion.getFood_Promotion_ID());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getContext(), "Đã lấy mã giảm giá!", Toast.LENGTH_SHORT).show();
            }
        });
        TextView tvName = convertView.findViewById(R.id.tvPromotionPromotionName);
        tvName.setText(promotion.getPromotion_Name());

        TextView tvPrice = convertView.findViewById(R.id.tvPromotionDiscountAmount);

        tvPrice.setText("Giảm giá: " + Formatter.FormatVND(promotion.getDiscount_Amount()));

        TextView tvPromotionLimitedQuantity = convertView.findViewById(R.id.tvPromotionTotalMin);
        tvPromotionLimitedQuantity.setText("Điều kiện: Hóa đơn có trị giá từ " + Formatter.FormatVND(promotion.getTotal_min()));

        // Thêm tính năng đếm ngược thời gian cho tvPromotionCountdown
        TextView tvPromotionCountdown = convertView.findViewById(R.id.tvPromotionCountdown);
        long millisecondsUntilEnd = promotion.getDate_End().getTime() - System.currentTimeMillis();

        if (millisecondsUntilEnd <= 0) {
            tvPromotionCountdown.setText("Khuyến mại đã kết thúc!");
        } else {
            new CountDownTimer(millisecondsUntilEnd, 1000) { // Update every second
                public void onTick(long millisUntilFinished) {
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                    int days = (int) (hours / 24);
                    int remainingHours = (int) (hours % 24);

                    String timeRemaining = String.format("%02d ngày %02d:%02d:%02d", days, remainingHours, minutes, seconds);
                    tvPromotionCountdown.setText("Thời gian còn lại: " + timeRemaining);

                }

                public void onFinish() {
                    tvPromotionCountdown.setText("Khuyến mại đã kết thúc!");
                }
            }.start();
        }
        return convertView;
    }
}

