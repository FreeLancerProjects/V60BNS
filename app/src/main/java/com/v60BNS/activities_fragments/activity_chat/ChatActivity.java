package com.v60BNS.activities_fragments.activity_chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.v60BNS.R;
import com.v60BNS.adapters.Chat_Adapter;
import com.v60BNS.databinding.ActivityChatBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.ChatUserModel;
import com.v60BNS.models.ExpertModel;
import com.v60BNS.models.MessageDataModel;
import com.v60BNS.models.MessageModel;
import com.v60BNS.models.RoomModelID;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.yourcard.Animate.CircleAnimationUtil;

public class ChatActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatBinding binding;
    private String lang;
    private ExpertModel.Data expertData;
    private Preferences preferences;
    private UserModel userModel;
    private Chat_Adapter chat_adapter;
    private List<MessageModel> messagedatalist;
    private LinearLayoutManager manager;
    private int current_page = 1;
    private boolean isLoading = false;
    private ChatUserModel chatUserModel;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        getdatafrominent();
        YoYo.with(Techniques.ZoomIn)
                .duration(900)
                .repeat(0)
                .playOn(binding.getRoot());
        initView();


    }

    private void getdatafrominent() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("data") != null) {
            expertData = (ExpertModel.Data) intent.getSerializableExtra("data");
        }
    }

    public void getchatroom() {
        if (userModel != null) {
            try {
                Api.getService(Tags.base_url)
                        .getchatroom("Bearer " + userModel.getToken(), userModel.getId() + "", expertData.getId() + "")
                        .enqueue(new Callback<RoomModelID>() {
                            @Override
                            public void onResponse(Call<RoomModelID> call, Response<RoomModelID> response) {
                                Log.e("dlldldl", response.code() + "");
                                if (response.isSuccessful()) {

                                    getChatMessages(response.body());           //   updateprofile(response.body());
                                } else {


                                    if (response.code() == 500) {
                                        Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                        try {

                                            Log.e("error", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RoomModelID> call, Throwable t) {
                                try {

                                    if (t.getMessage() != null) {
                                        Log.e("error", t.getMessage());
                                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                            Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
            } catch (Exception e) {
                Log.e("ldldldl", e.toString());
            }
            //return 1;


        } else {

            Common.CreateDialogAlert(this, getString(R.string.please_sign_in_or_sign_up));

        }
    }


    private void initView() {

        Paper.init(this);
        messagedatalist = new ArrayList<>();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setName(expertData.getName());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        chatUserModel = new ChatUserModel(expertData.getName(), expertData.getLogo(), expertData.getId(), 0);
        getchatroom();
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager=new LinearLayoutManager(this);
        chat_adapter = new Chat_Adapter(messagedatalist, userModel.getId(), chatUserModel.getImage(), this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setLayoutManager(manager);
        // binding.llMsgContainer.setVisibility(View.GONE);
        binding.recView.setAdapter(chat_adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    int lastItemPos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items = chat_adapter.getItemCount();

                    if (lastItemPos == (total_items - 2) && !isLoading) {
                        isLoading = true;
                        messagedatalist.add(0, null);
                        chat_adapter.notifyItemInserted(0);
                        int next_page = current_page + 1;
                        loadMore(next_page);


                    }
                }
            }
        });
    }


    @Override
    public void back() {
//        if (isDataAdded) {
//            setResult(RESULT_OK);
//        }
        finish();
    }


    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
//            createOrderModel = preferences.getCartData(this);
//            if (createOrderModel == null) {
//                createOrderModel = new CreateOrderModel();
//                createOrderModel.setMarkter_id(market.getId());
//                binding.setCartCount(0);
//                isDataAdded = true;
//
//            } else {
//
//                binding.setCartCount(createOrderModel.getProducts().size());
//            }
//        }
        }
    }


    private void getChatMessages(RoomModelID roomModelID) {
        Log.e("dlldld", roomModelID.getRoom_id() + "");
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getRoomMessages("Bearer "+userModel.getToken(), roomModelID.getRoom_id(), 1)
                    .enqueue(new Callback<MessageDataModel>() {
                        @Override
                        public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                chatUserModel = new ChatUserModel(expertData.getName(), expertData.getLogo(), expertData.getId(), roomModelID.getRoom_id());
                                preferences.create_update_ChatUserData(ChatActivity.this, chatUserModel);

                                messagedatalist.clear();
                                messagedatalist.addAll(response.body().getMessages().getData());
                                chat_adapter.notifyDataSetChanged();
                                scrollToLastPosition();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDataModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

            Api.getService(Tags.base_url)
                    .getRoomMessages("Bearer "+userModel.getToken(), chatUserModel.getRoom_id(), next_page)
                    .enqueue(new Callback<MessageDataModel>() {
                        @Override
                        public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                            isLoading = false;
                            messagedatalist.remove(0);
                            chat_adapter.notifyItemRemoved(0);
                            if (response.isSuccessful() && response.body() != null) {

                                current_page = response.body().getMessages().getCurrent_page();
                                messagedatalist.addAll(0, response.body().getMessages().getData());
                                chat_adapter.notifyItemRangeInserted(0, response.body().getMessages().getData().size());


                            } else {

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDataModel> call, Throwable t) {
                            try {
                                isLoading = false;

                                if (messagedatalist.get(0) == null) {
                                    messagedatalist.remove(0);
                                    chat_adapter.notifyItemRemoved(0);
                                }
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void scrollToLastPosition() {

        new Handler()
                .postDelayed(() -> binding.recView.scrollToPosition(messagedatalist.size() - 1), 10);
    }


}
