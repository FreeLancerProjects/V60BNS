package com.v60BNS.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Store;
import com.v60BNS.databinding.DepartmentRowBinding;
import com.v60BNS.models.CategoryDataModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryDataModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Store fragment_store;
    public CategoryAdapter(List<CategoryDataModel.Data> list,Fragment_Store fragment_store, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        fragment_store=fragment_store;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DepartmentRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.department_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view -> {
            Log.e("sssss",list.get(holder.getLayoutPosition()).getId()+"");

            Fragment_Store fragment_store=new Fragment_Store();
            fragment_store.setitemData(list.get(holder.getLayoutPosition()).getId()+"");
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public DepartmentRowBinding binding;

        public MyHolder(@NonNull DepartmentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
