package cmpt276.phosphorus.childapp.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cmpt276.phosphorus.childapp.model.child.Child;
import cmpt276.phosphorus.childapp.model.child.ChildManager;
import cmpt276.phosphorus.childapp.model.task.Task;
import cmpt276.phosphorus.childapp.model.task.TaskManager;

// ==============================================================================================
//
// The two types of data types locations that we use
//
// ==============================================================================================
public enum DataType {
    CHILDREN(Child.class) {
        @Override
        List<?> getDataToSave() {
            return ChildManager.getInstance().getAllChildren();
        }

        @Override
        <T> void load(List<T> data) {
            ChildManager.getInstance().setChildren((ArrayList<Child>) data);
        }
    },
    TASKS(Task.class) {
        @Override
        List<?> getDataToSave() {
            return TaskManager.getInstance().getAllTasks();
        }

        @Override
        <T> void load(List<T> data) {
            TaskManager.getInstance().setTasks((ArrayList<Task>) data);
        }
    };

    private final Class typeClass;

    DataType(Class typeClass) {
        this.typeClass = typeClass;
    }

    abstract List<?> getDataToSave();

    abstract <T> void load(List<T> data); // todo ?

    public Class getTypeClass() {
        return this.typeClass;
    }

    public String getFileName() {
        return this.name().toLowerCase(Locale.ROOT) + ".json";
    }

}
