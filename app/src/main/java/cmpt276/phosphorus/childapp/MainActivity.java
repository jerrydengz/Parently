package cmpt276.phosphorus.childapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Arrays;

import cmpt276.phosphorus.childapp.breathe.BreatheActivity;
import cmpt276.phosphorus.childapp.children.ChildrenActivity;
import cmpt276.phosphorus.childapp.coinflip.ChooseSideActivity;
import cmpt276.phosphorus.childapp.help.HelpActivity;
import cmpt276.phosphorus.childapp.model.data.DataManager;
import cmpt276.phosphorus.childapp.model.data.DataType;
import cmpt276.phosphorus.childapp.task.TaskActivity;
import cmpt276.phosphorus.childapp.timeout.TimeoutActivity;

// ==============================================================================================
//
// The main menu and landing page of the app
//
// ==============================================================================================
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Disables light-mode so coins background don't look sus
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        DataManager dataManager = new DataManager(this);
        Arrays.stream(DataType.values()).forEach(dataManager::loadData);

        this.createChildrenBtn();
        this.createFlipCoinBtn();
        this.createTimeoutBtn();
        this.createTaskBtn();
        this.createBreatheBtn();
        this.createHelpBtn();
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

    private void createTaskBtn() {
        Button button = findViewById(R.id.btnTask);
        button.setOnClickListener(view -> startActivity(TaskActivity.makeIntent(this)));
    }

    private void createBreatheBtn() {
        Button button = findViewById(R.id.btnBreathe);
        button.setOnClickListener(view -> startActivity(BreatheActivity.makeIntent(this)));
    }

    private void createHelpBtn() {
        Button button = findViewById(R.id.btnHelp);
        button.setOnClickListener(view -> startActivity(HelpActivity.makeIntent(this)));
    }

}