package com.example.quanlycuahangbandoanvat.GUI.AccountFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.BUS.CustomerBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCustomer;
import com.example.quanlycuahangbandoanvat.DAO.CustomerDAO;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.CustomerViewModel;
import com.example.quanlycuahangbandoanvat.Helper.Validation;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPassword_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPassword_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResetPassword_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPassword_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPassword_Fragment newInstance(String param1, String param2) {
        ResetPassword_Fragment fragment = new ResetPassword_Fragment();
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
    CustomerBUS customerBUS = new CustomerBUS();
    CustomerDAO customerDAO = new CustomerDAO();
    ArrayList<Customer> listCustomer= new ArrayList<>();
    Customer currentCustomer;
    EditText edtPass, edtNewPass, edtConfirmPass;
    Button btnUpdatePassword;
    private CustomerViewModel customerViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password_, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ánh xạ ID
        edtPass = view.findViewById(R.id.edtUpdateCurrentPassword);
        edtNewPass = view.findViewById(R.id.edtUpdateNewPassword);
        edtConfirmPass = view.findViewById(R.id.edtUpdateConfirmPassword);
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

        // Data from SharedReferences
        String currentCustomerID = getCustomerIDFromSharedReferences();
        if(!currentCustomerID.isEmpty()) {
            customerDAO.selectAll(new OnDataLoadedCallbackCustomer() {
                @Override
                public void onDataLoaded(ArrayList<Customer> t) {
                    listCustomer.addAll(t);
                    customerBUS = new CustomerBUS(listCustomer);
                    currentCustomer = customerBUS.getByCustomerID(currentCustomerID);

                    // button
                    btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = currentCustomer.getCus_Name();
                            String gender = currentCustomer.getCus_Sex();
                            String address = currentCustomer.getCus_Address();
                            String phone = currentCustomer.getCus_Phone();
                            String email = currentCustomer.getCus_Email();
                            String ID = getCustomerIDFromSharedReferences();
                            String password = currentCustomer.getCus_Password();

                            String oldPass = edtPass.getText().toString();
                            String newPass = edtNewPass.getText().toString();
                            String confirmPass = edtConfirmPass.getText().toString();

                            if(checkFillOutInformation(oldPass, newPass, confirmPass)) {
                                if(password.equals(oldPass)) {
                                    if(Validation.isValidPassword(newPass)) {
                                        if(newPass.equals(confirmPass)) {
                                            // Confirmation dialog before update
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("Xác nhận cập nhật mật khẩu")
                                                    .setMessage("Bạn có chắc chắn muốn cập nhật mật khẩu?")
                                                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Update password on confirmation
                                                            Customer customer = new Customer(ID, name, email, address, gender, newPass, phone);
                                                            customerDAO.update(customer, new CRUDCallback() {
                                                                @Override
                                                                public void onCRUDComplete(int result) {
                                                                    if (result == 1) {
                                                                        // lưu customer view model
                                                                        customerViewModel.setCustomer(customer);
                                                                        // lưu vào Shared References
                                                                        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                                        editor.putString("current_customer_id", customer.getCus_ID());
                                                                        editor.putString("current_customer_name", customer.getCus_Name());
                                                                        editor.apply();

                                                                        Toast.makeText(getContext(), "Update password successfully", Toast.LENGTH_SHORT).show();
                                                                        resetInput();
                                                                    } else {
                                                                        Toast.makeText(getContext(), "Opps, something went wrong, update password unsuccessfully, please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Dismiss dialog on cancel
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Please confirm the correct new password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Password must require 8 characters, uppercase, lowercase, number and special characters, try another one", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else {
                                    Toast.makeText(getContext(), "Please enter the correct old password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Please fill out all information", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                @Override
                public void onError(String errorMessage) {
                    System.out.println(errorMessage);
                }
            });
        }
    }
    private String getCustomerIDFromSharedReferences(){
        SharedPreferences sharedPref =  requireActivity().getPreferences(Context.MODE_PRIVATE);
        String currentCustomerID = sharedPref.getString("current_customer_id", "");
        return currentCustomerID;
    }
    public boolean checkFillOutInformation(String oldPass, String newPass, String confirmPass){
        if(oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            return false;
        }
        return true;
    }
    public void resetInput(){
        edtPass.setText("");
        edtNewPass.setText("");
        edtConfirmPass.setText("");
    }

}