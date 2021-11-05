package cmpt276.phosphorus.childapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenConfigureActivity extends AppCompatActivity {

    private boolean isEditingChild;
    private EditText childNameEditText;
    private String childName;
    private String childUUID;
    private static final String CONFIGURATION_STATE = "ChildConfigurationState";
    private static final String CHILD_UUID_TAG = "ChildUUIDTag";
    private static final String CHILD_NAME_TAG = "ChildNameTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_configure);

        this.createBackBtn();
        this.createSaveBtn();
        this.extractIntent();
        this.registerTextWatcher();
    }

    private void registerTextWatcher() {
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

        childNameEditText = findViewById(R.id.name_edit_text);
        childNameEditText.addTextChangedListener(nameTextWatcher);

        // initialize EditText box to the child name to edited
        if(isEditingChild){
            childNameEditText.setText(childName);
        }
    }

    private void createBackBtn(){
        Button button = findViewById(R.id.btnBackChildrenConfigure);
        button.setOnClickListener(view -> finish());
    }

    private void createSaveBtn() {
        ChildManager childManager = ChildManager.getInstance();
        Button button = findViewById(R.id.btnSave);
        button.setOnClickListener(view -> {

            // let user to continue to edit and change, till valid entry is entered, or exit
            if(childName.trim().isEmpty()){
                // https://www.youtube.com/watch?v=_5bSz4tsdP4
                AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
                dialogWarning.setTitle("Invalid Name");
                dialogWarning.setMessage("Make sure you aren't entering an empty name!");

                dialogWarning.setPositiveButton("OK", (dialogInterface, i) -> {});
                dialogWarning.show();

            }else if (isEditingChild){
               childManager.getChildByUUID(UUID.fromString(childUUID)).setName(childName.trim());
               finish();
            }else{
                childManager.addChild(new Child(childName.trim()));
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context, Child childObj, boolean isEditing){
        Intent intent = new Intent(context, ChildrenConfigureActivity.class);

        intent.putExtra(CONFIGURATION_STATE, isEditing);

        if(isEditing){
            intent.putExtra(CHILD_UUID_TAG, childObj.getUUID().toString());
            intent.putExtra(CHILD_NAME_TAG, childObj.getName());
        }

        return intent;
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();

        isEditingChild = packageInfo.getBooleanExtra(CONFIGURATION_STATE, false);
        if(isEditingChild){
            childUUID = packageInfo.getStringExtra(CHILD_UUID_TAG);
            childName = packageInfo.getStringExtra(CHILD_NAME_TAG);
        }
    }
}