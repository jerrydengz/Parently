package cmpt276.phosphorus.childapp.breathe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.utils.BreatheState;
import cmpt276.phosphorus.childapp.breathe.utils.ConfigureState;
import cmpt276.phosphorus.childapp.breathe.utils.ExhaleState;
import cmpt276.phosphorus.childapp.breathe.utils.IdleState;
import cmpt276.phosphorus.childapp.breathe.utils.InhaleState;

public class BreatheActivity extends AppCompatActivity {
    private final BreatheState inhaleState = new InhaleState(this);
    private final BreatheState exhaleState = new ExhaleState(this);
    private final BreatheState configureState = new ConfigureState(this);
    private BreatheState currentState = new IdleState(this);

    private ImageView circleImgView;
    private TextView remainBreathsText;
    private boolean isInitialized = false;

    private int remainingBreaths;
    private int chosenBreaths;
    private final String APP_PREFS = "ParentApp";
    private final String NUM_CHOSEN_BREATHS = "NumChosenBreaths - BreatheActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe);

        this.setTitle(getString(R.string.activity_breathe_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        this.getChosenBreathsFromPrefs();
        this.setUpNumBreathsBtn();
        this.setUpViewVisibility();
        this.setUpMainBreatheBtn();
        setState(inhaleState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
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

    public void setState(BreatheState newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    // https://androidexample365.com/a-simple-android-library-to-implement-a-number-counter-with-increment/
    private void setUpNumBreathsBtn() {
        TextView tvNumBreathesChosen = findViewById(R.id.numBreathesChosen);
        tvNumBreathesChosen.setText(getString(
                (chosenBreaths == 1) ?
                        R.string.singular_breath_chosen_text :
                        R.string.num_breaths_chosen_text, chosenBreaths));

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
        circleImgView = findViewById(R.id.circleBreatheAnimation);
        remainBreathsText = findViewById(R.id.remainingBreathesText);
        circleImgView.setVisibility(View.INVISIBLE);
        remainBreathsText.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpMainBreatheBtn() {
        Button btnBreatheState = findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(getResources().getString(R.string.initial_state_btn_text));

        // https://stackoverflow.com/questions/49972106/android-button-ontouch-if-return-true-has-no-click-animation-effect-if-retu
        // https://stackoverflow.com/questions/11690504/how-to-use-view-ontouchlistener-instead-of-onclick
        btnBreatheState.setOnTouchListener((v, event) -> {

            if (!isInitialized) {
                findViewById(R.id.numBreathesLinearLayout).setVisibility(View.GONE);
                circleImgView.setVisibility(View.VISIBLE);
                remainBreathsText.setVisibility(View.VISIBLE);
                remainBreathsText.setText(getString(R.string.remaining_breaths_text, chosenBreaths));
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreatheActivity.class);
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

    public ImageView getCircleAnimationView() {
        return circleImgView;
    }

    private void saveChosenBreathsToPrefs() {
        SharedPreferences numBreathesPrefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = numBreathesPrefs.edit();
        editor.putInt(NUM_CHOSEN_BREATHS, chosenBreaths);
        editor.apply();
    }

    private void getChosenBreathsFromPrefs() {
        SharedPreferences numBreathesPrefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        chosenBreaths = numBreathesPrefs.getInt(NUM_CHOSEN_BREATHS, 1);
        remainingBreaths = chosenBreaths;
    }

    public TextView getRemainBreathsView() {
        return remainBreathsText;
    }
}