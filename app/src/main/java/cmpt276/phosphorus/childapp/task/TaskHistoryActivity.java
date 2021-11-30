package cmpt276.phosphorus.childapp.task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.task.Task;
import cmpt276.phosphorus.childapp.model.task.TaskManager;

public class TaskHistoryActivity extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.task = TaskManager.getInstance().getViewHistory();
        setContentView(R.layout.activity_task_history);
        this.setTitle(this.task.getName() + R.string.history);
    }
}