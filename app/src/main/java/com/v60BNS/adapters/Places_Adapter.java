package com.v60BNS.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.v60BNS.R;
import com.v60BNS.activity_places.PlacesActivity;
import com.v60BNS.databinding.CommentRowBinding;
import com.v60BNS.databinding.StoreRowBinding;
import com.v60BNS.models.NearbyModel;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.ReviewModels;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Places_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NearbyModel> nearbyModels;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = -1;

    public Places_Adapter(List<NearbyModel> nearbyModels, Context context) {
        this.nearbyModels = nearbyModels;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        StoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.store_row, parent, false);
        return new EventsHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        EventsHolder msgRightHolder = (EventsHolder) holder;
msgRightHolder.binding.setModel(nearbyModels.get(position));
msgRightHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        PlacesActivity placesActivity=(PlacesActivity)context;
        placesActivity.choose(nearbyModels.get(msgRightHolder.getLayoutPosition()));
    }
});
//        Replayes_Adapter comments_adapter = new Replayes_Adapter(orderlist, context, this, position);
//        msgRightHolder.binding.recreplayes.setLayoutManager(new LinearLayoutManager(context));
//        msgRightHolder.binding.recreplayes.setAdapter(comments_adapter);

//        msgRightHolder.binding.tvreplay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (msgRightHolder.binding.expand.isExpanded()) {
//                    msgRightHolder.binding.expand.collapse(true);
//                } else {
//                    msgRightHolder.binding.expand.expand(true);
//
//                }
//            }
//        });
//        if (i == position) {
//
//            msgRightHolder.binding.expand.setExpanded(true);
//            msgRightHolder.binding.edtcomment.setCursorVisible(true);
//            msgRightHolder.binding.edtcomment.setText("الرد");
//            msgRightHolder.binding.edtcomment.setSelection(4);
//            Log.e(";lfll", comments_adapter.hight + "");
//            msgRightHolder.itemView.scrollTo(0, msgRightHolder.binding.recreplayes.getBottom() + comments_adapter.hight + 168);
//            msgRightHolder.binding.edtcomment.requestFocus();
//            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (inputMethodManager != null) {
//                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            }
//
//        } else {
//            msgRightHolder.binding.expand.setExpanded(false);

       // }

    }

    @Override
    public int getItemCount() {
        return nearbyModels.size();
    }

    public void setcursor(int posstion) {
        i = posstion;
        notifyItemChanged(posstion);
    }


    public class EventsHolder extends RecyclerView.ViewHolder {
        public StoreRowBinding binding;

        public EventsHolder(@NonNull StoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}