package com.v60BNS.activities_fragments.activity_home.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.databinding.FragmentProfileBinding;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import io.paperdb.Paper;

public class Fragment_Profile extends Fragment  {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;

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
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);


    }



}
