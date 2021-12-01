package cmpt276.phosphorus.childapp.breathe.utils;

import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public abstract class BreatheState {
    protected BreatheActivity context;

    protected boolean hasHeldThreeSecs = false;
    protected boolean hasHeldTenSecs = false;

    protected final int SECONDS_3 = 3000;
    protected final int SECONDS_10 = 10000;

    // Ensure BreatheState holds reference to BreatheActivity
    public BreatheState(BreatheActivity context) {
        this.context = context;
    }

    public void handleEnter(){}
    public void handleExit(){}
    public void handleOnTouch(){}
    public void handleOnRelease(){}
}
