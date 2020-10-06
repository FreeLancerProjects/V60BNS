package com.v60BNS.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.v60BNS.R;
import com.v60BNS.databinding.ReplayRowBinding;
import com.v60BNS.models.Comments_Model;
import com.v60BNS.models.StoryModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Replayes_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Comments_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = -1;
    private Comments_Adapter comments_adapter;
    public static int hight;

    public Replayes_Adapter(List<Comments_Model.Data> orderlist, Context context, Comments_Adapter comments_adapter) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.comments_adapter = comments_adapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ReplayRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.replay_row, parent, false);
        return new EventsHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        EventsHolder msgRightHolder = (EventsHolder) holder;
        msgRightHolder.binding.setModel(orderlist.get(position));


    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }


    public class EventsHolder extends RecyclerView.ViewHolder {
        public ReplayRowBinding binding;

        public EventsHolder(@NonNull ReplayRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}