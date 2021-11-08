package cmpt276.phosphorus.childapp.children;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

// ==============================================================================================
//
// Display all the current children
//
// ==============================================================================================
public class ChildrenActivity extends AppCompatActivity {

    private ChildManager childManager;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildrenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        this.setTitle(getString(R.string.child_activity_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.childManager = ChildManager.getInstance();
        this.createConfigureChildBtn();
        this.populateChildListView();
        this.createOnClickCallBack();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateChildListView();
    }

    private void createConfigureChildBtn() {
        Button button = findViewById(R.id.configureChildrenBtn);
        button.setOnClickListener(view -> startActivity(
                ChildConfigureActivity.makeIntentNewChild(this)
        ));
    }

    private void populateChildListView() {
        List<Child> childProfileList = childManager.getAllChildren();
        ArrayAdapter<Child> listAdapter = new ArrayAdapter<>(
                this,
                R.layout.child_profile,
                childProfileList);

        ListView listView = findViewById(R.id.listViewChildren);
        listView.setAdapter(listAdapter);
    }

    private void createOnClickCallBack() {
        ListView listView = findViewById(R.id.listViewChildren);

        // switch to ChildConfigureActivity to edit selected child object
        listView.setOnItemClickListener((adapter, view, position, arg) -> {
            Child selectedChild = childManager.getAllChildren().get(position);
            Intent intent = ChildConfigureActivity.makeIntent(this, selectedChild);
            startActivity(intent);
        });
    }

}