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
import com.v60BNS.activities_fragments.activity_cart.CartActivity;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.adapters.Ingredients_Adapter;
import com.v60BNS.adapters.SlidingImage_Adapter;
import com.v60BNS.databinding.ActivityCoffeeDetialsBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.Add_Order_Model;
import com.v60BNS.models.SingleProductModel;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.SliderModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.share.Common;
import com.v60BNS.tags.Tags;

import org.jsoup.Jsoup;

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
    private List<SingleProductModel.ProductImage> sliderModels;
    private SlidingImage_Adapter sliderAdapter;
    private List<StoryModel.Data> dataList;
    private Ingredients_Adapter food_adapter;
    private int current_page = 0, NUM_PAGES;
    private String product_id;
    private SingleProductModel singleProductModel;
    private Add_Order_Model add_order_model;

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
        add_order_model = preferences.getUserOrder(this);
        if (add_order_model == null) {
            binding.setCartCount(0);
        } else {
            binding.setCartCount(add_order_model.getOrder_details().size());
        }
        dataList = new ArrayList<>();
        singleProductModel = new SingleProductModel();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setModel(singleProductModel);
        binding.setBackListener(this);

        binding.tab.setupWithViewPager(binding.pager);
        binding.progBarSlider.setVisibility(View.GONE);
        change_slide_image();

        food_adapter = new Ingredients_Adapter(dataList, this, null);
        binding.recview.setLayoutManager(new LinearLayoutManager(this));
        binding.recview.setAdapter(food_adapter);
        binding.flSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoffeeDetialsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
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
        binding.flAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Add_Order_Model.OrderDetails> orderDetailsList;
                List<Add_Order_Model.ProductDetails> productDetailsList;
                Add_Order_Model add_order_model = preferences.getUserOrder(CoffeeDetialsActivity.this);
                if (add_order_model != null) {
                    orderDetailsList = add_order_model.getOrder_details();
                    productDetailsList = add_order_model.getProductDetails();
                } else {
                    add_order_model = new Add_Order_Model();
                    orderDetailsList = new ArrayList<>();
                    productDetailsList = new ArrayList<>();
                }
                int pos=-1;

                for(int i=0;i<orderDetailsList.size();i++){
                    if(orderDetailsList.get(i).getProduct_id()==singleProductModel.getId()){
                        pos=i;
                        break;
                    }
                }
                if(pos==-1){
                Add_Order_Model.ProductDetails productDetails = new Add_Order_Model.ProductDetails();
                Add_Order_Model.OrderDetails orderDetails = new Add_Order_Model.OrderDetails();
                productDetails.setAmount(Integer.parseInt(binding.tvAmount.getText().toString()));
                orderDetails.setAmount(Integer.parseInt(binding.tvAmount.getText().toString()));
                productDetails.setImage(singleProductModel.getMain_image());
                productDetails.setName(singleProductModel.getAr_title());
                productDetails.setTotal_cost(Double.parseDouble(singleProductModel.getPrice()) * productDetails.getAmount());
                orderDetails.setTotal_cost(Double.parseDouble(singleProductModel.getPrice()) * productDetails.getAmount());
                orderDetails.setProduct_id(singleProductModel.getId());
                orderDetailsList.add(orderDetails);
                productDetailsList.add(productDetails);
                add_order_model.setProductDetails(productDetailsList);
                add_order_model.setOrder_details(orderDetailsList);}
                else {
                    Add_Order_Model.ProductDetails productDetails = productDetailsList.get(pos);
                    Add_Order_Model.OrderDetails orderDetails = orderDetailsList.get(pos);
                    productDetails.setAmount(Integer.parseInt(binding.tvAmount.getText().toString())+productDetails.getAmount());
                    orderDetails.setAmount(Integer.parseInt(binding.tvAmount.getText().toString())+orderDetails.getAmount());
                    productDetails.setImage(singleProductModel.getMain_image());
                    productDetails.setName(singleProductModel.getAr_title());
                    productDetails.setTotal_cost(Double.parseDouble(singleProductModel.getPrice()) * productDetails.getAmount());
                    orderDetails.setTotal_cost(Double.parseDouble(singleProductModel.getPrice()) * productDetails.getAmount());
                    orderDetails.setProduct_id(singleProductModel.getId());
                    orderDetailsList.remove(pos);
                    orderDetailsList.set(pos,orderDetails);
                    productDetailsList.remove(pos);
                    productDetailsList.set(pos,productDetails);
                    add_order_model.setProductDetails(productDetailsList);
                    add_order_model.setOrder_details(orderDetailsList);
                }
                preferences.create_update_order(CoffeeDetialsActivity.this, add_order_model);
                Toast.makeText(CoffeeDetialsActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                if (add_order_model == null) {
                    binding.setCartCount(0);
                } else {
                    binding.setCartCount(add_order_model.getOrder_details().size());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (add_order_model == null) {
            binding.setCartCount(0);
        } else {
            binding.setCartCount(add_order_model.getOrder_details().size());
        }
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
        try {
            binding.tvContent.setText(Jsoup.parse(body.getAr_components()).text());
            binding.tvdesc.setText(Jsoup.parse(body.getAr_desc()).text());
        } catch (Exception e) {
            binding.tvContent.setText(body.getAr_components());
            binding.tvdesc.setText(body.getAr_desc());
            Log.e("kskskks", e.toString());
        }
        singleProductModel = body;
        binding.setModel(body);
        sliderModels = new ArrayList<>();
        sliderModels.addAll(body.getProduct_images());
        Log.e("dkdkdk", sliderModels.size() + "");
        sliderAdapter = new SlidingImage_Adapter(this, sliderModels);
        binding.pager.setAdapter(sliderAdapter);

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