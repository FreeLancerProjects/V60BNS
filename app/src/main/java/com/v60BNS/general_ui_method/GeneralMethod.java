package com.v60BNS.general_ui_method;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.v60BNS.R;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.v60BNS.tags.Tags.IMAGE_Places_URL;
import static com.v60BNS.tags.Tags.IMAGE_URL;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }


    @BindingAdapter("image")
    public static void image(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_Places_URL + endPoint)).placeholder(R.drawable.ic_avatar).into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_Places_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_Places_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        }

    }

    @BindingAdapter("image3")
    public static void image3(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null && endPoint.equals("0")) {
                Picasso.get().load(IMAGE_URL + Preferences.getInstance().getUserData(view.getContext()).getLogo()).into(imageView);

            } else if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).into(imageView);
            } else {

                Log.e("mmmm","اوعى الصورة جاية ب نل يا سعدون يا كوارى");
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;
            if (endPoint != null && endPoint.equals("0")) {
                Picasso.get().load(IMAGE_URL + Preferences.getInstance().getUserData(view.getContext()).getLogo()).into(imageView);

            } else if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null && endPoint.equals("0")) {
                Picasso.get().load(IMAGE_URL + Preferences.getInstance().getUserData(view.getContext()).getLogo()).into(imageView);

            } else if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        }

    }

    @BindingAdapter("image2")
    public static void image2(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null && endPoint.equals("0")) {
                Picasso.get().load(IMAGE_URL + Preferences.getInstance().getUserData(view.getContext()).getLogo()).placeholder(R.drawable.ic_avatar).into(imageView);

            } else if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;
            if (endPoint != null && endPoint.equals("0")) {
                Picasso.get().load(IMAGE_URL + Preferences.getInstance().getUserData(view.getContext()).getLogo()).placeholder(R.drawable.ic_avatar).into(imageView);

            } else if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null && endPoint.equals("0")) {
                Picasso.get().load(IMAGE_URL + Preferences.getInstance().getUserData(view.getContext()).getLogo()).placeholder(R.drawable.ic_avatar).into(imageView);

            } else if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        }

    }
    @BindingAdapter("rate")
    public static void rate(SimpleRatingBar simpleRatingBar, double rate) {
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setRatingTarget((float) rate)
                .setDuration(1000)
                .setRepeatCount(0)
                .setInterpolator(new LinearInterpolator());
        builder.start();
    }
    @BindingAdapter({"date"})
    public static void displayDate (TextView textView,long date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MMM", Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));

        textView.setText(String.format("%s",m_date));

    }
}










