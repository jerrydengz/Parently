package cmpt276.phosphorus.childapp.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChildManager {

    private static ChildManager instance;
    private final List<Children> allChildren;

    private ChildManager() {
        this.allChildren = new ArrayList<>();
    }

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }

        return instance;
    }

    public void addChildren(@NotNull Children children) {
        this.allChildren.add(Objects.requireNonNull(children));
    }

    public void addChildren(@NotNull Children... childrens) {
        Arrays.asList(childrens).forEach(this::addChildren); // Add children already checks for null
    }

    public Children getChildrenByUUID(@NotNull UUID uuid) {
        return this.allChildren.stream().filter(children -> children.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    public List<Children> getAllChildren() {
        return this.allChildren;
    }

    public boolean removeChildren(UUID byChildrenUUID) {
        Children target = this.getChildrenByUUID(byChildrenUUID);
        return this.removeChildren(target);
    }

    public boolean removeChildren(Children children) {
        return this.allChildren.remove(children);
    }

}
