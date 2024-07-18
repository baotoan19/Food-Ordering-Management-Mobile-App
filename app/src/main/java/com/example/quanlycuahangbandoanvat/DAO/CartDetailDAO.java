package com.example.quanlycuahangbandoanvat.DAO;


import androidx.annotation.NonNull;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCartDetail;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
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

public class CartDetailDAO implements DAOinterface<CartDetail>{

    private FirebaseFirestore firestore;
    private CollectionReference cartDetailCollection;

    public CartDetailDAO() {
        this.firestore = FirebaseFirestore.getInstance();
        this.cartDetailCollection = firestore.collection("cart_detail");
    }
    @Override
    public void insert(CartDetail cartDetail, final CRUDCallback callback) {
        final boolean[] check = {false};
        if (cartDetail != null) {
            // Add data to Firebase Store
            firestore.collection("cart_detail").add(cartDetail).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        check[0] = true;
                        String idForField = task.getResult().getId(); // Lấy ID của tài liệu mới được thêm vào
                        cartDetail.setCartDetail_ID(idForField); // Thiết lập ID cho đối tượng Food

                        // Update the document to set the "Food_ID" field
                        String documentId = task.getResult().getId();
                        firestore.collection("cart_detail").document(documentId).update("cartDetail_ID", idForField)
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
    public void update(CartDetail cartDetail, final CRUDCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (cartDetail != null && cartDetail.getCartDetail_ID() != null) {
            DocumentReference foodRef = db.collection("cart_detail").document(cartDetail.getCartDetail_ID());
            foodRef.set(cartDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        DocumentReference foodRef = firestore.collection("cart_detail").document(t);

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
    public ArrayList<CartDetail> selectAll(final OnDataLoadedCallbackCartDetail listener) {
        final ArrayList<CartDetail> listCartDetail = new ArrayList<>();
        this.cartDetailCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CartDetail cartDetail = document.toObject(CartDetail.class);
                        listCartDetail.add(cartDetail);
                    }
                    listener.onDataLoaded(listCartDetail);
                } else {
                    listener.onError(null);
                }
            }
        });
        return  listCartDetail;
    }

    public Task<ArrayList<CartDetail>> selectAlls() {
        return cartDetailCollection.get().continueWith(task -> {
            if (task.isSuccessful()) {
                ArrayList<CartDetail> listCartDetail = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    CartDetail cartDetail = document.toObject(CartDetail.class);
                    listCartDetail.add(cartDetail);
                }
                return listCartDetail;
            } else {
                // Handle errors: You could throw an exception, log the error, or return null.
                throw task.getException();
            }
        });
    }

    public void selectAllByCartID(String cartID, final OnDataLoadedCallbackCartDetail listener) {
        cartDetailCollection.whereEqualTo("cart_ID", cartID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<CartDetail> listCartDetail = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    CartDetail cartDetail = document.toObject(CartDetail.class);
                    listCartDetail.add(cartDetail);
                }
                listener.onDataLoaded(listCartDetail);
            } else {
                listener.onError(task.getException().getMessage());
            }
        });
    }

    @Override
    public CartDetail selectById(String t) {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference foodRef = firestore.collection("cart_detail").document(t);
        final CartDetail[] cartDetails = {new CartDetail()};

        foodRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        cartDetails[0] = document.toObject(CartDetail.class);
                    } else {

                    }
                } else {

                }
            }
        });
        return  cartDetails[0];
    }

}