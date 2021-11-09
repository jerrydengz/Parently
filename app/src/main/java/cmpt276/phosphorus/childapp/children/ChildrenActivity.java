package cmpt276.phosphorus.childapp.children;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.children.utils.ChildListAdapter;
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
        FloatingActionButton fabConfigure = findViewById(R.id.configure_child_fab);
        fabConfigure.setOnClickListener(view -> startActivity(
                ChildConfigureActivity.makeIntentNewChild(this)
        ));
    }

    private void populateChildListView() {
        ArrayAdapter<Child> listAdapter = new ChildListAdapter(this, childManager.getAllChildren());

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