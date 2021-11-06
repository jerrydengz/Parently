package cmpt276.phosphorus.childapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenConfigureActivity extends AppCompatActivity {

    private ChildManager childManager;
    private boolean isEditingChild;
    private EditText childNameEditText;
    private String childName = "";
    private UUID childUUID;
    private static final String CONFIGURATION_STATE = "ChildConfigurationState";
    private static final String CHILD_UUID_TAG = "ChildUUIDTag";
    private static final String CHILD_NAME_TAG = "ChildNameTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_configure);
        childManager = ChildManager.getInstance();

        this.createBackBtn();
        this.createSaveBtn();
        this.createDeleteBtn();
        this.extractIntent();
        this.registerTextWatcher();
        this.initializeEditingMode();
    }

    private void createBackBtn() {
        Button button = findViewById(R.id.btnBackChildrenConfigure);
        button.setOnClickListener(view -> finish());
    }

    // https://youtu.be/y6StJRn-Y-A
    private void showErrorAlert(String title, String dec) {
        AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
        dialogWarning.setTitle(title);
        dialogWarning.setMessage(dec);
        dialogWarning.setPositiveButton("OK", null);
        dialogWarning.show();
    }

    private void createSaveBtn() {
        Button button = findViewById(R.id.btnSave);

        button.setOnClickListener(view -> {
            String nameTemp = childName.trim();

            Child child = ChildManager.getInstance().getChildByUUID(childUUID);

            // let user to continue to edit and change, till valid entry is entered, or exit
            if (nameTemp.isEmpty()) {
                this.showErrorAlert("Invalid Name", "Make sure you aren't entering an empty name!");
                return;
            }

            if (isDuplicateChildName(nameTemp) && !(isEditingChild && nameTemp.equals(child.getName()))) {
                this.showErrorAlert("Duplicate Child Name Found", "Please enter a unique child name.");
                return;
            }

            if (isEditingChild) {
                child.setName(nameTemp);
            } else {
                childManager.addChild(new Child(nameTemp));
            }

            finish();
        });
    }

    private void createDeleteBtn() {
        Button button = findViewById(R.id.btnDelete);
        button.setOnClickListener(view -> {
            // https://youtu.be/y6StJRn-Y-A
            AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
            dialogWarning.setTitle("Delete Child");
            dialogWarning.setMessage("Are you sure you want to delete this child? 👀👮⚠🚨‍");

            dialogWarning.setPositiveButton("YES", (dialogInterface, i) -> {
                childManager.removeChild(childUUID);
                finish();
            });
            dialogWarning.setNegativeButton("NO", null);
            dialogWarning.show();
        });
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        isEditingChild = packageInfo.getBooleanExtra(CONFIGURATION_STATE, false);
        if (isEditingChild) {
            childUUID = UUID.fromString(packageInfo.getStringExtra(CHILD_UUID_TAG));
            childName = packageInfo.getStringExtra(CHILD_NAME_TAG);
        }
    }

    private void registerTextWatcher() {
        childNameEditText = findViewById(R.id.name_edit_text);

        TextWatcher nameTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // get contents of the edit text field
                childName = childNameEditText.getText().toString();
            }
        };

        childNameEditText.addTextChangedListener(nameTextWatcher);
    }

    private void initializeEditingMode() {
        Button button = findViewById(R.id.btnDelete);
        if (isEditingChild) {
            childNameEditText.setText(childName);
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    public static Intent makeIntent(Context context, Child childObj, boolean isEditing) {
        Intent intent = new Intent(context, ChildrenConfigureActivity.class);

        intent.putExtra(CONFIGURATION_STATE, isEditing);

        if (isEditing) {
            intent.putExtra(CHILD_UUID_TAG, childObj.getUUID().toString());
            intent.putExtra(CHILD_NAME_TAG, childObj.getName());
        }

        return intent;
    }

    private boolean isDuplicateChildName(String childName) {
        return childManager.getAllChildren()
                .stream()
                .map(Child::getName)
                .anyMatch(childName::equals);
    }

}