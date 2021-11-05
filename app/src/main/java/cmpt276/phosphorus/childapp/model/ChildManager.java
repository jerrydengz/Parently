package cmpt276.phosphorus.childapp.model;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChildManager {

    private static ChildManager instance;

    private final List<Child> allChildren;
    private Child lastCoinChooserChild;

    private ChildManager() {
        this.allChildren = new ArrayList<>();
        this.lastCoinChooserChild = null;
    }

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }

        return instance;
    }

    public void addChild(@NotNull Child child) {
        this.allChildren.add(Objects.requireNonNull(child));
    }

    public void addChildren(@NotNull Child... children) {
        Arrays.asList(children).forEach(this::addChild); // Add children already checks for null
    }

    public Child getChildByUUID(@NotNull UUID uuid) {
        return this.allChildren.stream().filter(child -> child.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    public List<Child> getAllChildren() {
        return this.allChildren;
    }

    public boolean removeChild(UUID byChildUUID) {
        Child target = this.getChildByUUID(byChildUUID);
        return this.removeChild(target);
    }

    public boolean removeChild(Child child) {
        return this.allChildren.remove(child);
    }

    public Child getLastCoinChooserChild() {
        return this.lastCoinChooserChild;
    }

    public void setLastCoinChooserChild(Child lastCoinChooserChild) {
        this.lastCoinChooserChild = lastCoinChooserChild;
    }

    public JSONArray getJSON() throws JSONException {
        JSONArray result = new JSONArray();
        JSONObject lastChild = new JSONObject();
        lastChild.put("lastCoinChooserChild", this.lastCoinChooserChild.getJSONChild());
        result.put(lastChild);
        for(Child i : this.allChildren){
            result.put(i.getJSONChild());
        }
        return result;
    }

}
