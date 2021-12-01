package cmpt276.phosphorus.childapp.breathe.utils;

import android.os.Handler;
import android.widget.Button;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public class ExhaleState extends BreatheState {
    private android.os.Handler timeHandler = new Handler();
    private Runnable timerRunnable3 = this::updateBreathesLeft;
    private Runnable timerRunnable10 = () -> {
        // TODO - stop animation and sound
    };

    public ExhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();

        // TODO - play animation, play (different?) sound
        // TODO - update guide text

        // disable button to be touched
        Button mainBreatheBtn = context.findViewById(R.id.btnBreatheState);
        mainBreatheBtn.setClickable(false);

        timeHandler.postDelayed(timerRunnable3, SECONDS_3);
        timeHandler.postDelayed(timerRunnable10, SECONDS_10);
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        if (context.getRemainingBreaths() > 0) {
            context.setState(context.inhaleState);
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();
        timeHandler.removeCallbacks(null); // removes all queued Runnable

        // TODO - stop animation & sound
    }

    private void updateBreathesLeft() {
        Button mainBreatheBtn = context.findViewById(R.id.btnBreatheState);
        mainBreatheBtn.setClickable(true);

        if (context.getRemainingBreaths() > 0) {

            // TODO - update guide text
            mainBreatheBtn.setText(R.string.breathe_state_in);

            context.setRemainingBreaths(context.getRemainingBreaths() - 1);
        } else {
            // TODO - update guide text

            mainBreatheBtn.setText(R.string.breathe_state_finished);
            mainBreatheBtn.setOnClickListener(view -> context.finish());
        }
    }


}
