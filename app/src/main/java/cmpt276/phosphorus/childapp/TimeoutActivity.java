package cmpt276.phosphorus.childapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * Represents a count down timer for the parent to use for
 * a child's timeout.
 */

// TODO: Options for time and custom time input

public class TimeoutActivity extends AppCompatActivity {

    // Time is in milliseconds, 1000ms = 1s
    private static final long START_TIME = 120000;

    private TextView tvCountDown;
    private Button btnStartAndPause;
    private Button btnReset;

    private CountDownTimer cdTimer;
    private boolean isTimerRunning;
    private long timeLeft = START_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);

        this.createBackBtn();
        this.setUpStartAndPauseBtn();
        this.setUpResetBtn();
        tvCountDown = findViewById(R.id.tvCountDown);
        updateCountDownText();
    }

    private void setUpStartAndPauseBtn() {
        btnStartAndPause = findViewById(R.id.btnStartAndPause);
        btnStartAndPause.setOnClickListener(v -> {
            if(!isTimerRunning){
                startTimer();
            }
            else{
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
        cdTimer = new CountDownTimer(timeLeft, 1000) {
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
                btnReset.setVisibility(View.VISIBLE);
            }
        }.start();

        isTimerRunning = true;
        btnStartAndPause.setText(getString(R.string.pause));
        btnReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        cdTimer.cancel();
        isTimerRunning = false;
        btnStartAndPause.setText(getString(R.string.start));
        btnReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        timeLeft = START_TIME;
        updateCountDownText();
        btnReset.setVisibility(View.INVISIBLE);
        btnStartAndPause.setVisibility(View.VISIBLE);
    }
    // Turns milliseconds it is given to minutes and seconds for timer
    // TODO: Account for hours (when user input > 59min)?
    private void updateCountDownText() {
        int minutes = (int) ((timeLeft / 1000) / 60);
        int seconds = (int) ((timeLeft / 1000) % 60);

        String timeLeftFormatted = String.format(Locale.getDefault(),
                "%02d:%02d", minutes, seconds);
        tvCountDown.setText(timeLeftFormatted);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutActivity.class);
    }
}