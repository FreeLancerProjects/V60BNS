package com.v60BNS.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

public class Language_Helper {


    public static void setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        updateResources(c, language);
    }
//    public static void setLocale(Activity context) {
//        Locale locale;
//        Sessions session = new Sessions(context);
//        //Log.e("Lan",session.getLanguage());
//        locale = new Locale(langCode);
//        Configuration config = new Configuration(context.getResources().getConfiguration());
//        Locale.setDefault(locale);
//        config.setLocale(locale);
//
//        context.getBaseContext().getResources().updateConfiguration(config,
//                context.getBaseContext().getResources().getDisplayMetrics());
//    }

    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
       // config.setLocale(locale);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);

        } else {
            config.locale = locale;

            res.updateConfiguration(config, res.getDisplayMetrics());

        }
        return context;


    }

    public static String getLanguage(Context c) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString("lang", Locale.getDefault().getLanguage());
    }
    private static void persistLanguage(Context c, String language) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",language);
        editor.apply();
    }
}
