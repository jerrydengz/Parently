package cmpt276.phosphorus.childapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import cmpt276.phosphorus.childapp.children.ChildrenActivity;
import cmpt276.phosphorus.childapp.coinflip.ChooseSideActivity;
import cmpt276.phosphorus.childapp.timeout.TimeoutActivity;

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
        button.setOnClickListener(view -> startActivity(ChooseSideActivity.makeIntent(this)));
    }

    private void createTimeoutBtn() {
        Button button = findViewById(R.id.btnTimeout);
        button.setOnClickListener(view -> startActivity(TimeoutActivity.makeIntent(this)));
    }

}