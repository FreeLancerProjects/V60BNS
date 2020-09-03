package com.v60BNS.activities_fragments.activity_splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_language.LanguageActivity;
import com.v60BNS.activities_fragments.activity_login.LoginActivity;
import com.v60BNS.databinding.ActivitySplashBinding;
import com.v60BNS.language.Language;
import com.v60BNS.preferences.Preferences;


import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language.updateResources(base, Language.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        preferences = Preferences.getInstance();
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1200);
                    int state = preferences.getFragmentState(SplashActivity.this);
                    switch (state) {
                        case 0:
                            Intent intentw = new Intent(SplashActivity.this, LanguageActivity.class);

                            startActivity(intentw);
                            finish();
                            break;
                        case 1:
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                            startActivity(intent);
                            finish();
                            break;

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }


}