package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.children.ChildConfigureActivity;
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChooseSideActivity.class);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.extractIntent();
        if (this.child == null) // If the child is still null even after getting intent, just use next child
            this.child = ChildManager.getInstance().getNextCoinFlipper();

        this.btnChooseChild();
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

    private void btnChooseChild() {
        Button btn = findViewById(R.id.btnChooseChild);

        if (ChildManager.getInstance().isEmpty()) {
            btn.setVisibility(View.INVISIBLE);
            return;
        }

        btn.setOnClickListener(view -> {
            startActivity(ChooseChildForCoinFlip.makeIntent(this));
            finish();
        });
    }

    private void btnHistory() {
        FloatingActionButton button = findViewById(R.id.btnHistory);
        button.setOnClickListener(view -> startActivity(CoinFlipHistoryActivity.makeIntent(this)));
    }

    private void displayChildName() {
        if (this.child == null) // If there aren't any children created yet for example
            return;

        TextView textView = findViewById(R.id.textSideChooseTitle);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText(this.child.getName());
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