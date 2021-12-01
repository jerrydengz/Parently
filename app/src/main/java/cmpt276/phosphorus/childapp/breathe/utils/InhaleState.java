package cmpt276.phosphorus.childapp.breathe.utils;

import android.os.Handler;
import android.widget.Button;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public class InhaleState extends BreatheState{
    private android.os.Handler timerHandler = new Handler();
    private Runnable timerRunnable3 = this::handleThreeSecsPassed;

    private Runnable timerRunnable10 = this::handleTenSecsPassed;

    private boolean hasHeldThreeSecs = false;
    private boolean hasHeldTenSecs = false;

    private final int SECONDS_3 = 3000;
    private final int SECONDS_10 = 10000;

    public InhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        // set button text, TODO (later) - set guide text
        Button inhaleBtn = context.findViewById(R.id.btnBreatheState);
        inhaleBtn.setText(R.string.breathe_state_in);

        // TODO (whoever is gonna deal with animations) - play sound/animation
        // NOTE: animation should keep playing till it hits max at 10secs

        // set timer for 3 seconds
        timerHandler.postDelayed(timerRunnable3,SECONDS_3);

        // set timer for 10 seconds
        timerHandler.postDelayed(timerRunnable10,SECONDS_10);
    }

    @Override
    public void handleOnRelease() {
        super.handleOnRelease();
        if(hasHeldThreeSecs || hasHeldTenSecs){
            context.setState(context.exhaleState);
        }else{
            // stop the timer
            hasHeldThreeSecs = false;
            timerHandler.removeCallbacks(timerRunnable3);
            timerHandler.removeCallbacks(timerRunnable10);

            // TODO (whoever is gonna deal with animations) - stop & reset animation, stop sound
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();
        timerHandler.removeCallbacks(timerRunnable3);
        timerHandler.removeCallbacks(timerRunnable10);
    }

    private void handleThreeSecsPassed(){
        hasHeldThreeSecs = true;
        Button inhaleBtn = context.findViewById(R.id.btnBreatheState);
        inhaleBtn.setText(R.string.breathe_state_out);
    }

    private void handleTenSecsPassed(){
        hasHeldTenSecs = true;
        // TODO (whoever is gonna deal with animations) - Stop animation, sound
    }


}
