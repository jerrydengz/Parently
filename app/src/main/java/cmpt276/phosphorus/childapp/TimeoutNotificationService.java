package cmpt276.phosphorus.childapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

    /* Code assistance from
        https://developer.android.com/training/notify-user/build-notification
        https://www.youtube.com/watch?v=FbpD5RZtbCc
    */

public class TimeoutNotificationService extends Service {
    public static final String CHANNEL_ID = "TimerNotificationServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        createTimeoutNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Timer Notification Service Channel";
            String description = "Timer is running notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createTimeoutNotification() {
        Intent notificationIntent = new Intent(this, TimeoutActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Timeout")
                // todo make text the timer (make inner class timer?)
                .setContentText("Time Remaining")
                .setSmallIcon(R.drawable.timer_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }
}
