package cmpt276.phosphorus.childapp.breathe.utils;

import android.os.Handler;
import android.widget.Button;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public class ExhaleState extends BreatheState {
    private final android.os.Handler timeHandler = new Handler();
    private final Runnable timerRunnableThreeSeconds = this::updateBreathesLeft;
    private final Runnable timerRunnableTenSeconds = () -> {
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
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setClickable(false);

        timeHandler.postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
        timeHandler.postDelayed(timerRunnableTenSeconds, TEN_SECONDS);
    }

    @Override
    public void handleOnTouch() {
        super.handleOnTouch();

        if (context.getRemainingBreaths() > 0) {
            context.setState(context.getInhaleState());
        }
    }

    @Override
    public void handleExit() {
        super.handleExit();
        // https://stackoverflow.com/questions/5883635/how-to-remove-all-callbacks-from-a-handler
        timeHandler.removeCallbacks(null); // removes all queued Runnable

        // TODO - stop animation & sound
    }

    private void updateBreathesLeft() {
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setClickable(true);

        if (context.getRemainingBreaths() > 0) {

            // TODO - update guide text
            btnBreatheState.setText(R.string.breathe_state_in);

            context.setRemainingBreaths(context.getRemainingBreaths() - 1);
        } else {
            // TODO - update guide text

            btnBreatheState.setText(R.string.breathe_state_finished);
            btnBreatheState.setOnClickListener(view -> context.finish());
        }
    }


}
