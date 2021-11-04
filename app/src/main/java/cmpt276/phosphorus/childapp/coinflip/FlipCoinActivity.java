package cmpt276.phosphorus.childapp.coinflip;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.coinflip.utils.CoinFlipAnimationDirection;
import cmpt276.phosphorus.childapp.coinflip.utils.CoinFlipIntent;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.CoinFlipResult;
import cmpt276.phosphorus.childapp.utils.CoinSide;


// Main Menu -> Select child page -> Choose head -> flip and keep track
public class FlipCoinActivity extends AppCompatActivity {

    private final CoinSide DEFAULT_SIDE = CoinSide.HEAD;

    private Child child;
    private CoinSide chosenWinningSide;
    private CoinSide currentCoinSide;

    public static Intent makeIntent(Context context, UUID childUUID, CoinSide chosenCoinSide) {
        Intent intent = new Intent(context, FlipCoinActivity.class);
        intent.putExtra(CoinFlipIntent.CHILD_UUID, childUUID.toString());
        intent.putExtra(CoinFlipIntent.CHOSEN_COIN_SIDE, chosenCoinSide.name());
        return intent;
    }

    // todo remember who picked last
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);

        this.extractIntentData();
        this.currentCoinSide = this.DEFAULT_SIDE;

        this.updateCoinDisplay();
        this.createBackBtn();
        this.createFlipBtn();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        String coinFlipVal = intent.getStringExtra(CoinFlipIntent.CHOSEN_COIN_SIDE);
        String childUUIDVal = intent.getStringExtra(CoinFlipIntent.CHILD_UUID);

        this.chosenWinningSide = CoinSide.valueOf(coinFlipVal);
        this.child = ChildManager.getInstance().getChildByUUID(UUID.fromString(childUUIDVal));
    }

    private void flipCoinState() {
        this.currentCoinSide = (this.currentCoinSide == CoinSide.HEAD) ? CoinSide.TAILS : CoinSide.HEAD;
    }

    private void updateCoinDisplay() {
        ImageView coinImg = findViewById(R.id.imgCoin);
        coinImg.setImageResource(this.currentCoinSide.getImgId());

        TextView currentSide = findViewById(R.id.textCurrentSide);
        currentSide.setText(getString(this.currentCoinSide.getTitleId()));
    }

    private int getDelayBetween(int x) {
        return (4 * x) + 25; // 4x + 25 | https://www.desmos.com/calculator/ya6hvzdgxj
    }

    private void randomlyChooseSide() {
        int MIN_FLIPS = 15;
        int MAX_FLIPS = 20;

        Random random = new Random();
        CoinSide[] coinSide = CoinSide.values();

        CoinSide randomSide = coinSide[random.nextInt(coinSide.length)];
        int totalRandomFlips = random.nextInt((MAX_FLIPS - MIN_FLIPS) + 1) + MIN_FLIPS;

        for (int i = 0; i < totalRandomFlips; i++) {
            int delay = getDelayBetween(i);
            // *2 the delay because flip coin rotates twice, and *i to queue them up so animations don't overlap
            (new Handler()).postDelayed(() -> flipCoin(delay), (delay * 2L) * i);
        }

        if (this.currentCoinSide != randomSide) {
            this.flipCoin(getDelayBetween(totalRandomFlips + 1));
        }

        this.sideLanded();
    }

    private ObjectAnimator rotateCoin90Degree(CoinFlipAnimationDirection direction, int rotationDelay) {
        ImageView coinImg = findViewById(R.id.imgCoin);

        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(FlipCoinActivity.this, direction.getAnimationId());
        anim.setTarget(coinImg);
        anim.setDuration(rotationDelay);
        anim.start();

        return anim;
    }

    private void flipCoin(int rotationDelay) {
        ObjectAnimator anim = this.rotateCoin90Degree(CoinFlipAnimationDirection.FORWARD, rotationDelay);

        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                flipCoinState();
                updateCoinDisplay();
                rotateCoin90Degree(CoinFlipAnimationDirection.BACKWARD, rotationDelay);
            }
        });
    }

    // todo activity change/particles/back button?
    private void sideLanded() {
        CoinFlipResult coinFlipResult = new CoinFlipResult(this.chosenWinningSide, this.currentCoinSide);
        this.child.addCoinFlipResult(coinFlipResult);

        if (this.currentCoinSide == this.chosenWinningSide) {
            // use children manager to find use by this.child and add
        } else {
            // Children loses
        }
    }

    private void createBackBtn() {
        Button button = findViewById(R.id.btnFlip);
        button.setOnClickListener(view -> finish());
    }

    private void createFlipBtn() {
        Button button = findViewById(R.id.btnFlip);
        button.setOnClickListener(view -> this.randomlyChooseSide());
    }

}