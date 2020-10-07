package com.v60BNS.activities_fragments.activity_home.fragments;


import android.app.ProgressDialog;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_chat.ChatActivity;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.activities_fragments.activity_orders.OrdersActivity;
import com.v60BNS.activities_fragments.activity_setting.SettingsActivity;
import com.v60BNS.adapters.Comments_Adapter;
import com.v60BNS.adapters.Post_Adapter;
import com.v60BNS.adapters.Replayes_Adapter;
import com.v60BNS.databinding.FragmentProfileBinding;
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

public class Fragment_Profile extends Fragment {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
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
    public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getprofile();
        getPosts();

    }

    private void initView() {

        postlist = new ArrayList<>();
        reviewsList = new ArrayList<>();
        dataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        replayes_adapter = new Replayes_Adapter(dataList, activity);

        userModel = preferences.getUserData(activity);
        Log.e("kdkfjjfj",Tags.IMAGE_URL+userModel.getBanner());
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        recViewcomments = binding.getRoot().findViewById(R.id.recViewcomments);
        tvcount = binding.getRoot().findViewById(R.id.tvcount);
        imclose = binding.getRoot().findViewById(R.id.imclose);
        ch_like = binding.getRoot().findViewById(R.id.chelike);
        imageshare = binding.getRoot().findViewById(R.id.imageshare);

        post_adapter = new Post_Adapter(postlist, activity, this);
        manager = new LinearLayoutManager(activity);
        binding.recViewFavoriteOffers.setLayoutManager(manager);
        binding.recViewFavoriteOffers.setAdapter(post_adapter);
        comments_adapter = new Comments_Adapter(reviewsList, activity);
        recViewcomments.setLayoutManager(new LinearLayoutManager(activity));
        recViewcomments.setAdapter(comments_adapter);
        binding.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                startActivityForResult(intent, 1);
            }
        });
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
        binding.recViewFavoriteOffers.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        loadMore(next_page);


                    }
                }
            }
        });
        binding.llorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, OrdersActivity.class);
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
            int uid;
            if (userModel != null) {
                uid = userModel.getId();
            } else {
                uid = 0;
            }

            Api.getService(Tags.base_url).
                    getmyposts("Bearer " + userModel.getToken(), "on", uid + "", 1).
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
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }


                        }
                    });
        } catch (Exception e) {

        }


    }

    private void loadMore(int next_page) {
        try {
            int uid;
            if (userModel != null) {
                uid = userModel.getId();
            } else {
                uid = 0;
            }
            Api.getService(Tags.base_url).
                    getmyposts("Bearer " + userModel.getToken(), "on", uid + "", next_page).
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
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    public void getPlaceDetails(String placeid, int position) {
        ch_like.setChecked(false);

        if (postlist.get(position).isLove_check()) {
            ch_like.setChecked(true);
        }
        recViewcomments.setAdapter(comments_adapter);
        reviewsList.clear();
        comments_adapter.notifyDataSetChanged();
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        this.position = position;
        // dialog.show();


        Api.getService("https://maps.googleapis.com/maps/api/")
                .getPlaceReview(placeid, getString(R.string.map_api_key), "ar")
                .enqueue(new Callback<NearbyStoreDataModel>() {
                    @Override
                    public void onResponse(Call<NearbyStoreDataModel> call, Response<NearbyStoreDataModel> response) {
                        dialog.dismiss();

                        if (response.isSuccessful() && response.body() != null && response.body().getResult().getReviews() != null) {
                            //  Log.e(";;;", response.body().getResult().getReviews().get(0).getAuthor_name());
                            //    Log.e("dddddata", response.body().getResult().getReviews().size() + "");
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            reviewsList.addAll(response.body().getResult().getReviews());
                            comments_adapter.notifyDataSetChanged();
                            tvcount.setText(response.body().getResult().getReviews().size() + "");
                        } else {
                            Log.e("dddddata", response.code() + "");
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_found), Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyStoreDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public int like_dislike(int pos) {
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
                                        Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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

            Common.CreateDialogAlert2(activity, getString(R.string.please_sign_in_or_sign_up));
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
        ProgressDialog dialog = Common.createProgressDialog(activity, activity.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
//        Log.e("llkkkk", "Bearer " + userModel.getToken());
        if (userModel != null) {
            try {
                Api.getService(Tags.base_url)
                        .getprofile("Bearer " + userModel.getToken())
                        .enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                dialog.dismiss();
                                if (response.isSuccessful()) {

                                    updateprofile(response.body());
                                } else {


                                    if (response.code() == 500) {
                                        Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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

            Common.CreateDialogAlert2(activity, getString(R.string.please_sign_in_or_sign_up));

        }
    }

    private void updateprofile(UserModel body) {
        Log.e("a;lallalal", body.getBanner());
        body.setToken(userModel.getToken());
        userModel = body;
        preferences.create_update_userdata(activity, userModel);
        binding.setModel(userModel);
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
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
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
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            dataList.addAll(response.body().getData());
                            replayes_adapter.notifyDataSetChanged();
                            tvcount.setText(response.body().getData().size());
                        } else {
                            Log.e("dddddatassss", response.code() + "" + response.body());
                            tvcount.setText("0" + "");
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_found), Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<Comments_Model> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

}
