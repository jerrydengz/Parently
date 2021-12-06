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
//    private final Runnable timerRunnableThreeSeconds = this::handleThreeSecsPassed;
//    private final Runnable timerRunnableTenSeconds = this::handleTenSecsPassed;

    public InhaleState(BreatheActivity context) {
        super(context);
    }

    private void initializeInhaleCountDownTimer(){
        timer = new CountDownTimer(TEN_SECONDS,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished == TEN_SECONDS - THREE_SECONDS){
                    handleThreeSecsPassed();
                }
            }

            @Override
            public void onFinish() {
                handleTenSecsPassed();
            }
        };
    }

    @Override
    public void handleEnter() {
        super.handleEnter();
//        timerHandler.removeCallbacks(timerRunnableThreeSeconds);
//        timerHandler.removeCallbacks(timerRunnableTenSeconds);
        stopAnimation(); // TODO
        initializeInhaleCountDownTimer();
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        // TODO (jack) - set guide text
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_in);

        // remove the handler running the 10 second runnable from ExhaleState
        // https://stackoverflow.com/questions/5883635/how-to-remove-all-callbacks-from-a-handler
        // timerHandler.removeCallbacks(timerRunnableTenSeconds);

//        timerHandler.postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
//        timerHandler.postDelayed(timerRunnableTenSeconds, TEN_SECONDS);

        // start and/or restart the timer
        timer.start();

        // TODO (jack) - 1. stop sound from exhale state (if playing)
        // TODO (jack) - 2. play mc sound for inhale state {0:00 - 0:10}
        startInhaleAnimation();
    }



    @Override
    public void handleOnRelease() {
        super.handleOnRelease();
        if (hasHeldThreeSecs) {
            context.setState(context.getExhaleState());
        } else {
//            timerHandler.removeCallbacks(timerRunnableThreeSeconds);
//            timerHandler.removeCallbacks(timerRunnableTenSeconds);

            // stop the timer
            timer.cancel();
            resetAnimation();
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();
//        timerHandler.removeCallbacksAndMessages(null); // TODO
//        timerHandler.removeCallbacks(timerRunnableThreeSeconds);
//        timerHandler.removeCallbacks(timerRunnableTenSeconds);

        timer.cancel();

        hasHeldThreeSecs = false;
        hasHeldTenSecs = false; // TODO - prob useless

        stopAnimation();
    }

    private void handleThreeSecsPassed() {
        hasHeldThreeSecs = true;
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setText(R.string.breathe_state_out);
    }

    private void handleTenSecsPassed() {
        hasHeldTenSecs = true;
        // TODO (jack) - Stop sound for inhale state

        stopAnimation();
        // white = inhale ten seconds runnable stops
        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.white));
    }

    private void startInhaleAnimation() {
        //https://stackoverflow.com/questions/33916287/android-scale-image-view-with-animation/33916973
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(context.getCircleAnimationView(), ViewGroup.SCALE_X, 8.5f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(context.getCircleAnimationView(), ViewGroup.SCALE_Y, 8.5f);

        final long animationDuration = TEN_SECONDS * (long) 2.5;
        scaleUpX.setDuration(animationDuration);
        scaleUpY.setDuration(animationDuration);

        animation.play(scaleUpX).with(scaleUpY);
        animation.setInterpolator(new LinearOutSlowInInterpolator());

        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.chalk_red));

        animation.start();
    }

    private void resetAnimation() {
        // https://stackoverflow.com/questions/45629326/trying-to-reset-values-from-property-animator-to-be-used-in-recycler-view/45700580#45700580
        animation.cancel();
        animation.reverse();
        animation.end();
    }

    private void stopAnimation() {
        animation.cancel();
        animation.end();
    }
}
