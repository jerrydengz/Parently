package cmpt276.phosphorus.childapp.model.child;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import cmpt276.phosphorus.childapp.model.coin.CoinFlipResult;

// ==============================================================================================
//
// Manager to keep track of all children data
//
// ==============================================================================================
public class ChildManager {

    private static ChildManager instance;

    private ArrayList<Child> allChildren;

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    private ChildManager() {
        this.allChildren = new ArrayList<>();
    }

    public void setChildren(ArrayList<Child> children) {
        this.allChildren = children;
    }

    public Child addChild(@NotNull Child child) {
        this.allChildren.add(Objects.requireNonNull(child));
        return child;
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

    public ArrayList<Child> getLastPickedOrderedChildren() {
        Child nextChild = this.getNextCoinFlipper();
        int indexOfNext = this.allChildren.indexOf(nextChild);

        // getNextCoinFlipper pretty much just goes to the next index each time called of the last picked player,
        // so we can continue getting the next index (or all at once) until they're all added
        ArrayList<Child> result = new ArrayList<>();

        // Puts the first child at the top of the list, adds the rest until end of list
        result.addAll(this.getChildrenInIndexRange(indexOfNext, this.allChildren.size()));
        // Adds any ones selected before to the end of the list
        result.addAll(this.getChildrenInIndexRange(0, indexOfNext));

        return result;
    }

    public Child getChildByUUID(String uuidStr) {
        if (uuidStr == null) return null;
        UUID targetChildUUID = UUID.fromString(uuidStr);
        return this.getChildByUUID(targetChildUUID);
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
        boolean isRemoved = this.allChildren.remove(child); // We make sure we do this before saving cause it might err
        if (isRemoved && child.isLastPicked() && !this.isEmpty()) {
            this.getNextCoinFlipper().setLastPicked(true);
        }
        return isRemoved;
    }

    public void clearAllLastPicked() {
        this.allChildren.forEach(child -> child.setLastPicked(false));
    }

    public Child getNextCoinFlipper() {
        if (this.allChildren.isEmpty()) return null;

        Child lastPicked = this.getLastPickedChild();
        if (lastPicked == null) // Haven't picked a child yet
            return this.getChildByPos(0);

        int nextPosition = this.getChildPosition(lastPicked) + 1;
        if (nextPosition >= this.allChildren.size())
            nextPosition = 0;

        return this.getChildByPos(nextPosition);
    }

    public boolean isEmpty() {
        return this.allChildren.isEmpty();
    }

    private List<Child> getChildrenInIndexRange(int start, int end) {
        return IntStream.range(start, end).mapToObj(this::getChildByPos).collect(Collectors.toList());
    }

    private Child getLastPickedChild() {
        return this.allChildren.stream().filter(Child::isLastPicked).findFirst().orElse(null);
    }

}