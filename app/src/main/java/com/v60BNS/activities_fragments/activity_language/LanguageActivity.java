package com.v60BNS.activities_fragments.activity_language;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_login.LoginActivity;
import com.v60BNS.databinding.ActivityLanguageBinding;
import com.v60BNS.language.Language;
import com.v60BNS.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class LanguageActivity extends AppCompatActivity {

    private ActivityLanguageBinding binding;
    private String lang;
    private boolean canSelect = false;
    private String selectedLang = "";
    private int type;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language.updateResources(base, Language.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        getDataFromIntent();

        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 0);

        }
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        selectedLang = lang;
        preferences = Preferences.getInstance();
        binding.setLang(lang);
        binding.close.setOnClickListener(v -> finish());
        if (lang.equals("ar")) {
            binding.tvAr.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageAr.setVisibility(View.VISIBLE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this, R.color.color4));
            binding.imageEn.setVisibility(View.GONE);

        } else {
            binding.tvAr.setTextColor(ContextCompat.getColor(this, R.color.color4));
            binding.imageAr.setVisibility(View.GONE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageEn.setVisibility(View.VISIBLE);
        }
        if (type == 0) {
            canSelect = true;
            updateBtnUi();
        }
        binding.consAr.setOnClickListener(v -> {

            binding.tvAr.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageAr.setVisibility(View.VISIBLE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this, R.color.color4));
            binding.imageEn.setVisibility(View.GONE);

            if (lang.equals("ar")) {
                selectedLang = lang;
                canSelect = false;

            } else {

                canSelect = true;
                selectedLang = "ar";


            }

            updateBtnUi();
        });


        binding.consEn.setOnClickListener(v -> {
            binding.tvAr.setTextColor(ContextCompat.getColor(this, R.color.color4));
            binding.imageAr.setVisibility(View.GONE);
            binding.tvEn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageEn.setVisibility(View.VISIBLE);

            if (lang.equals("ar")) {

                canSelect = true;
                selectedLang = "en";

            } else {
                selectedLang = lang;
                canSelect = false;
            }

            updateBtnUi();

        });


        binding.btnConfirm.setOnClickListener(v -> {
            if (canSelect) {
                if (type == 1) {
                    Paper.book().write("lang", selectedLang);
                    Language.updateResources(this, selectedLang);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
//                setResult(RESULT_OK);
//                finish();}
                } else {
                    Paper.book().write("lang", selectedLang);
                    Language.updateResources(this, selectedLang);
                    preferences.saveLoginFragmentState(this, 1);
                    Intent intent = new Intent(LanguageActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);

                }
            }
        });
    }

    private void updateBtnUi() {
        if (canSelect) {
            binding.btnConfirm.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.btnConfirm.setBackgroundResource(R.drawable.small_rounded_primary);
        } else {
            binding.btnConfirm.setTextColor(ContextCompat.getColor(this, R.color.gray9));
            binding.btnConfirm.setBackgroundResource(R.drawable.small_rounded_gray);
        }
    }
}