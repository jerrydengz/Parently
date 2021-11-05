package cmpt276.phosphorus.childapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * Represents a count down timer for the parent to use for
 * a child's timeout.
 */

    // Code assisted by https://www.youtube.com/playlist?list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd

public class TimeoutActivity extends AppCompatActivity {

    // Time is in milliseconds, 1000ms = 1s
    private long startTime = 60000;
    private long timeLeft;
    private long endTime;
    
    // Interval in milliseconds the timer updates its countdown
    public static final int COUNT_DOWN_INTERVAL = 1000;

    public static final long NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS = 60000L;

    private TextView tvCountDown;
    private RadioGroup timeGroup;
    private Button btnStartAndPause;
    private Button btnReset;

    private CountDownTimer cdTimer;
    private EditText customTimeInput;
    private boolean isTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);

        tvCountDown = findViewById(R.id.tvCountDown);
        customTimeInput = findViewById(R.id.inputCustomNumber);

        this.createBackBtn();
        this.setUpStartAndPauseBtn();
        this.setUpResetBtn();
        this.createTimeOptions();
        this.setUpCustomInput();
    }

    private void setUpStartAndPauseBtn() {
        btnStartAndPause = findViewById(R.id.btnStartAndPause);
        btnStartAndPause.setOnClickListener(v -> {
            if(!isTimerRunning){
                startTimer();
            } else{
                pauseTimer();
            }
        });
    }

    private void setUpResetBtn() {
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetTimer());
    }

    private void createBackBtn(){
        Button button = findViewById(R.id.btnBackTimeout);
        button.setOnClickListener(view -> finish());
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
    }

    private void setVisibilities() {
        int currentView = isTimerRunning ? View.INVISIBLE : View.VISIBLE;
        btnReset.setVisibility(currentView);
        timeGroup.setVisibility(currentView);
        customTimeInput.setVisibility(currentView);

        if(timeLeft == 0){
            btnStartAndPause.setVisibility(View.INVISIBLE);
        }
    }

    private void createTimeOptions() {
        timeGroup = findViewById(R.id.radio_group_time_options);
        int[] timeOptions = getResources().getIntArray(R.array.time_options);

        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_pref_pref), MODE_PRIVATE);

        for (int options : timeOptions) {
            RadioButton button = new RadioButton(this);
            button.setText(getString(R.string.time_selected, options));
            button.setOnClickListener(v -> {
                startTime = options * NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS;
                timeLeft = startTime;
                updateCountDownText();
                btnStartAndPause.setVisibility(View.VISIBLE);
                customTimeInput.setText("");
                button.setChecked(true);
            });
            timeGroup.addView((button));

            if(options * NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS ==
                    prefs.getLong(getString(R.string.shared_pref_start_time), startTime)){
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
                if(!customInput.isEmpty()){
                    long input = Long.parseLong(customInput);
                    if(input != 0){
                        startTime = input * NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS;
                        timeLeft = startTime;
                        updateCountDownText();
                        btnStartAndPause.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    // Turns milliseconds it is given to minutes and seconds for timer
    // Account for hours (when user input > 59min)?
    private void updateCountDownText() {
        int minutes = (int) ((timeLeft / 1000) / 60);
        int seconds = (int) ((timeLeft / 1000) % 60);

        String timeLeftFormatted = String.format(Locale.getDefault(),
                "%02d:%02d", minutes, seconds);
        tvCountDown.setText(timeLeftFormatted);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_pref_pref), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(getString(R.string.shared_pref_start_time), startTime);
        editor.putLong(getString(R.string.shared_pref_time_left), timeLeft);
        editor.putLong(getString(R.string.shared_pref_end_time), endTime);
        editor.putBoolean(getString(R.string.shared_pref_is_timer_running), isTimerRunning);

        editor.apply();

        if(cdTimer != null) {
            cdTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_pref_pref), MODE_PRIVATE);

        startTime = prefs.getLong(getString(R.string.shared_pref_start_time),
                NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS);
        timeLeft = prefs.getLong(getString(R.string.shared_pref_time_left), startTime);
        isTimerRunning = prefs.getBoolean(
                getString(R.string.shared_pref_is_timer_running), false);

        updateCountDownText();
        setVisibilities();

        if(isTimerRunning){
            endTime = prefs.getLong(getString(R.string.shared_pref_end_time), 0);
            timeLeft = endTime - System.currentTimeMillis();

            if(timeLeft < 0){
                timeLeft = 0;
                isTimerRunning = false;
                updateCountDownText();
                setVisibilities();
            } else{
                startTimer();
            }
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutActivity.class);
    }
}