package com.example.quanlycuahangbandoanvat.GUI.Admin.Statistic_Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.BUS.BillBUS;
import com.example.quanlycuahangbandoanvat.DAO.BillDAO;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCalbackBill;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.Helper.Formatter;
import com.example.quanlycuahangbandoanvat.Helper.Validation;
import com.example.quanlycuahangbandoanvat.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.checkerframework.checker.units.qual.A;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticAdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    BarChart barChart;

    public StatisticAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticAdminFragment newInstance(String param1, String param2) {
        StatisticAdminFragment fragment = new StatisticAdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistic_admin, container, false);
    }
    BillDAO billDAO = new BillDAO();
    BillBUS billBUS = new BillBUS();
    float [] dailyRevenue;
    Button btnThongKe;
    EditText edtMonth,edtYear;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barChart =  (view).findViewById(R.id.bar_chart);
        setupBarChart();
        // ánh xạ ID
        btnThongKe = view.findViewById(R.id.btnThongKe);
        edtMonth=view.findViewById(R.id.edtMonth);
        edtYear=view.findViewById(R.id.edtYear);


        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monthString = edtMonth.getText().toString();
                String yearString = edtYear.getText().toString();

                if(monthString.isEmpty() || yearString.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill out all information !", Toast.LENGTH_SHORT).show();
                }
                else {
                    int month = Integer.parseInt(monthString);
                    int year = Integer.parseInt(yearString);
                    if(Validation.isValidMonth(month) && Validation.isValidYear(year)) {
                        billDAO.selectAll(new OnDataLoadedCalbackBill() {
                            @Override
                            public void onDataLoaded(ArrayList<Bill> t) {
                                billBUS = new BillBUS(t);

                                dailyRevenue = billBUS.getStatisticByMonth(month, year);

                                if (hasRevenueData(dailyRevenue)) {

                                    TextView noDataTextView = view.findViewById(R.id.tv_nodata);
                                    loadBarChartData(dailyRevenue);
                                    barChart.setVisibility(View.VISIBLE);
                                    noDataTextView.setVisibility(View.GONE);

                                } else {
                                    TextView noDataTextView = view.findViewById(R.id.tv_nodata);
                                    barChart.setVisibility(View.GONE);
                                    noDataTextView.setVisibility(View.VISIBLE);
                                }
                            }
                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(), "Invalid month or year !", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
    private void setupBarChart() {
        //Tắt mô tả mặc định của biểu đồ.
        barChart.getDescription().setEnabled(false);
        //Tắt nền lưới của biểu đồ
        barChart.setDrawGridBackground(false);
        //Tắt việc vẽ bóng mờ cho các cột.
        barChart.setDrawBarShadow(false);
        //Hiển thị giá trị của mỗi cột phía trên cột.
        barChart.setDrawValueAboveBar(true);
        //Cho phép thu phóng bằng cách chụm hai ngón tay.
        barChart.setPinchZoom(true);
        //Cho phép phóng to và thu nhỏ biểu đồ.
        barChart.setScaleEnabled(true);
        //Đặt khoảng cách lề thêm cho biểu đồ
        barChart.setExtraOffsets(10, 10, 10, 10);
        //Đặt vị trí của trục X ở phía dưới biểu đồ--> thiết lập trục X//
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Đặt khoảng cách tối thiểu giữa các nhãn trục X
        xAxis.setGranularity(1f);
        //Tắt vẽ các đường lưới dọc trên trục X.
        xAxis.setDrawGridLines(false);
        //Đặt bộ định dạng cho các giá trị trục X.
        xAxis.setValueFormatter(new IndexAxisValueFormatter());
        //Tắt vẽ các đường lưới ngang trên trục Y bên trái.
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        //Tắt trục Y bên phải
        barChart.getAxisRight().setEnabled(false);
    }

    private void loadBarChartData(float[] dailyRevenue) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dailyRevenue.length; i++) {
            entries.add(new BarEntry(i + 1, dailyRevenue[i]));
        }

        if (entries.isEmpty()) {
            barChart.setVisibility(View.GONE);
            return;
        } else {
            barChart.setVisibility(View.VISIBLE);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu hàng ngày");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(16f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                if (barEntry.getY() == 0) {
                    return "";
                }
                return Formatter.FormatVND(barEntry.getY());
            }
        });

        BarData data = new BarData(dataSet);
        barChart.setData(data);

        // Thiết lập trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Ngày " + ((int) value);
            }
        });

        // Thiết lập cuộn ngang cho biểu đồ
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setAxisMaximum(entries.size());

        barChart.invalidate();
    }
    private boolean hasRevenueData(float[] dailyRevenue) {
        for (float revenue : dailyRevenue) {
            if (revenue != 0) {
                return true;
            }
        }
        return false;
    }
}

