package cmpt276.phosphorus.childapp.model;

import java.util.ArrayList;
import java.util.List;

// ==============================================================================================
//
// Manager to keep track of all task data
//
// ==============================================================================================
public class TaskManager {

    private static TaskManager instance;

    private List<Task> allTasks; //not final for when saving is implemented

    private TaskManager() {
        this.allTasks = new ArrayList<>();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void setTasks(List<Task> tasks) {
        this.allTasks = tasks;
    }

    public void addTask(Task task) {
        this.allTasks.add(task);
    }

    public boolean containsName(String name) {
        return this.getTaskByName(name) != null;
    }

    public void deleteTask(Task task) {
        this.allTasks.remove(task);
    }

    public void cycleChildren(Task task) {
        task.cycleChildren();
    }

    public Task getTaskByName(Task task) {
        return this.getTaskByName(task.getName());
    }

    public Task getTaskByName(String taskName) {
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
