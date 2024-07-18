package com.example.quanlycuahangbandoanvat.DAO;

import androidx.annotation.NonNull;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackNotification;
import com.example.quanlycuahangbandoanvat.DTO.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationDAO implements DAOinterface<Notification> {

    private FirebaseFirestore firestore;
    private CollectionReference notificationCollection;

    public NotificationDAO() {
        this.firestore = FirebaseFirestore.getInstance();
        this.notificationCollection = firestore.collection("notification");
    }

    @Override
    public void insert(Notification notification, final CRUDCallback callback) {
        final boolean[] check = {false};
        if (notification != null) {
            // Add data to Firebase Store
            firestore.collection("notification").add(notification).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        check[0] = true;
                        String idForField = task.getResult().getId(); // Lấy ID của tài liệu mới được thêm vào
                        notification.setNotification_ID(idForField); // Thiết lập ID cho đối tượng Food

                        // Update the document to set the "Food_ID" field
                        String documentId = task.getResult().getId();
                        firestore.collection("notification").document(documentId).update("notification_ID", idForField)
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
    public void update(Notification notification, final CRUDCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference notificationRef = db.collection("notification").document(notification.getNotification_ID());
        notificationRef.set(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        DocumentReference foodRef = firestore.collection("notification").document(t);
        foodRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCRUDComplete(1); // Thành công
                } else {
                    callback.onCRUDComplete(0);
                }
            }
        });
    }

    public ArrayList<Notification> selectAll(final OnDataLoadedCallbackNotification listener) {
        final ArrayList<Notification> listNotification = new ArrayList<>();
        this.notificationCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Notification notification = document.toObject(Notification.class);
                        listNotification.add(notification);
                    }
                    listener.onDataLoaded(listNotification); // Gửi kết quả về qua callback
                } else {
                    listener.onError(null); // Trường hợp không thành công
                }
            }
        });
        return  listNotification;
    }

    @Override
    public Notification selectById(String t) {
        DocumentReference foodRef = firestore.collection("notification").document(t);
        final Notification[] notifications = {new Notification()};

        foodRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        notifications[0] = document.toObject(Notification.class);
                    } else {

                    }
                } else {

                }
            }
        });
        return  notifications[0];
    }

    public void updateNotificationStatus(String notificationId, boolean status, final CRUDCallback callback) {
        DocumentReference notificationRef = firestore.collection("notification").document(notificationId);
        notificationRef.update("notification_Status", status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCRUDComplete(1); // Thành công
                } else {
                    callback.onCRUDComplete(0); // Thất bại
                }
            }
        });
    }
}

