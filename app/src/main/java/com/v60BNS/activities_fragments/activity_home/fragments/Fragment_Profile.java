package com.v60BNS.activities_fragments.activity_home.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.activities_fragments.activity_setting.SettingsActivity;
import com.v60BNS.adapters.Categorys_Adapter;
import com.v60BNS.adapters.Comments_Adapter;
import com.v60BNS.adapters.Post_Adapter;
import com.v60BNS.databinding.FragmentProfileBinding;
import com.v60BNS.models.MarketCatogryModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Profile extends Fragment  {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private List<MarketCatogryModel.Data> dataList;
    public BottomSheetBehavior behavior;
    private RecyclerView recViewcomments;
    private ImageView imclose;
    private Post_Adapter post_adapter;

    public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        dataList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        recViewcomments = binding.getRoot().findViewById(R.id.recViewcomments);
        imclose = binding.getRoot().findViewById(R.id.imclose);
        post_adapter = new Post_Adapter(dataList, activity, this);

        binding.recViewFavoriteOffers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewFavoriteOffers.setAdapter(post_adapter);
//        Comments_Adapter comments_adapter = new Comments_Adapter(dataList, activity);
//        recViewcomments.setLayoutManager(new LinearLayoutManager(activity));
//        recViewcomments.setAdapter(comments_adapter);
        Adddata();
        binding.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, SettingsActivity.class);
                startActivity(intent);
            }
        });
        if(lang.equals("ar")){
            imclose.setRotation(180);
        }
        setUpBottomSheet();

    }

    private void setUpBottomSheet() {

        behavior = BottomSheetBehavior.from(binding.getRoot().findViewById(R.id.root));

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
        post_adapter.notifyDataSetChanged();

    }


    public void showcomments() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

}
