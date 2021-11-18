package cmpt276.phosphorus.childapp.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Keeps track of all tasks
public class TaskManager {
    private List<Task> allTasks; //not final for when saving is implemented
    private File savedata;

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

    public boolean addTask(String taskName, List<UUID> children, UUID currentChild){
        Task test = getTaskByName(taskName);
        if(test == null) {
            this.allTasks.add(new Task(taskName, children, currentChild));
            saveToFile();
            return true;
        }
        return false;
    }

    public void deleteTask(Task task){
        this.allTasks.remove(task);
        saveToFile();
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

    public boolean isEmpty(){
        return this.allTasks.isEmpty();
    }

    private void saveToFile(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try {
            Writer writer = new FileWriter(savedata);
            gson.toJson(this.allTasks, writer);
            writer.close();
        } catch (IOException ignored) {}
    }

    public void loadFromFile(Context context){
        final String FILE_NAME = "tasks.json";

        File dir = context.getFilesDir();
        this.savedata = new File(dir, FILE_NAME);
        this.getFromFile();
    }

    private void getFromFile(){
        Gson gson = new Gson();
        try {
            BufferedReader buff = new BufferedReader(new FileReader(savedata));
            Type taskType = new TypeToken<List<Task>>(){}.getType();
            this.allTasks = gson.fromJson(buff, taskType);
            if(this.allTasks == null){
                this.allTasks = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            this.allTasks = new ArrayList<>();
        }
    }
}
