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

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.children.utils.ChildListAdapter;
import cmpt276.phosphorus.childapp.model.child.Child;
import cmpt276.phosphorus.childapp.model.child.ChildManager;

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

        this.noneBtn();
        this.setupList();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ChooseSideActivity.makeIntent(this);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void noneBtn() {
        Button btn = findViewById(R.id.btnChooseNoNoneChild);
        btn.setOnClickListener(view -> {
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