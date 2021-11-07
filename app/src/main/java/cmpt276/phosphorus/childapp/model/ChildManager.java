package cmpt276.phosphorus.childapp.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.Month;
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
//            instance.dummyData();
        }

        return instance;
    }

    public void addChild(@NotNull Child child) {
        this.allChildren.add(Objects.requireNonNull(child));
    }

    public Child getChild(@NotNull CoinFlipResult targetCoinFlip) {
        return this.allChildren.stream()
                .filter(child -> child.getCoinFlipResults().stream().anyMatch(targetCoinFlip::equals))
                .findFirst()
                .orElse(null);
    }

    public void addChildren(@NotNull Child... children) {
        Arrays.asList(children).forEach(this::addChild); // Add children already checks for null
    }

    public Child getChildByUUID(@NotNull UUID uuid) {
        return this.allChildren.stream().filter(child -> child.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    public Child getChildByPos(@NotNull int position) {
        return this.allChildren.get(position);
    }

    public int getChildPosition(@NotNull Child child) {
        return this.allChildren.indexOf(child);
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

    public Child getNextCoinFlipper() {
        if(this.allChildren.isEmpty()) return null;

        if (this.lastCoinChooserChild == null)
            return this.getChildByPos(0);

        int nextPosition = this.getChildPosition(this.lastCoinChooserChild) + 1;
        if (nextPosition >= this.allChildren.size())
            nextPosition = 0;

        return this.getChildByPos(nextPosition);
    }

    public void setLastCoinChooserChild(Child lastCoinChooserChild) {
        this.lastCoinChooserChild = lastCoinChooserChild;
    }

    private boolean isEmpty(){
        return this.allChildren.isEmpty();
    }

    private void dummyData() {
        Child child1 = new Child("Bob");
        child1.addCoinFlipResult(new CoinFlipResult(
                LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40),
                CoinSide.HEAD,
                CoinSide.TAILS)
        );
        child1.addCoinFlipResult(new CoinFlipResult(
                LocalDateTime.of(2016, Month.JULY, 2, 9, 30, 40),
                CoinSide.TAILS,
                CoinSide.HEAD)
        );

        Child child2 = new Child("Jack");
        child2.addCoinFlipResult(new CoinFlipResult(
                LocalDateTime.of(2020, Month.APRIL, 2, 9, 30, 40),
                CoinSide.TAILS,
                CoinSide.TAILS)
        );

        ChildManager.getInstance().addChildren(child1, child2);
    }

}
