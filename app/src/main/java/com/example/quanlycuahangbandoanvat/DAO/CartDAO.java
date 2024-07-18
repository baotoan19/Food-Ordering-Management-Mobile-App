package com.example.quanlycuahangbandoanvat.DAO;


import androidx.annotation.NonNull;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartDAO implements DAOinterface<Cart>{

    private FirebaseFirestore firestore;
    private CollectionReference foodCollection;

    public CartDAO() {
        this.firestore = FirebaseFirestore.getInstance();
        this.foodCollection = firestore.collection("cart");
    }
    @Override
    public void insert(Cart cart, final CRUDCallback callback) {
        final boolean[] check = {false};
        if (cart != null) {
            // Add data to Firebase Store
            firestore.collection("cart").add(cart).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        check[0] = true;
                        String idForField = task.getResult().getId(); // Lấy ID của tài liệu mới được thêm vào
                        cart.setCart_ID(idForField); // Thiết lập ID cho đối tượng Food

                        // Update the document to set the "Food_ID" field
                        String documentId = task.getResult().getId();
                        firestore.collection("cart").document(documentId).update("cart_ID", idForField)
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
    public void update(Cart cart, final CRUDCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference foodRef = db.collection("cart").document(cart.getCart_ID());

        foodRef.set(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        DocumentReference foodRef = firestore.collection("cart").document(t);

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
    public ArrayList<Cart> selectAll(final OnDataLoadedCallbackCart listener) {
        final ArrayList<Cart> listCart= new ArrayList<>();
        this.foodCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Cart cart = document.toObject(Cart.class);
                        listCart.add(cart);
                    }
                    listener.onDataLoaded(listCart); // Gửi kết quả về qua callback
                } else {
                    listener.onError(null); // Trường hợp không thành công
                }
            }
        });
        return  listCart;
    }

    @Override
    public Cart selectById(String t) {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference foodRef = firestore.collection("cart").document(t);
        final Cart[] carts = {new Cart()};
        foodRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        carts[0] = document.toObject(Cart.class);
                    } else {

                    }
                } else {

                }
            }
        });
        return  carts[0];
    }

    public void selectByIds(String cartId, OnDataLoadedCallbackCart callback) {
        DocumentReference cartRef = firestore.collection("cart").document(cartId);
        cartRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Cart cart = document.toObject(Cart.class);
                        callback.onDataLoadedSigle(cart); // Call the callback with the cart object
                    } else {
                        // Handle the case where the document doesn't exist
                        callback.onError("Cart not found");
                    }
                } else {
                    // Handle errors during the query
                    callback.onError(task.getException().getMessage());
                }
            }
        });
    }



}