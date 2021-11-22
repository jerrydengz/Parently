package cmpt276.phosphorus.childapp.model.task;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import cmpt276.phosphorus.childapp.model.child.Child;
import cmpt276.phosphorus.childapp.model.child.ChildManager;

// ==============================================================================================
//
// The task object
//
// ==============================================================================================
public class Task {

    private final int FIRST_INDEX = 0;

    private final List<UUID> children;
    private String name;

    public Task(@NotNull String name, @NotNull List<Child> children) {
        this.name =  Objects.requireNonNull(name, "Task name cannot be null");
        this.children = Objects.requireNonNull(children, "Task children cannot be null").stream()
                .map(Child::getUUID)
                .collect(Collectors.toList());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setNextChild(@NotNull UUID nextChild) {
        removeChild(nextChild);
        this.children.add(this.FIRST_INDEX, nextChild);
    }

    public UUID getCurrentChild() {
        if (this.isEmptyChildList()){
            return null;
        }

        return getChild(this.FIRST_INDEX);
    }

    public void cycleChildren() {
        this.children.add(this.children.remove(this.FIRST_INDEX));
    }

    public void removeChild(@NotNull UUID child) {
        this.children.remove(child);
    }

    public void addChild(@NotNull Child child) {
        UUID childUUID = child.getUUID();
        if (!this.children.contains(childUUID)) {
            this.children.add(childUUID);
        }
    }

    public UUID getChild(int pos) {
        return this.children.get(pos);
    }

    public boolean isEmptyChildList() {
        return children.isEmpty();
    }

    @NonNull
    public List<Child> getChildren() {
        return this.children.stream()
                .map(uuid -> ChildManager.getInstance().getChildByUUID(uuid))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Task{" +
                "children=" + children +
                ", name='" + name + '\'' +
                '}';
    }

}
