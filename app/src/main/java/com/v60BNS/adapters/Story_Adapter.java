package com.v60BNS.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Main;
import com.v60BNS.databinding.StatusRowBinding;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class Story_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StoryModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private Fragment fragment;

    public Story_Adapter(List<StoryModel.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(context);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        StatusRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.status_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        EventHolder msgLeftHolder = (EventHolder) holder;
//            orderlist.get(position).getImage()

        if (position > 0) {
            msgLeftHolder.binding.setModel(orderlist.get(position));
        }
        msgLeftHolder.binding.setPos(position);
        if (position == 0) {
            msgLeftHolder.binding.image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_plus));
        }
        ((EventHolder) holder).binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    if (orderlist.get(msgLeftHolder.getLayoutPosition()).getPhone_code() == null) {
                        story(position);
                    }
                } else {
                    if (fragment instanceof Fragment_Main) {
                        Fragment_Main fragment_main = (Fragment_Main) fragment;
                        fragment_main.addstory();
                    }
                }
            }
        });

    }

    public void story(int position) {
        ArrayList<MyStory> myStories = new ArrayList<>();
        MyStory myStory = new MyStory(
                Tags.IMAGE_URL + orderlist.get(position).getImage(),
                null);


//        MyStory myStory = new MyStory(
//                orderlist.get(position).getImage(),
//                null);
        myStories.add(myStory);


        new StoryView.Builder(((FragmentActivity) context).getSupportFragmentManager())
                .setStoriesList(myStories)
                .setTitleLogoUrl(Tags.IMAGE_URL + orderlist.get(position).getImage())
                .setTitleText(orderlist.get(position).getUser().getName())
                .setStoryDuration(5000)
                .setRtl(true)
                .setStoryClickListeners(new StoryClickListeners() {
                    @Override
                    public void onDescriptionClickListener(int position) {

                    }

                    @Override
                    public void onTitleIconClickListener(int position) {

                    }
                }).setStartingIndex(0)
                .build()
                .show();
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
        public StatusRowBinding binding;

        public EventHolder(@NonNull StatusRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
