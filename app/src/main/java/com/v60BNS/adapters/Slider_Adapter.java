package com.v60BNS.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.v60BNS.R;
import com.v60BNS.models.SliderModel;
import com.v60BNS.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Slider_Adapter extends PagerAdapter {
    private List<SliderModel.Data> list;
    private Context context;
    private LayoutInflater inflater;

    public Slider_Adapter(List<SliderModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.slider_row,container,false);
        ImageView image = view.findViewById(R.id.image);
        ProgressBar progressBar = view.findViewById(R.id.progBar);
        SliderModel.Data sliderModel = list.get(position);
//        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        String path = Tags.IMAGE_URL+sliderModel.getImage();
        Picasso.get().load(Uri.parse(path)).fit().into(image, new Callback() {
            @Override
            public void onSuccess() {
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }


        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
