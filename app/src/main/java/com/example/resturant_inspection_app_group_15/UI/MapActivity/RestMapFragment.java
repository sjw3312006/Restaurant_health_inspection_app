package com.example.resturant_inspection_app_group_15.UI.MapActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite.RestaurantViewModel;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.SecondActivity.clickRestaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class RestMapFragment extends Fragment {
    public static final String TAG = "RestMapsFragment";
    public  GoogleMap mMap = null;

    public RestMapFragment () {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        return view;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            ViewModelProvider viewModelProvider = ViewModelProviders.of(getActivity());
            if (viewModelProvider != null) {
                RestaurantViewModel restViewModel = viewModelProvider.get(RestaurantViewModel.class);
                if (restViewModel != null) {
                    restViewModel.getAll().removeObservers(getActivity());
                    restViewModel.getAll().observe(getActivity(), new Observer<ArrayList<Restaurant>>() {
                        @Override
                        public void onChanged(@Nullable ArrayList<Restaurant> rests) { //letting us know about the updated data
                            if (mMap != null) {
                                mMap.clear();
                                for (Restaurant entity : rests) {
                                    LatLng position = new LatLng(
                                            entity.getLatitude(),
                                            entity.getLongitude());

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.title(entity.getName());
                                    markerOptions.position(position);

                                    if (entity.getHazardImg() == 700121) {
                                        BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.med);
                                        Bitmap b = bitmapDraw.getBitmap();
                                        Bitmap moderateMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(moderateMarker));
                                        markerOptions.snippet(entity.getPhysicalAddress() + "\n" + getText(R.string.hlevel_add) + " " + getText(R.string.hlevel_mod));

                                    }

                                    else if (entity.getHazardImg() == 700003) {
                                        BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bad);
                                        Bitmap b = bitmapDraw.getBitmap();
                                        Bitmap badMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(badMarker));
                                        markerOptions.snippet(entity.getPhysicalAddress() + "\n" + getText(R.string.hlevel_add) + " " + getText(R.string.hlevel_high));

                                    }
                                    else{
                                        BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.good);
                                        Bitmap b = bitmapDraw.getBitmap();
                                        Bitmap lowMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(lowMarker));
                                        markerOptions.snippet(entity.getPhysicalAddress() + "\n" + getText(R.string.hlevel_add) + " " + getText(R.string.hlevel_low));

                                    }

                                    mMap.addMarker(markerOptions);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
                                }
                            }
                        }
                    });
                }
            }

            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View infoWindow = getLayoutInflater().inflate(R.layout.restaurant_marker, null);

                    TextView title = (TextView)infoWindow.findViewById(R.id.title);
                    title.setText(marker.getTitle());

                    TextView snippet = infoWindow.findViewById(R.id.snippet);
                    snippet.setText(marker.getSnippet());

                    return infoWindow;
                }
            });

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    String restaurantName = marker.getTitle();
                    //int position = Integer.parseInt(marker.getId());
                    Intent intent = new Intent(getActivity(), clickRestaurant.class);
                    intent.putExtra("Name",restaurantName);
                    //intent.putExtra("Position",position);
                    startActivity(intent);
                    Log.i(TAG, restaurantName + "Clicked");
                }
            });
        }
    };
}