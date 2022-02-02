package com.example.guardsmark.Fragments;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.guardsmark.NearbyUserActivity;
import com.example.guardsmark.R;
import com.example.guardsmark.databinding.FragmentDashboardBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;

import java.text.DecimalFormat;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class DashboardFragment extends Fragment implements EasyPermissions.PermissionCallbacks{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentDashboardBinding binding;
    private Context context;
    String TAG = "ANIK";
    double lat, lng;
    int REQUEST_CODE_CHECK_SETTINGS = 7;
    int REQUEST_LOCATION_PERMISSION = 101;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(getLayoutInflater(), container, false);
        //return inflater.inflate(R.layout.fragment_dashboard, container, false);

        binding.nearbyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, NearbyUserActivity.class));
            }
        });

       /* lng = 90.3818;
        lat = 23.7507;
        setMapBoxImage();*/
        locationStatusCheck();

        binding.healthContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber("1234");
            }
        });

        binding.policeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber("911");
            }
        });

        binding.helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, NearbyUserActivity.class));
            }
        });
        return binding.getRoot();
    }

    private void setMapBoxImage() {
        int map_height = 180;
        int map_width = 380;

        String mapBox_url = getMapBoxImgUrl(map_height, map_width);
        Log.e(TAG, mapBox_url);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new FitCenter(), new RoundedCorners(10));

        Glide.with(context)
                .load(mapBox_url)
                .apply(requestOptions)
                .placeholder(R.drawable.map_dummy_mapbox)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "failed glide");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e(TAG, "ready glide");
                        return false;
                    }
                })
                .into(binding.mapView);
    }

    private String getMapBoxImgUrl(int map_height, int map_width) {
        return "https://api.mapbox.com/styles/v1/mapbox/outdoors-v11/static/pin-s+00afff(" + lat + "," + lng + ")/" + lat + "," + lng + ",15,0/" + map_width + "x" + map_height + "@2x?access_token=" + context.getString(R.string.map_box_api_key);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void locationStatusCheck() {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLocationSettings();
        } else {
            requestLocationPermission();
        }
    }

    public void enableLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices
                .getSettingsClient(context)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(((Activity) context), (LocationSettingsResponse response) -> {
                    // startUpdatingLocation(...);
                    requestLocationPermission();
                })
                .addOnFailureListener(((Activity) context), ex -> {
                    if (ex instanceof ResolvableApiException) {
                        // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(((Activity) context), REQUEST_CODE_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                });
    }

    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(context, perms)) {
            getLocation();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Location location = null;
        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 10, locationListener);
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                lat = location.getLongitude();
                lng = location.getLatitude();
                Log.e(TAG,  lat+","+lng);
                setMapBoxImage();
            }


        } catch (Exception e) {
            Toast.makeText(context, "Location permission required!", Toast.LENGTH_LONG).show();
            Log.i(TAG, "" + e);
        }
    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLongitude();
            lng = location.getLatitude();
            Log.e(TAG,  lat+","+lng);
            setMapBoxImage();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            Log.d(TAG, "permission accepted");
            getLocation();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}