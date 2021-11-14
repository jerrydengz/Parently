package cmpt276.phosphorus.childapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskManager {
    private List<Task> allTasks;
    private static TaskManager instance;

    public TaskManager(){
        this.allTasks = new ArrayList<>();
    }

    public static TaskManager getInstance(){
        if(instance == null){
            instance = new TaskManager();
        }
        return instance;
    }

    public void addTask(String taskName, List<UUID> children, Child currentChild){
        this.allTasks.add(new Task(taskName, children, currentChild));
    }

    public void deleteTask(Task task){
        this.allTasks.remove(task);
    }


}
