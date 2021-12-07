package cmpt276.phosphorus.childapp.breathe.utils;

import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

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

        TextView guideMessage = context.findViewById(R.id.guideMessage);
        guideMessage.setText(R.string.guide_exhale);

        context.getCurrentSoundExhale().start();

        context.getIvCircle().setColorFilter(context.getColor(R.color.green_c1));
        context.getAnimationExhale().start();
    }

    @Override
    public void handleOnQuit() {
        super.handleOnQuit();
        this.stopSound();
    }

    private void updateBreathesLeft() {
        Button btnBreatheState = context.findViewById(R.id.btnBreatheState);
        btnBreatheState.setEnabled(true);
        TextView guideMessage = context.findViewById(R.id.guideMessage);

        context.setRemainingBreaths(context.getRemainingBreaths() - 1);
        context.getTvRemainingBreaths().setText(context.getString(R.string.remaining_breaths_text, context.getRemainingBreaths()));

        if (context.getRemainingBreaths() > 0) {
            guideMessage.setText(R.string.guide_message);
            btnBreatheState.setText(R.string.breathe_state_in);

            context.setState(context.getInhaleState());
        } else {
            guideMessage.setText(R.string.guide_complete);
            btnBreatheState.setText(R.string.breathe_state_finished);

            btnBreatheState.setOnClickListener(view -> {
                this.stopSound();
                stopAnimationExhale();
                context.finish();
            });
        }
    }

    private void handleTenSecsPassed() {
        stopSound();
        stopAnimationExhale();
        context.getIvCircle().setColorFilter(context.getColor(R.color.black));
    }

    private void stopAnimationExhale() {
        context.getAnimationExhale().cancel();
        context.getAnimationExhale().end();
    }

    private void stopSound() {
        try {
            context.getCurrentSoundExhale().stop();
            context.getCurrentSoundExhale().prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
