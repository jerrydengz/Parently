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
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.utils.CoinSide;

public class ChooseSideActivity extends AppCompatActivity {

    private Child nextChild;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChooseSideActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_choose_side);

        this.setTitle(getString(R.string.flip_coin_choose_action_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    private void btnHistory() {
        FloatingActionButton button = findViewById(R.id.btnHistory);
        button.setOnClickListener(view -> startActivity(CoinFlipHistoryActivity.makeIntent(this)));
    }

    private void displayChildName() {
        Child nextChild = ChildManager.getInstance().getNextCoinFlipper();

        TextView textView = findViewById(R.id.textSideChooseTitle);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText(nextChild.getName());
    }

    private void btnChooseHead() {
        ImageButton imgBtn = findViewById(R.id.imgBtnHeads);
        imgBtn.setOnClickListener(view -> {
            startActivity(FlipCoinActivity.makeIntent(this, CoinSide.HEAD));
            finish(); // We don't want users coming back here
        });
    }

    private void btnChooseTails() {
        ImageButton imgBtn = findViewById(R.id.imgBtnTails);
        imgBtn.setOnClickListener(view -> {
            startActivity(FlipCoinActivity.makeIntent(this, CoinSide.TAILS));
            finish(); // We don't want users coming back here
        });
    }

}