package cmpt276.phosphorus.childapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import cmpt276.phosphorus.childapp.coinflip.ChooseChildActivity;
import cmpt276.phosphorus.childapp.coinflip.FlipCoinActivity;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Disables light-mode so coins background don't look sus
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        this.createChildrenBtn();
        this.createFlipCoinBtn();
        this.createTimeoutBtn();
    }

    private void createChildrenBtn() {
        Button button = findViewById(R.id.btnChildren);
        button.setOnClickListener(view -> startActivity(ChildrenActivity.makeIntent(this)));
    }

    private void createFlipCoinBtn() {
        Button button = findViewById(R.id.btnFlipCoin);
        button.setOnClickListener(view -> {
            // If there are no children, we don't need to choose a child so we can skip to
            // coin flip page
            Intent intent = ChildManager.getInstance().isEmpty() ?
                    FlipCoinActivity.makeIntent(this) :
                    ChooseChildActivity.makeIntent(this);

            startActivity(intent);
        });
    }

    private void createTimeoutBtn() {
        Button button = findViewById(R.id.btnTimeout);
        button.setOnClickListener(view -> startActivity(TimeoutActivity.makeIntent(this)));
    }

}