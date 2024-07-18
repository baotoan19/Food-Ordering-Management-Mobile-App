package com.example.quanlycuahangbandoanvat.GUI.AccountFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.CustomerDAO;
import com.example.quanlycuahangbandoanvat.DTO.CustomerViewModel;
import com.example.quanlycuahangbandoanvat.GUI.MainActivity;
import com.example.quanlycuahangbandoanvat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteAccount_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteAccount_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeleteAccount_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteAccount_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteAccount_Fragment newInstance(String param1, String param2) {
        DeleteAccount_Fragment fragment = new DeleteAccount_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    CustomerDAO customerDAO = new CustomerDAO();
    Button btnDeleteAccount;
    private CustomerViewModel customerViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_account_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        // Data from SharedReferences
        String currentCustomerID = getCustomerIDFromSharedReferences();
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xác nhận xóa tài khoản")
                        .setMessage("Bạn có chắc chắn muốn xóa tài khoản này?")
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xóa tài khoản khi người dùng chọn "Xác nhận"
                                customerDAO.delete(currentCustomerID, new CRUDCallback() {
                                    @Override
                                    public void onCRUDComplete(int result) {
                                        if(result == 1) {
                                            Toast.makeText(getContext(), "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                                            // Xóa dữ liệu Shared References
                                            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("current_customer_id", "");
                                            editor.apply();

                                            // Xóa dữ liệu trong View Model
                                            customerViewModel.setCustomer(null);

                                            // Chuyển về Main Activity
                                            Intent myIntent = new Intent(getContext(), MainActivity.class);
                                            startActivity(myIntent);
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private String getCustomerIDFromSharedReferences(){
        SharedPreferences sharedPref =  requireActivity().getPreferences(Context.MODE_PRIVATE);
        String currentCustomerID = sharedPref.getString("current_customer_id", "");
        return currentCustomerID;
    }
}