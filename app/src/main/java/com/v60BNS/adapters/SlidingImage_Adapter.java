package com.v60BNS.adapters;


import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.v60BNS.R;
import com.v60BNS.models.SingleProductModel;
import com.v60BNS.models.SliderModel;
import com.v60BNS.tags.Tags;

import java.util.List;


public class SlidingImage_Adapter extends PagerAdapter {
    List<SingleProductModel.ProductImage> IMAGES;
    private LayoutInflater inflater;
    Context context;

    public SlidingImage_Adapter(Context context, List<SingleProductModel.ProductImage> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slider_row, view, false);
        ImageView image = imageLayout.findViewById(R.id.image);
        ProgressBar progressBar = imageLayout.findViewById(R.id.progBar);
        SingleProductModel.ProductImage sliderModel = IMAGES.get(position);
//        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        String path = Tags.IMAGE_URL + sliderModel.getImage();
        Picasso.get().load(Uri.parse(path)).fit().into(image);
        view.addView(imageLayout.getRootView());

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
