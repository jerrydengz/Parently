package cmpt276.phosphorus.childapp.model;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

    private JSONArray getJSON() throws JSONException {
        JSONArray result = new JSONArray();
        JSONArray childArr = new JSONArray();
        JSONObject lastChild = new JSONObject();
        JSONObject children = new JSONObject();
        lastChild.put("lastCoinChooserChild", this.lastCoinChooserChild.getJSONChild());
        result.put(lastChild);
        for(Child i : this.allChildren){
            childArr.put(i.getJSONChild());
        }
        children.put("allChildren", childArr);
        result.put(children);
        return result;
    }

    public void saveData(Context context){
        try {
            JSONArray data = getJSON();
            File dir = new File(context.getFilesDir(), "childData");
            if(!dir.exists()){dir.mkdir();}
            File saveData = new File(dir, "child_data.json");
            try (FileWriter writer = new FileWriter(saveData)) {
                writer.write(data.toString());
                writer.flush();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void readData(){

    }
}
