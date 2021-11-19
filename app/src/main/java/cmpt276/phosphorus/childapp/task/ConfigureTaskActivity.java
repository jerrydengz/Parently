package cmpt276.phosphorus.childapp.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.ChildManager;
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
        if (this.task == null) // If we're creating a task
            this.task = new Task("", ChildManager.getInstance().getAllChildren());

        this.updateDisplay();
        this.btnConfirm();
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

    private void btnConfirm() {
        Button confirmBtn = findViewById(R.id.btnConfirmTask);
        confirmBtn.setOnClickListener(view -> {
            EditText childNameEditText = findViewById(R.id.inputEditTaskName);
            this.task.setName(childNameEditText.getText().toString());
            boolean isSucecssful = TaskManager.getInstance().addTask(this.task);

            if (!isSucecssful) {
                this.showDialogAlert(R.string.task_alert_duplicate_title, R.string.task_alert_duplicate_dec);
                return;
            }

            finish();
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
}