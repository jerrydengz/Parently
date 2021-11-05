package cmpt276.phosphorus.childapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenActivity extends AppCompatActivity {

    private ChildManager childManager;
    private final int NEW_CHILD_IDX = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        childManager = ChildManager.getInstance();
        this.createBackBtn();
        this.createConfigureChildBtn();
        this.populateChildListView();
        this.createOnClickCallBack();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateChildListView();
    }

    private void createConfigureChildBtn() {
        Button button = findViewById(R.id.configureChildrenBtn);
        button.setOnClickListener(view -> startActivity(ChildrenConfigureActivity.makeIntent(
                this, null, NEW_CHILD_IDX, false)
        ));
    }

    private void createBackBtn(){
        Button button = findViewById(R.id.btnBackChildren);
        button.setOnClickListener(view -> finish());
    }

    private void populateChildListView() {
        childManager.addChild(new Child("Jerry"));
        childManager.addChild(new Child("Jack"));
        childManager.addChild(new Child("Johnson"));
        childManager.addChild(new Child("Joshua"));
        childManager.addChild(new Child("Jonathan"));
        childManager.addChild(new Child("Josh"));

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
            Intent intent = ChildrenConfigureActivity.makeIntent(
                    getApplicationContext(),
                    childManager.getAllChildren().get(position),
                    position, true);
            startActivity(intent);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildrenActivity.class);
    }

}