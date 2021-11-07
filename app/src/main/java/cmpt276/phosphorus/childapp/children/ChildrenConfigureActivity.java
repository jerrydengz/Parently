package cmpt276.phosphorus.childapp.children;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenConfigureActivity extends AppCompatActivity {

    private ChildManager childManager;
    private EditText childNameEditText;
    private String childName = "";
    private UUID childUUID;
    private static final String CHILD_UUID_TAG = "ChildUUIDTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_configure);
        childManager = ChildManager.getInstance();

        this.createBackBtn();
        this.createSaveBtn();
        this.createDeleteBtn();
        this.registerTextWatcher();
        this.extractIntent();
    }

    private void createBackBtn() {
        Button button = findViewById(R.id.btnBackChildrenConfigure);
        button.setOnClickListener(view -> finish());
    }

    // https://youtu.be/y6StJRn-Y-A
    private void showDialogAlert(String title, String dec) {
        AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
        dialogWarning.setTitle(title);
        dialogWarning.setMessage(dec);
        dialogWarning.setPositiveButton(getResources().getString(R.string.dialog_confirm), null);
        dialogWarning.show();
    }

    private void createSaveBtn() {
        Button button = findViewById(R.id.btnSave);

        button.setOnClickListener(view -> {
            String nameTemp = childName.trim();

            Child child = ChildManager.getInstance().getChildByUUID(childUUID);

            // let user to continue to edit and change, till valid entry is entered, or exit
            if (nameTemp.isEmpty()) {
                this.showDialogAlert(getResources().getString(R.string.dialog_title_invalid_name),
                        getResources().getString(R.string.dialog_msg_invalid_name));
                return;
            }

            if (isDuplicateChildName(nameTemp) && !(isEditingChild() && nameTemp.equals(child.getName()))) {
                this.showDialogAlert(getResources().getString(R.string.dialog_title_dupe_name),
                        getResources().getString(R.string.dialog_msg_dupe_name));
                return;
            }

            if (isEditingChild()) {
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
            dialogWarning.setTitle(getResources().getString(R.string.dialog_title_delete));
            dialogWarning.setMessage(getResources().getString(R.string.dialog_msg_delete));
            dialogWarning.setPositiveButton(getResources().getString(R.string.dialog_positive),
                    (dialogInterface, i) -> {
                        childManager.removeChild(childUUID);
                        finish();
                    });
            dialogWarning.setNegativeButton(getResources().getString(R.string.dialog_negative), null);
            dialogWarning.show();
        });
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        Button button = findViewById(R.id.btnDelete);
        TextView txtView = findViewById(R.id.configure_child_title);

        if (packageInfo.hasExtra(CHILD_UUID_TAG)) {
            // initialize fields and EditText for editing a child profile
            childUUID = UUID.fromString(packageInfo.getStringExtra(CHILD_UUID_TAG));
            childName = childManager.getChildByUUID(childUUID).getName();

            txtView.setText(childName);
            childNameEditText.setText(childName);
            button.setVisibility(View.VISIBLE);
        } else {
            txtView.setText(R.string.add_child_title);
            button.setVisibility(View.INVISIBLE);
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

    public static Intent makeIntent(Context context, Child childObj) {
        Intent intent = new Intent(context, ChildrenConfigureActivity.class);

        if (childObj != null) {
            intent.putExtra(CHILD_UUID_TAG, childObj.getUUID().toString());
        }

        return intent;
    }

    private boolean isDuplicateChildName(String childName) {
        return childManager.getAllChildren()
                .stream()
                .map(Child::getName)
                .anyMatch(childName::equals);
    }

    private boolean isEditingChild() {
        return childUUID != null;
    }

}