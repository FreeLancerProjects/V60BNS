package com.v60BNS.adapters;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.v60BNS.R;
import com.v60BNS.models.Model_images;
import com.v60BNS.models.images_model;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class Add_Post_Adapter extends ArrayAdapter<images_model> {

    Context context;
    ViewHolder viewHolder;
    ArrayList<Model_images> al_menu = new ArrayList<>();


    public Add_Post_Adapter(Context context, ArrayList<Model_images> al_menu) {
        super(context, R.layout.image_row);
        this.al_menu = al_menu;
        this.context = context;


    }

    @Override
    public int getCount() {

        Log.e("ADAPTER LIST SIZE", al_menu.size() + "");
        return al_menu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.size() > 0) {
            return al_menu.size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_row, parent, false);
            viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.tv_folder);
            viewHolder.tv_foldersize = (TextView) convertView.findViewById(R.id.tv_folder2);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_foldern.setText(al_menu.get(position).getStr_folder());
            viewHolder.tv_foldersize.setText(al_menu.get(position).getAl_imagepath().size()+"");


        Bitmap myBitmap = BitmapFactory.decodeFile(al_menu.get(position).getAl_imagepath().get(0));

        Glide.with(context).load(getUriFromBitmap(myBitmap)).placeholder(R.drawable.post)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.iv_image);


        return convertView;




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


    private static class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;


    }


}
