package com.boss.capture.DownloadImage;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationChannel {
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification";
            String description = "download";
            int importance = NotificationManager.IMPORTANCE_MIN;
            android.app.NotificationChannel channel = new android.app.NotificationChannel("1", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
