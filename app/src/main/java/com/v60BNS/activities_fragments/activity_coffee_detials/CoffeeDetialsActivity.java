package com.v60BNS.activities_fragments.activity_coffee_detials;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.v60BNS.R;
import com.v60BNS.adapters.Ingredients_Adapter;
import com.v60BNS.adapters.SlidingImage_Adapter;
import com.v60BNS.databinding.ActivityCoffeeDetialsBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language;
import com.v60BNS.models.MarketCatogryModel;
import com.v60BNS.models.SliderModel;
import com.v60BNS.preferences.Preferences;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import io.paperdb.Paper;


public class CoffeeDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCoffeeDetialsBinding binding;
    private String lang;
    private Preferences preferences;
    private List<SliderModel.Data> sliderModels;
    private SlidingImage_Adapter sliderAdapter;
    private List<MarketCatogryModel.Data> dataList;
    private Ingredients_Adapter food_adapter;
    private int current_page = 0, NUM_PAGES;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language.updateResources(base, Language.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coffee_detials);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        dataList = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        change_slide_image();
        food_adapter = new Ingredients_Adapter(dataList, this, null);
        binding.recview.setLayoutManager(new LinearLayoutManager(this));
        binding.recview.setAdapter(food_adapter);
        binding.imageDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(binding.tvAmount.getText().toString()) > 1) {
                    binding.tvAmount.setText((Integer.parseInt(binding.tvAmount.getText().toString()) - 1) + "");
                }
            }
        });
        binding.imageIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvAmount.setText((Integer.parseInt(binding.tvAmount.getText().toString()) + 1) + "");

            }
        });
        initData();

    }

    private void initData() {

        sliderModels = new ArrayList<>();
        sliderModels.add(new SliderModel.Data());
        sliderModels.add(new SliderModel.Data());
        sliderModels.add(new SliderModel.Data());
        sliderModels.add(new SliderModel.Data());
        sliderModels.add(new SliderModel.Data());
        sliderModels.add(new SliderModel.Data());


        sliderAdapter = new SlidingImage_Adapter(this, sliderModels);
        binding.pager.setAdapter(sliderAdapter);
        binding.tab.setupWithViewPager(binding.pager);
        binding.progBarSlider.setVisibility(View.GONE);
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
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    @Override
    public void back() {
        finish();
    }
}