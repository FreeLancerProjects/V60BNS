package com.v60BNS.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_coffee_detials.CoffeeDetialsActivity;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.adapters.CategoryAdapter;
import com.v60BNS.adapters.Department_Adapter;
import com.v60BNS.adapters.Food_Adapter;
import com.v60BNS.adapters.ProductAdapter;
import com.v60BNS.adapters.Slider_Adapter;
import com.v60BNS.databinding.FragmentStoreBinding;
import com.v60BNS.models.CategoryDataModel;
import com.v60BNS.models.ProductModel;
import com.v60BNS.models.SingleProductModel;
import com.v60BNS.models.StoryModel;
import com.v60BNS.models.SliderModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Store extends Fragment {

    private HomeActivity activity;
    private FragmentStoreBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private List<CategoryDataModel.Data> categoryDataModelDataList;
    private List<SingleProductModel> reDataList;
    private int current_page = 0, NUM_PAGES;
    private CategoryAdapter categoryAdapter;
    private Slider_Adapter sliderAdapter;
    private ProductAdapter productAdapter;
    private List<SliderModel.Data> sliderModelList;
    private TimerTask timerTask;
    private Timer timer;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    private String category_id = "all";

    public static Fragment_Store newInstance() {
        return new Fragment_Store();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false);
        initView();
        change_slide_image();
        YoYo.with(Techniques.ZoomIn)
                .duration(900)
                .repeat(0)
                .playOn(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView() {
        sliderModelList = new ArrayList<>();
        reDataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        categoryDataModelDataList = new ArrayList<>();

        manager = new LinearLayoutManager(activity);

        binding.progBar.setVisibility(View.GONE);
        categoryAdapter = new CategoryAdapter(categoryDataModelDataList, this, activity);

        productAdapter = new ProductAdapter(reDataList, this, activity);
        binding.recViewdepart.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

        binding.recViewdepart.setAdapter(categoryAdapter);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(productAdapter);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int total_item = binding.recView.getAdapter().getItemCount();
                    int last_visible_item = ((LinearLayoutManager) binding.recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    if (total_item >= 20 && (total_item - last_visible_item) == 5 && !isLoading) {

                        isLoading = true;
                        int page = current_page + 1;
                        reDataList.add(null);
                        productAdapter.notifyItemInserted(reDataList.size() - 1);

                        loadMore(page);
                    }
                }
            }
        });

        getData();
        getMainCategory();
        getProducts();
    }

    private void getProducts() {

        try {
            reDataList.clear();
            productAdapter.notifyDataSetChanged();
            // binding.tvNoData.setVisibility(View.GONE);
            binding.progBar.setVisibility(View.VISIBLE);
            current_page = 1;
            Api.getService(Tags.base_url)
                    .getProduct("on", category_id + "", "20", current_page)
                    .enqueue(new Callback<ProductModel>() {
                        @Override
                        public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                reDataList.addAll(response.body().getData());
                                if (reDataList.size() > 0) {

                                    productAdapter.notifyDataSetChanged();

                                    //   binding.tvNoData.setVisibility(View.GONE);
                                } else {
                                    //  binding.tvNoData.setVisibility(View.VISIBLE);

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
                        public void onFailure(Call<ProductModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);

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
Log.e("flfllflfl",e.toString());
        }
    }

    private void loadMore(int page) {


        try {

            Api.getService(Tags.base_url)
                    .getProduct("on", category_id + "", "20", page)
                    .enqueue(new Callback<ProductModel>() {
                        @Override
                        public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                            isLoading = false;
                            reDataList.remove(reDataList.size() - 1);
                            productAdapter.notifyItemRemoved(reDataList.size() - 1);


                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                int oldPos = reDataList.size() - 1;

                                reDataList.addAll(response.body().getData());

                                if (response.body().getData().size() > 0) {
                                    current_page = response.body().getCurrent_page();
                                    productAdapter.notifyItemRangeChanged(oldPos, reDataList.size() - 1);

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
                        public void onFailure(Call<ProductModel> call, Throwable t) {
                            try {

                                if (reDataList.get(reDataList.size() - 1) == null) {
                                    isLoading = false;
                                    reDataList.remove(reDataList.size() - 1);
                                    productAdapter.notifyItemRemoved(reDataList.size() - 1);

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

    private void getMainCategory() {
        Api.getService(Tags.base_url)
                .getMainCategory("off")
                .enqueue(new Callback<CategoryDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                        binding.progBarCategory.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            categoryDataModelDataList.clear();
                            categoryDataModelDataList.addAll(response.body().getData());

                            if (categoryDataModelDataList.size() > 0) {
                                categoryAdapter.notifyDataSetChanged();
//                                binding.tvNoDatadepart.setVisibility(View.GONE);
                                Log.e(",dkdfkfkkfk", categoryDataModelDataList.get(0).getAr_title());
                            } else {
//                                binding.tvNoDatadepart.setVisibility(View.VISIBLE);

                            }


                        } else {
                            binding.progBarCategory.setVisibility(View.GONE);

                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
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
                    public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                        try {
                            binding.progBarCategory.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

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

    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                binding.pager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        YoYo.with(Techniques.ZoomIn)
                .duration(900)
                .repeat(0)
                .playOn(binding.getRoot());

    }


    public void showdetials() {
        Intent intent = new Intent(activity, CoffeeDetialsActivity.class);
        startActivity(intent);
    }


    private void getData() {
        Api.getService(Tags.base_url)
                .getSliders("off")
                .enqueue(new Callback<SliderModel>() {
                    @Override
                    public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                        binding.progBarSlider.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getData() != null) {
                                updateSliderData(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SliderModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

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

    private void updateSliderData(List<SliderModel.Data> list) {
        sliderModelList.clear();
        sliderModelList.addAll(list);

        if (sliderModelList.size() > 0) {
            binding.flSlider.setVisibility(View.VISIBLE);
            sliderAdapter = new Slider_Adapter(sliderModelList, activity);
            binding.pager.setAdapter(sliderAdapter);
            startTimer();
        } else {
            binding.flSlider.setVisibility(View.GONE);

        }


    }

    public void setitemData(String s) {

        category_id = s;
        getProducts();
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                if (binding.pager.getCurrentItem() < binding.pager.getAdapter().getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1, true);

                } else {
                    binding.pager.setCurrentItem(0, true);
                }
            });
        }
    }

    private void startTimer() {
        if (sliderModelList.size() > 1) {
            timer = new Timer();
            timerTask = new MyTimerTask();
            timer.scheduleAtFixedRate(timerTask, 6000, 6000);
        }


    }

    public void setProduct(String s) {
        Intent intent = new Intent(activity, CoffeeDetialsActivity.class);
        intent.putExtra("product_id", s);
        startActivity(intent);
    }

    private void stopTimer() {


        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.purge();
            timer.cancel();
            ;
        }

    }
}
