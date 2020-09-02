package com.v60BNS.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.activity_places.PlacesActivity;
import com.v60BNS.adapters.Comments_Adapter;
import com.v60BNS.adapters.Post_Adapter;
import com.v60BNS.adapters.Categorys_Adapter;
import com.v60BNS.databinding.FragmentMainBinding;
import com.v60BNS.models.MarketCatogryModel;
import com.v60BNS.models.NearbyModel;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.ReviewModels;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;


    private String lang;
    private Post_Adapter post_adapter;
    private List<MarketCatogryModel.Data> dataList;
    private Categorys_Adapter categorys_adapter;
    public BottomSheetBehavior behavior;
    private RecyclerView recViewcomments;
    private List<ReviewModels.Reviews> reviewsList;
    private ImageView imclose;
    private Comments_Adapter comments_adapter;

    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();

        return binding.getRoot();
    }


    private void initView() {

        dataList = new ArrayList<>();
        reviewsList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        recViewcomments = binding.getRoot().findViewById(R.id.recViewcomments);
        imclose = binding.getRoot().findViewById(R.id.imclose);
        post_adapter = new Post_Adapter(dataList, activity, this);
        categorys_adapter = new Categorys_Adapter(dataList, activity);

        binding.recViewFavoriteOffers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewFavoriteOffers.setAdapter(post_adapter);
        binding.recViewStatus.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewStatus.setAdapter(categorys_adapter);
        comments_adapter = new Comments_Adapter(reviewsList, activity);
        recViewcomments.setLayoutManager(new LinearLayoutManager(activity));
        recViewcomments.setAdapter(comments_adapter);
        Adddata();
        if (lang.equals("ar")) {
            imclose.setRotation(180);
        }
        setUpBottomSheet();

    }

    private void setUpBottomSheet() {

        behavior = BottomSheetBehavior.from(binding.getRoot().findViewById(R.id.root));

    }

    private void Adddata() {
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        dataList.add(new MarketCatogryModel.Data());
        post_adapter.notifyDataSetChanged();
        categorys_adapter.notifyDataSetChanged();

    }


    public void showcomments() {
        Intent intent = new Intent(activity, PlacesActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            getPlaceDetails((NearbyModel) data.getSerializableExtra("data"));
        }
    }

    private void getPlaceDetails(NearbyModel nearbyModel) {

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        Api.getService("https://maps.googleapis.com/maps/api/")
                .getPlaceReview(nearbyModel.getPlace_id(), getString(R.string.map_api_key))
                .enqueue(new Callback<NearbyStoreDataModel>() {
                    @Override
                    public void onResponse(Call<NearbyStoreDataModel> call, Response<NearbyStoreDataModel> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getResult().getReviews() != null) {
Log.e(";;;",response.body().getResult().getReviews().get(0).getAuthor_name());
                            reviewsList.addAll(response.body().getResult().getReviews());
                            comments_adapter.notifyDataSetChanged();

                        } else {
                            Log.e("dddddata",response.code()+"");

                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyStoreDataModel> call, Throwable t) {
                        try {

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

}
