package cmpt276.phosphorus.childapp.breathe.utils;

import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public class ExhaleState extends BreatheState {
    private final Runnable timerHandlerThreeSecs = this::updateBreathesLeft;
    public ExhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();

        // TODO (jack) - play mc sound {0:10-0:20}
        // TODO (jack) - update guide text

        // disable button to be touched
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setEnabled(false);

        context.getTimer().start();
        System.out.println("Exhale 10 second timer started!");
        timerHandler.postDelayed(timerHandlerThreeSecs, THREE_SECONDS);

        context.getAnimationExhale().cancel();
        context.getAnimationExhale().end();
        startExhaleAnimation();
    }

    private void startExhaleAnimation() {
        //https://stackoverflow.com/questions/33916287/android-scale-image-view-with-animation/33916973
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(context.getCircleAnimationView(), ViewGroup.SCALE_X, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(context.getCircleAnimationView(), ViewGroup.SCALE_Y, 1f);

        final long animationDuration = TEN_SECONDS * (long) ANIMATION_RATE;
        scaleDownX.setDuration(animationDuration);
        scaleDownY.setDuration(animationDuration);

        context.getAnimationExhale().play(scaleDownX).with(scaleDownY);
        context.getAnimationExhale().setInterpolator(new LinearOutSlowInInterpolator());
        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.chalk_red_var));

        context.getAnimationExhale().start();
    }

    private void updateBreathesLeft() {
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setEnabled(true);

        context.setRemainingBreaths(context.getRemainingBreaths() - 1);
        context.getRemainBreathsView().setText(
                context.getString(R.string.remaining_breaths_text, context.getRemainingBreaths()));

        if (context.getRemainingBreaths() > 0) {
            // TODO (jack) - update guide text
            btnBreatheState.setText(R.string.breathe_state_in);
            context.setState(context.getInhaleState());
        } else {
            // TODO (jack) - update guide text
            btnBreatheState.setText(R.string.breathe_state_finished);
            btnBreatheState.setOnClickListener(view -> {
                context.getAnimationExhale().cancel();
                context.getAnimationExhale().end();
                context.finish();
            });
        }
    }

}
