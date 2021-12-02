package cmpt276.phosphorus.childapp.breathe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

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

    // TODO - save totalBreaths to sharedPrefs
    private int totalBreaths;
    private int remainingBreaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe);

        this.setUpNumBreathesBtn();
        this.initialize();
    }
    // https://androidexample365.com/a-simple-android-library-to-implement-a-number-counter-with-increment/
    @SuppressLint("SetTextI18n")
    private void setUpNumBreathesBtn() {
        TextView numBreathsDisplayed = findViewById(R.id.numBreathesChosen);
        numBreathsDisplayed.setText(getResources().getString(R.string.num_breathes_chosen_text) + 1);
        ElegantNumberButton btn = findViewById(R.id.elegantNumberButton);
        btn.setOnClickListener((ElegantNumberButton.OnClickListener) view -> {
            totalBreaths = Integer.parseInt(btn.getNumber());
            numBreathsDisplayed.setText(getResources().getString(R.string.num_breathes_chosen_text) + totalBreaths);

            remainingBreaths = totalBreaths;
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpMainBreatheBtn() {
        Button btnBreatheState = findViewById(R.id.btnBreatheState);

        // https://stackoverflow.com/questions/49972106/android-button-ontouch-if-return-true-has-no-click-animation-effect-if-retu
        // https://stackoverflow.com/questions/11690504/how-to-use-view-ontouchlistener-instead-of-onclick
        btnBreatheState.setOnTouchListener((v, event) -> {

            LinearLayout numBreathesConfigure = findViewById(R.id.numBreathesLinearLayout);
            numBreathesConfigure.setVisibility(View.GONE);

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    currentState.handleOnTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    currentState.handleOnRelease();
                    break;
            }

            v.setVisibility(View.INVISIBLE);
            v.setVisibility(View.VISIBLE);

            return false;
        });
    }

    private void initialize(){
        this.setUpMainBreatheBtn();
        setState(inhaleState);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreatheActivity.class);
    }

    public int getTotalBreaths() {
        return totalBreaths;
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
}