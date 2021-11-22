package cmpt276.phosphorus.childapp.model.task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// ==============================================================================================
//
// Manager to keep track of all task data
//
// ==============================================================================================
public class TaskManager {

    private static TaskManager instance;

    private List<Task> allTasks;

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    private TaskManager() {
        this.allTasks = new ArrayList<>();
    }

    public void setTasks(@NotNull List<Task> tasks) {
        this.allTasks = Objects.requireNonNull(tasks);
    }

    public void addTask(@NotNull Task task) {
        this.allTasks.add(Objects.requireNonNull(task));
    }

    public boolean containsName(@NotNull String name) {
        return this.getTaskByName(name) != null;
    }

    public void deleteTask(Task task) {
        this.allTasks.remove(task);
    }

    public Task getTaskByName(@NotNull String taskName) {
        for (Task task : this.allTasks) {
            if (task.getName().equals(taskName)) {
                return task;
            }
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return this.allTasks;
    }

    public boolean isEmpty() {
        return this.allTasks.isEmpty();
    }

}
