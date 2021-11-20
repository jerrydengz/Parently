package cmpt276.phosphorus.childapp.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Task {
    private final int FIRST_INDEX = 0;
    @Expose
    private String name;
    @Expose
    private final List<UUID> children;

    public Task(String name, List<Child> children) {
        this.name = name;
        this.children = children.stream().map(Child::getUUID).collect(Collectors.toList());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setNextChild(UUID nextChild) {
        removeChild(nextChild);
        this.children.add(this.FIRST_INDEX, nextChild);
    }

    public UUID getCurrentChild() {
        return getChild(this.FIRST_INDEX);
    }

    public void cycleChildren() {
        this.children.add(this.children.remove(this.FIRST_INDEX));
    }

    public void removeChild(UUID child) {
        this.children.remove(child);
    }

    public void addChild(Child child) {
        UUID childUUID = child.getUUID();
        if (!this.children.contains(childUUID)) {
            this.children.add(childUUID);
        }
    }

    public UUID getChild(int pos) {
        return this.children.get(pos);
    }

    @Override
    public String toString() {
        return "Task{" +
                "children=" + children +
                ", name='" + name + '\'' +
                '}';
    }

}
