package com.greasemonkey.vendor.utility;


/**
 * Created by dell on 12/18/2019.
 */

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.greasemonkey.vendor.R;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.os.Build.VERSION.SDK_INT;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    NotificationManager notificationManager;
    String ADMIN_CHANNEL_ID = "Default";
    String defaultChannelName = "General";
    String getDefaultChannelDesc = "General Grease Monkey Notification";
    UserPrefManager userSession;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Notification -->","Inside Notification Receive");
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        Log.e("MyFirebaseMessaging", "onMessageReceived remoteMessage value--->" +
                remoteMessage.getData().toString());

        if (remoteMessage != null) {
            if (remoteMessage.getNotification() != null &&
                    remoteMessage.getNotification().getBody() != null) {
                message = remoteMessage.getNotification().getBody();
            } else {
                if (remoteMessage.getData() != null &&
                        remoteMessage.getData().get("title") != null) {
                    title = remoteMessage.getData().get("title");
                }
            }

            if (remoteMessage.getNotification() != null &&
                    remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            } else {
                if (remoteMessage.getData() != null &&
                        remoteMessage.getData().get("message") != null) {
                    title = remoteMessage.getData().get("message");
                }
            }

            if (remoteMessage.getNotification() != null &&
                    remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            } else {
                if (remoteMessage.getData() != null &&
                        remoteMessage.getData().get("message") != null) {
                    title = remoteMessage.getData().get("message");
                }
            }

            Log.e("MyFirebaseMessaging", "--->onMessageReceived title value--->" +
                    remoteMessage.getData().get("title"));
            Log.e("MyFirebaseMessaging", "--->onMessageReceived body value--->" +
                    remoteMessage.getData().get("body"));

            if (remoteMessage.getData().get("title") != null &&
                    remoteMessage.getData().get("body") != null)
                showNotification(
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("body")
                );
        }

        Log.e("MyFirebaseMessaging", "onMessageReceived title value--->" + title);
        Log.e("MyFirebaseMessaging", "onMessageReceived message value--->" + message);
       /* Intent intent = new Intent(this, DashobardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        String channelId = "Default";
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        //Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.notification);
        Log.e("SOund Path -->", sound.toString());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                channelId)
                .setSmallIcon(R.drawable.grease_monkey_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setSound(sound);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Default channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(1410, builder.build());
    }

    private void showNotification(String title, String message) {
        long[] pattern = {1000, 1000, 1000, 1000, 1000};

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random(60000).nextInt();
        if (SDK_INT >= Build.VERSION_CODES.O) {
            setUpNotificationChannels();
        }
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/raw/notification");
        Log.d("Sound-->",sound.toString());
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.grease_monkey_logo);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setSound(sound);
        builder.setVibrate(pattern);

        notificationManager.notify(notificationId, builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpNotificationChannels() {
        @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(ADMIN_CHANNEL_ID,
                defaultChannelName,
                NotificationManager.IMPORTANCE_HIGH);


        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/notification");
        Log.d("sound-->",sound.toString());
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        notificationChannel.setDescription(getDefaultChannelDesc);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setSound(sound, attributes);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}

