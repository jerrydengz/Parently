package cmpt276.phosphorus.childapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenConfigureActivity extends AppCompatActivity {

    private boolean isEditingChild; // TODO for editing
    private EditText childNameEditText;
    private String childName = "";
    private static final String CONFIGURATION_STATE = "ChildConfigurationState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_configure);

        this.createBackBtn();
        this.createSaveBtn();
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

            }else{
                childManager.addChild(new Child(childName.trim()));
                finish();
            }
        });
    }

    // TODO Editing - putExtras for childObj and position
    public static Intent makeIntent(Context context, Child childObj, int position, boolean isEditing){
        Intent intent = new Intent(context, ChildrenConfigureActivity.class);

        intent.putExtra(CONFIGURATION_STATE, isEditing);

        // TODO add intent extras for editing

        return intent;
    }

    // TODO Editing - extract extras from intent to edit
    private void extractIntent() {
        Intent packageInfo = getIntent();

        isEditingChild = packageInfo.getBooleanExtra(CONFIGURATION_STATE, false);

    }

}