package cmpt276.phosphorus.childapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChildManager {
    private List<String> children;
    private List<CoinFlipResult> flips;

    public ChildManager(){
        this.children = new ArrayList<>();
        this.flips = new ArrayList<>();
    }

    public void addChild(String name){
        this.children.add(name);
    }

    public void addFlip(CoinFlipResult result){
        this.flips.add(result);
    }

    public String getChild(int i){return this.children.get(i);}

    public CoinFlipResult getFlip(int i){return this.flips.get(i);}

    public String getFlipTime(int i){return getFlip(i).getFormattedTime();}
}
