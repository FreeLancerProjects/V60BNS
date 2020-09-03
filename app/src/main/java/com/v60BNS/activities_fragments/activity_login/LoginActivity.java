package com.v60BNS.activities_fragments.activity_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.activities_fragments.activity_verification_code.VerificationCodeActivity;
import com.v60BNS.adapters.CountriesAdapter;
import com.v60BNS.databinding.ActivityLoginBinding;
import com.v60BNS.databinding.DialogCountriesBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language;
import com.v60BNS.models.CountryModel;
import com.v60BNS.models.LoginModel;
import com.v60BNS.share.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements Listeners.LoginListener {
    private ActivityLoginBinding binding;
    private LoginModel loginModel;
    private List<CountryModel> countryModelList = new ArrayList<>();
    private CountriesAdapter countriesAdapter;
    private AlertDialog dialog;
    private String lang;
    private String phone_code = "+966";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language.updateResources(base, Language.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new TransitionSet());
            getWindow().setExitTransition(new TransitionSet());


        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {
        loginModel = new LoginModel();
        binding.setLoginModel(loginModel);
        binding.setListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.lanuch);
        binding.cons.startAnimation(animation);
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0"))
                {
                    binding.edtPhone.setText("");
                }
            }
        });
        binding.tvSkip.setOnClickListener(v -> {
            navigateToHomeActivity();
        });
        createCountriesDialog();
        getPhoneCodes();
    }



    private void getPhoneCodes() {

        countryModelList.add(new CountryModel("+966","1"));
        countryModelList.add(new CountryModel("+20","2"));
        runOnUiThread(() -> countriesAdapter.notifyDataSetChanged());
        if (countryModelList.size()>0){
            binding.tvCode.setText(countryModelList.get(0).getCode());

        }else {

            binding.tvCode.setText(phone_code);

        }

    }


    private void createCountriesDialog() {

        dialog = new AlertDialog.Builder(this)
                .create();
        countriesAdapter = new CountriesAdapter(countryModelList,this);

        DialogCountriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_countries, null, false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(countriesAdapter);

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());

    }



    @Override
    public void validate() {
        if (loginModel.isDataValid(this))
        {
            Common.CloseKeyBoard(this,binding.edtPhone);
            navigateToVerificationCodeActivity();
        }
    }

    @Override
    public void showCountryDialog() {
        dialog.show();
    }

    private void navigateToVerificationCodeActivity() {

        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("phone_code",phone_code);
        intent.putExtra("phone",loginModel.getPhone());
        startActivity(intent);
        finish();

    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void setItemData(CountryModel countryModel) {
        dialog.dismiss();
        phone_code = countryModel.getCode();
        binding.tvCode.setText(countryModel.getCode());
    }
}
