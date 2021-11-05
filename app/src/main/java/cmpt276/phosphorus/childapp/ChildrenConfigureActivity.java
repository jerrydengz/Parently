package cmpt276.phosphorus.childapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenConfigureActivity extends AppCompatActivity {

    private ChildManager childManager;
    private boolean isEditingChild;
    private EditText childNameEditText;
    private String childName = "";
    private static final String CONFIGURATION_STATE = "ChildConfigurationState";

    // TODO ask brian the conditions on error checking and saving
    private final TextWatcher nameTextWatcher = new TextWatcher() {
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

            // error checking
            // no empty string name
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_configure);
        childManager = ChildManager.getInstance();

        this.createBackBtn();
        this.createSaveBtn();
        this.registerTextWatcher();
    }

    private void registerTextWatcher() {
        childNameEditText = findViewById(R.id.name_edit_text);
        childNameEditText.addTextChangedListener(nameTextWatcher);
    }

    private void createBackBtn(){
        Button button = findViewById(R.id.btnBackChildrenConfigure);
        button.setOnClickListener(view -> finish());
    }

    private void createSaveBtn() {
        Button button = findViewById(R.id.btnSave);
        button.setOnClickListener(view -> {

            if(!childName.trim().equals("")){
                childManager.addChild(new Child(childName));

            }else{
                Toast.makeText(getApplicationContext(), "Invalid Name. Child Profile Not Created", Toast.LENGTH_SHORT).show();
            }

            finish();
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