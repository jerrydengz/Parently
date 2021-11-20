package cmpt276.phosphorus.childapp.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Task;
import cmpt276.phosphorus.childapp.model.TaskManager;
import cmpt276.phosphorus.childapp.task.utils.TaskListAdapter;

public class TaskActivity extends AppCompatActivity {
    private TaskManager taskManager;

    /*
     TODO ( whoever is going to do this :^) )
         3. Create a Custom AlertDialog displaying task info
            i. a dialog button option to cancel
            ii. a dialog button option to edit -> goes to ConfigureTaskActivity.java (possibly implement delete button in here)
            iii. text to display:
                 - task name
                 - current turn for child's name
                 - picture of child
            iv. button to indicate "finished"
         5. implement adding task in ConfigureTaskActivity.java
     */

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        this.setTitle(getString(R.string.task_activity_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        taskManager = TaskManager.getInstance();

        this.createConfigureTaskBtn();
        this.populateTaskListView();

        Log.d("asd", taskManager.getAllTasks().toString());
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.populateTaskListView();
    }

    private void createConfigureTaskBtn() {
        FloatingActionButton button = findViewById(R.id.add_task_fab);

        // NOTE: preliminary, model after methods in ChildActivity.java to configure children
        button.setOnClickListener(view -> startActivity(ConfigureTaskActivity.makeIntent(this)));
    }

    // TODO
    // https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
    private void displayTaskDialog(Task selected) {
        AlertDialog.Builder taskDialog = new AlertDialog.Builder(this);
        String dialogTitle = getResources().getString(R.string.task_info_title).replace("%name%", selected.getName());
        taskDialog.setTitle(dialogTitle);
//        taskDialog.setMessage(getResources().getString(dec));
        taskDialog.setPositiveButton(R.string.task_info_close, null);
        taskDialog.setNeutralButton(R.string.task_info_edit, (dialogInterface, i) -> {
            startActivity(ConfigureTaskActivity.makeIntent(this, selected));
        });
        taskDialog.show();
    }

    private void populateTaskListView() {
        ListView listView = findViewById(R.id.taskListView);
        ListAdapter listAdapter = new TaskListAdapter(this, taskManager.getAllTasks());
        listView.setOnItemClickListener((adapter, view, position, arg) -> {
            Task selectedTask = TaskManager.getInstance().getAllTasks().get(position);
            displayTaskDialog(selectedTask);
        });
        listView.setAdapter(listAdapter);
    }

}