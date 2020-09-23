package com.v60BNS.notification;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_chat.ChatActivity;
import com.v60BNS.models.ChatUserModel;
import com.v60BNS.models.MessageModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

public class FireBaseMessaging extends FirebaseMessagingService {

    Preferences preferences = Preferences.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> map = remoteMessage.getData();

        for (String key:map.keySet())
        {
            Log.e("keys",key+"    value "+map.get(key));
        }

        if (getSession().equals(Tags.session_login))
        {
            if (map.get("to_user_id")!=null)
            {
                int to_id = Integer.parseInt(map.get("to_user_id"));

                if (getCurrentUser_id()==to_id)
                {
                    manageNotification(map);
                }
            }
            else if(map.get("id")!=null){

                manageNotification(map);

            }
        }
        }





    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            //createNewNotificationVersion(map);
        }else
            {
           //     createOldNotificationVersion(map);

            }
    }

//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    private void createNewNotificationVersion(Map<String, String> map) {
//
//        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;
//
//        String not_type = map.get("notification_type");
//
//        if (not_type!=null&&not_type.equals("chat"))
//        {
//            String file_link="";
//            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//            String current_class =activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
//
//            int msg_id = Integer.parseInt(map.get("id"));
//            int room_id = Integer.parseInt(map.get("room_id"));
//            int from_user_id = Integer.parseInt(map.get("from_user_id"));
//            int to_user_id = Integer.parseInt(map.get("to_user_id"));
//            int type = Integer.parseInt(map.get("type"));
//
//
//
//            int date = Integer.parseInt(map.get("date"));
//            int isRead = Integer.parseInt(map.get("is_read"));
//
//            String message = map.get("message");
//            String from_user_name = map.get("from_user_name");
//            String from_user_email = map.get("from_user_email");
//            String from_user_phone = map.get("from_user_phone");
//            String from_user_phone_code = map.get("from_user_phone_code");
//            String from_user_avatar = map.get("from_user_avatar");
//
//            String to_user_name = map.get("to_user_name");
//            String to_user_email = map.get("to_user_email");
//            String to_user_phone = map.get("to_user_phone");
//            String to_user_avatar = map.get("to_user_avatar");
//            String to_user_phone_code = map.get("to_user_phone_code");
//
//
//
//
//            //MessageModel messageModel = new MessageModel(msg_id,room_id,from_user_id,to_user_id,type,message,date,isRead,from_user_name,from_user_email,from_user_phone_code,from_user_phone,from_user_avatar,to_user_name,to_user_email,to_user_phone_code,to_user_phone,to_user_avatar);
//
//
//            Log.e("mkjjj",current_class);
//
//
//            if (current_class.equals("com.creative.share.apps.ebranch.activities_fragments.activity_chat.ChatActivity"))
//            {
//
//                int chat_user_id = getChatUser_id();
//
//                if (chat_user_id==from_user_id)
//                {
//                    EventBus.getDefault().post(messageModel);
//                }else
//                {
//                //    LoadChatImage(messageModel,sound_Path,1);
//                }
//
//
//
//
//            }else
//            {
//
//                EventBus.getDefault().post(messageModel);
//              //  LoadChatImage(messageModel,sound_Path,1);
//
//
//            }
//
//        }
//
//
//    }
//    private void LoadChatImage(MessageModel messageModel, String sound_path, int type) {
//
//
//        Target target = new Target() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//                if (type==1)
//                {
//                    sendChatNotification_VersionNew(messageModel,sound_path,bitmap);
//
//                }else
//                {
//                    sendChatNotification_VersionOld(messageModel,sound_path,bitmap);
//
//                }
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_nav_notification);
//
//                if (type==1)
//                {
//                    sendChatNotification_VersionNew(messageModel,sound_path,bitmap);
//
//                }else
//                {
//                    sendChatNotification_VersionOld(messageModel,sound_path,bitmap);
//
//                }
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        };
//
//       // new Handler(Looper.getMainLooper()).postDelayed(() -> Picasso.get().load(Uri.parse(Tags.IMAGE_URL+messageModel.getFrom_user_avatar())).into(target),100);
//
//    }


//
//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    private void createOldNotificationVersion(Map<String, String> map)
//    {
//
//
//        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;
//
//        String not_type = map.get("notification_type");
//
//        if (not_type!=null&&not_type.equals("chat"))
//        {
//            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//            String current_class =activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
//
//            int msg_id = Integer.parseInt(map.get("id"));
//            int room_id = Integer.parseInt(map.get("room_id"));
//            int from_user_id = Integer.parseInt(map.get("from_user_id"));
//            int to_user_id = Integer.parseInt(map.get("to_user_id"));
//            int type = Integer.parseInt(map.get("type"));
//
//
//            int date = Integer.parseInt(map.get("date"));
//            int isRead = Integer.parseInt(map.get("is_read"));
//
//            String message = map.get("message");
//            String from_user_name = map.get("from_user_name");
//            String from_user_email = map.get("from_user_email");
//            String from_user_phone = map.get("from_user_phone");
//            String from_user_phone_code = map.get("from_user_phone_code");
//            String from_user_avatar = map.get("from_user_avatar");
//
//            String to_user_name = map.get("to_user_name");
//            String to_user_email = map.get("to_user_email");
//            String to_user_phone = map.get("to_user_phone");
//            String to_user_avatar = map.get("to_user_avatar");
//            String to_user_phone_code = map.get("to_user_phone_code");
//
//
//
//
//            MessageModel messageModel = new MessageModel(msg_id,room_id,from_user_id,to_user_id,type,message,date,isRead,from_user_name,from_user_email,from_user_phone_code,from_user_phone,from_user_avatar,to_user_name,to_user_email,to_user_phone_code,to_user_phone,to_user_avatar);
//
//            if (current_class.equals("com.creative.share.apps.ebranch.activities_fragments.activity_chat.ChatActivity"))
//            {
//
//                int chat_user_id = getChatUser_id();
//
//                if (chat_user_id==from_user_id)
//                {
//                    EventBus.getDefault().post(messageModel);
//                }else
//                {
//                    LoadChatImage(messageModel,sound_Path,0);
//                }
//
//
//
//
//            }else
//            {
//
//
//                EventBus.getDefault().post(messageModel);
//                LoadChatImage(messageModel,sound_Path,0);
//
//
//            }
//
//        }
//       else if(not_type!=null&&not_type.equals("order")) {
//            String content = null;
//            String title = null;
//
//            int status= Integer.parseInt(map.get("status"));
//            int order_id= Integer.parseInt(map.get("id"));
//            title=getResources().getString(R.string.order_num)+" "+order_id;
//            OrderModel orderModel=new OrderModel(status);
//if(status==1){
//    content=getResources().getString(R.string.market_accept_order)+ " "+order_id;
//}
//else if(status==2){
//    content=getResources().getString(R.string.market_refues_order)+ " "+order_id;
//
//}
//else if(status==5){
//    content=getResources().getString(R.string.driver_accept_order)+ " "+order_id;
//
//}
//else if(status==7||status==8){
//    content=getResources().getString(R.string.driver_cancel_order)+ " "+order_id;
//
//}
//else if(status==14){
//    content=getResources().getString(R.string.driver_recevie_order)+ " "+order_id;
//
//}
//else if(status==9){
//    content=getResources().getString(R.string.order_finsihed)+ " "+order_id;
//
//}
//
//            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//            String current_class =activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
//Log.e("mkjjj",current_class);
//            if (current_class.equals("com.creative.share.apps.ebranch.activities_fragments.activity_orders.OrdersActivity")){
//                EventBus.getDefault().post(orderModel);
//
//
//            }
////                sendNotification_VersionOld(content,title,orderModel,sound_Path);
//        }
//    }



//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void sendChatNotification_VersionNew(MessageModel messageModel, String sound_path, Bitmap bitmap) {
//
//
//        String CHANNEL_ID = "my_channel_02";
//        CharSequence CHANNEL_NAME = "my_channel_name";
//        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
//
//        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
//        channel.setShowBadge(true);
//        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
//                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
//                .build()
//        );
//        builder.setChannelId(CHANNEL_ID);
//        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
//        builder.setSmallIcon(R.drawable.ic_nav_notification);
//        builder.setContentTitle(messageModel.getFrom_user_name());
//        builder.setLargeIcon(bitmap);
//        Intent intent = new Intent(this, ChatActivity.class);
//        ChatUserModel chatUserModel = new ChatUserModel(messageModel.getFrom_user_name(),messageModel.getFrom_user_avatar(),messageModel.getFrom_user_id(),messageModel.getRoom_id(),messageModel.getFrom_user_phone_code(),messageModel.getFrom_user_phone());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("chat_user_data", chatUserModel);
//        intent.putExtra("from_fire", true);
//
//        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
//        taskStackBuilder.addNextIntent(intent);
//
//        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentIntent(pendingIntent);
//
//
//            builder.setContentText(messageModel.getMessage());
//
//
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (manager != null) {
//            manager.createNotificationChannel(channel);
//            manager.notify(12352, builder.build());
//        }
//
//
//    }
//
//    private void sendChatNotification_VersionOld(MessageModel messageModel, String sound_path, Bitmap bitmap) {
//
//        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
//        builder.setSmallIcon(R.drawable.ic_nav_notification);
//        builder.setContentTitle(messageModel.getFrom_user_name());
//
//        Intent intent = new Intent(this, ChatActivity.class);
//        ChatUserModel chatUserModel = new ChatUserModel(messageModel.getFrom_user_name(),messageModel.getFrom_user_avatar(),messageModel.getFrom_user_id(),messageModel.getRoom_id(),messageModel.getFrom_user_phone_code(),messageModel.getFrom_user_phone());
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("chat_user_data", chatUserModel);
//        intent.putExtra("from_fire", true);
//
//        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
//        taskStackBuilder.addNextIntent(intent);
//
//        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentIntent(pendingIntent);
//
//
//            builder.setContentText(messageModel.getMessage());
//
//
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (manager != null) {
//            manager.notify(12352, builder.build());
//        }
//
//
//    }
//
//


    private int getCurrentUser_id()
    {
        return preferences.getUserData(this).getId();
    }

    private int getChatUser_id()
    {
        if (preferences.getChatUserData(this)!=null)
        {
            return preferences.getChatUserData(this).getId();

        }else
            {
                return -1;

            }
    }

    private String getSession()
    {
        return preferences.getSession(this);
    }


}
