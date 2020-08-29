package com.v60BNS.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.v60BNS.R;
import com.v60BNS.databinding.CommentRowBinding;
import com.v60BNS.databinding.PostRowBinding;
import com.v60BNS.models.MarketCatogryModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Comments_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MarketCatogryModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = -1;

    public Comments_Adapter(List<MarketCatogryModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CommentRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.comment_row, parent, false);
        return new EventsHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        EventsHolder msgRightHolder = (EventsHolder) holder;

        Replayes_Adapter comments_adapter=new Replayes_Adapter(orderlist,context);
        msgRightHolder.binding.recreplayes.setLayoutManager(new LinearLayoutManager(context));
        msgRightHolder.binding.recreplayes.setAdapter(comments_adapter);
        msgRightHolder.binding.tvreplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(msgRightHolder.binding.expand.isExpanded()){
                    msgRightHolder.binding.expand.collapse(true);
                }
                else {
                    msgRightHolder.binding.expand.expand(true);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }


    public class EventsHolder extends RecyclerView.ViewHolder {
        public CommentRowBinding binding;

        public EventsHolder(@NonNull CommentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}