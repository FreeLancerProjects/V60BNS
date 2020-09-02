package com.v60BNS.activity_places;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.v60BNS.R;
import com.v60BNS.adapters.CartAdapter;
import com.v60BNS.adapters.Places_Adapter;
import com.v60BNS.databinding.ActivityCartBinding;
import com.v60BNS.databinding.ActivityPlacesBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language;
import com.v60BNS.models.MarketCatogryModel;
import com.v60BNS.models.NearbyModel;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlacesActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityPlacesBinding binding;
    private String lang;
    private Preferences preferences;

    private List<NearbyModel> dataList;
    private Places_Adapter food_adapter;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_places);
        initView();
    }

    private void initView() {
        dataList = new ArrayList<>();

        preferences = Preferences.getInstance();

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        food_adapter = new Places_Adapter(dataList, this);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(food_adapter);
        initData();

    }

    private void initData() {


        getNearbyPlaces();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    @SuppressLint("MissingPermission")
    public void getNearbyPlaces() {

        String loc = "30.567533" + "," + "31.0135732";

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
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}