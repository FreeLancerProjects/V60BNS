package com.v60BNS.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_chat.ChatActivity;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.adapters.Room_Adapter;
import com.v60BNS.adapters.StarComments_Adapter;
import com.v60BNS.databinding.FragmentCommentsBinding;
import com.v60BNS.models.ChatUserModel;
import com.v60BNS.models.ExpertModel;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.models.UserRoomModelData;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.share.Common;
import com.v60BNS.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Comments extends Fragment {

    private HomeActivity activity;
    private FragmentCommentsBinding binding;
    private List<ExpertModel.Data> dataList;
    private StarComments_Adapter starComments_adapter;
    private Preferences preferences;
    private UserModel userModel;
    private List<UserRoomModelData.UserRoomModel> userRoomModelList;
    private Room_Adapter room_adapter;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    private int current_page = 1;

    public static Fragment_Comments newInstance() {
        return new Fragment_Comments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false);
        initView();


        return binding.getRoot();
    }

    private void initView() {
        dataList = new ArrayList<>();
        userRoomModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        starComments_adapter = new StarComments_Adapter(dataList, activity, this);
        room_adapter = new Room_Adapter(activity, userRoomModelList, this);
        binding.recViewFavoriteOffers.setLayoutManager(new LinearLayoutManager(activity));
        binding.progBarexpert.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        if (userModel.getUser_type().equals("client")) {
            binding.recViewFavoriteOffers.setAdapter(starComments_adapter);
            binding.tv.setVisibility(View.GONE);

            getExpertusers();
        } else if (userModel.getIs_accepted().equals("accepted")) {
            binding.recViewFavoriteOffers.setAdapter(room_adapter);
            binding.tv.setVisibility(View.GONE);
            getRooms();
        } else {
            binding.progBarexpert.setVisibility(View.GONE);
            binding.tv.setVisibility(View.VISIBLE);
        }
        if (userModel.getUser_type().equals("client")) {
            binding.recViewFavoriteOffers.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        int total_items = room_adapter.getItemCount();
                        int lastItemPos = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                        if (total_items >= 6 && (lastItemPos == total_items - 2) && !isLoading) {
                            isLoading = true;
                            userRoomModelList.add(null);
                            room_adapter.notifyItemInserted(userRoomModelList.size() - 1);
                            int page = current_page + 1;
                            loadMore(page);
                        }

                    }
                }
            });
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void showchat(ExpertModel.Data data) {
        if (userModel != null) {
            Intent intent = new Intent(activity, ChatActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        } else {
            Common.CreateDialogAlert2(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
        }
    }

    public void setItemData(UserRoomModelData.UserRoomModel userRoomModel, int adapterPosition) {

        userRoomModel.setMy_message_unread_count(0);
        userRoomModelList.set(adapterPosition, userRoomModel);
        room_adapter.notifyItemChanged(adapterPosition);
        ChatUserModel chatUserModel = new ChatUserModel(userRoomModel.getOther_user_name(), userRoomModel.getOther_user_avatar(), userRoomModel.getFirst_user_id(), userRoomModel.getId());
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("chat_user_data", chatUserModel);
        startActivityForResult(intent, 1000);
    }

    public void getExpertusers() {

        try {
            int uid;
            binding.progBarexpert.setVisibility(View.VISIBLE);


            Api.getService(Tags.base_url).
                    getExperts("off").
                    enqueue(new Callback<ExpertModel>() {
                        @Override
                        public void onResponse(Call<ExpertModel> call, Response<ExpertModel> response) {
                            binding.progBarexpert.setVisibility(View.GONE);

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                dataList.clear();

                                dataList.addAll(response.body().getData());
                                if (dataList.size() > 0) {
                                    starComments_adapter.notifyDataSetChanged();
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
                        public void onFailure(Call<ExpertModel> call, Throwable t) {
                            binding.progBarexpert.setVisibility(View.GONE);
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

    public void getRooms() {
        userModel = preferences.getUserData(activity);
        binding.progBarexpert.setVisibility(View.VISIBLE);
        try {
            Api.getService(Tags.base_url)
                    .getRooms("Bearer " + userModel.getToken(), userModel.getId(), 1)
                    .enqueue(new Callback<UserRoomModelData>() {
                        @Override
                        public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                            binding.progBarexpert.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                userRoomModelList.clear();
                                userRoomModelList.addAll(response.body().getData());
                                if (userRoomModelList.size() > 0) {
                                    room_adapter.notifyDataSetChanged();
                                    // binding..setVisibility(View.GONE);
                                } else {
                                    //    binding.tvNoConversation.setVisibility(View.VISIBLE);

                                }
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
                        public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                            try {
                                binding.progBarexpert.setVisibility(View.GONE);
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
            Log.e("eeee", e.getMessage() + "__");
            binding.progBarexpert.setVisibility(View.GONE);
        }
    }


    private void loadMore(int page) {
        try {

            Api.getService(Tags.base_url)
                    .getRooms("Bearer " + userModel.getToken(), userModel.getId(), page)
                    .enqueue(new Callback<UserRoomModelData>() {
                        @Override
                        public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                            userRoomModelList.remove(userRoomModelList.size() - 1);
                            room_adapter.notifyItemRemoved(userRoomModelList.size() - 1);
                            isLoading = false;

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                userRoomModelList.addAll(response.body().getData());
                                room_adapter.notifyDataSetChanged();
                                current_page = response.body().getCurrent_page();
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
                        public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                            try {
                                if (userRoomModelList.get(userRoomModelList.size() - 1) == null) {
                                    userRoomModelList.remove(userRoomModelList.size() - 1);
                                    room_adapter.notifyItemRemoved(userRoomModelList.size() - 1);
                                    isLoading = false;
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
            binding.progBarexpert.setVisibility(View.GONE);
        }
    }
}
