package cmpt276.phosphorus.childapp.breathe.utils;

import android.animation.AnimatorSet;
import android.os.CountDownTimer;
import android.os.Handler;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public abstract class BreatheState {
    protected BreatheActivity context;
    protected final double ANIMATION_RATE = 2.5;

    protected boolean hasHeldThreeSecs = false;
    protected final long THREE_SECONDS = 3000;
    protected final long TEN_SECONDS = 10000;

    protected android.os.Handler timerHandler = new Handler();

    // Ensure BreatheState holds reference to BreatheActivity
    public BreatheState(BreatheActivity context) {
        this.context = context;
    }

    public void handleEnter(){}
    public void handleExit(){}
    public void handleOnTouch(){}
    public void handleOnRelease(){}
}
