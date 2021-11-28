package cmpt276.phosphorus.childapp.breathe.utils;

import cmpt276.phosphorus.childapp.breathe.BreatheActivity;

public abstract class BreatheState {
    private BreatheActivity context;

    // Ensure BreatheState holds reference to BreatheActivity
    public BreatheState(BreatheActivity context) {
        this.context = context;
    }

    protected void handleEnter(){}
    protected void handleExit(){}
    protected void handleClickInhale(){}
    protected void handleClickExhale(){}
}
