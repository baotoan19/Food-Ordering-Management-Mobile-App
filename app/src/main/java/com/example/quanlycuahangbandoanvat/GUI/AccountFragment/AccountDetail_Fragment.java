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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.quanlycuahangbandoanvat.GUI.MainActivity;
import com.example.quanlycuahangbandoanvat.GUI.MainFragment.Account;
import com.example.quanlycuahangbandoanvat.GUI.MainFragment.Home;
import com.example.quanlycuahangbandoanvat.Helper.Validation;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountDetail_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountDetail_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountDetail_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountDetail_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountDetail_Fragment newInstance(String param1, String param2) {
        AccountDetail_Fragment fragment = new AccountDetail_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_detail_, container, false);
    }
    CustomerBUS customerBUS = new CustomerBUS();
    CustomerDAO customerDAO = new CustomerDAO();
    ArrayList<Customer> listCustomer= new ArrayList<>();
    Customer currentCustomer;
    EditText edtName, edtEmail, edtPhone, edtAddress;
    RadioButton rdoMale, rdoFemale;
    Button btnUpdateAccount, btnLogout;
    private CustomerViewModel customerViewModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ánh xạ ID
        edtName = view.findViewById(R.id.edtUpdateName);
        edtEmail = view.findViewById(R.id.edtUpdateEmail);
        edtPhone = view.findViewById(R.id.edtUpdatePhone);
        edtAddress = view.findViewById(R.id.edtUpdateAddress);
        rdoFemale = view.findViewById(R.id.rdoUpdateFemale);
        rdoMale = view.findViewById(R.id.rdoUpdateMale);
        btnUpdateAccount = view.findViewById(R.id.btnUpdateAccount);
        btnLogout = view.findViewById(R.id.btnLogout);
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
                    // set customer logged in to the field
                    setInformationInputValue(currentCustomer);
                    // button
                    btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = edtName.getText().toString();
                            String gender = "";
                            if(rdoMale.isChecked()) {
                                gender = "Nam";
                            }
                            if(rdoFemale.isChecked()){
                                gender = "Nữ";
                            }
                            String address = edtAddress.getText().toString();
                            String phone = edtPhone.getText().toString();
                            String email = edtEmail.getText().toString();
                            String ID = getCustomerIDFromSharedReferences();
                            String password = currentCustomer.getCus_Password();
                            if(checkFillOutInformation(name, gender, address, phone,email)) {
                                if(!Validation.isEmail(email)) {
                                    Toast.makeText(getContext(), "Invalid email, try another one", Toast.LENGTH_SHORT).show();
                                } else if(!Validation.isValidPhoneNumber(phone)) {
                                    Toast.makeText(getContext(), "Invalid number phone, try another one", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // Confirmation dialog before update
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    String finalGender = gender;
                                    builder.setTitle("Xác nhận cập nhật thông tin")
                                            .setMessage("Bạn có chắc chắn muốn cập nhật thông tin tài khoản?")
                                            .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Update information on confirmation
                                                    Customer customer = new Customer(ID, name, email, address, finalGender, password, phone);
                                                    customerDAO.update(customer, new CRUDCallback() {
                                                        @Override
                                                        public void onCRUDComplete(int result) {
                                                            if (result == 1) {
                                                                Toast.makeText(getContext(), "Update account information successfully", Toast.LENGTH_SHORT).show();
                                                                // lưu customer view model
                                                                customerViewModel.setCustomer(customer);
                                                                // lưu vào Shared References
                                                                SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedPref.edit();
                                                                editor.putString("current_customer_id", customer.getCus_ID());
                                                                editor.putString("current_customer_name", customer.getCus_Name());
                                                                editor.apply();
                                                            } else {
                                                                Toast.makeText(getContext(), "Opps, something went wrong, update unsuccessfully, please try again", Toast.LENGTH_SHORT).show();
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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Confirmation dialog before logout
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xác nhận đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Logout on confirmation
                                SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("current_customer_id", "");
                                editor.putString("current_customer_name", "");
                                editor.putString("current_cart_id", "");
                                editor.apply();

                                customerViewModel.setCustomer(null);

                                Intent myIntent = new Intent(getContext(), MainActivity.class);
                                startActivity(myIntent);
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
        });
    }

    private String getCustomerIDFromSharedReferences(){
        SharedPreferences sharedPref =  requireActivity().getPreferences(Context.MODE_PRIVATE);
        String currentCustomerID = sharedPref.getString("current_customer_id", "");
        return currentCustomerID;
    }
    private void setInformationInputValue(Customer customer){
        if(customer!= null) {
            edtName.setText(customer.getCus_Name());
            edtEmail.setText(customer.getCus_Email());
            edtAddress.setText(customer.getCus_Address());
            edtPhone.setText(customer.getCus_Phone());
            if(customer.getCus_Sex().equals("Nam")) {
                rdoMale.setChecked(true);
            }
            else {
                rdoFemale.setChecked(true);
            }
        }
    }
    public boolean checkFillOutInformation(String name, String gender, String address, String phone, String email){
        if(email.isEmpty() ||  name.isEmpty() || gender.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            return false;
        }
        return true;
    }
    private void loadFragmentAccount() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutMainActivity, new Account());
        fragmentTransaction.commit();
    }
}