package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.CoinSide;
import cmpt276.phosphorus.childapp.utils.Intents;

// ==============================================================================================
//
// Allows the user to either choose heads or tails. A child is automatically selected
// Will not show child name if there aren't any (but can still flip)
//
// ==============================================================================================
public class ChooseSideActivity extends AppCompatActivity {

    private Child child;

    public static Intent makeIntent(Context context) {
        return ChooseSideActivity.makeIntent(context, ChildManager.getInstance().getNextCoinFlipper());
    }

    public static Intent makeIntent(Context context, Child child) {
        Intent intent = new Intent(context, ChooseSideActivity.class);
        intent.putExtra(Intents.CHILD_UUID_TAG, (child != null ? child.getUUID().toString() : null));
        return intent;
    }

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
        if (this.child == null) {  // If there aren't any children created yet for example
            textView.setVisibility(View.GONE);
            return;
        }else{
            textView.setVisibility(View.VISIBLE);
        }

        textView.setText(this.child.getName());
        textView.setTypeface(null, Typeface.BOLD_ITALIC);

        textView.setOnClickListener(view -> {
            startActivity(ChooseChildForCoinFlip.makeIntent(this));
            finish();
        });
    }

    private void displayChildPortrait() {
        ImageView childPortrait = findViewById(R.id.child_portrait_coin_flip);

        if(this.child == null){
            childPortrait.setVisibility(View.GONE);
            return;
        }else{
            childPortrait.setVisibility(View.VISIBLE);
        }

        // https://github.com/bumptech/glide
        if(this.child.getChildPortraitPath() != null){
            Glide.with(this)
                 .load(child.getChildPortraitPath())
                 .into(childPortrait);
        }else{
            childPortrait.setImageResource(R.drawable.child_portrait_default);
        }
    }

    private void btnChooseHead() {
        ImageButton imgBtn = findViewById(R.id.imgBtnHeads);
        imgBtn.setOnClickListener(view -> {
            startActivity(FlipCoinActivity.makeIntent(this, this.child, CoinSide.HEAD));
            finish(); // We don't want users coming back here
        });
    }

    private void btnChooseTails() {
        ImageButton imgBtn = findViewById(R.id.imgBtnTails);
        imgBtn.setOnClickListener(view -> {
            startActivity(FlipCoinActivity.makeIntent(this, this.child, CoinSide.TAILS));
            finish(); // We don't want users coming back here
        });
    }

}