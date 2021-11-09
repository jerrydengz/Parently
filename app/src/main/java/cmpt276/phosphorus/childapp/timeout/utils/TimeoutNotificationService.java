package cmpt276.phosphorus.childapp.timeout.utils;


import static cmpt276.phosphorus.childapp.timeout.utils.TimeConversionUtils.*;
import static cmpt276.phosphorus.childapp.timeout.utils.TimeConversionUtils.timeLeftFormatter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.timeout.TimeoutActivity;

    /* Code assistance from
        https://developer.android.com/training/notify-user/build-notification
        https://www.youtube.com/watch?v=FbpD5RZtbCc
    */

// ==============================================================================================
//
// Manages the timeout notifications when leaving the app
//
// ==============================================================================================
public class TimeoutNotificationService extends Service {
    public static final String CHANNEL_ID = "TimerNotificationServiceChannel";

    private boolean isTimerRunning;
    private CountDownTimer cdTimer;
    private long endTime;

    private NotificationCompat.Builder notification;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        createTimeoutNotification();

        startForeground(1, notification.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(cdTimer != null) {
            cdTimer.cancel();
            isTimerRunning = false;
        }
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServiceTimer();
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
            CharSequence name = getString(R.string.timer_notification_service_channel);
            String description = getString(R.string.timeout_notification_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Disable alarm sound that plays as long as notification is alive
            channel.setSound(null, null);

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

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(getString(R.string.timeout_remaining))
                .setSmallIcon(R.drawable.timer_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, notification.build());
    }

    private void startServiceTimer() {
        SharedPreferences prefs = getSharedPreferences(
                TimeoutPrefConst.PREFERENCE_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        long startTime = prefs.getLong(TimeoutPrefConst.START_TIME,
                NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS);
        long timeLeft = prefs.getLong(TimeoutPrefConst.TIME_LEFT, startTime);

        endTime = System.currentTimeMillis() + timeLeft;
        if(isTimerRunning) {
            endTime = prefs.getLong(TimeoutPrefConst.END_TIME, 0);
            timeLeft = endTime - System.currentTimeMillis();
        }

        Intent endIntent = new Intent(this, TimeoutActivity.class);
        endIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        editor.putLong(TimeoutPrefConst.END_TIME, endTime);
        cdTimer = new CountDownTimer(timeLeft, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeLeftFormatted = timeLeftFormatter(millisUntilFinished);

                // Continually update time left for inner timer
                editor.putLong(TimeoutPrefConst.TIME_LEFT,
                        endTime - System.currentTimeMillis());
                editor.apply();

                if(notification != null) {
                    notification.setContentTitle(timeLeftFormatted);
                    NotificationManager notificationManager = (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, notification.build());
                }
            }

            @Override
            public void onFinish() {
                startActivity(endIntent);
            }
        }.start();

        isTimerRunning = true;
    }
}
