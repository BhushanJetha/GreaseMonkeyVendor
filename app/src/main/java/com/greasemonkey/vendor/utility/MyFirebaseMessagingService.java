package com.greasemonkey.vendor.utility;


/**
 * Created by dell on 12/18/2019.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.greasemonkey.vendor.BuildConfig;
import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.R;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage.getNotification().getTitle());
        }
    }

    public void handleNotification(String message) {
        sendNotification(message);
    }

    public void sendNotification(String message) {

        Log.d("Notification Class","Testing");
        String channelId = getApplicationContext().getString(R.string.default_notification_channel_id);

        Intent intent = new Intent(this, DashobardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.notification);
        final long[] VIBRATE_PATTERN = {0, 500};
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(VIBRATE_PATTERN)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (soundUri != null) {
                // Changing Default mode of notification
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                // Creating an Audio Attribute
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();

                // Creating Channel
                NotificationChannel notificationChannel = new NotificationChannel(channelId, "Testing_Audio",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(soundUri, audioAttributes);
                notificationChannel.setVibrationPattern(VIBRATE_PATTERN);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(notificationChannel);
                    notificationBuilder.setChannelId(channelId);
                }
            }
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(0, notificationBuilder.build());
        }
    }
}

