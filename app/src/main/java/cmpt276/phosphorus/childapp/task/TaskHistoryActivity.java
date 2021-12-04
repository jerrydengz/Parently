package cmpt276.phosphorus.childapp.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.task.Task;
import cmpt276.phosphorus.childapp.model.task.TaskManager;
import cmpt276.phosphorus.childapp.task.utils.TaskHistoryListAdapter;
import cmpt276.phosphorus.childapp.utils.Intents;

public class TaskHistoryActivity extends AppCompatActivity {

    private Task task;

    public static Intent makeIntent(Context context, Task currTask) {
        Intent intent = new Intent(context, TaskHistoryActivity.class);
        intent.putExtra(Intents.TASK_NAME_TAG, currTask.getName());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractIntent();
        setContentView(R.layout.activity_task_history);
        this.setTitle(this.task.getName());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        this.makeList();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


    private void makeList() {
        ListView history = findViewById(R.id.historyList);
        TextView none = findViewById(R.id.noHistory);
        if (this.task.getHistory() != null) {
            TaskHistoryListAdapter adapt = new TaskHistoryListAdapter(this, this.task.getHistory());
            history.setAdapter(adapt);
            history.setVisibility(View.VISIBLE);
            none.setVisibility(View.GONE);
        } else {
            history.setVisibility(View.GONE);
            none.setVisibility(View.VISIBLE);
        }
    }

    private void extractIntent() {
        Intent intent = getIntent();
        String taskName = intent.getStringExtra(Intents.TASK_NAME_TAG);
        this.task = TaskManager.getInstance().getTaskByName(taskName);
    }
}