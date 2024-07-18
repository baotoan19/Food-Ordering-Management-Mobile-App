package com.example.quanlycuahangbandoanvat.GUI.MainFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlycuahangbandoanvat.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends Fragment {

    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);


        // Initialize map fragment programmatically
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_fragment_container, mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                addLocationKFCToMap();
            }
        });

        return view;
    }

    private void zoomToLocations(List<LatLng> locations) {
        if (mMap == null || locations.isEmpty()) {
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng location : locations) {
            builder.include(location);
        }
        LatLngBounds bounds = builder.build();
        int padding = 80;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    private void addLocationKFCToMap() {
        java.util.Map<LatLng, String> locations = new HashMap<>();
        locations.put(new LatLng(10.782236150121104, 106.61240323098937), "KFC Lê Văn Qưới");
        locations.put(new LatLng(10.787898433596572, 106.62963343107536), "KFC Nguyễn Sơn");
        locations.put(new LatLng(10.79632321971519, 106.63901264025594), "KFC Âu Cơ");
        locations.put(new LatLng(10.80454652007892, 106.65972610861607), "KFC Cộng Hòa");
        locations.put(new LatLng(10.793272707943066, 106.66682634019931), "KFC Cách Mạng Tháng 8");
        locations.put(new LatLng(10.753496540278533, 106.63669105329161), "KFC Hậu Giang");
        locations.put(new LatLng(10.75911713744777, 106.66742173064883), "KFC Ngô Quyền");

        for (java.util.Map.Entry<LatLng, String> entry : locations.entrySet()) {
            LatLng latLng = entry.getKey();
            String title = entry.getValue();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_kfc_map));
            mMap.addMarker(markerOptions);
        }
        zoomToLocations(new ArrayList<>(locations.keySet()));
    }
}
