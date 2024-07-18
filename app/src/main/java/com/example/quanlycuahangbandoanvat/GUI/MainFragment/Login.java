package com.example.quanlycuahangbandoanvat.GUI.MainFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterFragmentAccount;
import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterListViewFood;
import com.example.quanlycuahangbandoanvat.BUS.CustomerBUS;
import com.example.quanlycuahangbandoanvat.BUS.FoodBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.CRUDCallback;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackCustomer;
import com.example.quanlycuahangbandoanvat.DAO.CustomerDAO;
import com.example.quanlycuahangbandoanvat.DAO.FoodDAO;
import com.example.quanlycuahangbandoanvat.DTO.Customer;
import com.example.quanlycuahangbandoanvat.DTO.CustomerViewModel;
import com.example.quanlycuahangbandoanvat.DTO.Food;
import com.example.quanlycuahangbandoanvat.GUI.Interface.OnNavigationLinkClickListener;
import com.example.quanlycuahangbandoanvat.GUI.MainActivity;
import com.example.quanlycuahangbandoanvat.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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

    //
    private CustomerViewModel customerViewModel;
    Button btnLogin, btnLoginGoogle;
    EditText edtEmail, edtPass;
    CustomerBUS customerBUS = new CustomerBUS();
    CustomerDAO customerDAO = new CustomerDAO();
    ArrayList<Customer> listCustomer= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
        btnLogin = getView().findViewById(R.id.btnLogin);
        btnLoginGoogle = view.findViewById(R.id.btnLoginWithGoogle);
        edtEmail = getView().findViewById(R.id.edtEmailAddress);
        edtPass = getView().findViewById(R.id.edtPassword);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

        // lấy tất cả danh sách khách hàng và kiểm tra đăng nhập
        customerDAO.selectAll(new OnDataLoadedCallbackCustomer() {
            @Override
            public void onDataLoaded(ArrayList<Customer> t) {
                listCustomer.addAll(t);
                customerBUS = new CustomerBUS(listCustomer);
                // button login
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String email = edtEmail.getText().toString();
                        String pass = edtPass.getText().toString();
                        if(checkValidation(email, pass)) {
                            // kiểm tra đăng nhập
                            Customer customerLogin = customerBUS.getCustomerByEmailPassword(email, pass);
                            if(customerLogin != null) {
                                // Lưu trữ Customer vào ViewModel
                                customerViewModel.setCustomer(customerLogin);
                                // lưu vào Shared References
                                SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("current_customer_id", customerLogin.getCus_ID());
                                editor.putString("current_customer_name", customerLogin.getCus_Name());
                                editor.apply();
                                // hiển thị thông báo
                                Toast.makeText(getContext(), "Login successfully !", Toast.LENGTH_SHORT).show();
                                // replace Login Fragment from Account Fragment in MainActivity
                                loadFragmentAccount();
                            }
                            else {
                                Toast.makeText(getContext(), "Login unsuccessfully, please check Email or Password !", Toast.LENGTH_SHORT).show();
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

            }
        });

        // login with google
        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Lấy ID token từ google-services.json
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }


    private void signIn() {
        // Đăng xuất tài khoản Google hiện tại
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Sau khi đăng xuất xong, bắt đầu quy trình đăng nhập lại
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("MainActivity", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công, lấy thông tin người dùng hiện tại từ Firebase Auth
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Kiểm tra và xử lý lưu trữ thông tin người dùng
                            handleUserLogin(user);
                        } else {
                            Log.w("MainActivity", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void handleUserLogin(FirebaseUser user) {
        // Lấy email từ người dùng đã đăng nhập
        String email = user.getEmail();

        // Truy vấn Firestore để kiểm tra xem email này đã tồn tại hay chưa
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customer")
                .whereEqualTo("cus_Email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Email đã tồn tại, lấy thông tin khách hàng từ Firestore
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            Customer existingCustomer = document.toObject(Customer.class);

                            // Lưu trữ Customer vào ViewModel và SharedPreferences
                            customerViewModel.setCustomer(existingCustomer);
                            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("current_customer_id", existingCustomer.getCus_ID());
                            editor.putString("current_customer_name", existingCustomer.getCus_Name());
                            editor.apply();


                            Toast.makeText(getContext(), "Welcome back, " + existingCustomer.getCus_Name(), Toast.LENGTH_SHORT).show();
                            loadFragmentAccount();
                        } else {
                            // Email chưa tồn tại, tạo mới tài khoản khách hàng
                            saveUserToFirestore(user);
                        }
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user) {
        // Tạo đối tượng Customer mới
        String email = user.getEmail();
        String phone = user.getPhoneNumber();
        String name = user.getDisplayName();
        String id = user.getUid();
        Customer customer = new Customer(id, name, email, "", "Nam", "", phone);

        // Lưu trữ Customer mới vào Firestore
        customerDAO.insert(customer, new CRUDCallback() {
            @Override
            public void onCRUDComplete(int result) {
                if(result == 1) {
                    // Lưu trữ Customer vào ViewModel và SharedPreferences
                    customerViewModel.setCustomer(customer);
                    SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("current_customer_id", customer.getCus_ID());
                    editor.putString("current_customer_name", customer.getCus_Name());
                    editor.apply();

                    // Hiển thị thông báo và chuyển sang giao diện tài khoản
                    Toast.makeText(getContext(), "Register successfully ! Please add your address and phone number information ", Toast.LENGTH_SHORT).show();
                    loadFragmentAccount();
                }
                else {
                    Toast.makeText(getContext(), "Register unsuccessfully :<", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkValidation(String email, String password){
        if(email.isEmpty() || password.isEmpty()) {
            return false;
        }
        return true;
    }

    // THỰC HIỆN CHUYỂN HƯỚNG KHI CLICK VÀO LINK REGISTER (Bạn chưa có tài khoản? Đăng kí)
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

    // Phương thức này được gọi khi TextView "Đăng kí" được click
    public void onRegisterLinkClick(View view) {
        if (navigationLinkClickListener != null) {
            navigationLinkClickListener.onRegisterLinkClicked();
        }
    }

    private void loadFragmentAccount() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutMainActivity, new Account());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

    }
}