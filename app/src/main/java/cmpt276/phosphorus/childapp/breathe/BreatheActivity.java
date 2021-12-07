package cmpt276.phosphorus.childapp.breathe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.utils.BreatheState;
import cmpt276.phosphorus.childapp.breathe.utils.ExhaleState;
import cmpt276.phosphorus.childapp.breathe.utils.IdleState;
import cmpt276.phosphorus.childapp.breathe.utils.InhaleState;

// ==============================================================================================
//
// Breathing Activity which manages all the states
//
// ==============================================================================================
public class BreatheActivity extends AppCompatActivity {

    private BreatheState inhaleState;
    private BreatheState exhaleState;
    private BreatheState currentState = new IdleState(this);

    private ImageView ivCircle;
    private TextView tvRemainingBreaths;
    private boolean isInitialized = false;

    private int remainingBreaths;
    private int chosenBreaths;
    private final String APP_PREFS = "ParentApp";
    private final String NUM_CHOSEN_BREATHS = "NumChosenBreaths - BreatheActivity.java";

    // TODO - turn into enum?
    private final long TEN_SECONDS_IN_MILLISECONDS = 10000;
    private final double ANIMATION_RATE = 2.5;
    private final float ANIMATION_SCALE_RANGE_MIN = 1f;
    private final float ANIMATION_SCALE_RANGE_MAX = 8.5f;

    private final AnimatorSet animationInhale = new AnimatorSet();
    private final AnimatorSet animationExhale = new AnimatorSet();

    private final android.os.Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe);

        this.setTitle(getString(R.string.activity_breathe_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        this.inhaleState = new InhaleState(this);
        this.exhaleState  = new ExhaleState(this);

        this.getChosenBreathsFromPrefs();
        this.setUpNumBreathsBtn();
        this.setUpViewVisibility();
        this.setUpMainBreatheBtn();
        this.initializeAnimationInhale();
        this.initializeAnimationExhale();
        this.setState(inhaleState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        this.setState(inhaleState);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // copied from https://stackoverflow.com/questions/2486934/programmatically-relaunch-recreate-an-activity
        // fixes bug when running animation during exhale state, and switching applications and back, will cause
        // bug with the buttons and states when trying to reenter the state
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStop () {
        super.onStop();
        exhaleState.handleOnQuit();
    }

    private void setUpNumBreathsBtn() {
        TextView tvNumBreathesChosen = findViewById(R.id.numBreathesChosen);
        tvNumBreathesChosen.setText(getString(
                (chosenBreaths == 1) ?
                        R.string.singular_breath_chosen_text :
                        R.string.num_breaths_chosen_text, chosenBreaths));

        // https://androidexample365.com/a-simple-android-library-to-implement-a-number-counter-with-increment/
        ElegantNumberButton btnElegantNumber = findViewById(R.id.btnElegantNumber);
        btnElegantNumber.setNumber(String.valueOf(chosenBreaths));
        btnElegantNumber.setOnClickListener((ElegantNumberButton.OnClickListener) view -> {
            chosenBreaths = Integer.parseInt(btnElegantNumber.getNumber());
            tvNumBreathesChosen.setText(getString(
                    (chosenBreaths == 1) ?
                            R.string.singular_breath_chosen_text :
                            R.string.num_breaths_chosen_text, chosenBreaths));

            remainingBreaths = chosenBreaths;
            saveChosenBreathsToPrefs();
        });
    }

    private void setUpViewVisibility() {
        ivCircle = findViewById(R.id.ivCircle);
        tvRemainingBreaths = findViewById(R.id.tvRemainingBreaths);
        ivCircle.setVisibility(View.INVISIBLE);
        tvRemainingBreaths.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpMainBreatheBtn() {
        Button btnBreatheState = findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(getResources().getString(R.string.initial_state_btn_text));

        // https://stackoverflow.com/questions/49972106/android-button-ontouch-if-return-true-has-no-click-animation-effect-if-retu
        // https://stackoverflow.com/questions/11690504/how-to-use-view-ontouchlistener-instead-of-onclick
        btnBreatheState.setOnTouchListener((v, event) -> {
            v.performClick();

            if (!isInitialized) {
                BreatheActivity.this.findViewById(R.id.numBreathesLinearLayout).setVisibility(View.GONE);
                ivCircle.setVisibility(View.VISIBLE);
                tvRemainingBreaths.setVisibility(View.VISIBLE);
                tvRemainingBreaths.setText(BreatheActivity.this.getString(R.string.remaining_breaths_text, chosenBreaths));
                isInitialized = true;
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                currentState.handleOnTouch();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                currentState.handleOnRelease();
            }
            return false;
        });
    }

    private void initializeAnimationInhale() {
        //https://stackoverflow.com/questions/33916287/android-scale-image-view-with-animation/33916973
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(ivCircle, ViewGroup.SCALE_X, ANIMATION_SCALE_RANGE_MIN, ANIMATION_SCALE_RANGE_MAX);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(ivCircle, ViewGroup.SCALE_Y, ANIMATION_SCALE_RANGE_MIN, ANIMATION_SCALE_RANGE_MAX);

        final long animationDuration = TEN_SECONDS_IN_MILLISECONDS * (long) ANIMATION_RATE;
        scaleUpX.setDuration(animationDuration);
        scaleUpY.setDuration(animationDuration);

        animationInhale.play(scaleUpX).with(scaleUpY);
        animationInhale.setInterpolator(new LinearOutSlowInInterpolator());
    }

    private void initializeAnimationExhale() {
        //https://stackoverflow.com/questions/33916287/android-scale-image-view-with-animation/33916973
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(ivCircle, ViewGroup.SCALE_X, ANIMATION_SCALE_RANGE_MAX, ANIMATION_SCALE_RANGE_MIN);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(ivCircle, ViewGroup.SCALE_Y, ANIMATION_SCALE_RANGE_MAX, ANIMATION_SCALE_RANGE_MIN);

        final long animationDuration = TEN_SECONDS_IN_MILLISECONDS * (long) ANIMATION_RATE;
        scaleDownX.setDuration(animationDuration);
        scaleDownY.setDuration(animationDuration);

        animationExhale.play(scaleDownX).with(scaleDownY);
        animationExhale.setInterpolator(new LinearOutSlowInInterpolator());
    }

    public void setState(BreatheState newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    public int getRemainingBreaths() {
        return remainingBreaths;
    }

    public void setRemainingBreaths(int remainingBreathes) {
        this.remainingBreaths = remainingBreathes;
    }

    public BreatheState getInhaleState() {
        return inhaleState;
    }

    public BreatheState getExhaleState() {
        return exhaleState;
    }

    public ImageView getIvCircle() {
        return ivCircle;
    }

    public TextView getTvRemainingBreaths() {
        return tvRemainingBreaths;
    }

    private void saveChosenBreathsToPrefs() {
        SharedPreferences numBreathesPrefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        numBreathesPrefs.edit()
                .putInt(NUM_CHOSEN_BREATHS, chosenBreaths)
                .apply();
    }

    private void getChosenBreathsFromPrefs() {
        SharedPreferences numBreathesPrefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        chosenBreaths = numBreathesPrefs.getInt(NUM_CHOSEN_BREATHS, 1);
        remainingBreaths = chosenBreaths;
    }

    public AnimatorSet getAnimationInhale() {
        return animationInhale;
    }

    public AnimatorSet getAnimationExhale() {
        return animationExhale;
    }

    public Handler getTimerHandler() {
        return timerHandler;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreatheActivity.class);
    }
}