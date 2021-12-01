package cmpt276.phosphorus.childapp.breathe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.utils.BreatheState;
import cmpt276.phosphorus.childapp.breathe.utils.ConfigureState;
import cmpt276.phosphorus.childapp.breathe.utils.ExhaleState;
import cmpt276.phosphorus.childapp.breathe.utils.IdleState;
import cmpt276.phosphorus.childapp.breathe.utils.InhaleState;

public class BreatheActivity extends AppCompatActivity {
    public final BreatheState inhaleState = new InhaleState(this);
    public final BreatheState exhaleState = new ExhaleState(this);
    public final BreatheState configureState = new ConfigureState(this);
    private BreatheState currentState = new IdleState(this);

    private int totalBreaths;
    private int remainingBreaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe);

        this.setUpMainBreatheBtn();
        setState(inhaleState);
    }

    public void setState(BreatheState newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpMainBreatheBtn() {
        Button breatheBtn = findViewById(R.id.btnBreatheState);

        // https://stackoverflow.com/questions/11690504/how-to-use-view-ontouchlistener-instead-of-onclick
        breatheBtn.setOnTouchListener((v, event) -> {
            switch (event.getAction()){
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
}