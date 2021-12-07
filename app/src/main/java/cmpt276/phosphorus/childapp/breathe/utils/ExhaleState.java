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
    private final Runnable timerRunnableTenSeconds = this::handleTenSecsPassed;

    public ExhaleState(BreatheActivity context) {
        super(context);
    }

    @Override
    public void handleEnter() {
        super.handleEnter();

        // disable button to be touched
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setEnabled(false);

        context.getTimerHandler().postDelayed(timerRunnableThreeSeconds, THREE_SECONDS);
        context.getTimerHandler().postDelayed(timerRunnableTenSeconds, TEN_SECONDS);

        // TODO (jack) - play mc sound {0:10-0:20}
        // TODO (jack) - update guide text

        context.getIvCircle().setColorFilter(context.getColor(R.color.chalk_red_var));
        context.getAnimationExhale().start();
    }

    private void updateBreathesLeft() {
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setEnabled(true);

        context.setRemainingBreaths(context.getRemainingBreaths() - 1);
        context.getTvRemainingBreaths().setText(
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

    private void handleTenSecsPassed() {
        // TODO (jack) - stop exhale sound

        stopAnimationExhale();

        // TODO - visual marker, remove when done
        context.getIvCircle().setColorFilter(context.getColor(R.color.black));
    }

    private void stopAnimationExhale() {
        context.getAnimationExhale().cancel();
        context.getAnimationExhale().end();
    }
}
