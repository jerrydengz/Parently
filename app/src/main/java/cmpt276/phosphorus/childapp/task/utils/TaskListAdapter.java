package cmpt276.phosphorus.childapp.task.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.Task;
import cmpt276.phosphorus.childapp.model.TaskManager;

public class TaskListAdapter extends ArrayAdapter<Task> {
    public TaskListAdapter(Context context, List<Task> tasks) {
        super(context, R.layout.task_item, tasks);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View taskView = convertView;

        if (taskView == null) {
            taskView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        Task currentTask = getItem(position);
        
        // Set the name of the task
        TextView taskName = taskView.findViewById(R.id.task_name);
        taskName.setText(currentTask.getName());
        taskName.setTextColor(taskView.getResources().getColor(R.color.black, null));
        taskName.setTypeface(null, Typeface.BOLD);

        // Set the name of the child assigned to the task
        TextView childTurnName = taskView.findViewById(R.id.current_turn_child_name);
        String result = currentTask.isEmptyChildList() ? "No children" : ChildManager.getInstance()
                .getChildByUUID(currentTask.getCurrentChild())
                .getName();
        childTurnName.setText(taskView.getResources().getString(R.string.current_turn_display) + result);
        childTurnName.setTextColor(taskView.getResources().getColor(R.color.black, null));

        return taskView;
    }
}
