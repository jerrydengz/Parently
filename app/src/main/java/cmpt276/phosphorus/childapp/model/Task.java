package cmpt276.phosphorus.childapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task {
    private final int FIRST_INDEX = 0;

    private String name;
    private final List<UUID> children;

    public Task(String name, List<UUID> children, Child currentChild){
        this.name = name;
        this.children = new ArrayList<>();
        this.children.add(currentChild.getUUID());
        for(UUID iter : children){
            if(iter != currentChild.getUUID()){
                this.children.add(iter);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public void setNextChild(UUID nextChild){
        removeChild(nextChild);
        this.children.add(this.FIRST_INDEX, nextChild);
    }

    public UUID getCurrentChild(){
        return getChild(this.FIRST_INDEX);
    }

    public void cycleChildren(){

        this.children.add(this.children.remove(this.FIRST_INDEX));
    }

    public void removeChild(UUID child){

        this.children.remove(child);
    }

    public void addChild(UUID child){
        if(!this.children.contains(child)){
            this.children.add(child);
        }
    }

    public UUID getChild(int pos){

        return this.children.get(pos);
    }
}
