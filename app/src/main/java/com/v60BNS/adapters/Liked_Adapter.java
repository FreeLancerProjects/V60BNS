package com.v60BNS.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.v60BNS.R;
import com.v60BNS.databinding.LikedRowBinding;
import com.v60BNS.databinding.StatusRowBinding;
import com.v60BNS.models.MarketCatogryModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Liked_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MarketCatogryModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;

    public Liked_Adapter(List<MarketCatogryModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LikedRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.liked_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        EventHolder msgLeftHolder = (EventHolder) holder;

        if (position > 0) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            if (lang.equals("en")) {
                lp.setMargins(-30, 1, 5, 0);

            } else {
                lp.setMargins(5, 1, -30, 0);

            }
            msgLeftHolder.binding.flImage.setLayoutParams(lp);
            if(position>2){
                msgLeftHolder.binding.flImage.setVisibility(View.GONE);
            }
        }


    }
/*
if(i==position){
    if(i!=0) {
        if (((EventHolder) holder).binding.expandLayout.isExpanded()) {
            ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
            ((EventHolder) holder).binding.expandLayout.collapse(true);
            ((EventHolder) holder).binding.expandLayout.setVisibility(View.GONE);



        }
        else {

          //  ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.expandLayout.setVisibility(View.VISIBLE);

           ((EventHolder) holder).binding.expandLayout.expand(true);
        }
    }
    else {
        eventHolder.binding.tvTitle.setBackground(activity.getResources().getDrawable(R.drawable.linear_bg_green));

        ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));

    }
}
if(i!=position) {
    eventHolder.binding.tvTitle.setBackground(activity.getResources().getDrawable(R.drawable.linear_bg_white));
    ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
    ((EventHolder) holder).binding.expandLayout.collapse(true);


}*/


    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public LikedRowBinding binding;

        public EventHolder(@NonNull LikedRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
