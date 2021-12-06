package cmpt276.phosphorus.childapp.breathe.utils;

import android.widget.Button;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

// ==============================================================================================
//
// Exhale state with timing button presses
//
// ==============================================================================================
public class ExhaleState extends BreatheState {

    private final Runnable timerRunnableThreeSeconds = this::updateBreathesLeft;
    private final Runnable timerRunnableTenSeconds = () -> {
        // TODO (jack) - stop sound

        stopAnimationExhale();

        // visual marker
        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.black));
    };

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

        context.timerHandler.postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
        context.timerHandler.postDelayed(timerRunnableTenSeconds, TEN_SECONDS);

        context.getCircleAnimationView().setColorFilter(context.getColor(R.color.chalk_red_var));
        context.animationExhale.start();
    }

    @Override
    public void handleExit() {
        super.handleExit();
        context.timerHandler.removeCallbacks(timerRunnableThreeSeconds);

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
                stopAnimationExhale();
                context.finish();
            });
        }
    }

    private void stopAnimationExhale(){
        context.animationExhale.cancel();
        context.animationExhale.end();
    }
}
