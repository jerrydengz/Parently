package cmpt276.phosphorus.childapp.breathe.utils;

import android.os.Handler;
import android.widget.Button;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public class InhaleState extends BreatheState{
    private final android.os.Handler timerHandler = new Handler();
    private final Runnable timerRunnableThreeSeconds = this::handleThreeSecsPassed;
    private final Runnable timerRunnableTenSeconds = this::handleTenSecsPassed;

    public InhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();
        timerHandler.removeCallbacks(timerRunnableThreeSeconds);
        timerHandler.removeCallbacks(timerRunnableTenSeconds);
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        // set button text, TODO (later) - set guide text
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_in);

        // TODO (whoever is gonna deal with animations) - play sound & animation
        // NOTE: animation should keep playing till it hits max at 10secs

        // set timer for 3 seconds
        timerHandler.postDelayed(timerRunnableThreeSeconds,THREE_SECONDS);

        // set timer for 10 seconds
        timerHandler.postDelayed(timerRunnableTenSeconds,TEN_SECONDS);
    }

    @Override
    public void handleOnRelease() {
        super.handleOnRelease();
        if(hasHeldThreeSecs){
            context.setState(context.getExhaleState());
        }else{
            // stop the timer
            timerHandler.removeCallbacks(timerRunnableThreeSeconds);
            timerHandler.removeCallbacks(timerRunnableTenSeconds);

            // TODO (whoever is gonna deal with animations) - stop & reset animation, stop sound
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();
        timerHandler.removeCallbacks(timerRunnableThreeSeconds);
        timerHandler.removeCallbacks(timerRunnableTenSeconds);

        hasHeldThreeSecs = false;
        hasHeldTenSecs = false;
    }

    private void handleThreeSecsPassed(){
        hasHeldThreeSecs = true;
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_out);
    }

    private void handleTenSecsPassed(){
        hasHeldTenSecs = true;
        // TODO (whoever is gonna deal with animations) - Stop animation, sound
    }


}
