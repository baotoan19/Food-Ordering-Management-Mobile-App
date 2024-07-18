package com.example.quanlycuahangbandoanvat.GUI.AccountFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterRecycleViewOderBillTrue;
import com.example.quanlycuahangbandoanvat.BUS.BillBUS;
import com.example.quanlycuahangbandoanvat.DAO.BillDAO;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCalbackBill;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccountOrder_Fragment extends Fragment {

    private RecyclerView recyclerViewOderBillTrue;
    private CustomAdapterRecycleViewOderBillTrue recyclerViewOderBillTrueAdapter;
    private ArrayList<Bill> listBill = new ArrayList<>();
    private BillDAO billDAO = new BillDAO();
    private CartDAO cartDAO = new CartDAO();
    private FirebaseFirestore firestore;
    private ListenerRegistration cartListener;
    private ListenerRegistration billListener;
    private String current_cusID;

    FrameLayout emptyFragmentContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_order_, container, false);

        // Controls
        recyclerViewOderBillTrue = view.findViewById(R.id.recyclerViewTrue);
        recyclerViewOderBillTrue.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get Customer ID
        current_cusID = getCustomerIDFromSharedReferences();

        emptyFragmentContainer = view.findViewById(R.id.empty_fragment_container);

        // Set up real-time listeners
        setupListeners();

        return view;
    }

    private void setupListeners() {
        billListener = firestore.collection("bill")
                .whereEqualTo("cus_ID", current_cusID)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("AccountOrder_Fragment", "Error listening for bill changes: " + error.getMessage());
                        return;
                    }

                    if (querySnapshot != null) {
                        updateBills(querySnapshot.getDocuments());
                    }
                });
    }

    private void updateBills(List<DocumentSnapshot> documents) {
        listBill.clear();

        for (DocumentSnapshot document : documents) {
            Bill bill = document.toObject(Bill.class);
            if (bill != null) {
                listBill.add(bill);
            }
        }

        if (emptyFragmentContainer != null && listBill.isEmpty()) {
            loadEmptyFragment();
        } else {
            updateRecyclerView();
        }
    }


    private void loadEmptyFragment() {
        if (emptyFragmentContainer != null) {
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
            fragmentTransaction.replace(emptyFragmentContainer.getId(), new AcountOrderEmpty_Fragment());
            fragmentTransaction.commit();
        }
    }

    private void updateRecyclerView() {
        recyclerViewOderBillTrueAdapter = new CustomAdapterRecycleViewOderBillTrue(getContext(), listBill);
        recyclerViewOderBillTrue.setAdapter(recyclerViewOderBillTrueAdapter);
    }

    private String getCustomerIDFromSharedReferences() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("current_customer_id", "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (cartListener != null) {
            cartListener.remove();
        }
        if (billListener != null) {
            billListener.remove();
        }
    }
}