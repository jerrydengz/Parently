package cmpt276.phosphorus.childapp.breathe.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public class InhaleState extends BreatheState {
    private final android.os.Handler timerHandler = new Handler();
    private final Runnable timerRunnableThreeSeconds = this::handleThreeSecsPassed;
    private final Runnable timerRunnableTenSeconds = this::handleTenSecsPassed;

    private AnimatorSet animation = new AnimatorSet();

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


        // set timer for 3 seconds
        timerHandler.postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);

        // set timer for 10 seconds
        timerHandler.postDelayed(timerRunnableTenSeconds, TEN_SECONDS);

        // TODO (whoever is gonna deal with animations) - play sound
        startExhaleAnimation();

    }

    @Override
    public void handleOnRelease() {
        super.handleOnRelease();
        if (hasHeldThreeSecs) {
            context.setState(context.getExhaleState());
        } else {
            // stop the timer
            timerHandler.removeCallbacks(timerRunnableThreeSeconds);
            timerHandler.removeCallbacks(timerRunnableTenSeconds);
            resetAnimation();
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();
        timerHandler.removeCallbacks(timerRunnableThreeSeconds);
        timerHandler.removeCallbacks(timerRunnableTenSeconds);

        hasHeldThreeSecs = false;
        hasHeldTenSecs = false;

        animation.cancel();
        animation.end();
    }

    private void handleThreeSecsPassed() {
        hasHeldThreeSecs = true;
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_out);
    }

    private void handleTenSecsPassed() {
        hasHeldTenSecs = true;
        // TODO (jack) - Stop sound & animation
        animation.end();
    }

    private void startExhaleAnimation(){
        //https://stackoverflow.com/questions/33916287/android-scale-image-view-with-animation/33916973
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(context.getCircleAnimation(), ViewGroup.SCALE_X, 8.5f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(context.getCircleAnimation(), ViewGroup.SCALE_Y, 8.5f);

        scaleUpX.setDuration(TEN_SECONDS*(long)2.5);
        scaleUpY.setDuration(TEN_SECONDS*(long)2.5);

        animation.play(scaleUpX).with(scaleUpY);
        animation.setInterpolator(new LinearOutSlowInInterpolator());

        animation.start();
    }

    private void resetAnimation(){
    // https://stackoverflow.com/questions/45629326/trying-to-reset-values-from-property-animator-to-be-used-in-recycler-view/45700580#45700580
        animation.cancel();
        animation.reverse();
        animation.end();

    }
}
