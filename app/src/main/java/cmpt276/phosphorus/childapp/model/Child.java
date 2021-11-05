package cmpt276.phosphorus.childapp.model;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Child {

    private final UUID uuid;
    private final List<CoinFlipResult> coinFlipResults;
    private String name;

    // Normal way to create children
    public Child(@NotNull String name) {
        this(UUID.randomUUID(), new ArrayList<>(), name);
    }

    // We might want this for saving/loading the children, not sure yet. Will leave for now
    public Child(@NotNull UUID uuid, @NotNull List<CoinFlipResult> coinFlipResults, @NotNull String name) {
        this.uuid = Objects.requireNonNull(uuid, "Children UUID cannot be null");
        this.coinFlipResults = Objects.requireNonNull(coinFlipResults, "Children flips results cannot be null");
        this.setName(name); // setName already makes sure name isn't null || empty
    }

    public int getTotalLosses() {
        // Ex 10 flips:
        // 10 flips - 7 wins = 3 losses
        return this.coinFlipResults.size() - this.getTotalWins();
    }

    public int getTotalWins() {
        return (int) this.coinFlipResults.stream().filter(CoinFlipResult::getDidWin).count();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.name = name;
    }

    // Returns sorted coin results from Oldest -> Newest
    public List<CoinFlipResult> getCoinFlipResults() {
        return this.coinFlipResults.stream().sorted(Comparator.comparing(CoinFlipResult::getTime)).collect(Collectors.toList());
    }

    public void addCoinFlipResult(@NotNull CoinFlipResult coinFlipResult) {
        this.coinFlipResults.add(Objects.requireNonNull(coinFlipResult, "Cannot add a null coin result"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) return false;
        Child child = (Child) o;
        return Objects.equals(this.uuid, child.getUUID()) && Objects.equals(this.name, child.getName());
    }

    @NonNull
    @Override
    public String toString() {
        return "Children{" +
                "uuid=" + this.uuid +
                ", name='" + this.name + '\'' +
                ", coinFlipResults=" + this.coinFlipResults +
                '}';
    }

    protected JSONObject getJSONChild() throws JSONException{
        JSONObject inner = new JSONObject();
        JSONObject outer = new JSONObject();
        JSONArray flips = new JSONArray();
        inner.put("UUID", this.uuid);
        inner.put("name", this.name);
        for(CoinFlipResult i : this.coinFlipResults){
            flips.put(i.toJSON());
        }
        inner.put("coinFlipResults", flips);
        outer.put("child", inner);
        return outer;
    }

}
