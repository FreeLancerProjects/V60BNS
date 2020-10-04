package com.v60BNS.activities_fragments.activity_places;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.v60BNS.R;
import com.v60BNS.adapters.Places_Adapter;
import com.v60BNS.databinding.ActivityPlacesBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.NearbyModel;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.PlaceGeocodeData;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.share.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlacesActivity extends AppCompatActivity implements Listeners.BackListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private ActivityPlacesBinding binding;
    private String lang;
    private Preferences preferences;

    private List<NearbyModel> dataList;
    private Places_Adapter food_adapter;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    public Location location = null;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private String address;
    private double lat, lng;
    private String query;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_places);
        CheckPermission();

        initView();
    }

    private void initView() {
        dataList = new ArrayList<>();

        preferences = Preferences.getInstance();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        food_adapter = new Places_Adapter(dataList, this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(food_adapter);
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    query = editable.toString();

                    Search();
                    binding.recView.setVisibility(View.VISIBLE);
                } else {
                    query = "";
                    //productModelList.clear();
                    //searchAdapter.notifyDataSetChanged();


                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    @SuppressLint("MissingPermission")
    public void getNearbyPlaces(Location location) {
        getGeoData(location.getLatitude(), location.getLongitude());

        String loc = location.getLatitude() + "," + location.getLatitude();

        Api.getService("https://maps.googleapis.com/maps/api/")
                .getNearbyStores(loc, 5000, "all", lang, getString(R.string.map_api_key))
                .enqueue(new Callback<NearbyStoreDataModel>() {
                    @Override
                    public void onResponse(Call<NearbyStoreDataModel> call, Response<NearbyStoreDataModel> response) {
                        //    Log.e("jjjjjj",response.code()+"");

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getResults().size() > 0) {
                                dataList.addAll(response.body().getResults());
                                food_adapter.notifyDataSetChanged();
                            }
                        } else {


                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyStoreDataModel> call, Throwable t) {
                        try {

                            Log.e("Error", t.getMessage());
//                                progBar.setVisibility(View.GONE);
//                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });


    }

    @Override
    public void back() {
        finish();
    }

    public void choose(NearbyModel nearbyModel) {
        Intent intent = getIntent();
        intent.putExtra("data", nearbyModel);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("address", address);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{gps_perm}, gps_req);
        } else {

            initGoogleApiClient();
           /* if (isGpsOpen())
            {
                StartService(LocationRequest.PRIORITY_HIGH_ACCURACY);
            }else
                {
                    CreateGpsDialog();

                }*/
        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1255) {
            if (resultCode == RESULT_OK) {
                startLocationUpdate();
            } else {
                //create dialog to open_gps
            }
        }


        /*if (requestCode == 33) {
            if (isGpsOpen()) {
                StartService(LocationRequest.PRIORITY_LOW_POWER);
            } else {
                CreateGpsDialog();
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == gps_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            initGoogleApiClient();
        }
    }

    private void intLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationRequest.setInterval(1000 * 60 * 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {

                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(PlacesActivity.this, 1255);
                        } catch (Exception e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("not available", "not available");
                        break;
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        intLocationRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        LocationListener(location);


    }

    private void LocationListener(final Location location) {

        if (location != null) {
            getNearbyPlaces(location);

        }
    }

    private void getGeoData(final double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        String location = lat + "," + lng;
        //  Log.e("fllflfl", location);
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, lang, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getResults().size() > 0) {
                                address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                //  Log.e("kkfkkfkfk", address);
                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (Exception e) {
                                // e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            //   binding.progBar.setVisibility(View.GONE);

                            Toast.makeText(PlacesActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void Search() {

        binding.progBar.setVisibility(View.VISIBLE);
        //AIzaSyArjmbYWTWZhDFFtPOLRLKYwjtBDkOEGrY
        Common.CloseKeyBoard(this, binding.edtSearch);
        dataList.clear();
        if (food_adapter != null) {
            food_adapter.notifyDataSetChanged();
        }


        String loc = lat + "," + lng;
        Log.e("mmmmm", "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + loc + "5000" + query + lang + getString(R.string.map_api_key));
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getNearbySearchStores(loc, 5000, query, lang, getString(R.string.map_api_key))
                .enqueue(new Callback<NearbyStoreDataModel>() {
                    @Override
                    public void onResponse(Call<NearbyStoreDataModel> call, Response<NearbyStoreDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body() != null && response.body().getResults() != null && response.body().getResults().size() > 0) {
                                //  preferences.saveQuery(activity, new QueryModel(query.trim()));
                                //   updateAdapter(response.body().getResults());
                                dataList.addAll(response.body().getResults());
                                food_adapter.notifyDataSetChanged();
                            } else {
                                // ll_no_store.setVisibility(View.VISIBLE);

                            }
                        } else {

                            //progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyStoreDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            //   progBar.setVisibility(View.GONE);
                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });

       /* String loc = "circle:15000@"+lat+","+lng;
        String fields ="id,place_id,name,geometry,rating,formatted_address,icon,opening_hours";

        Api.getService("https://maps.googleapis.com/maps/api/")
                .getNearbyStoresWithKeyword(loc,"textquery",(query+user_address),fields,current_language,getString(R.string.map_api_key))
                .enqueue(new Callback<SearchDataModel>() {
                    @Override
                    public void onResponse(Call<SearchDataModel> call, Response<SearchDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            progBar.setVisibility(View.GONE);
                            if (response.body().getCandidates().size() > 0) {
                                preferences.saveQuery(activity, new QueryModel(query.trim()));
                                updateAdapter(response.body().getCandidates());

                            } else {
                                ll_no_store.setVisibility(View.VISIBLE);

                            }
                        } else {

                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SearchDataModel> call, Throwable t) {
                        try {


                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });*/
    }

}