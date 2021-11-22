package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.extractIntent();

        this.displayChildName();
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
        if (this.child == null) {  // If there aren't any children created yet for example
            return;
        }

        TextView textView = findViewById(R.id.textSideChooseTitle);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText(this.child.getName());

        textView.setOnClickListener(view -> {
            startActivity(ChooseChildForCoinFlip.makeIntent(this));
            finish();
        });
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