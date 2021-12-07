package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.child.Child;
import cmpt276.phosphorus.childapp.model.child.ChildManager;
import cmpt276.phosphorus.childapp.model.coin.CoinSide;
import cmpt276.phosphorus.childapp.utils.Intents;

// ==============================================================================================
//
// Allows the user to either choose heads or tails. A child is automatically selected
// Will not show child name if there aren't any (but can still flip)
//
// ==============================================================================================
public class ChooseSideActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return ChooseSideActivity.makeIntent(context, ChildManager.getInstance().getNextCoinFlipper());
    }

    public static Intent makeIntent(Context context, Child child) {
        Intent intent = new Intent(context, ChooseSideActivity.class);
        intent.putExtra(Intents.CHILD_UUID_TAG, (child != null ? child.getUUID().toString() : null));
        return intent;
    }

    private Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_choose_side);

        this.setTitle(getString(R.string.flip_coin_choose_action_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        this.extractIntent();

        this.displayChildName();
        this.displayChildPortrait();
        this.btnChooseHead();
        this.btnChooseTails();
        this.btnHistory();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        String intentChildUUID = packageInfo.getStringExtra(Intents.CHILD_UUID_TAG);
        this.child = ChildManager.getInstance().getChildByUUID(intentChildUUID);
    }

    private void btnHistory() {
        FloatingActionButton button = findViewById(R.id.btnHistory);
        button.setOnClickListener(view -> startActivity(CoinFlipHistoryActivity.makeIntent(this)));
    }

    private void displayChildName() {
        TextView textView = findViewById(R.id.textSideChooseTitle);

        textView.setText(this.child == null ? getString(R.string.flip_coin_choose_child_none) : this.child.getName());
        textView.setTypeface(null, Typeface.BOLD);

        textView.setOnClickListener(view -> {
            if (ChildManager.getInstance().isEmpty()) {
                return;
            }

            startActivity(ChooseChildForCoinFlip.makeIntent(this, this.child));
            finish();
        });
    }

    private void displayChildPortrait() {
        ImageView childPortrait = findViewById(R.id.childPortraitCoinFlip);

        if (this.child != null) {
            if (this.child.getChildPortraitPath() != null) {
                // https://github.com/bumptech/glide
                Glide.with(this)
                        .load(this.child.getChildPortraitPath())
                        .into(childPortrait);
            } else {
                childPortrait.setImageResource(R.drawable.child_portrait_default);
            }
        }else{
            childPortrait.setVisibility(View.GONE);
        }

        childPortrait.setOnClickListener(view -> {
            if (ChildManager.getInstance().isEmpty()) {
                return;
            }

            startActivity(ChooseChildForCoinFlip.makeIntent(this, this.child));
            finish();
        });
    }

    private void btnChooseHead() {
        ImageButton imgBtnHeads = findViewById(R.id.imgBtnHeads);
        imgBtnHeads.setOnClickListener(view -> {
            startActivity(FlipCoinActivity.makeIntent(this, this.child, CoinSide.HEAD));
            finish(); // We don't want users coming back here
        });
    }

    private void btnChooseTails() {
        ImageButton imgBtnTails = findViewById(R.id.imgBtnTails);
        imgBtnTails.setOnClickListener(view -> {
            startActivity(FlipCoinActivity.makeIntent(this, this.child, CoinSide.TAILS));
            finish(); // We don't want users coming back here
        });
    }

}