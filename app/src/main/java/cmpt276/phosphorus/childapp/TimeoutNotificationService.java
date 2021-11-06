package cmpt276.phosphorus.childapp;

import static cmpt276.phosphorus.childapp.TimeoutActivity.COUNT_DOWN_INTERVAL;
import static cmpt276.phosphorus.childapp.TimeoutActivity.NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS;
import static cmpt276.phosphorus.childapp.TimeoutActivity.timeLeftFormatter;

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

    /* Code assistance from
        https://developer.android.com/training/notify-user/build-notification
        https://www.youtube.com/watch?v=FbpD5RZtbCc
    */

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

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Time Remaining")
                .setSmallIcon(R.drawable.timer_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, notification.build());
    }

    private void startServiceTimer() {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_pref_pref), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        long startTime = prefs.getLong(getString(R.string.shared_pref_start_time),
                NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS);
        long timeLeft = prefs.getLong(getString(R.string.shared_pref_time_left), startTime);

        endTime = System.currentTimeMillis() + timeLeft;
        if(isTimerRunning) {
            endTime = prefs.getLong(getString(R.string.shared_pref_end_time), 0);
            timeLeft = endTime - System.currentTimeMillis();
        }

        editor.putLong(getString(R.string.shared_pref_end_time), endTime);
        cdTimer = new CountDownTimer(timeLeft, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeLeftFormatted = timeLeftFormatter(millisUntilFinished);

                // Continually update time left for inner timer
                editor.putLong(getString(R.string.shared_pref_time_left),
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
                // todo: alert dialog
            }
        }.start();

        isTimerRunning = true;
    }
}
