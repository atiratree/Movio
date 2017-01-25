package cz.muni.fi.pv256.movio2.fk410022.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.MainActivity;

public class NotificationUtils {

    public static final int ERROR_NOTIFICATION_ID = 0;
    public static final int DOWNLOADING_NOTIFICATION_ID = 1;
    public static final int DOWNLOADED_NOTIFICATION_ID = 2;

    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationUtils(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationManager = notificationManager;
    }

    public void fireNotification(int id, String message, boolean strong) {
        fireNotification(id, getMainActivityNotificationBuilder(message), strong);
    }

    public void fireNotification(int id, NotificationCompat.Builder builder, boolean strong) {
        if (strong) {
            builder.setDefaults(Notification.DEFAULT_ALL);
        }
        fireNotification(id, builder);
    }

    public void fireNotification(int id, String message) {
        fireNotification(id, getMainActivityNotificationBuilder(message));
    }

    public void fireNotification(int id, NotificationCompat.Builder builder) {
        notificationManager.notify(id, builder.build());
    }

    public void cancelNotification(int id) {
        notificationManager.cancel(id);
    }

    public NotificationCompat.Builder getMainActivityNotificationBuilder(String message) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        return getBuilder(message, pIntent);
    }

    public NotificationCompat.Builder getNetworkSettingsotificationBuilder(String message) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        return getBuilder(message, pIntent);
    }

    private NotificationCompat.Builder getBuilder(String message, PendingIntent pIntent) {
        return new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true);
    }
}
