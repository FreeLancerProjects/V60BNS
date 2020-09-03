package com.v60BNS.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_chat.ChatActivity;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.adapters.StarComments_Adapter;
import com.v60BNS.databinding.FragmentCommentsBinding;
import com.v60BNS.models.MarketCatogryModel;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Comments extends Fragment {

    private HomeActivity activity;
    private FragmentCommentsBinding binding;
    private List<MarketCatogryModel.Data> dataList;
    private StarComments_Adapter starComments_adapter;


    public static Fragment_Comments newInstance() {
        return new Fragment_Comments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false);
        initView();


        return binding.getRoot();
    }

    private void initView() {
        dataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        starComments_adapter = new StarComments_Adapter(dataList, activity,this);
        binding.recViewFavoriteOffers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewFavoriteOffers.setAdapter(starComments_adapter);
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
        starComments_adapter.notifyDataSetChanged();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void showchat() {
        Intent intent = new Intent(activity, ChatActivity.class);
        startActivity(intent);
    }
}
