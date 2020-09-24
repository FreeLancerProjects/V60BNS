package com.v60BNS.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_cart.CartActivity;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Add;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Main;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Comments;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Store;
import com.v60BNS.activities_fragments.activity_notification.NotificationActivity;
import com.v60BNS.databinding.ActivityHomeBinding;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.NotFireModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.share.Common;
import com.v60BNS.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Locale;

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
    private Fragment_Comments fragment_comments;
    private Fragment_Profile fragment_profile;
    private UserModel userModel;
    private String lang;
    private String token;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
        setimage();
        if (savedInstanceState == null) {
            displayFragmentMain();
        }
        getdatafromintent();

    }

    private void getdatafromintent() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra("not", false)) {
            displayFragmentProfile();
        }
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            EventBus.getDefault().register(this);
            updateToken();

        }
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        displayFragmentMain();
                        break;
                    case R.id.store:
                        displayFragmentStore();
                        break;
                    case R.id.add:
                        displayFragmentAddPost();
                        break;
                    case R.id.comments:
                        displayFragmentComments();
                        break;
                    case R.id.profile:
                        if (userModel != null) {
                            displayFragmentProfile();
                        } else {
                            Common.CreateDialogAlert(HomeActivity.this, getResources().getString(R.string.please_sign_in_or_sign_up));
                        }
                        break;
                }

                return true;
            }
        });
        binding.flSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        binding.flNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        //  setUpBottomNavigation();


    }


    public void setimage() {
        userModel = preferences.getUserData(this);

        if (userModel != null) {
            binding.bottomNav.setItemIconTintList(null);

            // this is important
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.bottomNav.getMenu().getItem(4).setIconTintList(null);
                binding.bottomNav.getMenu().getItem(4).setIconTintMode(null);
            }
            Glide.with(getApplicationContext()).asBitmap().load(R.drawable.user)
                    .apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    // Log.e("lflgllg,";fllflf");

                    Drawable profileImage = new BitmapDrawable(getResources(), resource);
                    binding.bottomNav.getMenu().findItem(R.id.profile).setIcon(profileImage);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {


                    binding.bottomNav.getMenu().findItem(R.id.profile).setIcon(R.drawable.user);


                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    binding.bottomNav.getMenu().findItem(R.id.profile).setIcon(R.drawable.user);

                }
            });
        } else {
            binding.bottomNav.setItemIconTintList(null); // this is important
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.bottomNav.getMenu().getItem(4).setIconTintList(null);
                binding.bottomNav.getMenu().getItem(4).setIconTintMode(null);
            }
            Glide.with(getApplicationContext()).asBitmap().load(R.drawable.user)
                    .apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    // Log.e("lflgllg,";fllflf");

                    Drawable profileImage = new BitmapDrawable(getResources(), resource);
                    binding.bottomNav.getMenu().findItem(R.id.profile).setIcon(profileImage);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {


                    binding.bottomNav.getMenu().findItem(R.id.profile).setIcon(R.drawable.user);


                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    binding.bottomNav.getMenu().findItem(R.id.profile).setIcon(R.drawable.user);

                }
            });
        }
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.bottomNav.getChildAt(0);

        final View iconView =
                menuView.getChildAt(4).findViewById(com.google.android.material.R.id.icon);
        final ViewGroup.LayoutParams layoutParams =
                iconView.getLayoutParams();
        final DisplayMetrics displayMetrics =
                getResources().getDisplayMetrics();
        layoutParams.height = (int)
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
                        displayMetrics);
        layoutParams.width = (int)
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
                        displayMetrics);
        iconView.setLayoutParams(layoutParams);
    }

    //    private void setUpBottomNavigation() {
//
//        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_home);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.store), R.drawable.ic_store);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.add), R.drawable.ic_add);
//        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.orders), R.drawable.ic_experience);
//        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.user);
//
//        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
//        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.color3));
//        binding.ahBottomNav.setTitleTextSizeInSp(13, 13);
//        binding.ahBottomNav.setForceTint(true);
//        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.white));
//
//        binding.ahBottomNav.addItem(item1);
//        binding.ahBottomNav.addItem(item2);
//        binding.ahBottomNav.addItem(item3);
//        binding.ahBottomNav.addItem(item4);
//        binding.ahBottomNav.addItem(item5);
//
//
//        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
//            return false;
//        });
//
//        updateBottomNavigationPosition(0);
//
//    }
//
//    public void updateBottomNavigationPosition(int pos) {
//
//        binding.ahBottomNav.setCurrentItem(pos, false);
//
//    }
    @Override
    public void onResume() {
        super.onResume();
        setimage();
    }

    @Override
    public void onStart() {
        super.onStart();
        setimage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setimage();
    }


    public void displayFragmentMain() {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }


            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_add != null && fragment_add.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_add).commit();
            }

            if (fragment_comments != null && fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_comments).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }

            //  binding.setTitle(getString(R.string.home));
        } catch (Exception e) {
        }

    }

    public void displayFragmentStore() {
        try {
            if (fragment_store == null) {
                fragment_store = Fragment_Store.newInstance();
            }


            if (fragment_add != null && fragment_add.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_add).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_comments != null && fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_comments).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_store.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_store).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_store, "fragment_store").addToBackStack("fragment_store").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentComments() {
        try {
            if (fragment_comments == null) {
                fragment_comments = Fragment_Comments.newInstance();
            }


            if (fragment_add != null && fragment_add.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_add).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_comments).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_comments, "fragment_comments").addToBackStack("fragment_comments").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentProfile() {
        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }

            if (fragment_add != null && fragment_add.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_add).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_comments != null && fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_comments).commit();
            }
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();
                fragment_profile.getprofile();
                fragment_profile.getPosts();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentAddPost() {
        try {
            if (fragment_add == null) {
                fragment_add = Fragment_Add.newInstance();
            }


            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_comments != null && fragment_comments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_comments).commit();
            }
            if (fragment_add.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_add).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_add, "fragment_add").addToBackStack("fragment_add").commit();

            }

        } catch (Exception e) {
        }

    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {

            if (userModel != null) {
                if (fragment_main.behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                    fragment_main.behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    finish();
                }
            } else {
                if (fragment_main.behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                    fragment_main.behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    finish();
                    //  navigateToSignInActivity();}
                }
            }
        } else {
            displayFragmentMain();
        }
    }

    private void updateToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            task.getResult().getId();
                            Log.e("sssssss", token);
                            Api.getService(Tags.base_url)
                                    .updateToken(userModel.getId(), token, "android")
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                            if (response.isSuccessful()) {
                                                try {
                                                    Log.e("Success", "token updated");
                                                } catch (Exception e) {
                                                    //  e.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {
                                                Log.e("Error", t.getMessage());
                                            } catch (Exception e) {
                                            }
                                        }
                                    });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNotifications(NotFireModel notFireModel) {
        if (userModel != null) {
            if (fragment_profile != null && fragment_profile.isVisible()) {
                fragment_profile.getPosts();
            }
        }
    }
}
