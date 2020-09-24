package com.v60BNS.activities_fragments.activity_orders;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_orders.fragments.Fragment_Current_Order;
import com.v60BNS.activities_fragments.activity_orders.fragments.Fragment_Finshied_Order;
import com.v60BNS.adapters.ViewPagersAdapter;
import com.v60BNS.databinding.ActivityOrderBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.OrderModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import omari.hamza.storyview.utils.ViewPagerAdapter;

public class OrdersActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderBinding binding;
    private String lang;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private ViewPagersAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Language_Helper.getLanguage(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        initView();
        EventBus.getDefault().register(this);
        getdatafromintent();

    }
    private void getdatafromintent() {
        if(getIntent().getSerializableExtra("data")!=null){
            OrderModel orderModel= (OrderModel) getIntent().getSerializableExtra("data");

                 }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenNotificationChange(OrderModel order_model)
    {


//        if (fragment_myorders!=null&&fragment_myorders.isAdded()&&fragment_myorders.isVisible())
//        {
//            new Handler()
//                    .postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            fragment_myorders.getOrders();                        }
//                    },1);
//        }
//        else {
//            new Handler()
//                    .postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(fragment_myorders!=null){
//                                fragment_myorders.getOrders();
//                            }
//
//                            DisplayFragmentMyorders();
//                        }
//                    },1);
//        }


    }
    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
binding.setBackListener(this);
       binding.tab.setupWithViewPager(binding.pager);
        addFragments_Titles();
        binding.pager.setOffscreenPageLimit(fragmentList.size());

        adapter = new ViewPagersAdapter(getSupportFragmentManager());
        adapter.addFragments(fragmentList);
        adapter.addTitles(titles);
        binding.pager.setAdapter(adapter);







    }

    private void addFragments_Titles() {
        fragmentList.add(Fragment_Current_Order.newInstance());
        fragmentList.add(Fragment_Finshied_Order.newInstance());


        titles.add(getString(R.string.current_order));
        titles.add(getString(R.string.finish_order));



    }

    @Override
    public void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
    }

}
