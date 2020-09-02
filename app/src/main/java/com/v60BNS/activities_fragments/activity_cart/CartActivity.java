package com.v60BNS.activities_fragments.activity_cart;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.v60BNS.R;
import com.v60BNS.adapters.CartAdapter;
import com.v60BNS.adapters.Ingredients_Adapter;
import com.v60BNS.adapters.SlidingImage_Adapter;
import com.v60BNS.databinding.ActivityCartBinding;
import com.v60BNS.databinding.ActivityCoffeeDetialsBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language;
import com.v60BNS.models.MarketCatogryModel;
import com.v60BNS.models.SliderModel;
import com.v60BNS.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;


public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;
    private String lang;
    private Preferences preferences;

    private List<MarketCatogryModel.Data> dataList;
    private CartAdapter food_adapter;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();
    }

    private void initView() {
        dataList = new ArrayList<>();

        preferences = Preferences.getInstance();

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        food_adapter=new CartAdapter(dataList,this);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(food_adapter);
        initData();

    }

    private void initData() {


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
        food_adapter.notifyDataSetChanged();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    @Override
    public void back() {
        finish();
    }
}