package cmpt276.phosphorus.childapp.breathe.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public class ExhaleState extends BreatheState {
    private final android.os.Handler timerHandler = new Handler();
    private final Runnable timerRunnableThreeSeconds = this::updateBreathesLeft;
    private final Runnable timerRunnableTenSeconds = () -> {
        // TODO - stop animation and sound
    };

    public ExhaleState(BreatheActivity context) {
        super(context);
    }
    private AnimatorSet animation = new AnimatorSet();

    @Override
    public void handleEnter() {
        super.handleEnter();

        // TODO - play animation, play (different?) sound
        // TODO - update guide text

        // disable button to be touched
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setClickable(false);

        timerHandler.postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
        timerHandler.postDelayed(timerRunnableTenSeconds, TEN_SECONDS);
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();
    }

    @Override
    public void handleExit() {
        super.handleExit();
        timerHandler.removeCallbacks(timerRunnableThreeSeconds);
        timerHandler.removeCallbacks(timerRunnableTenSeconds);

        // TODO - stop animation & sound
    }

    private void updateBreathesLeft() {
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setClickable(true);

        context.setRemainingBreaths(context.getRemainingBreaths() - 1);
        // TODO - display updated remaining breathes

        if (context.getRemainingBreaths() > 0) {

            // TODO - update guide text
            btnBreatheState.setText(R.string.breathe_state_in);
            context.setState(context.getInhaleState());
        } else {
            // TODO - update guide text

            btnBreatheState.setText(R.string.breathe_state_finished);
            btnBreatheState.setOnClickListener(view -> context.finish());
        }
    }

    private void startExhaleAnimation(){
        //https://stackoverflow.com/questions/33916287/android-scale-image-view-with-animation/33916973
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(context.getCircleAnimation(), ViewGroup.SCALE_X, 8.5f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(context.getCircleAnimation(), ViewGroup.SCALE_Y, 8.5f);

        scaleUpX.setDuration(TEN_SECONDS*2);
        scaleUpY.setDuration(TEN_SECONDS*2);

        animation.play(scaleUpX).with(scaleUpY);
        animation.setInterpolator(new LinearInterpolator());

        animation.start();
    }
}
