package com.v60BNS.activities_fragments.activity_order_detials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;


import com.v60BNS.R;
import com.v60BNS.adapters.Order_Detials_Adapter;
import com.v60BNS.databinding.ActivityOrderdetialsBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.OrderModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderdetialsBinding binding;

    private Preferences preferences;
    private UserModel userModel;

    private Order_Detials_Adapter order_detials_adapter;
    private List<OrderModel.OrderDetails> orderDetails;
    private String lang;
    private String order_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Language_Helper.getLanguage(newBase)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orderdetials);
        EventBus.getDefault().register(this);

        getdatafromintent();

        initView();
        if (order_id != null) {
            getOrderDetials();
        }
    }

    private void getdatafromintent() {
        if (getIntent().getStringExtra("orderid") != null) {
            order_id = getIntent().getStringExtra("orderid");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenNotificationChange(OrderModel order_model) {
        order_id = order_model.getId() + "";
        getOrderDetials();

    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        orderDetails = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        order_detials_adapter = new Order_Detials_Adapter(orderDetails, this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recMarket.setItemViewCacheSize(25);
        binding.recMarket.setDrawingCacheEnabled(true);
        binding.recMarket.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        binding.recMarket.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recMarket.setAdapter(order_detials_adapter);
        binding.setLang(lang);
        binding.setBackListener(this);

    }


    public void getOrderDetials() {
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getorderdetials("Bearer " + userModel.getToken(), order_id)
                .enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getOrderDetails() != null) {

                            orderDetails.addAll(response.body().getOrderDetails());
                            if (response.body().getOrderDetails().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                binding.llNoStore.setVisibility(View.GONE);
                                order_detials_adapter.notifyDataSetChanged();
                                // updatestatus(response.body());
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                binding.llNoStore.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(OrderDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {
                        try {

                            binding.progBar.setVisibility(View.GONE);

                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void back() {
        finish();
    }
}
