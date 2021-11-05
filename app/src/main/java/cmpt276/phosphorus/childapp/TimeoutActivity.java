package cmpt276.phosphorus.childapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
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

public class TimeoutActivity extends AppCompatActivity {

    // Time is in milliseconds, 1000ms = 1s
    private static long START_TIME = 60000;
    
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
    private long timeLeft = START_TIME;

    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);

        tvCountDown = findViewById(R.id.tvCountDown);
        customTimeInput = findViewById(R.id.inputCustomNumber);
        constraintLayout = findViewById(R.id.timeoutLayout);


        this.createBackBtn();
        this.setUpStartAndPauseBtn();
        this.setUpResetBtn();
        this.createTimeOptions();
        this.setUpCustomInput();

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
        timeLeft = START_TIME;
        updateCountDownText();
        btnReset.setVisibility(View.INVISIBLE);
        btnStartAndPause.setVisibility(View.VISIBLE);
    }

    private void setVisibilities() {
        int currentView = isTimerRunning ? View.INVISIBLE : View.VISIBLE;
        btnReset.setVisibility(currentView);
        timeGroup.setVisibility(currentView);
        customTimeInput.setVisibility(currentView);

        int background = isTimerRunning ? R.drawable.relaxing_background : 0;
        constraintLayout.setBackgroundResource(background);
    }

    private void createTimeOptions() {
        timeGroup = findViewById(R.id.radio_group_time_options);
        int[] timeOptions = getResources().getIntArray(R.array.time_options);

        for (int options : timeOptions) {
            RadioButton button = new RadioButton(this);
            button.setText(getString(R.string.time_selected, options));
            button.setOnClickListener(v -> {
                START_TIME = options * NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS;
                timeLeft = START_TIME;
                updateCountDownText();
                customTimeInput.setVisibility(View.INVISIBLE);
                btnStartAndPause.setVisibility(View.VISIBLE);
                customTimeInput.setText("");
            });
            timeGroup.addView((button));
            // Only accounts for base time (1 minute), need to refactor for saving data if needed
            if(options * 60000L == START_TIME){
                button.setChecked(true);
            }
        }
        // Custom time button
        RadioButton button = new RadioButton(this);
        button.setText(getString(R.string.custom));
        button.setOnClickListener(v -> {
            customTimeInput.setVisibility(View.VISIBLE);
            btnStartAndPause.setVisibility(View.INVISIBLE);
            btnReset.setVisibility(View.INVISIBLE);
            tvCountDown.setText(getString(R.string.empty_timer));
        });
        timeGroup.addView(button);
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
                if(!customInput.isEmpty()){
                    long input = Long.parseLong(customInput);
                    if(input != 0){
                        START_TIME = input * NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS;
                        timeLeft = START_TIME;
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutActivity.class);
    }
}