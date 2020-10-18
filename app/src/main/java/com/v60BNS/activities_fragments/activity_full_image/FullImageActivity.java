package com.v60BNS.activities_fragments.activity_full_image;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_orders.OrdersActivity;
import com.v60BNS.adapters.Comments_Adapter;
import com.v60BNS.adapters.Post_Adapter;
import com.v60BNS.adapters.Replayes_Adapter;
import com.v60BNS.databinding.ActivityFullImageBinding;
import com.v60BNS.databinding.ActivityProfileBinding;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.Comments_Model;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.PostModel;
import com.v60BNS.models.ReviewModels;
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

import static com.v60BNS.adapters.Post_Adapter.phone;
import static com.v60BNS.adapters.Post_Adapter.user_id;
import static com.v60BNS.tags.Tags.IMAGE_URL;

public class FullImageActivity extends AppCompatActivity {


    ActivityFullImageBinding binding;
    private String imageUrl,title;
    public static FullImageActivity newInstance() {

        return new FullImageActivity();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_image);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        if (getIntent()!=null){
            imageUrl=getIntent().getStringExtra("imageUri");
            title=getIntent().getStringExtra("title");

        }


    }


    private void initView() {

        Picasso.get().load(IMAGE_URL + imageUrl).into(binding.fl);
        binding.tvName.setText(title);
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
