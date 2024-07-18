package com.example.quanlycuahangbandoanvat.GUI;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterListViewUser;
import com.example.quanlycuahangbandoanvat.BUS.UserBUS;
import com.example.quanlycuahangbandoanvat.Config.InitDatabase;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallback;
import com.example.quanlycuahangbandoanvat.DAO.UserDAO;
import com.example.quanlycuahangbandoanvat.DTO.User;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

public class MainDemoFirebase extends AppCompatActivity {
    Button btnInser, btnView, btnUpdate, btnDelete;
    EditText edtName, edtAge, edtEmail, edtSearch;
    ListView listViewUser;

    CustomAdapterListViewUser customAdapterUser;
    UserDAO userDAO = new UserDAO();
    UserBUS userBUS = new UserBUS();
    ArrayList<User> listUser = new ArrayList<>();

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_demo_firebase);
        // ánh xạ ID
        btnInser = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        edtAge = findViewById(R.id.edtAge);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtSearch = findViewById(R.id.edtSearch);
        listViewUser = findViewById(R.id.listViewUser);

        // init database QuanLyCuaHangBanDoAnVat
        //InitDatabase initDatabase = new InitDatabase(this);
        //initDatabase.initData();

        // init arraylist user
        userDAO.selectAll(new OnDataLoadedCallback() {
            @Override
            public void onDataLoaded(ArrayList<User> users) {
                listUser.addAll(users);
                userBUS.setListUser(listUser); // cập nhật mới danh sách listUser cho userBUS
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        // list view
        loadArrayListUser();
        customAdapterUser = new CustomAdapterListViewUser(MainDemoFirebase.this, R.layout.layout_user_item, listUser);
        listViewUser.setAdapter(customAdapterUser);

        // button (CREATE)
        btnInser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insertUser();
                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                int age = Integer.parseInt(edtAge.getText().toString());
                User user = new User(null, name, email, age);
//                boolean check = userBUS.add(user);
//                if(check) {
//                    resetDefaultInput();
//                    Toast.makeText(getApplicationContext(), "Insert Successfully :3", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Insert Unsuccessfully :< ", Toast.LENGTH_SHORT).show();
//                }
                userDAO.insert(user, new CRUDCallback() {
                    @Override
                    public void onCRUDComplete(int result) { // result = 1 / 0
                        if(result == 1) {
//                            check[0] = true;
                            listUser.add(user);
                            loadArrayListUser();
                            resetDefaultInput();
                            Toast.makeText(getApplicationContext(), "Insert Successfully :3", Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            check[0] = false;
                            Toast.makeText(getApplicationContext(), "Insert Unsuccessfully :< ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // button (READ)
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listUser.clear();
                loadArrayListUser();
                //userBUS.setListUser(listUser);
                //listUser.addAll(userBUS.getAll());
                //customAdapterUser.notifyDataSetChanged();
            }
        });

        // selected item listview
        listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = listUser.get(position);
                selectedPosition = position;
                loadDataItemClick(user);
                Toast.makeText(getApplicationContext(), "User ID : " + user.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        // button (UPDATE)
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition != -1) {
                    User userUpdate = listUser.get(selectedPosition);
                    String ID = userUpdate.getId();
//                    User currentUser = userBUS.getByUserID(ID);
                    if(validationInput()) {
                        User updateUser = new User(ID, edtName.getText().toString(), edtEmail.getText().toString(), Integer.parseInt(edtAge.getText().toString()));
//                        boolean check = userBUS.update(user);
//                        if(check) {
//                            Toast.makeText(getApplicationContext(), "Update Successfully :3", Toast.LENGTH_SHORT).show();
//                            resetDefaultInput();
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "Update Unsuccessfully :< ", Toast.LENGTH_SHORT).show();
//                        }
                        userDAO.update(updateUser, new CRUDCallback() {
                            @Override
                            public void onCRUDComplete(int result) {
                                if(result == 1) {
                                    listUser.set(selectedPosition, updateUser);
                                    loadArrayListUser();
                                    Toast.makeText(getApplicationContext(), "Update Successfully :3", Toast.LENGTH_SHORT).show();
                                    resetDefaultInput();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Update Unsuccessfully :< ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        // button (DELETE)
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition != -1) {
                    User userDelete = listUser.get(selectedPosition);
                    String ID = userDelete.getId();
//                    User user = userBUS.getByUserID(ID);
//                    if(!ID.isEmpty()) {
//                        boolean check = userBUS.delete(ID);
//                        if(check) {
//                            Toast.makeText(getApplicationContext(), "Delete Successfully :3", Toast.LENGTH_SHORT).show();
//                            resetDefaultInput();
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "Delete Unsuccessfully :< ", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                    userDAO.delete(ID, new CRUDCallback() {
                        @Override
                        public void onCRUDComplete(int result) { // 1 / 0
                            if(result == 1) {
                                Toast.makeText(getApplicationContext(), "Delete Successfully :3", Toast.LENGTH_SHORT).show();
                                resetDefaultInput();
                                listUser.remove(userDelete);
                                loadArrayListUser();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Delete Unsuccessfully :< ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        // edittext (SEARCH)

    }

    public void resetDefaultInput() {
        edtName.setText("");
        edtEmail.setText("");
        edtAge.setText("");
        edtName.requestFocus();
    }

    public void loadDataItemClick(User user){
        if(user != null) {
            edtName.setText(user.getName());
            edtEmail.setText(user.getEmail());
            edtAge.setText(String.valueOf(user.getAge()));
        }
        else {
            Toast.makeText(getApplicationContext(), "Vui lòng chọn user !", Toast.LENGTH_SHORT).show();
        }

    }
    public void loadArrayListUser(){
        userDAO.selectAll(new OnDataLoadedCallback() {
            @Override
            public void onDataLoaded(ArrayList<User> users) {
                listUser.clear();
                listUser.addAll(users);
                customAdapterUser.notifyDataSetChanged();
                userBUS.setListUser(listUser);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
    public boolean validationInput(){
        if(!edtName.getText().toString().isEmpty() || !edtAge.getText().toString().isEmpty() || !edtEmail.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }
}