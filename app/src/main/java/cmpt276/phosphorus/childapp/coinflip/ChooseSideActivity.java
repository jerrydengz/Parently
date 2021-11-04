package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.coinflip.utils.CoinFlipIntent;
import cmpt276.phosphorus.childapp.utils.CoinSide;

public class ChooseSideActivity extends AppCompatActivity {

    private UUID childUUID;

    public static Intent makeIntent(Context context, UUID childUUID) {
        Intent intent = new Intent(context, ChooseSideActivity.class);
        intent.putExtra(CoinFlipIntent.CHILD_UUID, childUUID.toString());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_choose_side);

        this.extractIntentData();

        this.btnChooseHead();
        this.btnChooseTails();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        this.childUUID = UUID.fromString(intent.getStringExtra(CoinFlipIntent.CHILD_UUID));
    }

    private void btnChooseHead() {
        ImageButton imgBtn = findViewById(R.id.imgBtnHeads);
        imgBtn.setOnClickListener(view -> {
            startActivity(FlipCoinActivity.makeIntent(this, this.childUUID, CoinSide.HEAD));
            finish(); // We don't want users coming back here
        });
    }

    private void btnChooseTails() {
        ImageButton imgBtn = findViewById(R.id.imgBtnTails);
        imgBtn.setOnClickListener(view -> {
            startActivity(FlipCoinActivity.makeIntent(this, this.childUUID, CoinSide.TAILS));
            finish(); // We don't want users coming back here
        });
    }

}