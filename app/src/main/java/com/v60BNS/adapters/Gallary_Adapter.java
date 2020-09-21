package com.v60BNS.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.v60BNS.R;
import com.v60BNS.databinding.GallaryRowBinding;
import com.v60BNS.models.Model_images;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Gallary_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Model_images> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = -1;

    public Gallary_Adapter(List<Model_images> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        GallaryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.gallary_row, parent, false);
        return new EventsHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        EventsHolder msgRightHolder = (EventsHolder) holder;
        Bitmap myBitmap = BitmapFactory.decodeFile(orderlist.get(position).getAl_imagepath().get(0));

        Picasso.get().load( getUriFromBitmap(myBitmap)).placeholder(R.drawable.post).into(msgRightHolder.binding.image);


    }
    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            //  Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            //Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }


    public class EventsHolder extends RecyclerView.ViewHolder {
        public GallaryRowBinding binding;

        public EventsHolder(@NonNull GallaryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}