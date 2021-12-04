package cmpt276.phosphorus.childapp.breathe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private ImageView circle;

    // TODO - save totalBreaths to sharedPrefs
    private int chosenBreathes;
    private int remainingBreaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe);

        this.setTitle(getString(R.string.activity_breathe_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        this.initialize();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        this.setUpMainBreatheBtn();
        this.setUpNumBreathesBtn();
        setState(inhaleState);
    }

    // https://androidexample365.com/a-simple-android-library-to-implement-a-number-counter-with-increment/
    @SuppressLint("SetTextI18n")
    private void setUpNumBreathesBtn() {

        // TODO - update to previously chosen breathes, set ElegantNumberButton to prev chosen breathes
        TextView numBreathsDisplayed = findViewById(R.id.numBreathesChosen);
        numBreathsDisplayed.setText(getResources().getString(R.string.num_breathes_chosen_text) + 1);

        ElegantNumberButton btn = findViewById(R.id.elegantNumberButton);
        btn.setOnClickListener((ElegantNumberButton.OnClickListener) view -> {
            chosenBreathes = Integer.parseInt(btn.getNumber());
            numBreathsDisplayed.setText(getResources().getString(R.string.num_breathes_chosen_text) + chosenBreathes);

            remainingBreaths = chosenBreathes;
        });

    }

    public void setState(BreatheState newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    @Override
    public void onResume() {
        super.onResume();

        this.initialize();
        toggleChooseBreathesOff(View.VISIBLE);
        circle.setVisibility(View.GONE);
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

    @SuppressLint("ClickableViewAccessibility")
    private void setUpMainBreatheBtn() {
        Button btnBreatheState = findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(getResources().getString(R.string.initial_state_btn_text));

        circle = findViewById(R.id.circleBreatheAnimation);

        // https://stackoverflow.com/questions/49972106/android-button-ontouch-if-return-true-has-no-click-animation-effect-if-retu
        // https://stackoverflow.com/questions/11690504/how-to-use-view-ontouchlistener-instead-of-onclick
        btnBreatheState.setOnTouchListener((v, event) -> {
            toggleChooseBreathesOff(View.GONE);
            circle.setVisibility(View.VISIBLE);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    currentState.handleOnTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    currentState.handleOnRelease();
                    break;
            }

            return false;
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreatheActivity.class);
    }

    // TODO - for later
    public int getChosenBreathes() {
        return chosenBreathes;
    }

    public int getRemainingBreaths() {
        return remainingBreaths;
    }

    public void setRemainingBreaths(int remainingBreaths) {
        this.remainingBreaths = remainingBreaths;
    }

    public BreatheState getInhaleState() {
        return inhaleState;
    }

    public BreatheState getExhaleState() {
        return exhaleState;
    }

    public ImageView getCircleAnimation(){ return circle;}

    private void toggleChooseBreathesOff(int visibility){
        LinearLayout numBreathesConfigure = findViewById(R.id.numBreathesLinearLayout);
        numBreathesConfigure.setVisibility(visibility);
    }

}