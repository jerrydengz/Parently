package cmpt276.phosphorus.childapp.breathe.utils;

import android.media.MediaPlayer;
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

    // Ref https://developer.android.com/reference/android/media/MediaPlayer
    private MediaPlayer currentSound;

    public ExhaleState(BreatheActivity context) {
        super(context);
        currentSound = MediaPlayer.create(context, R.raw.exhale);
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

        currentSound.start();

        context.getIvCircle().setColorFilter(context.getColor(R.color.chalk_red_var));
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
            btnBreatheState.setOnClickListener(view -> this.stopSound());
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

    private void stopSound(){
        try {
            currentSound.stop();
            currentSound.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
