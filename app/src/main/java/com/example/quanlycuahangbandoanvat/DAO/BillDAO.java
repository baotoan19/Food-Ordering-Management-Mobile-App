package com.example.quanlycuahangbandoanvat.DAO;


import androidx.annotation.NonNull;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCalbackBill;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DTO.Bill;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.DTO.CartDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BillDAO implements DAOinterface<Bill>{

    private FirebaseFirestore firestore;
    private CollectionReference billCollection;

    public BillDAO() {
        this.firestore = FirebaseFirestore.getInstance();
        this.billCollection = firestore.collection("bill");
    }
    @Override
    public void insert(Bill bill, final CRUDCallback callback) {
        final boolean[] check = {false};
        if (bill != null) {
            // Add data to Firebase Store
            firestore.collection("bill").add(bill).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        check[0] = true;
                        String idForField = task.getResult().getId();
                        bill.setBill_ID(idForField);

                        String documentId = task.getResult().getId();
                        firestore.collection("bill").document(documentId).update("bill_ID", idForField)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> updateTask) {
                                        if (updateTask.isSuccessful()) {
                                            callback.onCRUDComplete(1);
                                        } else {
                                            callback.onCRUDComplete(0);
                                        }
                                    }
                                });
                    } else {
                        check[0] = false;
                        callback.onCRUDComplete(0);
                    }
                }
            });
        } else {
            callback.onCRUDComplete(-1);
        }
    }

    @Override
    public void update(Bill bill, final CRUDCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (bill != null && bill.getBill_ID() != null) {
            DocumentReference foodRef = db.collection("bill").document(bill.getBill_ID());
            foodRef.set(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        callback.onCRUDComplete(1);
                    } else {
                        callback.onCRUDComplete(0);
                    }
                }
            });
        } else {
            // Handle error: Either cartDetail is null or cartDetail_ID is null
            callback.onCRUDComplete(-1); // Or handle the error differently
        }
    }

    @Override
    public void delete(String t, final CRUDCallback callback) {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference foodRef = firestore.collection("bill").document(t);

        final int[] result = new int[1];
        foodRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //result[0] = 1;
                    callback.onCRUDComplete(1); // Thành công
                } else {
                    //result[0] = 0;
                    callback.onCRUDComplete(0);
                }
            }
        });
    }

    //    @Override
    public ArrayList<Bill> selectAll(final OnDataLoadedCalbackBill listener) {
        final ArrayList<Bill> listBill = new ArrayList<>();
        this.billCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Bill bill = document.toObject(Bill.class);
                        listBill.add(bill);
                    }
                    listener.onDataLoaded(listBill);
                } else {
                    listener.onError(null);
                }
            }
        });
        return  listBill;
    }

    public Task<ArrayList<Bill>> selectAlls() {
        return billCollection.get().continueWith(task -> {
            if (task.isSuccessful()) {
                ArrayList<Bill> listBill = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Bill bill = document.toObject(Bill.class);
                    listBill.add(bill);
                }
                return listBill;
            } else {
                // Handle errors: You could throw an exception, log the error, or return null.
                throw task.getException();
            }
        });
    }

    public void selectAllByCartID(String cartID, final OnDataLoadedCalbackBill listener) {
        billCollection.whereEqualTo("bill_ID", cartID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Bill> listBill = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Bill bill = document.toObject(Bill.class);
                    listBill.add(bill);
                }
                listener.onDataLoaded(listBill);
            } else {
                listener.onError(task.getException().getMessage());
            }
        });
    }

    @Override
    public Bill selectById(String t) {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference foodRef = firestore.collection("bill").document(t);
        final Bill[] bills = {new Bill()};

        foodRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        bills[0] = document.toObject(Bill.class);
                    } else {

                    }
                } else {

                }
            }
        });
        return  bills[0];
    }
}