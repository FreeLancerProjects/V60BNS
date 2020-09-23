package com.v60BNS.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Store;
import com.v60BNS.databinding.CoffeeRowBinding;
import com.v60BNS.databinding.LoadmoreRowBinding;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.share.Common;
import com.v60BNS.models.SingleProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA_ROW = 1;
    private final int LOAD_ROW = 2;

    private List<SingleProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private HomeActivity homeActivity;
    private Fragment_Store fragment_store;
    private int i = -1;

    public ProductAdapter(List<SingleProductModel> list, Fragment_Store fragment_store, Context context) {
        this.list = list;
        this.context = context;
        this.fragment_store=fragment_store;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA_ROW) {
            CoffeeRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.coffee_row, parent, false);
            return new MyHolder(binding);
        } else {
            LoadmoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.loadmore_row, parent, false);
            return new LoadMoreHolder(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;

            myHolder.binding.setModel(list.get(position));

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof HomeActivity) {
                        homeActivity = (HomeActivity) context;
                        fragment_store.setProduct(list.get(holder.getLayoutPosition()).getId() + "");

                    }
                }
            });

        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public CoffeeRowBinding binding;

        public MyHolder(@NonNull CoffeeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        public LoadmoreRowBinding binding;

        public LoadMoreHolder(@NonNull LoadmoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }


    @Override
    public int getItemViewType(int position) {
        SingleProductModel model = list.get(position);
        if (model == null) {
            return LOAD_ROW;
        } else {
            return DATA_ROW;
        }
    }
}