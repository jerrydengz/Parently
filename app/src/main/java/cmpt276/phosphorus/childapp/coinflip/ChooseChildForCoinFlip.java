package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.children.utils.ChildListAdapter;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

// ==============================================================================================
//
// Allows the user to select a specific user for the coin flip (instead of it auto-picking)
//
// ==============================================================================================
public class ChooseChildForCoinFlip extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChooseChildForCoinFlip.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_child_for_coin_flip);

        this.setTitle(getString(R.string.flip_coin_choose_child_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setupList();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ChooseSideActivity.makeIntent(this);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void setupList() {
        ChildManager childManager = ChildManager.getInstance();
        ArrayAdapter<Child> listAdapter = new ChildListAdapter(this, childManager.getAllChildren());

        ListView listView = findViewById(R.id.listViewChooseChild);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((adapter, view, position, arg) -> {
            Child selectedChild = childManager.getAllChildren().get(position);

            startActivity(ChooseSideActivity.makeIntent(this, selectedChild));
            finish();
        });
    }

}