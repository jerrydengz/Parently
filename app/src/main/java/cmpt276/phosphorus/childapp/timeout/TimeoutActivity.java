package cmpt276.phosphorus.childapp.timeout;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.timeout.utils.TimeoutNotificationService;
import cmpt276.phosphorus.childapp.timeout.utils.TimeoutPrefConst;

/**
 * Represents a count down timer for the parent to use for
 * a child's timeout.
 */

// Code assisted by https://www.youtube.com/playlist?list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd

public class TimeoutActivity extends AppCompatActivity {

    // Interval in milliseconds the timer updates its countdown
    public static final int COUNT_DOWN_INTERVAL = 1000;
    public static final long NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS = 60000L;
    // Time is in milliseconds, 1000ms = 1s
    private long startTime = 60000;
    private long timeLeft;
    private long endTime;
    private TextView tvCountDown;
    private RadioGroup timeGroup;
    private Button btnStartAndPause;
    private Button btnReset;

    private CountDownTimer cdTimer;
    private EditText customTimeInput;
    private boolean isTimerRunning;

    public static String timeLeftFormatter(long timeLeft) {
        int minutes = (int) ((timeLeft / 1000) / 60);
        int seconds = (int) ((timeLeft / 1000) % 60);

        return String.format(Locale.getDefault(),
                "%02d:%02d", minutes, seconds);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);

        tvCountDown = findViewById(R.id.tvCountDown);
        customTimeInput = findViewById(R.id.inputCustomNumber);

        this.setTitle(getString(R.string.timeout_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setUpStartAndPauseBtn();
        this.setUpResetBtn();
        this.createTimeOptions();
        this.setUpCustomInput();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void setUpStartAndPauseBtn() {
        btnStartAndPause = findViewById(R.id.btnStartAndPause);
        btnStartAndPause.setOnClickListener(v -> {
            if (!isTimerRunning) {
                startTimer();
            } else {
                pauseTimer();
            }
        });
    }

    private void setUpResetBtn() {
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        // Helper variable for saving data regarding timer
        endTime = System.currentTimeMillis() + timeLeft;

        cdTimer = new CountDownTimer(timeLeft, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                btnStartAndPause.setText(getString(R.string.start));
                btnStartAndPause.setVisibility(View.INVISIBLE);
                setVisibilities();
                createAlertDialog();
            }
        }.start();

        isTimerRunning = true;
        btnStartAndPause.setText(getString(R.string.pause));
        setVisibilities();
    }

    private void pauseTimer() {
        cdTimer.cancel();
        isTimerRunning = false;
        btnStartAndPause.setText(getString(R.string.start));
        setVisibilities();
    }

    private void resetTimer() {
        timeLeft = startTime;
        updateCountDownText();
        btnReset.setVisibility(View.INVISIBLE);
        btnStartAndPause.setVisibility(View.VISIBLE);
        btnStartAndPause.setText(getString(R.string.start));
    }

    private void setVisibilities() {
        int currentView = isTimerRunning ? View.INVISIBLE : View.VISIBLE;
        btnReset.setVisibility(currentView);
        timeGroup.setVisibility(currentView);
        customTimeInput.setVisibility(currentView);

        if (timeLeft == startTime) {
            btnReset.setVisibility(View.INVISIBLE);
        }

        if (timeLeft == 0) {
            btnStartAndPause.setVisibility(View.INVISIBLE);
        }

        int background = isTimerRunning ? R.drawable.relaxing_background : 0;
        ConstraintLayout constraintLayout = findViewById(R.id.timeoutLayout);
        constraintLayout.setBackgroundResource(background);
    }

    private void createTimeOptions() {
        timeGroup = findViewById(R.id.radio_group_time_options);
        int[] timeOptions = getResources().getIntArray(R.array.time_options);

        SharedPreferences prefs = getSharedPreferences(
                TimeoutPrefConst.PREFERENCE_PREF, MODE_PRIVATE);

        for (int options : timeOptions) {
            RadioButton button = new RadioButton(this);
            button.setText(getString(R.string.time_selected, options));
            button.setOnClickListener(v -> {
                startTime = options * NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS;
                timeLeft = startTime;
                updateCountDownText();
                setVisibilities();
                btnStartAndPause.setVisibility(View.VISIBLE);
                customTimeInput.setText("");
                button.setChecked(true);
            });
            timeGroup.addView((button));

            if (options * NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS ==
                    prefs.getLong(TimeoutPrefConst.START_TIME, startTime)) {
                button.setChecked(true);
            }
        }
    }

    private void setUpCustomInput() {
        customTimeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String customInput = customTimeInput.getText().toString();
                timeGroup.clearCheck();
                if (!customInput.isEmpty()) {
                    long input = Long.parseLong(customInput);
                    btnReset.setVisibility(View.INVISIBLE);
                    if (input != 0) {
                        startTime = input * NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS;
                        timeLeft = startTime;
                        updateCountDownText();
                        btnStartAndPause.setVisibility(View.VISIBLE);
                    } else {
                        // Unique case so not using setVisibilities()
                        timeLeft = 0;
                        updateCountDownText();
                        btnStartAndPause.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    // Turns milliseconds it is given to minutes and seconds for timer
    // Account for hours (when user input > 59min)?
    private void updateCountDownText() {
        String timeLeftFormatted = timeLeftFormatter(timeLeft);
        tvCountDown.setText(timeLeftFormatted);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences(
                TimeoutPrefConst.PREFERENCE_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(TimeoutPrefConst.START_TIME, startTime);
        editor.putLong(TimeoutPrefConst.TIME_LEFT, timeLeft);
        editor.putLong(TimeoutPrefConst.END_TIME, endTime);
        editor.putBoolean(TimeoutPrefConst.IS_TIMER_RUNNING, isTimerRunning);

        editor.apply();

        if (isTimerRunning) {
            startTimeoutNotificationService();
        }
        if (cdTimer != null) {
            cdTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        stopTimeoutNotificationService();
        stopNotification(1);

        SharedPreferences prefs = getSharedPreferences(
                TimeoutPrefConst.PREFERENCE_PREF, MODE_PRIVATE);

        startTime = prefs.getLong(TimeoutPrefConst.START_TIME,
                NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS);
        timeLeft = prefs.getLong(TimeoutPrefConst.TIME_LEFT, startTime);
        isTimerRunning = prefs.getBoolean(
                TimeoutPrefConst.IS_TIMER_RUNNING, false);

        updateCountDownText();
        setVisibilities();

        if (isTimerRunning) {
            endTime = prefs.getLong(TimeoutPrefConst.END_TIME, 0);
            timeLeft = endTime - System.currentTimeMillis();

            if (timeLeft < 0) {
                timeLeft = 0;
                isTimerRunning = false;
                updateCountDownText();
                setVisibilities();
                createAlertDialog();
            } else {
                startTimer();
            }
        }
    }

    private void createAlertDialog() {
        FragmentManager manager = getSupportFragmentManager();
        TimeoutMessageFragment dialog = new TimeoutMessageFragment();
        dialog.show(manager, "MessageDialog");
    }

    public void startTimeoutNotificationService() {
        Intent serviceIntent = new Intent(this, TimeoutNotificationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopTimeoutNotificationService() {
        Intent serviceIntent = new Intent(this, TimeoutNotificationService.class);
        stopService(serviceIntent);
    }

    public void stopNotification(int id) {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}