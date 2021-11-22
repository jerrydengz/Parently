package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.children.utils.ChildListAdapter;
import cmpt276.phosphorus.childapp.model.child.Child;
import cmpt276.phosphorus.childapp.model.child.ChildManager;
import cmpt276.phosphorus.childapp.utils.Intents;

// ==============================================================================================
//
// Allows the user to select a specific user for the coin flip (instead of it auto-picking)
//
// ==============================================================================================
public class ChooseChildForCoinFlip extends AppCompatActivity {

    public static Intent makeIntent(Context context, Child curChild) {
        Intent intent = new Intent(context, ChooseChildForCoinFlip.class);
        intent.putExtra(Intents.CHILD_UUID_TAG, (curChild != null ? curChild.getUUID().toString() : null));
        return intent;
    }

    private Child currentChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_child_for_coin_flip);

        this.setTitle(getString(R.string.flip_coin_choose_child_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        this.extractIntent();

        this.noneBtn();
        this.setupList();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(ChooseSideActivity.makeIntent(this, this.currentChild));
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(ChooseSideActivity.makeIntent(this, this.currentChild));
        finish();
        super.onBackPressed();
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        String intentChildUUID = packageInfo.getStringExtra(Intents.CHILD_UUID_TAG);
        this.currentChild = ChildManager.getInstance().getChildByUUID(intentChildUUID);
    }

    private void noneBtn() {
        Button btnChooseNoNoneChild = findViewById(R.id.btnChooseNoNoneChild);
        btnChooseNoNoneChild.setOnClickListener(view -> {
            startActivity(ChooseSideActivity.makeIntent(this, null));
            finish();
        });
    }

    private void setupList() {
        ArrayList<Child> orderedChildren = ChildManager.getInstance().getLastPickedOrderedChildren();
        ArrayAdapter<Child> listAdapter = new ChildListAdapter(this, orderedChildren);

        ListView listView = findViewById(R.id.listViewChooseChild);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((adapter, view, position, arg) -> {
            Child selectedChild = orderedChildren.get(position);

            startActivity(ChooseSideActivity.makeIntent(this, selectedChild));
            finish();
        });
    }

}