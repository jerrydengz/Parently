package cmpt276.phosphorus.childapp.breathe.utils;

import android.media.MediaPlayer;
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
    private MediaPlayer currentSound;

    public InhaleState(BreatheActivity context) {
        super(context);
        currentSound = MediaPlayer.create(context, R.raw.inhale);
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
        context.getTimerHandler().removeCallbacksAndMessages(null);
        context.getAnimationExhale().cancel();
        context.getAnimationExhale().end();

        context.getTimerHandler().postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
        context.getTimerHandler().postDelayed(timerRunnableTenSeconds, TEN_SECONDS);

        currentSound.start();

        context.getIvCircle().setColorFilter(context.getColor(R.color.chalk_red));
        context.getAnimationInhale().start();
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

    private void stopSound(){
        try {
            currentSound.stop();
            currentSound.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
