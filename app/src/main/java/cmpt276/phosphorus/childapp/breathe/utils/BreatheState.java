package cmpt276.phosphorus.childapp.breathe.utils;

import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public abstract class BreatheState {
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
