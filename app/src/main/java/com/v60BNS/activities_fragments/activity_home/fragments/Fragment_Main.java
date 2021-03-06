package com.v60BNS.activities_fragments.activity_home.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.adapters.Comments_Adapter;
import com.v60BNS.adapters.Post_Adapter;
import com.v60BNS.adapters.Replayes_Adapter;
import com.v60BNS.adapters.Story_Adapter;
import com.v60BNS.databinding.FragmentMainBinding;
import com.v60BNS.models.Comments_Model;
import com.v60BNS.models.PostModel;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.NearbyModel;
import com.v60BNS.models.NearbyStoreDataModel;
import com.v60BNS.models.ReviewModels;
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
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;
    private String lang;
    private Post_Adapter post_adapter;
    private List<StoryModel.Data> storylist;
    private List<PostModel.Data> postlist;
    private Story_Adapter story_adapter;
    public BottomSheetBehavior behavior;
    private RecyclerView recViewcomments;
    private TextView tvcount;
    private List<ReviewModels.Reviews> reviewsList;
    private ImageView imclose, imshare;
    private CheckBox ch_like;
    private Comments_Adapter comments_adapter;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int READ_REQ = 2;
    private Uri uri;
    private UserModel userModel;
    private int pos;
    private List<Comments_Model.Data> dataList;
    private Replayes_Adapter replayes_adapter;

    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();
        getStories();
        getPosts();
        return binding.getRoot();
    }


    private void initView() {

        storylist = new ArrayList<>();
        reviewsList = new ArrayList<>();
        postlist = new ArrayList<>();
        dataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progpost.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        //binding.progBarStory.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recViewcomments = binding.getRoot().findViewById(R.id.recViewcomments);
        ch_like = binding.getRoot().findViewById(R.id.chelike);
        imclose = binding.getRoot().findViewById(R.id.imclose);
        imshare = binding.getRoot().findViewById(R.id.imageshare);

        tvcount = binding.getRoot().findViewById(R.id.tvcount);

        post_adapter = new Post_Adapter(postlist, activity, this);
        story_adapter = new Story_Adapter(storylist, activity, this);
        replayes_adapter = new Replayes_Adapter(dataList, activity);
        binding.recViewpost.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewpost.setDrawingCacheEnabled(true);
        binding.recViewpost.setItemViewCacheSize(20);
        binding.recViewpost.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewpost.setAdapter(post_adapter);
    /*    binding.recViewStatus.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewStatus.setDrawingCacheEnabled(true);
        binding.recViewStatus.setItemViewCacheSize(20);
        binding.recViewStatus.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewStatus.setAdapter(story_adapter);*/

        comments_adapter = new Comments_Adapter(reviewsList, activity);
        recViewcomments.setLayoutManager(new LinearLayoutManager(activity));
        recViewcomments.setAdapter(comments_adapter);
        if (lang.equals("ar")) {
            imclose.setRotation(180);
        }
        setUpBottomSheet();
        imshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(pos);
            }
        });
        ch_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    like_dislike(pos);
                } else {
                    ch_like.setChecked(false);
                    Common.CreateDialogAlert2(activity, getResources().getString(R.string.please_sign_in_or_sign_up));
                }
            }
        });
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStories();
                getPosts();

            }
        });
    }

    private void setUpBottomSheet() {

        behavior = BottomSheetBehavior.from(binding.getRoot().findViewById(R.id.root));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            uri = data.getData();
            addstoryimage();
            // File file = new File(Common.getImagePath(activity, uri));

        }

    }

    public void getPlaceDetails(String placeid, int pos) {
        ch_like.setChecked(false);

        if (postlist.get(pos).isLove_check()) {
            ch_like.setChecked(true);
        }
        recViewcomments.setAdapter(comments_adapter);
        reviewsList.clear();
        comments_adapter.notifyDataSetChanged();
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        this.pos = pos;
        // dialog.show();
        Log.e("sskksdkkd", placeid);

        Api.getService("https://maps.googleapis.com/maps/api/")
                .getPlaceReview(placeid, getString(R.string.map_api_key), "ar")
                .enqueue(new Callback<NearbyStoreDataModel>() {
                    @Override
                    public void onResponse(Call<NearbyStoreDataModel> call, Response<NearbyStoreDataModel> response) {
                        dialog.dismiss();
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        if (response.isSuccessful() && response.body() != null && response.body().getResult() != null && response.body().getResult().getReviews() != null) {
                            Log.e(";;;", response.body().getResult().getReviews().get(0).getAuthor_name());
                            Log.e("dddddata", response.body().getResult().getReviews().size() + "");

                            reviewsList.addAll(response.body().getResult().getReviews());
                            comments_adapter.notifyDataSetChanged();
                            tvcount.setText(response.body().getResult().getReviews().size() + "");
                        } else {
                            Log.e("dddddatassss", response.code() + "" + response.body());
                            tvcount.setText("0" + "");
                            //    Toast.makeText(activity, activity.getResources().getString(R.string.no_data_found), Toast.LENGTH_LONG).show();
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

    public void getStories() {

      /*  try {
            int uid;
            binding.progBarStory.setVisibility(View.VISIBLE);


            Api.getService(Tags.base_url).
                    getStories("off", "users").
                    enqueue(new Callback<StoryModel>() {
                        @Override
                        public void onResponse(Call<StoryModel> call, Response<StoryModel> response) {
                            binding.progBarStory.setVisibility(View.GONE);

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                storylist.clear();
                                if (userModel != null) {
                                    storylist.add(new StoryModel.Data());
                                }
                                storylist.addAll(response.body().getData());
                                if (storylist.size() > 0) {
                                    story_adapter.notifyDataSetChanged();
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
                        public void onFailure(Call<StoryModel> call, Throwable t) {
                            binding.progBarStory.setVisibility(View.GONE);
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

        }*/


    }

    public void getPosts() {
        binding.progpost.setVisibility(View.VISIBLE);
        try {
            String uid;
            if (userModel != null) {
                uid = userModel.getId() + "";
            } else {
                uid = "all";
            }

            Api.getService(Tags.base_url).
                    getposts("off", uid).
                    enqueue(new Callback<PostModel>() {
                        @Override
                        public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                            binding.progpost.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);
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
                            binding.progpost.setVisibility(View.GONE);

                            try {
                                binding.swipeRefresh.setRefreshing(false);

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
            binding.swipeRefresh.setRefreshing(false);

        }


    }

    public void addstory() {
        checkReadPermission();
    }

    public void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void addstoryimage() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        MultipartBody.Part image = Common.getMultiPart(activity, uri, "image");


        Api.getService(Tags.base_url)
                .addstory("Bearer " + userModel.getToken(), image)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            getStories();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
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

    public void share(int pos) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, postlist.get(pos).getLink_for_share());
        startActivity(intent);
    }

    public void getcomment(int id, int position) {
        if(userModel!=null){
        recViewcomments.setAdapter(replayes_adapter);
        ch_like.setChecked(false);

        if (postlist.get(pos).isLove_check()) {
            ch_like.setChecked(true);
        }
        dataList.clear();
        replayes_adapter.notifyDataSetChanged();
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        this.pos = position;
        dialog.show();

        Api.getService(Tags.base_url)
                .getComment("Bearer " + userModel.getToken(), id + "")
                .enqueue(new Callback<Comments_Model>() {
                    @Override
                    public void onResponse(Call<Comments_Model> call, Response<Comments_Model> response) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {

                            Log.e("dddddatassss", response.code() + "" + response.body().getData().size());

                            dataList.addAll(response.body().getData());
                            replayes_adapter.notifyDataSetChanged();

                            tvcount.setText(dataList.size() + "");
                        } else {
                            Log.e("dddddatassss", response.code() + "" + response.body());
                            tvcount.setText("0" + "");
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_found), Toast.LENGTH_LONG).show();
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
                });}
        else {
            Common.CreateDialogAlert2(activity,activity.getResources().getString(R.string.please_sign_in_or_sign_up));
        }
    }
}
