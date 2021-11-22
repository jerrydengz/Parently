package cmpt276.phosphorus.childapp.task;

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

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.child.ChildManager;
import cmpt276.phosphorus.childapp.model.data.DataManager;
import cmpt276.phosphorus.childapp.model.data.DataType;
import cmpt276.phosphorus.childapp.model.task.Task;
import cmpt276.phosphorus.childapp.model.task.TaskManager;
import cmpt276.phosphorus.childapp.utils.Intents;

// ==============================================================================================
//
// The activity for people to create/edit their tasks
//
// ==============================================================================================
public class ConfigureTaskActivity extends AppCompatActivity {

    private boolean isEditing;
    private String initalName;
    private Task task;

    public static Intent makeIntent(Context context, Task editTask) {
        Intent intent = new Intent(context, ConfigureTaskActivity.class);
        intent.putExtra(Intents.TASK_NAME_TAG, (editTask != null ? editTask.getName() : null));
        return intent;
    }

    public static Intent makeIntent(Context context) {
        return makeIntent(context, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_task);

        this.setTitle(getString(R.string.configure_task_activity_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        this.extractIntent();

        this.updateDisplay();
        this.updateTitle();
        this.btnSave();
        this.createDeleteBtn();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        String intentTaskName = packageInfo.getStringExtra(Intents.TASK_NAME_TAG);
        Task editedTask = TaskManager.getInstance().getTaskByName(intentTaskName);
        this.isEditing = (editedTask != null);
        this.initalName = intentTaskName;
        this.task = this.isEditing ? editedTask : new Task("", ChildManager.getInstance().getAllChildren());
    }

    private void updateDisplay() {
        EditText childNameEditText = findViewById(R.id.inputEditTaskName);
        childNameEditText.setText(this.task.getName());
    }

    private void updateTitle() {
        TextView taskTitle = findViewById(R.id.task_title);
        taskTitle.setText(this.isEditing ? R.string.task_title_edit : R.string.task_title_new);
    }

    private void btnSave() {
        Button btnSaveTask = findViewById(R.id.btnSaveTask);
        EditText childNameEditText = findViewById(R.id.inputEditTaskName);
        btnSaveTask.setOnClickListener(view -> {
            String newName = childNameEditText.getText().toString().trim();

            if (TaskManager.getInstance().containsName(newName) && (!this.initalName.equals(newName))) {
                this.showDialogAlert(R.string.task_alert_duplicate_title, R.string.task_alert_duplicate_dec);
                return;
            }

            if (newName.isEmpty()) {
                this.showDialogAlert(R.string.dialog_title_invalid_name, R.string.dialog_msg_invalid_name);
                return;
            }

            final int MAX_CHAR_LENGTH = 88;
            if (newName.length() >= MAX_CHAR_LENGTH) {
                this.showDialogAlert(R.string.dialog_title_name_too_large, R.string.dialog_msg_name_too_large);
                return;
            }

            this.task.setName(newName);
            if (!this.isEditing) {
                TaskManager.getInstance().addTask(this.task);
            }

            DataManager.getInstance(this).saveData(DataType.TASKS);
            finish();
        });
    }

    private void createDeleteBtn() {
        Button btnDeleteTask = findViewById(R.id.btnDeleteTask);
        if (!this.isEditing) { // Is creating
            btnDeleteTask.setVisibility(View.INVISIBLE);
            return;
        }

        btnDeleteTask.setOnClickListener(view -> {
            // https://youtu.be/y6StJRn-Y-A
            AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
            dialogWarning.setTitle(R.string.task_delete_title);
            dialogWarning.setMessage(R.string.task_delete_msg);
            dialogWarning.setPositiveButton(getResources().getString(R.string.dialog_positive), (dialogInterface, i) -> {

                TaskManager.getInstance().deleteTask(this.task);
                DataManager.getInstance(this).saveData(DataType.TASKS);
                finish();
            });
            dialogWarning.setNegativeButton(getResources().getString(R.string.dialog_negative), null);
            dialogWarning.show();
        });
    }

    private void showDialogAlert(@StringRes int title, @StringRes int dec) {
        AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
        dialogWarning.setTitle(title);
        dialogWarning.setMessage(dec);
        dialogWarning.setPositiveButton(R.string.dialog_confirm, null);
        dialogWarning.show();
    }
}