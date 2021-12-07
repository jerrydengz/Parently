package cmpt276.phosphorus.childapp.breathe.utils;

import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

// ==============================================================================================
//
// Abstract state for breathing
//
// ==============================================================================================
public abstract class BreatheState {

    protected final int THREE_SECONDS = 3000;
    protected final int TEN_SECONDS = 10000;
    protected BreatheActivity context;

    // Ensure BreatheState holds reference to BreatheActivity
    public BreatheState(BreatheActivity context) {
        this.context = context;
    }

    public void handleEnter(){}
    public void handleExit(){}
    public void handleOnTouch(){}
    public void handleOnRelease(){}

}
