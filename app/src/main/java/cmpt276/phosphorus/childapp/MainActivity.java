package cmpt276.phosphorus.childapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Disables light-mode so coins background dont look janky
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
        button.setOnClickListener(view -> startActivity(FlipCoinActivity.makeIntent(this)));
    }

    private void createTimeoutBtn() {
        Button button = findViewById(R.id.btnTimeout);
        button.setOnClickListener(view -> startActivity(TimeoutActivity.makeIntent(this)));
    }

}