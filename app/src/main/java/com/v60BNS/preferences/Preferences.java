package com.v60BNS.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.reflect.TypeToken;
import com.v60BNS.models.Add_Order_Model;
import com.v60BNS.models.ChatUserModel;
import com.v60BNS.models.DefaultSettings;
import com.v60BNS.models.UserModel;
import com.v60BNS.tags.Tags;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }


       public void createUpdateAppSetting(Context context, DefaultSettings settings) {
           SharedPreferences preferences = context.getSharedPreferences("settingsRef", Context.MODE_PRIVATE);
           Gson gson = new Gson();
           String data = gson.toJson(settings);
           SharedPreferences.Editor editor = preferences.edit();
           editor.putString("settings", data);
           editor.apply();
       }
    public DefaultSettings getAppSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settingsRef", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson.fromJson(preferences.getString("settings", ""), DefaultSettings.class);
    }


    public void create_update_userdata(Context context, UserModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }

    public void create_update_session(Context context, String session) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", session);
        editor.apply();


    }


    public String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }


    public void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
        create_update_session(context, Tags.session_logout);
    }
    public void saveLoginFragmentState(Context context,int state)
    {
        SharedPreferences preferences = context.getSharedPreferences("fragment_state",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("state",state);
        editor.apply();
    }
    public int getFragmentState(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("fragment_state",Context.MODE_PRIVATE);
        return preferences.getInt("state",0);
    }

    public void create_update_ChatUserData(Context context , ChatUserModel chatUserModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String userDataGson = gson.toJson(chatUserModel);
        editor.putString("chat_user_data",userDataGson);
        editor.apply();
    }

    public ChatUserModel getChatUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref",Context.MODE_PRIVATE);
        String userDataGson = preferences.getString("chat_user_data","");
        return new Gson().fromJson(userDataGson, ChatUserModel.class);
    }

    public void clearChatUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
    public void create_update_order(Context context, Add_Order_Model buy_models){
        SharedPreferences sharedPreferences=context.getSharedPreferences("order",Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String user_order=gson.toJson(buy_models);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user_order",user_order);
        editor.apply();
        editor.commit();
    }
    public Add_Order_Model getUserOrder(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("order",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_order = preferences.getString("user_order",null);
        Type type=new TypeToken<Add_Order_Model>(){}.getType();
        Add_Order_Model buy_models=gson.fromJson(user_order,type);
        return buy_models;
    }
}
