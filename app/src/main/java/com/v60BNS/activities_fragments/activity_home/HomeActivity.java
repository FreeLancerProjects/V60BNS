package com.v60BNS.activities_fragments.activity_home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.firebase.iid.FirebaseInstanceId;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Add;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Main;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Orders;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Store;
import com.v60BNS.activities_fragments.activity_login.LoginActivity;
import com.v60BNS.databinding.ActivityHomeBinding;
import com.v60BNS.language.Language;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Store fragment_store;
    private Fragment_Add fragment_add;
    private Fragment_Orders fragment_orders;
    private Fragment_Profile fragment_profile;
    private UserModel userModel;
    private String lang;
    private String token;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        setUpBottomNavigation();



    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.store), R.drawable.ic_store);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.add), R.drawable.ic_add);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.orders), R.drawable.ic_experience);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.user);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.color3));
        binding.ahBottomNav.setTitleTextSizeInSp(13, 13);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.white));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);
        binding.ahBottomNav.addItem(item5);


        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            return false;
        });

        updateBottomNavigationPosition(0);

    }

    public void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);

    }


}
