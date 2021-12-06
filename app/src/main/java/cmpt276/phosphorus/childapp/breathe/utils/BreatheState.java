package cmpt276.phosphorus.childapp.breathe.utils;

import android.animation.AnimatorSet;
import android.os.CountDownTimer;
import android.os.Handler;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public abstract class BreatheState {
    protected BreatheActivity context;

    protected boolean hasHeldThreeSecs = false;

    protected final int THREE_SECONDS = 3000;
    protected final int TEN_SECONDS = 10000;

    // Ensure BreatheState holds reference to BreatheActivity
    public BreatheState(BreatheActivity context) {
        this.context = context;
    }

    public void handleEnter(){}
    public void handleExit(){}
    public void handleOnTouch(){}
    public void handleOnRelease(){}
}
