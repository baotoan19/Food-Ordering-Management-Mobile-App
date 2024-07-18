package com.example.quanlycuahangbandoanvat.DAO;


import androidx.annotation.NonNull;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackFood;
import com.example.quanlycuahangbandoanvat.DTO.Food;
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

public class FoodDAO implements DAOinterface<Food>{

    private FirebaseFirestore firestore;
    private CollectionReference foodCollection;

    public FoodDAO() {
        this.firestore = FirebaseFirestore.getInstance();
        this.foodCollection = firestore.collection("food");
    }
    @Override
    public void insert(Food food, final CRUDCallback callback) {
        final boolean[] check = {false};
        if (food != null) {
            // Add data to Firebase Store
            firestore.collection("food").add(food).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        check[0] = true;
                        String idForField = task.getResult().getId(); // Lấy ID của tài liệu mới được thêm vào
                        food.setFood_ID(idForField); // Thiết lập ID cho đối tượng Food

                        // Update the document to set the "Food_ID" field
                        String documentId = task.getResult().getId();
                        firestore.collection("food").document(documentId).update("Food_ID", idForField)
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
    public void update(Food food, final CRUDCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference foodRef = db.collection("food").document(food.getFood_ID());

        foodRef.set(food).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        DocumentReference foodRef = firestore.collection("food").document(t);

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
    public ArrayList<Food> selectAlls(final OnDataLoadedCallbackFood listener) {
        final ArrayList<Food> listFood = new ArrayList<>();
        this.foodCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Food food = document.toObject(Food.class);
                        listFood.add(food);
                    }
                    listener.onDataLoaded(listFood); // Gửi kết quả về qua callback
                } else {
                    listener.onError(null); // Trường hợp không thành công
                }
            }
        });
        return  listFood;
    }

    public void selectAll(final OnDataLoadedCallbackFood listener) {
        this.foodCollection.whereEqualTo("Food_isDeleted", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Food> listFood = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Food food = document.toObject(Food.class);
                        listFood.add(food);
                    }
                    listener.onDataLoaded(listFood); // Gửi kết quả về qua callback
                } else {
                    listener.onError(task.getException().getMessage()); // Trường hợp không thành công
                }
            }
        });
    }

    @Override
    public Food selectById(String t) {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference foodRef = firestore.collection("food").document(t);
        final Food[] food = {new Food()};

        foodRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        food[0] = document.toObject(Food.class);
                    } else {

                    }
                } else {

                }
            }
        });
        return  food[0];
    }
}