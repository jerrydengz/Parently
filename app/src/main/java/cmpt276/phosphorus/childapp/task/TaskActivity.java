package cmpt276.phosphorus.childapp.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        this.setTitle(getString(R.string.task_activity_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        taskManager = TaskManager.getInstance();

        this.createConfigureTaskBtn();
        this.setUpTaskDialog();
        this.populateTaskListView();
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskActivity.class);
    }

    private void createConfigureTaskBtn() {
        FloatingActionButton button = findViewById(R.id.add_task_fab);

        // NOTE: preliminary, model after methods in ChildActivity.java to configure children
        button.setOnClickListener(view -> startActivity(ConfigureTaskActivity.makeIntent(this)));
    }

    // TODO
    // https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
    private void setUpTaskDialog() {
    }

    private void populateTaskListView() {
        ListAdapter listAdapter = new TaskListAdapter(this, taskManager.getAllTasks());
        ListView listView = findViewById(R.id.taskListView);
        listView.setAdapter(listAdapter);
    }

}