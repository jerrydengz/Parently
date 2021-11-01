package cmpt276.phosphorus.childapp.model;

import java.util.ArrayList;
import java.util.List;

public class ChildManager {
    private final List<String> children;
    private final List<CoinFlipResult> flips;

    public ChildManager(){
        this.children = new ArrayList<>();
        this.flips = new ArrayList<>();
    }

    public void addChild(String name){this.children.add(name);}

    public void addFlip(CoinFlipResult result){this.flips.add(result);}

    public String getChild(int index){return this.children.get(index);}

    public CoinFlipResult getFlip(int index){return this.flips.get(index);}

    public String getFlipTime(int index){return getFlip(index).getFormattedTime();}

    public int getNumFlips(){return this.flips.size();}

    public int getNumChild(){return this.children.size();}
}
