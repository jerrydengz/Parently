package cmpt276.phosphorus.childapp.breathe.utils;

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
        // TODO - stop sound
        stopAnimation();
    };

    private boolean isTransitionToInhaleState = false;

    public ExhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();

        // TODO - play (different?) sound
        // TODO - update guide text

        // disable button to be touched
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setEnabled(false);

        timerHandler.postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
        timerHandler.postDelayed(timerRunnableTenSeconds, TEN_SECONDS);

        startExhaleAnimation();
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        if(isTransitionToInhaleState){
            context.setState(context.getInhaleState());
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();
        timerHandler.removeCallbacks(timerRunnableThreeSeconds);
        timerHandler.removeCallbacks(timerRunnableTenSeconds);

        // TODO - stop sound
        stopAnimation();
        isTransitionToInhaleState = false;
    }

    private void updateBreathesLeft() {
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setEnabled(true);


        context.setRemainingBreaths(context.getRemainingBreaths() - 1);
        // TODO - display updated remaining breathes

        if (context.getRemainingBreaths() > 0) {

            // TODO - update guide text
            btnBreatheState.setText(R.string.breathe_state_in);
            isTransitionToInhaleState = true;
        } else {
            // TODO - update guide text

            btnBreatheState.setText(R.string.breathe_state_finished);
            btnBreatheState.setOnClickListener(view -> {
                animation.cancel();
                animation.end();
                context.finish();
            });
        }
    }

    private void startExhaleAnimation(){
        //https://stackoverflow.com/questions/33916287/android-scale-image-view-with-animation/33916973
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(context.getCircleAnimation(), ViewGroup.SCALE_X, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(context.getCircleAnimation(), ViewGroup.SCALE_Y, 1f);

        final int animationDuration = TEN_SECONDS*2;
        scaleDownX.setDuration(animationDuration);
        scaleDownY.setDuration(animationDuration);

        animation.play(scaleDownX).with(scaleDownY);
        animation.setInterpolator(new LinearInterpolator());

        animation.start();
    }

    private void stopAnimation(){
        animation.end();
    }
}
