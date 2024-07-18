package com.example.quanlycuahangbandoanvat.GUI.MainFragment;

import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.BUS.CartBUS;
import com.example.quanlycuahangbandoanvat.BUS.CustomerBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCart;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCustomer;
import com.example.quanlycuahangbandoanvat.DAO.CartDAO;
import com.example.quanlycuahangbandoanvat.DAO.CustomerDAO;
import com.example.quanlycuahangbandoanvat.DTO.Cart;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.CustomerViewModel;
import com.example.quanlycuahangbandoanvat.GUI.Interface.OnNavigationLinkClickListener;
import com.example.quanlycuahangbandoanvat.Helper.Formatter;
import com.example.quanlycuahangbandoanvat.Helper.Validation;
import com.example.quanlycuahangbandoanvat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    // THỰC HIỆN CHUYỂN HƯỚNG KHI CLICK VÀO LINK LOGIN (Bạn đã có tài khoản? Đăng nhập)
    private OnNavigationLinkClickListener navigationLinkClickListener;

    // Override onAttach để kết nối Interface với Activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationLinkClickListener) {
            navigationLinkClickListener = (OnNavigationLinkClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnNavigationLinkClickListener");
        }
    }

    // Phương thức này được gọi khi TextView "Đăng nhập" được click
    public void onLoginLinkClick(View view) {
        if (navigationLinkClickListener != null) {
            navigationLinkClickListener.onLoginLinkClicked();
        }
    }

    private CustomerViewModel customerViewModel;
    Button btnRegister;
    CustomerBUS customerBUS = new CustomerBUS();
    CustomerDAO customerDAO = new CustomerDAO();
    ArrayList<Customer> listCustomer= new ArrayList<>();
    EditText edtName, edtEmail, edtPass, edtAddr, edtPhone;
    CheckBox checkBox;
    RadioButton rdoMale, rdoFemale;

    CartDAO cartDAO = new CartDAO();
    CartBUS cartBUS = new CartBUS();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ánh xạ ID
        btnRegister = view.findViewById(R.id.btnRegister);
        edtName = view.findViewById(R.id.edtRegName);
        edtEmail = view.findViewById(R.id.edtRegEmail);
        edtAddr = view.findViewById(R.id.edtRegAddress);
        edtPass = view.findViewById(R.id.edtRegPassword);
        edtPhone = view.findViewById(R.id.edtRegPhone);
        rdoFemale = view.findViewById(R.id.rdoRegFemale);
        rdoMale = view.findViewById(R.id.rdoRegMale);
        checkBox = view.findViewById(R.id.cbRegChinhSach);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

        // default Button Register
        btnRegister.setEnabled(true);
        btnRegister.setBackgroundColor(getResources().getColor(R.color.gray100));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnRegister.setEnabled(true);
                    btnRegister.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    btnRegister.setEnabled(false);
                    btnRegister.setBackgroundColor(getResources().getColor(R.color.gray100));
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
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
                String address = edtAddr.getText().toString();
                String phone = edtPhone.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();
                if(checkFillOutInformation(name, gender, address, phone,email,password)) {
                    if(!Validation.isEmail(email)) {
                        Toast.makeText(getContext(), "Invalid email, try another one", Toast.LENGTH_SHORT).show();
                    } else if(!Validation.isValidPhoneNumber(phone)) {
                        Toast.makeText(getContext(), "Invalid number phone, try another one", Toast.LENGTH_SHORT).show();
                    } else if(!Validation.isValidPassword(password)) {
                        Toast.makeText(getContext(), "Password must require 8 characters, uppercase, lowercase, number and special characters, try another one", Toast.LENGTH_SHORT).show();
                    }else {
                        Customer customer = new Customer(null, name, email, address, gender, password, phone);

                        customerDAO.insert(customer, new CRUDCallback() {
                            @Override
                            public void onCRUDComplete(int result) {
                                if(result == 1) {
                                    Toast.makeText(getContext(), "Register successfully, your are here now by your account", Toast.LENGTH_SHORT).show();
                                    Cart cart = new Cart(null,customer.getCus_ID(),0.0,false);
                                    cartDAO.insert(cart, new CRUDCallback() {
                                        @Override
                                        public void onCRUDComplete(int result) {
                                            if (result == 1){

                                            }
                                        }
                                    });
                                    // lưu customer view model
                                    customerViewModel.setCustomer(customer);
                                    // lưu vào Shared References
                                    SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("current_customer_id", customer.getCus_ID());
                                    editor.putString("current_customer_name", customer.getCus_Name());
                                    editor.apply();

                                    //add cart

                                    // load fragment
                                    switchToMenuFragment();

                                }
                                else {
                                    Toast.makeText(getContext(), "Opps, something went wrong, register unsuccessfully, please try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getContext(), "Please fill out all information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // google register

    }

    public boolean checkFillOutInformation(String name, String gender, String address, String phone, String email, String password){
        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || gender.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            return false;
        }
        return true;
    }
    private void loadFragmentHome() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutMainActivity, new Home());
        fragmentTransaction.commit();
    }
    private void switchToMenuFragment() {
        FrameLayout frameLayoutMainActivity = requireActivity().findViewById(R.id.FrameLayoutMainActivity);
        frameLayoutMainActivity.removeAllViews();
        frameLayoutMainActivity.addView(LayoutInflater.from(getContext()).inflate(R.layout.fragment_menu, frameLayoutMainActivity, false));
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.BottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_menu);
    }
}