package com.v60BNS.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_orders.fragments.Fragment_Current_Order;
import com.v60BNS.activities_fragments.activity_orders.fragments.Fragment_Finshied_Order;
import com.v60BNS.databinding.LoadMoreBinding;
import com.v60BNS.databinding.OrderRowBinding;
import com.v60BNS.models.OrderModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MyOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<OrderModel> list;
    private Context context;
    private Fragment fragment;
    private String lang;
private Fragment_Finshied_Order fragment_finshied_order;
private Fragment_Current_Order fragment_current_order;
    public MyOrdersAdapter(List<OrderModel> list, Context context, Fragment fragment) {

        this.list = list;
        this.context = context;
        this.fragment = fragment;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            OrderRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_row,parent,false);
            return new MyHolder(binding);
        } else {
            LoadMoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.load_more,parent,false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {


            final MyHolder myHolder = (MyHolder) holder;
            OrderModel model = list.get(myHolder.getAdapterPosition());
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(fragment instanceof  Fragment_Current_Order){
            fragment_current_order=(Fragment_Current_Order)fragment;
            fragment_current_order.DisplayOrderDetials(list.get(holder.getLayoutPosition()).getId());
        }
        else if(fragment instanceof Fragment_Finshied_Order){
            fragment_finshied_order=(Fragment_Finshied_Order)fragment;
            fragment_finshied_order.DisplayOrderDetials(list.get(holder.getLayoutPosition()).getId());

        }
    }
});

            myHolder.binding.setLang(lang);
            myHolder.binding.setModel(model);


        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       private OrderRowBinding binding;
        public MyHolder(OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {

        private LoadMoreBinding binding;
        public LoadMoreHolder(LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemViewType(int position) {
        OrderModel model = list.get(position);
        if (model == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }



    }
}
