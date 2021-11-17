package cmpt276.phosphorus.childapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Keeps track of all tasks
public class TaskManager {
    private List<Task> allTasks; //not final for when saving is implemented

    private static TaskManager instance;

    private TaskManager(){

        this.allTasks = new ArrayList<>();
    }

    public static TaskManager getInstance(){
        if(instance == null){
            instance = new TaskManager();
        }
        return instance;
    }

    public boolean addTask(String taskName, List<UUID> children, Child currentChild){
        Task test = getTaskByName(taskName);
        if(test == null) {
            this.allTasks.add(new Task(taskName, children, currentChild));
            return true;
        }
        return false;
    }

    public void deleteTask(Task task){
        this.allTasks.remove(task);
    }

    public void cycleChildren(Task task){
        task.cycleChildren();
    }

    public Task getTaskByName(String taskName){
        for(Task task : this.allTasks){
            if(task.getName().equals(taskName)){
                return task;
            }
        }
        return null;
    }
    
    public List<Task> getAllTasks(){
        return this.allTasks;
    }

    //todo: implement saving and loading
}
