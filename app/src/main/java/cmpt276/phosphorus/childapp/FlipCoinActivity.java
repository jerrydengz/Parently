package cmpt276.phosphorus.childapp;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import cmpt276.phosphorus.childapp.utils.CoinFlipAnimationDirection;
import cmpt276.phosphorus.childapp.utils.CoinSide;

// Main Menu -> Select child page & head -> flip and keep track
public class FlipCoinActivity extends AppCompatActivity {

    private CoinSide coinSide = CoinSide.HEAD;

    public static Intent makeIntent(Context context) {
        return new Intent(context, FlipCoinActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);

        this.updateCoinDisplay();
        this.createBackBtn();
        this.createFlipBtn();
    }

    private void flipCoinState() {
        this.coinSide = (this.coinSide == CoinSide.HEAD) ? CoinSide.TAILS : CoinSide.HEAD;
    }

    private void updateCoinDisplay() {
        ImageView coinImg = findViewById(R.id.imgCoin);
        coinImg.setImageResource(this.coinSide.getImgId());
    }

    private void randomlyChooseSide() {
        int MIN_FLIPS = 5;
        int MAX_FLIPS = 10;

        Random random = new Random();
        CoinSide[] coinSide = CoinSide.values();

        CoinSide randomSide = coinSide[random.nextInt(coinSide.length)];
        int totalRandomFlips = random.nextInt((MAX_FLIPS - MIN_FLIPS) + 1) + MIN_FLIPS;

        for (int i = 0; i < totalRandomFlips; i++) {
            (new Handler()).postDelayed(this::flipCoin, (150 * 2) * i);
        }

        if (this.coinSide != randomSide) {
            this.flipCoin();
        }
    }

    private ObjectAnimator rotateCoin90Degree(CoinFlipAnimationDirection direction) {
        ImageView coinImg = findViewById(R.id.imgCoin);

        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(FlipCoinActivity.this, direction.getAnimationId());
        anim.setTarget(coinImg);
        anim.setDuration(150);
        anim.start();

        return anim;
    }

    private void flipCoin() {
        ObjectAnimator anim = this.rotateCoin90Degree(CoinFlipAnimationDirection.FORWARD);

        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                flipCoinState();
                updateCoinDisplay();
                rotateCoin90Degree(CoinFlipAnimationDirection.BACKWARD);
            }
        });
    }

    private void createBackBtn() {
        Button button = findViewById(R.id.btnBackFlipCoin);
        button.setOnClickListener(view -> finish());
    }

    private void createFlipBtn() {
        Button button = findViewById(R.id.btnFlip);
        button.setOnClickListener(view -> this.randomlyChooseSide());
    }

}