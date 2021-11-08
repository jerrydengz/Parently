package cmpt276.phosphorus.childapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

import cmpt276.phosphorus.childapp.coinflip.ChooseChildActivity;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.CoinFlipResult;
import cmpt276.phosphorus.childapp.utils.CoinSide;



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
        button.setOnClickListener(view -> startActivity(ChooseChildActivity.makeIntent(this)));
    }

    private void createTimeoutBtn() {
        Button button = findViewById(R.id.btnTimeout);
        button.setOnClickListener(view -> startActivity(TimeoutActivity.makeIntent(this)));
    }

}