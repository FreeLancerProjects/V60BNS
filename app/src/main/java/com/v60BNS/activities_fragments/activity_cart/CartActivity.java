package com.v60BNS.activities_fragments.activity_cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_map.MapActivity;
import com.v60BNS.activities_fragments.activity_orders.OrdersActivity;
import com.v60BNS.adapters.CartAdapter;
import com.v60BNS.databinding.ActivityCartBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.Add_Order_Model;
import com.v60BNS.models.SelectedLocation;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.share.Common;
import com.v60BNS.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;
    private String lang;
    private Preferences preferences;
    private List<Add_Order_Model.ProductDetails> dataList;
    private CartAdapter cartAdapter;
    private List<Add_Order_Model.OrderDetails> orderDetailsList;
    private SelectedLocation selectedLocation;
    private Add_Order_Model add_order_model;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();
        getorders();
    }

    private void initView() {
        dataList = new ArrayList<>();
        orderDetailsList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        cartAdapter = new CartAdapter(dataList, this);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(cartAdapter);
        binding.flPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    checkdata();
                } else {
                    Common.CreateDialogAlert2(CartActivity.this, getResources().getString(R.string.please_sign_in_or_sign_up));
                }
            }
        });
        //   initData();

    }

    private void getorders() {
        if (preferences.getUserOrder(this) != null) {
            binding.consTotal.setVisibility(View.VISIBLE);

            dataList.clear();
            orderDetailsList.clear();
            dataList.addAll(preferences.getUserOrder(this).getProductDetails());
            orderDetailsList.addAll(preferences.getUserOrder(this).getOrder_details());

            cartAdapter.notifyDataSetChanged();
            gettotal();
        } else {
            binding.consTotal.setVisibility(View.GONE);
binding.llEmptyCart.setVisibility(View.VISIBLE);
        }
    }

    private void gettotal() {

        double total = 0;
        for (int i = 0; i < orderDetailsList.size(); i++) {
            total += orderDetailsList.get(i).getTotal_cost();

        }


        binding.tvquatity.setText(total + "");
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


    public void removeitem(int layoutPosition) {
        dataList.remove(layoutPosition);
        orderDetailsList.remove(layoutPosition);
        if (dataList.size() > 0) {
            Add_Order_Model add_order_model = preferences.getUserOrder(this);
            add_order_model.setProductDetails(dataList);
            add_order_model.setOrder_details(orderDetailsList);
            preferences.create_update_order(this, add_order_model);
            gettotal();
        } else {
            preferences.create_update_order(this, null);
            binding.llEmptyCart.setVisibility(View.VISIBLE);
            binding.consTotal.setVisibility(View.GONE);


        }

        cartAdapter.notifyDataSetChanged();

    }

    public void additem(int layoutPosition) {
        Add_Order_Model.OrderDetails products1 = orderDetailsList.get(layoutPosition);
        products1.setTotal_cost((products1.getTotal_cost() / products1.getAmount()) * (products1.getAmount() + 1));
        products1.setAmount(products1.getAmount() + 1);
        orderDetailsList.remove(layoutPosition);
        orderDetailsList.add(layoutPosition, products1);
        Add_Order_Model add_order_model = preferences.getUserOrder(this);
        add_order_model.setOrder_details(orderDetailsList);
        preferences.create_update_order(this, add_order_model);
        Add_Order_Model.ProductDetails products2 = dataList.get(layoutPosition);
        products2.setTotal_cost((products2.getTotal_cost() / products2.getAmount()) * (products2.getAmount() + 1));
        products2.setAmount(products2.getAmount() + 1);
        dataList.remove(layoutPosition);
        dataList.add(layoutPosition, products2);
        add_order_model.setProductDetails(dataList);
        preferences.create_update_order(this, add_order_model);
        cartAdapter.notifyDataSetChanged();
        gettotal();
    }

    public void minusitem(int layoutPosition) {

        Add_Order_Model.OrderDetails products1 = orderDetailsList.get(layoutPosition);
        Add_Order_Model.ProductDetails products2 = dataList.get(layoutPosition);

        if (products1.getAmount() > 1) {
            products1.setTotal_cost((products1.getTotal_cost() / products1.getAmount()) * (products1.getAmount() - 1));
            products1.setAmount(products1.getAmount() - 1);
            orderDetailsList.remove(layoutPosition);
            orderDetailsList.add(layoutPosition, products1);
            Add_Order_Model add_order_model = preferences.getUserOrder(this);
            add_order_model.setOrder_details(orderDetailsList);
            products2.setTotal_cost((products2.getTotal_cost() / products2.getAmount()) * (products2.getAmount() - 1));
            products2.setAmount(products2.getAmount() - 1);
            dataList.remove(layoutPosition);
            dataList.add(layoutPosition, products2);
            add_order_model.setProductDetails(dataList);
            preferences.create_update_order(this, add_order_model);
            cartAdapter.notifyDataSetChanged();
            gettotal();

        }
    }

    private void checkdata() {
        add_order_model = preferences.getUserOrder(this);
        add_order_model.setTotal_cost(Double.parseDouble(binding.tvquatity.getText().toString()));
        add_order_model.setPayment_type("cash");

        selectlocation();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("location")) {
                selectedLocation = (SelectedLocation) data.getSerializableExtra("location");

                add_order_model.setAddress(selectedLocation.getAddress());
                add_order_model.setLatitude(selectedLocation.getLat());
                add_order_model.setLongitude(selectedLocation.getLng());


                accept_order();
            }
        }


    }

    public void selectlocation() {
        Intent intent = new Intent(CartActivity.this, MapActivity.class);
        startActivityForResult(intent, 1);
    }

    private void accept_order() {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).accept_orders("Bearer " + userModel.getToken(), add_order_model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    showdetials();
// Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

                    //  activity.refresh(Send_Data.getType());
                } else {
                    Common.CreateDialogAlert(CartActivity.this, getString(R.string.failed));

                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    dialog.dismiss();
                    Toast.makeText(CartActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    private void showdetials() {
        binding.consTotal.setVisibility(View.GONE);

        preferences.create_update_order(CartActivity.this, null);
        dataList.clear();
        orderDetailsList.clear();
        cartAdapter.notifyDataSetChanged();
        getorders();
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
    }

}