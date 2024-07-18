package com.example.quanlycuahangbandoanvat.DAO;

import androidx.annotation.NonNull;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCustomer;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomerDAO implements DAOinterface<Customer>{

    private FirebaseFirestore firestore;
    private CollectionReference CustomerCollection;

    public CustomerDAO() {
        this.firestore = FirebaseFirestore.getInstance();
        this.CustomerCollection = firestore.collection("customer");
    }
    @Override
    public void insert(Customer customer, final CRUDCallback callback) {
        if (customer != null) {
            // Add data to Firebase Store
            firestore.collection("customer").add(customer).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        String idForField = task.getResult().getId(); // Lấy ID của tài liệu mới được thêm vào
                        customer.setCus_ID(idForField); // Thiết lập ID cho đối tượng Customer

                        // Update the document to set the "Customer_ID" field
                        String documentId = task.getResult().getId();
                        firestore.collection("customer").document(documentId).update("cus_ID", idForField)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> updateTask) {
                                        if (updateTask.isSuccessful()) {
                                            callback.onCRUDComplete(1); // Thành công -> lưu trữ kết quả
                                        } else {
                                            callback.onCRUDComplete(0); // Thất bại
                                        }
                                    }
                                });
                    } else {
                        callback.onCRUDComplete(0);
                    }
                }
            });
        } else {
            callback.onCRUDComplete(-1);
        }
    }

    @Override
    public void update(Customer Customer, final CRUDCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference CustomerRef = db.collection("customer").document(Customer.getCus_ID());

        CustomerRef.set(Customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCRUDComplete(1);
                } else {
                    callback.onCRUDComplete(0);
                }
            }
        });
    }

    @Override
    public void delete(String t, final CRUDCallback callback) {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference CustomerRef = firestore.collection("customer").document(t);

        final int[] result = new int[1];
        CustomerRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public ArrayList<Customer> selectAll(final OnDataLoadedCallbackCustomer listener) {
        final ArrayList<Customer> listCustomer = new ArrayList<>();
        this.CustomerCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Customer Customer = document.toObject(Customer.class);
                        listCustomer.add(Customer);
                    }
                    listener.onDataLoaded(listCustomer); // Gửi kết quả về qua callback
                } else {
                    listener.onError("Get list unsuccessfully"); // Trường hợp không thành công
                }
            }
        });
        return  listCustomer;
    }


    @Override
    public Customer selectById(String t) {
        DocumentReference CustomerRef = firestore.collection("customer").document(t);
        final Customer[] Customer = {new Customer()};
        CustomerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Customer[0] = document.toObject(Customer.class);
                    } else {

                    }
                } else {

                }
            }
        });
        return  Customer[0];
    }
}

