package com.example.quanlycuahangbandoanvat.DAO;


import androidx.annotation.NonNull;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackPromotion;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.DTO.Promotion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PromotionDAO implements DAOinterface<Promotion>{

    private FirebaseFirestore firestore;
    private CollectionReference promotionCollection;

    public PromotionDAO() {
        this.firestore = FirebaseFirestore.getInstance();
        this.promotionCollection = firestore.collection("promotion");
    }
    @Override
    public void insert(Promotion promotion, final CRUDCallback callback) {
        final boolean[] check = {false};
        if (promotion != null) {
            // Add data to Firebase Store
            firestore.collection("promotion").add(promotion).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        check[0] = true;
                        String idForField = task.getResult().getId(); // Lấy ID của tài liệu mới được thêm vào
                        promotion.setFood_Promotion_ID(idForField); // Thiết lập ID cho đối tượng Food

                        // Update the document to set the "Food_ID" field
                        String documentId = task.getResult().getId();
                        firestore.collection("promotion").document(documentId).update("food_Promotion_ID", idForField)
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
    public void update(Promotion promotion, final CRUDCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference promotionRef = db.collection("promotion").document(promotion.getFood_Promotion_ID());

        promotionRef.set(promotion).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        DocumentReference foodRef = firestore.collection("promotion").document(t);

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

    public ArrayList<Promotion> selectAll(final OnDataLoadedCallbackPromotion listener) {
        final ArrayList<Promotion> listPromotion = new ArrayList<>();
        this.promotionCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Promotion promotion = document.toObject(Promotion.class);
                        listPromotion.add(promotion);
                    }
                    listener.onDataLoaded(listPromotion); // Gửi kết quả về qua callback
                } else {
                    listener.onError(null); // Trường hợp không thành công
                }
            }
        });
        return  listPromotion;
    }

    @Override
    public Promotion selectById(String t) {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference foodRef = firestore.collection("promotion").document(t);
        final Promotion[] promotions = {new Promotion()};

        foodRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        promotions[0] = document.toObject(Promotion.class);
                    } else {

                    }
                } else {

                }
            }
        });
        return  promotions[0];
    }

    public void selectByIds(String t, final OnDataLoadedCallbackPromotion listener) {
        DocumentReference foodRef = firestore.collection("promotion").document(t);

        foodRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Promotion promotion = document.toObject(Promotion.class);
                        listener.onDataLoadedSingle(promotion);
                    } else {
                        listener.onError("Promotion không tồn tại");
                    }
                } else {
                    listener.onError(task.getException().getMessage());
                }
            }
        });
    }

}
