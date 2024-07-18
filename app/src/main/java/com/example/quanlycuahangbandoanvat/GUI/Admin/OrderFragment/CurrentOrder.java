package com.example.quanlycuahangbandoanvat.GUI.Admin.OrderFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycuahangbandoanvat.Adapter.AdminCustomAdapterRecyclerViewShowCurrentDetailOrder;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrder extends Fragment {
    private AdminCustomAdapterRecyclerViewShowCurrentDetailOrder adminCustomAdapterRecyclerViewCurrentOrder;
    private ArrayList<Bill> listBill = new ArrayList<>();
    private RecyclerView recyclerViewAdminCurrOrder;
    private FirebaseFirestore firestore;
    private ListenerRegistration listenerRegistration; // Store the listener registration

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_order, container, false);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Add Controls
        recyclerViewAdminCurrOrder = view.findViewById(R.id.recyclerViewCurrentOrder);
        recyclerViewAdminCurrOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        // Add listener for real-time updates
        loadAllBillCurrentOrder();


        return view;
    }

    private void loadAllBillCurrentOrder() {
        listenerRegistration = firestore.collection("bill").whereEqualTo("bill_Status", "Chờ xác nhận").addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                return;
            }
            if (querySnapshot != null) {
                List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                listBill.clear();

                for (DocumentSnapshot document : documentSnapshots) {
                    Bill bill = document.toObject(Bill.class);
                    if (bill != null) {
                        listBill.add(bill);
                    }
                }
                updateRecyclerView();
            }
        });
    }


    private void updateRecyclerView() {
        adminCustomAdapterRecyclerViewCurrentOrder = new AdminCustomAdapterRecyclerViewShowCurrentDetailOrder(getContext(), listBill);
        recyclerViewAdminCurrOrder.setAdapter(adminCustomAdapterRecyclerViewCurrentOrder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}