package com.v60BNS.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_cart.CartActivity;
import com.v60BNS.databinding.CartRowBinding;
import com.v60BNS.models.Add_Order_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Add_Order_Model.ProductDetails> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
private CartActivity cartActivity;
    public CartAdapter(List<Add_Order_Model.ProductDetails> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CartRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_row, parent, false);

        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
       eventHolder.binding.setLang(lang);
        eventHolder.binding.setModel(orderlist.get(position));
eventHolder.binding.icon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof  CartActivity){
            cartActivity=(CartActivity)context;
            cartActivity.removeitem(eventHolder.getLayoutPosition());
        }
    }
});
eventHolder.binding.imgIncrease.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof  CartActivity){
            cartActivity=(CartActivity)context;
            cartActivity.additem(eventHolder.getLayoutPosition());
        }
    }
});
eventHolder.binding.imgDecrease.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof  CartActivity){
            cartActivity=(CartActivity)context;
            cartActivity.minusitem(eventHolder.getLayoutPosition());
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public CartRowBinding binding;

        public EventHolder(@NonNull CartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
