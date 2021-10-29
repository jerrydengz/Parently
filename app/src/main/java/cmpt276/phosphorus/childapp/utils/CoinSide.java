package cmpt276.phosphorus.childapp.utils;

import androidx.annotation.DrawableRes;

import cmpt276.phosphorus.childapp.R;

public enum CoinSide {

    HEAD(R.drawable.coin_head),
    TAILS(R.drawable.coin_tail);

    private @DrawableRes
    final
    int imgId;

    CoinSide(@DrawableRes int imgId) {
        this.imgId = imgId;
    }

    public @DrawableRes
    int getImgId() {
        return this.imgId;
    }

}
