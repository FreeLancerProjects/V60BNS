package com.v60BNS.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_full_image.FullImageActivity;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Main;
import com.v60BNS.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.v60BNS.activities_fragments.activity_profile.ProfileActivity;
import com.v60BNS.databinding.PostRowBinding;
import com.v60BNS.models.PostModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.share.Common;
import java.util.List;
import java.util.Locale;
import io.paperdb.Paper;

import static java.security.AccessController.getContext;

public class Post_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PostModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = -1;
    private Fragment fragment;
    private ProfileActivity activity;
    private Preferences preferences;
    private UserModel userModel;
    public static String phone;
    public static int user_id;

    public Post_Adapter(List<PostModel.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.fragment = fragment;
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(context);
    }

    public Post_Adapter(List<PostModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (ProfileActivity)context;
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        PostRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.post_row, parent, false);
        return new EventsHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        EventsHolder msgRightHolder = (EventsHolder) holder;
        msgRightHolder.binding.setModel(orderlist.get(position));
        msgRightHolder.binding.setLang(lang);


//        Liked_Adapter comments_adapter = new Liked_Adapter(orderlist, context);
//        msgRightHolder.binding.recliked.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
//        msgRightHolder.binding.recliked.setAdapter(comments_adapter);


        msgRightHolder.binding.tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("xxxxxxxx",orderlist.get(msgRightHolder.getLayoutPosition()).getLatitude()+" "+ orderlist.get(msgRightHolder.getLayoutPosition()).getLongitude());
                Uri navigationIntentUri = Uri.parse("google.navigation:q=" + orderlist.get(msgRightHolder.getLayoutPosition()).getLatitude() + "," + orderlist.get(msgRightHolder.getLayoutPosition()).getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
        msgRightHolder.binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("imageUri", orderlist.get(msgRightHolder.getLayoutPosition()).getImage());
                intent.putExtra("title", orderlist.get(msgRightHolder.getLayoutPosition()).getUser().getName());

                context.startActivity(intent);
            }
        });
        msgRightHolder.binding.imagegoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof Fragment_Main) {
                    Fragment_Main fragment_main = (Fragment_Main) fragment;
                    fragment_main.getPlaceDetails(orderlist.get(msgRightHolder.getLayoutPosition()).getPlace_id(), msgRightHolder.getLayoutPosition());
                } else if (fragment instanceof Fragment_Profile) {
                    Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                    fragment_profile.getPlaceDetails(orderlist.get(msgRightHolder.getLayoutPosition()).getPlace_id(), msgRightHolder.getLayoutPosition());
                }else if  (context instanceof ProfileActivity){
                    activity.getPlaceDetails(orderlist.get(msgRightHolder.getLayoutPosition()).getPlace_id(), msgRightHolder.getLayoutPosition());

                }
            }
        });
        msgRightHolder.binding.imagecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dlldldldls", msgRightHolder.getLayoutPosition() + "");

                if (fragment instanceof Fragment_Main) {
                    Fragment_Main fragment_main = (Fragment_Main) fragment;
                    fragment_main.getcomment(orderlist.get(msgRightHolder.getLayoutPosition()).getId(), msgRightHolder.getLayoutPosition());
                } else if (fragment instanceof Fragment_Profile) {
                    Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                    fragment_profile.getcomment(orderlist.get(msgRightHolder.getLayoutPosition()).getId(), msgRightHolder.getLayoutPosition());
                }
                else  {
                    activity.getcomment(orderlist.get(msgRightHolder.getLayoutPosition()).getId(), msgRightHolder.getLayoutPosition());
                }
            }
        });
        msgRightHolder.binding.imagelike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    if (fragment instanceof Fragment_Main) {
                        Fragment_Main fragment_main = (Fragment_Main) fragment;
                        fragment_main.like_dislike(msgRightHolder.getLayoutPosition());
                    } else if (fragment instanceof Fragment_Profile) {
                        Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                        fragment_profile.like_dislike(msgRightHolder.getLayoutPosition());
                    }else if (context instanceof ProfileActivity){
                        activity.like_dislike(msgRightHolder.getLayoutPosition());

                    }
                } else {
                    i = position;
                    notifyDataSetChanged();

                }
            }
        });

        msgRightHolder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {

                        Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                        fragment_profile.deletePost(msgRightHolder.getLayoutPosition());

                } else {
                    i = position;
                    notifyDataSetChanged();

                }
            }
        });
        msgRightHolder.binding.imageshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof Fragment_Main) {
                    Fragment_Main fragment_main = (Fragment_Main) fragment;
                    fragment_main.share(msgRightHolder.getLayoutPosition());
                } else if (fragment instanceof Fragment_Profile) {
                    Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                    fragment_profile.share(msgRightHolder.getLayoutPosition());
                }else if (context instanceof ProfileActivity){
                    activity.share(msgRightHolder.getLayoutPosition());

                }

            }
        });
    msgRightHolder.binding.imageSend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String query = msgRightHolder.binding.edtcomment.getText().toString();

                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(context, msgRightHolder.binding.edtcomment);
                    msgRightHolder.binding.edtcomment.setText("");
                    if (context instanceof HomeActivity) {
                        HomeActivity homeActivity = (HomeActivity) context;
                        homeActivity.Addcomment(query, orderlist.get(msgRightHolder.getLayoutPosition()).getId(), msgRightHolder.getLayoutPosition());
                    } else if (context instanceof ProfileActivity) {
                        activity.Addcomment(query, orderlist.get(msgRightHolder.getLayoutPosition()).getId(), msgRightHolder.getLayoutPosition());

                    }
                }else {
                    msgRightHolder.binding.edtcomment.setError(context.getResources().getString(R.string.field_req));
                }

        }
    });
        if (i == position && userModel == null) {
            msgRightHolder.binding.imagelike.setChecked(false);
            Common.CreateDialogAlert(context, context.getResources().
                    getString(R.string.please_sign_in_or_sign_up));

        }
        if (fragment instanceof Fragment_Profile) {
            msgRightHolder.binding.imgDelete.setVisibility(View.VISIBLE);
        } else {
            msgRightHolder.binding.imgDelete.setVisibility(View.GONE);

        }
        msgRightHolder.binding.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id=orderlist.get(msgRightHolder.getLayoutPosition()).getUser_id();
                phone=orderlist.get(msgRightHolder.getLayoutPosition()).getUser().getPhone();

                Intent intent=new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
            }
        });
        msgRightHolder.binding.rImSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id=orderlist.get(msgRightHolder.getLayoutPosition()).getUser_id();
                phone=orderlist.get(msgRightHolder.getLayoutPosition()).getUser().getPhone();

                Intent intent=new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }


    public class EventsHolder extends RecyclerView.ViewHolder {
        public PostRowBinding binding;

        public EventsHolder(@NonNull PostRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}