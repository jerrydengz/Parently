package cmpt276.phosphorus.childapp.breathe.utils;

import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

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
    private boolean hasHeldThreeSecs = false;

    public InhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        TextView guideMessage = context.findViewById(R.id.guideMessage);
        guideMessage.setText(R.string.guide_message);

        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_in);

        // remove the handler running the 10 second runnable from ExhaleState
        // https://stackoverflow.com/questions/5883635/how-to-remove-all-callbacks-from-a-handler
        stopExhaleStateActivities();

        context.getTimerHandler().postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
        context.getTimerHandler().postDelayed(timerRunnableTenSeconds, TEN_SECONDS);


        context.getIvCircle().setColorFilter(context.getColor(R.color.chalk_red));
        context.getAnimationInhale().start();
        context.getCurrentSoundInhale().start();
    }

    @Override
    public void handleOnRelease() {
        super.handleOnRelease();
        if (hasHeldThreeSecs) {
            context.setState(context.getExhaleState());
        } else {
            // stop all queued timer runnable
            context.getTimerHandler().removeCallbacksAndMessages(null);
            resetAnimationInhale();
        }
        this.stopSound();
    }


    @Override
    public void handleExit() {
        super.handleExit();
        context.getTimerHandler().removeCallbacksAndMessages(null);

        hasHeldThreeSecs = false;

        stopSound();
        stopAnimationInhale();
    }

    private void handleThreeSecsPassed() {
        hasHeldThreeSecs = true;
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_out);

        TextView guideMessage = context.findViewById(R.id.guideMessage);
        guideMessage.setText(R.string.guide_exhale);
    }

    private void handleTenSecsPassed() {
        stopSound();
        stopAnimationInhale();

        context.getIvCircle().setColorFilter(context.getColor(R.color.white));
    }

    private void resetAnimationInhale() {
        // https://stackoverflow.com/questions/45629326/trying-to-reset-values-from-property-animator-to-be-used-in-recycler-view/45700580#45700580
        context.getAnimationInhale().cancel();
        context.getAnimationInhale().reverse();
        context.getAnimationInhale().end();
    }

    private void stopAnimationInhale() {
        context.getAnimationInhale().cancel();
        context.getAnimationInhale().end();
    }

    private void stopExhaleStateActivities() {
        context.getTimerHandler().removeCallbacksAndMessages(null);
        context.getAnimationExhale().cancel();
        context.getAnimationExhale().end();

        try {
            context.getCurrentSoundExhale().stop();
            context.getCurrentSoundExhale().prepare();
        } catch (IOException ignored) {
        }
    }

    private void stopSound() {
        try {
            context.getCurrentSoundInhale().stop();
            context.getCurrentSoundInhale().prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
