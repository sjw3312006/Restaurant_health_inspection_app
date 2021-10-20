package com.example.resturant_inspection_app_group_15.UI.MapActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite.RestaurantViewModel;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.RestaurantManager;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.FirstActivity.AdapterForDisplay;
import com.example.resturant_inspection_app_group_15.UI.FirstActivity.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class RestListFragment extends Fragment {
    public static final String TAG = "RestListsFragment";

    RestaurantManager restaurantManager = RestaurantManager.getInstance();

    AdapterForDisplay mAdaptor = null;
    RecyclerView mRecyclerView = null;
    RestaurantViewModel mViewModel = null;

    public RestListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.restaurant_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdaptor = new AdapterForDisplay(getContext(), restaurantManager.getRestaurantList());
        mRecyclerView.setAdapter(mAdaptor);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(RestaurantViewModel.class);
        if (mViewModel != null) {
            mViewModel.getAll().removeObservers(this);
            mViewModel.getAll().observe(getActivity(), new Observer<ArrayList<Restaurant>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Restaurant> rests) {
                    if (mRecyclerView != null && mAdaptor != null) {
                        mRecyclerView.removeAllViewsInLayout();
                        mAdaptor.submit(null);
                        mAdaptor.notifyDataSetChanged();
                    }

                    if (rests != null) {
                        mAdaptor.submit(rests);
                    }
                }
            });
        }
    }
}