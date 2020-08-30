package com.v60BNS.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.tabs.TabLayout;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.adapters.Categorys_Adapter;
import com.v60BNS.adapters.Department_Adapter;
import com.v60BNS.adapters.Food_Adapter;
import com.v60BNS.adapters.Slider_Adapter;
import com.v60BNS.databinding.FragmentStoreBinding;
import com.v60BNS.models.MarketCatogryModel;
import com.v60BNS.models.SliderModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Store extends Fragment {

    private HomeActivity activity;
    private FragmentStoreBinding binding;
    private Preferences preferences;
    private String lang;
    private List<SliderModel> sliderModels;
    private Slider_Adapter sliderAdapter;
    private UserModel userModel;
    private List<MarketCatogryModel.Data> dataList;
    private Department_Adapter categorys_adapter;
    private Food_Adapter food_adapter;
    private int current_page = 0, NUM_PAGES;

    public static Fragment_Store newInstance() {
        return new Fragment_Store();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false);
        initView();
        initData();

        change_slide_image();
        YoYo.with(Techniques.ZoomIn)
                .duration(900)
                .repeat(0)
                .playOn(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initData() {
        dataList = new ArrayList<>();
        sliderModels.add(new SliderModel());
        sliderModels.add(new SliderModel());
        sliderModels.add(new SliderModel());
        sliderModels.add(new SliderModel());
        sliderModels.add(new SliderModel());
        sliderModels.add(new SliderModel());
        List<SliderModel.Data> data = new ArrayList<>();
        data.add(new SliderModel.Data());
        data.add(new SliderModel.Data());

        data.add(new SliderModel.Data());
        data.add(new SliderModel.Data());
        data.add(new SliderModel.Data());
        data.add(new SliderModel.Data());
        data.add(new SliderModel.Data());
        for (int i = 0; i < sliderModels.size(); i++) {

            sliderModels.get(i).setData(data);

        }
        sliderAdapter = new Slider_Adapter(activity, sliderModels);
        binding.pager.setAdapter(sliderAdapter);
        binding.progBarSlider.setVisibility(View.GONE);
        binding.progBarOffer.setVisibility(View.GONE);

        categorys_adapter = new Department_Adapter(dataList, activity, this);

        food_adapter = new Food_Adapter(dataList, activity, this);
        binding.recViewStatus.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewStatus.setAdapter(categorys_adapter);
        binding.recViewFavoriteOffers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewFavoriteOffers.setAdapter(food_adapter);
        Adddata();
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
        categorys_adapter.notifyDataSetChanged();
        food_adapter.notifyDataSetChanged();
    }


    private void initView() {
        sliderModels = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarOffer.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


    }

    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                binding.pager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        YoYo.with(Techniques.ZoomIn)
                .duration(900)
                .repeat(0)
                .playOn(binding.getRoot());

    }


}
