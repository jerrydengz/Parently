package cmpt276.phosphorus.childapp.breathe.utils;

import android.widget.Button;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

// ==============================================================================================
//
// Inhale while holding down the button
//
// ==============================================================================================
public class InhaleState extends BreatheState {

    private final Runnable timerRunnableThreeSeconds = this::handleThreeSecsPassed;
    private final Runnable timerRunnableTenSeconds = this::handleTenSecsPassed;

    public InhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        // TODO (jack) - set guide text
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_in);

        // remove the handler running the 10 second runnable from ExhaleState
        // https://stackoverflow.com/questions/5883635/how-to-remove-all-callbacks-from-a-handler
        context.timerHandler.removeCallbacksAndMessages(null);
        context.animationExhale.cancel();
        context.animationExhale.end();

        context.timerHandler.postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
        context.timerHandler.postDelayed(timerRunnableTenSeconds, TEN_SECONDS);

        // TODO (jack) - 1. stop sound from exhale state (if playing)
        // TODO (jack) - 2. play mc sound for inhale state {0:00 - 0:10}
        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.chalk_red));
        context.animationInhale.start();
    }

    @Override
    public void handleOnRelease() {
        super.handleOnRelease();
        if (hasHeldThreeSecs) {
            context.setState(context.getExhaleState());
        } else {
            // stop the timer
            context.timerHandler.removeCallbacksAndMessages(null);
            resetAnimationInhale();
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();
        context.timerHandler.removeCallbacksAndMessages(null);

        hasHeldThreeSecs = false;

        stopAnimationInhale();
    }

    private void handleThreeSecsPassed() {
        hasHeldThreeSecs = true;
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_out);
    }

    private void handleTenSecsPassed() {
        // TODO (jack) - Stop sound for inhale state

        stopAnimationInhale();

        // visual marker
        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.white));
    }

    private void resetAnimationInhale(){
        // https://stackoverflow.com/questions/45629326/trying-to-reset-values-from-property-animator-to-be-used-in-recycler-view/45700580#45700580
        context.animationInhale.cancel();
        context.animationInhale.reverse();
        context.animationInhale.end();
    }

    private void stopAnimationInhale(){
        context.animationInhale.cancel();
        context.animationInhale.end();
    }

}
