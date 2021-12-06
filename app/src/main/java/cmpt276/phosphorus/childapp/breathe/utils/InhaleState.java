package cmpt276.phosphorus.childapp.breathe.utils;

import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public class InhaleState extends BreatheState {
    private final Runnable timerHandlerThreeSecs = this::handleThreeSecsPassed;
    private final Runnable timerHandlerTenSecs = this::handleTenSecsPassed;

    public InhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();
        context.getAnimationExhale().cancel();
        context.getAnimationExhale().end();
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        // TODO (jack) - set guide text
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_in);

        // stop the timer from ExhaleState for the 10 secs timer
        context.getTimer().cancel();

        // start and/or restart the timer
        timerHandler.postDelayed(timerHandlerThreeSecs, THREE_SECONDS);
        timerHandler.postDelayed(timerHandlerTenSecs, TEN_SECONDS);

        // TODO (jack) - play mc sound for inhale state {0:00 - 0:10}
        startInhaleAnimation();
    }

    private void startInhaleAnimation() {
        //https://stackoverflow.com/questions/33916287/android-scale-image-view-with-animation/33916973
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(context.getCircleAnimationView(), ViewGroup.SCALE_X, 8.5f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(context.getCircleAnimationView(), ViewGroup.SCALE_Y, 8.5f);

        final long animationDuration = TEN_SECONDS * (long) ANIMATION_RATE;
        scaleUpX.setDuration(animationDuration);
        scaleUpY.setDuration(animationDuration);

        context.getAnimationInhale().play(scaleUpX).with(scaleUpY);
        context.getAnimationInhale().setInterpolator(new LinearOutSlowInInterpolator());

        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.chalk_red));

        context.getAnimationInhale().start();
    }

    @Override
    public void handleOnRelease() {
        super.handleOnRelease();
        if (hasHeldThreeSecs) {
            context.setState(context.getExhaleState());
        } else {
            // stop the timer
            timerHandler.removeCallbacksAndMessages(null);
            resetAnimation();
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();

        // https://stackoverflow.com/questions/5883635/how-to-remove-all-callbacks-from-a-handler
        timerHandler.removeCallbacksAndMessages(null);
        hasHeldThreeSecs = false;

        context.getAnimationInhale().cancel();
        context.getAnimationInhale().end();
    }

    private void handleThreeSecsPassed() {
        hasHeldThreeSecs = true;
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_out);
    }

    private void handleTenSecsPassed() {

        // TODO (jack) - Stop sound for inhale state
        context.getAnimationInhale().cancel();
        context.getAnimationInhale().end();

        // TODO - jack, use this as visual indicator for 10 secs has passed, remove when you're done
        // white = inhale ten seconds runnable stops
        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.white));
    }

    private void resetAnimation() {
        // https://stackoverflow.com/questions/45629326/trying-to-reset-values-from-property-animator-to-be-used-in-recycler-view/45700580#45700580
        context.getAnimationInhale().cancel();
        context.getAnimationInhale().reverse();
        context.getAnimationInhale().end();
    }


}
