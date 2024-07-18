package com.example.quanlycuahangbandoanvat.GUI.MainFragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.quanlycuahangbandoanvat.Adapter.CustomAdapterFragmentNotification;
import com.example.quanlycuahangbandoanvat.BUS.NotificationBUS;
import com.example.quanlycuahangbandoanvat.DAO.Callback.OnDataLoadedCallbackNotification;
import com.example.quanlycuahangbandoanvat.DAO.NotificationDAO;
import com.example.quanlycuahangbandoanvat.DTO.Notification;
import com.example.quanlycuahangbandoanvat.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Option#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Option extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Option() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Option.
     */
    // TODO: Rename and change types and number of parameters
    public static Option newInstance(String param1, String param2) {
        Option fragment = new Option();
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
        return inflater.inflate(R.layout.fragment_option, container, false);
    }

    NotificationBUS notificationBUS = new NotificationBUS();
    NotificationDAO notificationDAO = new NotificationDAO();
    ArrayList<Notification> listNotification = new ArrayList<>();
    ArrayList<Notification> listCustomerNotification = new ArrayList<>();
    CustomAdapterFragmentNotification adapterFragmentNotification;
    ListView listViewNotification;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ánh xạ ID
        listViewNotification = view.findViewById(R.id.listViewNotification);
        // get Customer_ID
        String Customer_ID = getCustomerIDFromSharedReferences();
        // hiển thị thông báo
        notificationDAO.selectAll(new OnDataLoadedCallbackNotification() {
            @Override
            public void onDataLoaded(ArrayList<Notification> t) {
                listNotification.addAll(t);
                notificationBUS = new NotificationBUS(listNotification);
                adapterFragmentNotification = new CustomAdapterFragmentNotification(getContext(), R.layout.layout_notification_item, notificationBUS.getListNotificationByCustomerID(Customer_ID));
                listViewNotification.setAdapter(adapterFragmentNotification);
                adapterFragmentNotification.notifyDataSetChanged();
            }

            @Override
            public void onDataLoadedSingle(Notification t) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
    private String getCustomerIDFromSharedReferences(){
        SharedPreferences sharedPref =  requireActivity().getPreferences(Context.MODE_PRIVATE);
        String currentCustomerID = sharedPref.getString("current_customer_id", "");
        return currentCustomerID;
    }

}