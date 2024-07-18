package com.example.quanlycuahangbandoanvat.BUS;

import com.example.quanlycuahangbandoanvat.DTO.Bill;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;

public class BillBUS {
    private ArrayList<Bill> listBill = new ArrayList<>();

    public ArrayList<Bill> getListBill() {
        return listBill;
    }

    public void setListBill(ArrayList<Bill> listBill) {
        this.listBill = listBill;
    }

    public BillBUS(ArrayList<Bill> listBill) {
        this.listBill = listBill;
    }

    public BillBUS() {

    }
    public Bill getByBillID(String id) {
        int vitri = -1;
        int i = 0;
        for (i = 0; i < this.listBill.size(); i++) {
            if (this.listBill.get(i).getBill_ID().equals(id)) {
                vitri = i;
                break;
            }
        }
        if (vitri != -1) {
            return this.listBill.get(vitri);
        } else {
            return null;
        }
    }
    public Bill getBillByCustomerIDs(String id) {
        int vitri = -1;
        for (int i = 0; i < this.listBill.size(); i++) {
            if (this.listBill.get(i).getCus_ID().equals(id)) {
                vitri = i;
                break;
            }
        }
        if (vitri != -1) {
            return this.listBill.get(vitri);
        } else {
            return null;
        }
    }
    public float[] getStatisticByMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        float[] dailyRevenue = new float[daysInMonth];

        for (Bill bill : listBill) {
            Timestamp orderTimestamp = bill.getOrder_Date();
            if (orderTimestamp != null) {
                calendar.setTimeInMillis(orderTimestamp.toDate().getTime());

                int billMonth = calendar.get(Calendar.MONTH) + 1;
                int billYear = calendar.get(Calendar.YEAR);

                if (billMonth == month && billYear == year) {
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    dailyRevenue[day - 1] += bill.getTotal_Bill();
                }
            }
        }
        return dailyRevenue;
    }
    public int getTotalBill() {
        return this.listBill.size();
    }
    public int getUnconfirmedBill() {
        int result = 0;
        for(Bill item : this.listBill) {
            if(item.getBill_Status().equals("Chờ xác nhận"));
            result ++;
        }
        return result;
    }
    public double getRevenueBill() {
        double result = 0;
        for(Bill item : this.listBill) {
            result+=item.getTotal_Bill();
        }
        return result;
    }
}
