package cmpt276.phosphorus.childapp.model;

//Stores information about the child
public class Child {
    private String name;
    private boolean isTimeout;

    public Child(String name){
        this.name = name;
        this.isTimeout = false;
    }

    public void setTimeout(){this.isTimeout = true;}

    public boolean getIsTimeout(){return this.isTimeout;}

    public void endTimeout(){this.isTimeout = false;}

    public String getName(){return this.name;}
}
