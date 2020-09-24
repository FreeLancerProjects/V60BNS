package com.v60BNS.activities_fragments.activity_coffee_detials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.v60BNS.R;
import com.v60BNS.adapters.Ingredients_Adapter;
import com.v60BNS.adapters.SlidingImage_Adapter;
import com.v60BNS.databinding.ActivityCoffeeDetialsBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.SingleProductModel;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.SliderModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.share.Common;
import com.v60BNS.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CoffeeDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCoffeeDetialsBinding binding;
    private String lang;
    private Preferences preferences;
    private List<SliderModel.Data> sliderModels;
    private SlidingImage_Adapter sliderAdapter;
    private List<StoryModel.Data> dataList;
    private Ingredients_Adapter food_adapter;
    private int current_page = 0, NUM_PAGES;
    private String product_id;
    private SingleProductModel singleProductModel;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coffee_detials);
        getDataFromIntent();
        getSingleProduct();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            product_id = intent.getStringExtra("product_id");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        dataList = new ArrayList<>();
        singleProductModel=new SingleProductModel();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setModel(singleProductModel);
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
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
        dataList.add(new StoryModel.Data());
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

    private void update(SingleProductModel body) {
        singleProductModel = body;
        binding.setModel(body);

    }
    public void getSingleProduct() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(CoffeeDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
      //  try {
            Api.getService(Tags.base_url)
                    .getSingleProduct(product_id)
                    .enqueue(new Callback<SingleProductModel>() {
                        @Override
                        public void onResponse(Call<SingleProductModel> call, Response<SingleProductModel> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);

                                update(response.body());
                            } else {


                                Toast.makeText(CoffeeDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleProductModel> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                Toast.makeText(CoffeeDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
//        } catch (Exception e) {
//            Log.e("fllvlvl", e.toString());
//            dialog.dismiss();
//        }
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