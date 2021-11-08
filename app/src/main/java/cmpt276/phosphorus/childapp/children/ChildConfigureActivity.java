package cmpt276.phosphorus.childapp.children;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

// ==============================================================================================
//
// Activity to handle both creating, and editing of children selected
// from the ChildrenActivity list
//
// ==============================================================================================
public class ChildConfigureActivity extends AppCompatActivity {

    private static final String CHILD_UUID_TAG = "ChildUUIDTag";

    private ChildManager childManager;
    private Child child;

    public static Intent makeIntentNewChild(Context context) {
        return makeIntent(context, null);
    }

    public static Intent makeIntent(Context context, Child childObj) {
        Intent intent = new Intent(context, ChildConfigureActivity.class);
        intent.putExtra(CHILD_UUID_TAG, (childObj != null ? childObj.getUUID().toString() : null));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_configure);
        this.childManager = ChildManager.getInstance();

        this.extractIntent(); // Gotta get intent info before we change the title
        int titleId = this.isEditingChild() ? R.string.child_configure_edit_title : R.string.child_configure_create_title;
        this.setTitle(getString(titleId));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.loadValues();
        this.createSaveBtn();
        this.createDeleteBtn();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    // https://youtu.be/y6StJRn-Y-A
    private void showDialogAlert(@StringRes int title, @StringRes int dec) {
        AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
        dialogWarning.setTitle(getResources().getString(title));
        dialogWarning.setMessage(getResources().getString(dec));
        dialogWarning.setPositiveButton(getResources().getString(R.string.dialog_confirm), null);
        dialogWarning.show();
    }

    private void createSaveBtn() {
        EditText childNameEditText = findViewById(R.id.name_edit_text);
        Button button = findViewById(R.id.btnSave);

        button.setOnClickListener(view -> {
            String cleanedName = childNameEditText.getText().toString().trim(); // Removes spaces/whtie space

            // let user to continue to edit and change, till valid entry is entered, or exit
            if (cleanedName.isEmpty()) {
                this.showDialogAlert(R.string.dialog_title_invalid_name, R.string.dialog_msg_invalid_name);
                return;
            }

            if (cleanedName.length() >= 15) {
                this.showDialogAlert(R.string.dialog_title_name_too_large, R.string.dialog_msg_name_too_large);
                return;
            }

            if (isDuplicateChildName(cleanedName) && !(isEditingChild() && cleanedName.equals(child.getName()))) {
                this.showDialogAlert(R.string.dialog_title_dupe_name, R.string.dialog_msg_dupe_name);
                return;
            }

            if (isEditingChild()) {
                this.child.setName(cleanedName);
            } else {
                this.childManager.addChild(new Child(cleanedName));
            }

            finish();
        });
    }

    private void createDeleteBtn() {
        Button button = findViewById(R.id.btnDelete);
        button.setOnClickListener(view -> {
            // https://youtu.be/y6StJRn-Y-A
            AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
            dialogWarning.setTitle(getResources().getString(R.string.dialog_title_delete));
            dialogWarning.setMessage(getResources().getString(R.string.dialog_msg_delete));
            dialogWarning.setPositiveButton(getResources().getString(R.string.dialog_positive), (dialogInterface, i) -> {
                this.childManager.removeChild(this.child);
                finish();
            });
            dialogWarning.setNegativeButton(getResources().getString(R.string.dialog_negative), null);
            dialogWarning.show();
        });
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        String intentChildUUID = packageInfo.getStringExtra(CHILD_UUID_TAG);
        if (intentChildUUID != null) {
            this.child = this.childManager.getChildByUUID(UUID.fromString(intentChildUUID));
        }
    }

    private void loadValues() {
        TextView childTitleText = findViewById(R.id.configure_child_title);
        Button deleteBtn = findViewById(R.id.btnDelete);

        boolean isEditing = this.isEditingChild();

        if (isEditing) {
            // Update's the text input with the childs name
            EditText childNameEditText = findViewById(R.id.name_edit_text);
            childNameEditText.setText(this.child.getName());
        }

        childTitleText.setText(isEditing ? this.child.getName() : getString(R.string.add_child_title));
        deleteBtn.setVisibility(isEditing ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean isDuplicateChildName(String childName) {
        return this.childManager.getAllChildren()
                .stream()
                .map(Child::getName)
                .anyMatch(childName::equals);
    }

    private boolean isEditingChild() {
        return this.child != null;
    }

}