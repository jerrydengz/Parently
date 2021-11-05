package cmpt276.phosphorus.childapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenConfigureActivity extends AppCompatActivity {

    private ChildManager childManager;
    private boolean isEditingChild;
    private static final String CONFIGURATION_STATE = "ChildConfigurationState";

//    // TODO
//    private final TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            // get the content of both the edit text
//            String emailInput = etEmail.getText().toString();
//            String passwordInput = etPassword.getText().toString();
//
//            // check whether both the fields are empty or not
//            bLogin.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_configure);
        childManager = ChildManager.getInstance();


        this.createBackBtn();
        this.createSaveBtn();
    }

    private void createBackBtn(){
        Button button = findViewById(R.id.btnBackChildrenConfigure);
        button.setOnClickListener(view -> finish());
    }

    // TODO
    private void createSaveBtn() {
    }


    public static Intent makeIntent(Context context, Child childObj, int position, boolean isEditing){
        Intent intent = new Intent(context, ChildrenConfigureActivity.class);

        intent.putExtra(CONFIGURATION_STATE, isEditing);

        // TODO add intent extras for editing

        return intent;
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();

        isEditingChild = packageInfo.getBooleanExtra(CONFIGURATION_STATE, false);

    }

}