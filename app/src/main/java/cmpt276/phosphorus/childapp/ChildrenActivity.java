package cmpt276.phosphorus.childapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenActivity extends AppCompatActivity {

    private ChildManager childManager;

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
        button.setOnClickListener(view -> startActivity(
                ChildrenConfigureActivity.makeIntent(
                this, null)
        ));
    }

    private void createBackBtn(){
        Button button = findViewById(R.id.btnBackChildren);
        button.setOnClickListener(view -> finish());
    }

    private void populateChildListView() {
        ArrayAdapter<Child> listAdapter = new ChildListAdapter();

        ListView listView = findViewById(R.id.listViewChildren);
        listView.setAdapter(listAdapter);
    }

    private void createOnClickCallBack() {
        ListView listView = findViewById(R.id.listViewChildren);

        // switch to ChildConfigureActivity to edit selected child object
        listView.setOnItemClickListener((adapter, view, position, arg) -> {
            Intent intent = ChildrenConfigureActivity.makeIntent(
                    this,
                    childManager.getAllChildren().get(position));
            startActivity(intent);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildrenActivity.class);
    }

    // https://www.youtube.com/watch?v=WRANgDgM2Zg
    private class ChildListAdapter extends ArrayAdapter<Child>{

        public ChildListAdapter() {
            super(ChildrenActivity.this, R.layout.child_profile, childManager.getAllChildren());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View childView = convertView;

            if (childView == null) {
                childView = getLayoutInflater().inflate(R.layout.child_profile, parent, false);
            }


            Child childProfile = childManager.getAllChildren().get(position);

            // Set the image
            ImageView childIcon = childView.findViewById(R.id.childProfileIcon);
            childIcon.setImageResource(R.drawable.child_profile_img);

            // Set the name
            TextView childName = childView.findViewById(R.id.child_profile_name);
            childName.setText(childProfile.getName());
            childName.setTypeface(null, Typeface.BOLD);

            return childView;
        }

    }

}