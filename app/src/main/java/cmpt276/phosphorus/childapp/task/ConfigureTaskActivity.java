package cmpt276.phosphorus.childapp.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.DataManager;
import cmpt276.phosphorus.childapp.model.DataType;
import cmpt276.phosphorus.childapp.model.Task;
import cmpt276.phosphorus.childapp.model.TaskManager;
import cmpt276.phosphorus.childapp.utils.Intents;

public class ConfigureTaskActivity extends AppCompatActivity {

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
        this.btnSaveTask();
        this.createDeleteBtn();

        Button deleteBtn = findViewById(R.id.btnDeleteTask);
        if (!isEditingTask()) { // If we're creating a task
            deleteBtn.setVisibility(View.INVISIBLE);
            this.task = new Task("", ChildManager.getInstance().getAllChildren());
        }else{
            deleteBtn.setVisibility(View.VISIBLE);
            this.updateDisplay();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        String intentTaskName = packageInfo.getStringExtra(Intents.TASK_NAME_TAG);
        this.task = TaskManager.getInstance().getTaskByName(intentTaskName);
    }

    private void updateDisplay() {
        EditText childNameEditText = findViewById(R.id.inputEditTaskName);
        childNameEditText.setText(this.task.getName());
    }

    private void btnSaveTask() {
        Button saveBtn = findViewById(R.id.btnSaveTask);
        saveBtn.setOnClickListener(view -> {
            EditText childNameEditText = findViewById(R.id.inputEditTaskName);
            this.task.setName(childNameEditText.getText().toString());
            boolean isSuccessful = TaskManager.getInstance().addTask(this.task);

            if (!isSuccessful) {
                this.showDialogAlert(R.string.task_alert_duplicate_title, R.string.task_alert_duplicate_dec);
                return;
            }

            DataManager.getInstance(this).saveData(DataType.TASKS);
            finish();
        });
    }

    private void createDeleteBtn() {
        Button button = findViewById(R.id.btnDeleteTask);
        button.setOnClickListener(view -> {
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

    // https://youtu.be/y6StJRn-Y-A
    private void showDialogAlert(@StringRes int title, @StringRes int dec) {
        AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
        dialogWarning.setTitle(getResources().getString(title));
        dialogWarning.setMessage(getResources().getString(dec));
        dialogWarning.setPositiveButton(getResources().getString(R.string.dialog_confirm), null);
        dialogWarning.show();
    }

    private boolean isEditingTask() {
        return this.task != null;
    }


}