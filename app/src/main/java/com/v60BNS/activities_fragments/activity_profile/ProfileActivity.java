package com.v60BNS.activities_fragments.activity_profile;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_chat.ChatActivity;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.activities_fragments.activity_orders.OrdersActivity;
import com.v60BNS.activities_fragments.activity_setting.SettingsActivity;
import com.v60BNS.adapters.Comments_Adapter;
import com.v60BNS.adapters.Post_Adapter;
import com.v60BNS.adapters.Replayes_Adapter;
import com.v60BNS.databinding.ActivityProfileBinding;
import com.v60BNS.databinding.FragmentProfileBinding;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.Comments_Model;
import com.v60BNS.models.MessageDataModel;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.PostModel;
import com.v60BNS.models.ReviewModels;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.share.Common;
import com.v60BNS.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.v60BNS.adapters.Post_Adapter.user_id;
import static com.v60BNS.adapters.Post_Adapter.phone;
import static com.v60BNS.tags.Tags.IMAGE_URL;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private List<PostModel.Data> postlist;
    public BottomSheetBehavior behavior;
    private RecyclerView recViewcomments;
    private ImageView imclose, imageshare;
    private Post_Adapter post_adapter;
    private List<ReviewModels.Reviews> reviewsList;
    private Comments_Adapter comments_adapter;
    private TextView tvcount;
    private CheckBox ch_like;
    private int position;
    private LinearLayoutManager manager;
    private int current_page = 1;
    private boolean isLoading = false;
    private List<Comments_Model.Data> dataList;
    private Replayes_Adapter replayes_adapter;

    public static ProfileActivity newInstance() {

        return new ProfileActivity();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        initView();
        getprofile();
        getPosts();
    }


    private void initView() {

        postlist = new ArrayList<>();
        reviewsList = new ArrayList<>();
        dataList = new ArrayList<>();
        preferences = Preferences.getInstance();
        replayes_adapter = new Replayes_Adapter(dataList, this);

        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        recViewcomments = binding.getRoot().findViewById(R.id.recViewcomments);
        tvcount = binding.getRoot().findViewById(R.id.tvcount);
        imclose = binding.getRoot().findViewById(R.id.imclose);
        ch_like = binding.getRoot().findViewById(R.id.chelike);
        imageshare = binding.getRoot().findViewById(R.id.imageshare);

        post_adapter = new Post_Adapter(postlist, this);
        manager = new LinearLayoutManager(this);
        binding.recViewFavoriteOffers.setLayoutManager(manager);
        binding.recViewFavoriteOffers.setAdapter(post_adapter);
        comments_adapter = new Comments_Adapter(reviewsList, this);
        recViewcomments.setLayoutManager(new LinearLayoutManager(this));
        recViewcomments.setAdapter(comments_adapter);

        imageshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(position);
            }
        });
        ch_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    like_dislike(position);
                } else {
                    recViewcomments = binding.getRoot().findViewById(R.id.recViewcomments);
                }
            }
        });
        if (lang.equals("ar")) {
            imclose.setRotation(180);
        }
        setUpBottomSheet();
     /*   binding.recViewFavoriteOffers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    int lastItemPos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items = post_adapter.getItemCount();

                    if (total_items > 5 && (total_items - lastItemPos) == 1 && !isLoading) {
                        isLoading = true;
                        postlist.add(postlist.size(), null);
                        post_adapter.notifyItemInserted(postlist.size() - 1);
                        int next_page = current_page + 1;
                        //loadMore(next_page);


                    }
                }
            }
        });*/
        binding.llorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpBottomSheet() {

        behavior = BottomSheetBehavior.from(binding.getRoot().findViewById(R.id.root));

    }

    public void getPosts() {
        binding.progBarOffer.setVisibility(View.VISIBLE);
        try {
            int uid=user_id;


            Api.getService(Tags.base_url).
                    getOtherUserposts("Bearer " + userModel.getToken(), uid + "").
                    enqueue(new Callback<PostModel>() {
                        @Override
                        public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                            binding.progBarOffer.setVisibility(View.GONE);

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                postlist.clear();
                                postlist.addAll(response.body().getData());
                                if (postlist.size() > 0) {
                                    post_adapter.notifyDataSetChanged();
                                } else {

                                }

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(ProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<PostModel> call, Throwable t) {
                            binding.progBarOffer.setVisibility(View.GONE);
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ProfileActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }


                        }
                    });
        } catch (Exception e) {

        }


    }
    public void Addcomment(String comment, int postid, int layoutPosition) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .AddComment("Bearer " + userModel.getToken(), postid + "", comment)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                getcomment(postid, layoutPosition);

                                // Toast.makeText(HomeActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                //  e.printStackTrace();
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

  /*  private void loadMore(int next_page) {
        try {
           int uid=user_id;
            Api.getService(Tags.base_url).
                    getOtherUserposts("Bearer " + userModel.getToken(), "on", uid + "", next_page).
                    enqueue(new Callback<PostModel>() {
                        @Override
                        public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                            isLoading = false;
                            postlist.remove(postlist.size() - 1);
                            post_adapter.notifyItemRemoved(postlist.size() - 1);
                            if (response.isSuccessful() && response.body() != null) {

                                current_page = response.body().getCurrent_page();
                                postlist.addAll(response.body().getData());
                                post_adapter.notifyDataSetChanged();

                            } else {

                                if (response.code() == 500) {
                                    Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<PostModel> call, Throwable t) {
                            try {
                                isLoading = false;

                                if (postlist.get(postlist.size() - 1) == null) {
                                    postlist.remove(postlist.size() - 1);
                                    post_adapter.notifyItemRemoved(postlist.size() - 1);
                                }
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ProfileActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }
*/
    public void getPlaceDetails(String placeid, int position) {
        ch_like.setChecked(false);

        if (postlist.get(position).isLove_check()) {
            ch_like.setChecked(true);
        }
        recViewcomments.setAdapter(comments_adapter);
        reviewsList.clear();
        comments_adapter.notifyDataSetChanged();
        ProgressDialog dialog = Common.createProgressDialog(ProfileActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        this.position = position;
        // dialog.show();


        Api.getService("https://maps.googleapis.com/maps/api/")
                .getPlaceReview(placeid, getString(R.string.map_api_key), "ar")
                .enqueue(new Callback<NearbyStoreDataModel>() {
                    @Override
                    public void onResponse(Call<NearbyStoreDataModel> call, Response<NearbyStoreDataModel> response) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        dialog.dismiss();

                        if (response.isSuccessful() && response.body() != null && response.body().getResult().getReviews() != null) {
                            //  Log.e(";;;", response.body().getResult().getReviews().get(0).getAuthor_name());
                            //    Log.e("dddddata", response.body().getResult().getReviews().size() + "");

                            reviewsList.addAll(response.body().getResult().getReviews());
                            comments_adapter.notifyDataSetChanged();
                            tvcount.setText(response.body().getResult().getReviews().size() + "");
                        } else {
                            Log.e("dddddata", response.code() + "");
                            Toast.makeText(ProfileActivity.this, ProfileActivity.this.getResources().getString(R.string.no_data_found), Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyStoreDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public int like_dislike(int pos) {

        Log.e("ggggggg",userModel.getId()+"----");
        if (userModel != null) {
            try {
                Api.getService(Tags.base_url)
                        .likepost("Bearer " + userModel.getToken(), postlist.get(pos).getId() + "")
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {

                                    getPosts();
                                } else {


                                    if (response.code() == 500) {
                                        Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(ProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                        try {

                                            Log.e("error", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                try {

                                    if (t.getMessage() != null) {
                                        Log.e("error", t.getMessage());
                                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                            Toast.makeText(ProfileActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
            } catch (Exception e) {

            }
            return 1;

        } else {

            Common.CreateDialogAlert2(ProfileActivity.this, getString(R.string.please_sign_in_or_sign_up));
            return 0;

        }
    }



    public void share(int position) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, postlist.get(position).getLink_for_share());
        startActivity(intent);
    }

    public void getprofile() {
        ProgressDialog dialog = Common.createProgressDialog(ProfileActivity.this, ProfileActivity.this.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
//        Log.e("llkkkk", "Bearer " + userModel.getToken());
        if (userModel != null) {

            Log.e("nnnn",phone+"______");
            try {
                Api.getService(Tags.base_url)
                        .getUserprofile(phone)
                        .enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                dialog.dismiss();
                                if (response.isSuccessful()) {
                                    Log.e("ccccc",response.body().getOrders_count()+"----");
                                    binding.tvPosts.setText(response.body().getOrders_count()+"");
                                    binding.tvPosts.setText(response.body().getPosts_count()+"");
                                    binding.tvName.setText(response.body().getName()+"");

                                    Picasso.get().load(IMAGE_URL + response.body().getLogo()).into(binding.image);
                                    Picasso.get().load(IMAGE_URL + response.body().getBanner()).placeholder(R.drawable.ic_avatar).into(binding.fl);

                                } else {


                                    if (response.code() == 500) {
                                        Toast.makeText(ProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(ProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                        try {

                                            Log.e("error", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {
                                try {
                                    dialog.dismiss();
                                    if (t.getMessage() != null) {
                                        Log.e("error", t.getMessage());
                                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                            Toast.makeText(ProfileActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
            } catch (Exception e) {

            }
            //return 1;

        } else {

            Common.CreateDialogAlert2(ProfileActivity.this, getString(R.string.please_sign_in_or_sign_up));

        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (userModel != null) {
            getprofile();
            getPosts();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getPosts();
            getprofile();
        }
    }

    public void getcomment(int id, int position) {
        recViewcomments.setAdapter(replayes_adapter);
        ch_like.setChecked(false);

        if (postlist.get(position).isLove_check()) {
            ch_like.setChecked(true);
        }
        dataList.clear();
        replayes_adapter.notifyDataSetChanged();
        ProgressDialog dialog = Common.createProgressDialog(ProfileActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        this.position = position;
        dialog.show();

        Api.getService(Tags.base_url)
                .getComment("Bearer " + userModel.getToken(), id + "")
                .enqueue(new Callback<Comments_Model>() {
                    @Override
                    public void onResponse(Call<Comments_Model> call, Response<Comments_Model> response) {
                        dialog.dismiss();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {


                            dataList.addAll(response.body().getData());
                            replayes_adapter.notifyDataSetChanged();
                            Log.e("mmmmmmmmmm",response.body().getData().size()+"--");
                            tvcount.setText(response.body().getData().size()+"");
                        } else {
                            Log.e("dddddatassss", response.code() + "" + response.body());
                            tvcount.setText("0" + "");
                            // Toast.makeText(activity, activity.getResources().getString(R.string.no_data_found), Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<Comments_Model> call, Throwable t) {
                        try {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

}
