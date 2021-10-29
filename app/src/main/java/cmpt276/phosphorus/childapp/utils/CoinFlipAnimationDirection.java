package cmpt276.phosphorus.childapp.utils;

import androidx.annotation.AnimatorRes;

import cmpt276.phosphorus.childapp.R;

public enum CoinFlipAnimationDirection {

    FORWARD(R.animator.flip_forward),
    BACKWARD(R.animator.flip_backward);

    private final
    int animationId;

    CoinFlipAnimationDirection(@AnimatorRes int animationId) {
        this.animationId = animationId;
    }

    public @AnimatorRes
    int getAnimationId() {
        return this.animationId;
    }

}
