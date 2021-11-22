package cmpt276.phosphorus.childapp.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;
import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.DataManager;
import cmpt276.phosphorus.childapp.model.DataType;
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
    private AlertDialog alertDialog;

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

    // https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
    private void displayTaskDialog(Task selected) {
        AlertDialog.Builder taskDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.task_dialog, null);
        taskDialog.setView(dialogView);

        String dialogTitle = getResources().getString(R.string.task_info_title).replace("%name%", selected.getName());
        TextView title = dialogView.findViewById(R.id.textTaskDialougeName);
        title.setText(dialogTitle);

        // todo set child icon
        ImageView taskChildIcon = dialogView.findViewById(R.id.imgTaskChildIcon);

        Button btnTaskComplete = dialogView.findViewById(R.id.btnTaskComplete);
        btnTaskComplete.setOnClickListener(view -> {
            selected.cycleChildren();
            DataManager.getInstance(this).saveData(DataType.TASKS);
            this.populateTaskListView();
            this.alertDialog.dismiss();
        });

        Button btnTaskEdit = dialogView.findViewById(R.id.btnTaskEdit);
        btnTaskEdit.setOnClickListener(view -> {
            this.alertDialog.dismiss();
            startActivity(ConfigureTaskActivity.makeIntent(this, selected));
        });

        Button btnTaskDelete = dialogView.findViewById(R.id.btnTaskDelete);
        btnTaskDelete.setOnClickListener(view -> {
            // todo delete task

            DataManager.getInstance(this).saveData(DataType.TASKS);
            this.populateTaskListView();
            this.alertDialog.dismiss();
        });


        TextView textCurrentTurn = dialogView.findViewById(R.id.textCurrentTurn);
        UUID currChild = selected.getCurrentChild();
        String childName = (currChild == null) ? "Not Available" : ChildManager.getInstance().getChildByUUID(currChild).getName();
        String currentChild = getResources().getString(R.string.task_info_current_child).replace("%name%", childName);
        textCurrentTurn.setText(currentChild);

        this.alertDialog = taskDialog.create();
        this.alertDialog.show();
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