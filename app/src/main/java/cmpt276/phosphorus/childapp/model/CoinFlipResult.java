package cmpt276.phosphorus.childapp.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

// todo temp, change this to the one from CoinAnimation branch
enum CoinSide {
    HEAD,
    TAILS
}

// Stored the result of a coin flip
public class CoinFlipResult {

    private final LocalDateTime time;
    private final CoinSide pickedSide;
    private final CoinSide flipResault;

    // Normal way to create resaults
    public CoinFlipResult(@NotNull CoinSide pickedSide, @NotNull CoinSide flipResault) {
        this(LocalDateTime.now(), pickedSide, flipResault);
    }

    // We might want this for saving/loading the resault, not sure yet. Will leave for now
    public CoinFlipResult(@NotNull LocalDateTime time, @NotNull CoinSide pickedSide, @NotNull CoinSide flipResault) {
        this.time = Objects.requireNonNull(time, "Resault cannot have a null time");
        this.pickedSide = Objects.requireNonNull(pickedSide, "Resault cannot have null pickedSide");
        this.flipResault = Objects.requireNonNull(flipResault, "Resault cannot have null flipSide");
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    // Gets date/time in format YYYY/MM/DD HH:mm
    public String getFormattedTime() {
        return this.time.getYear() + // YYYY
                "/" + this.time.getMonth() + // /MM
                "/" + this.time.getDayOfYear() + // /DD
                " " + this.time.getHour() + ":" + this.time.getHour(); // HH:mm
    }

    public CoinSide getPickedSide() {
        return this.pickedSide;
    }

    public CoinSide getFlipResault() {
        return this.flipResault;
    }

    public boolean getDidWin() {
        return this.pickedSide == this.flipResault;
    }

    @Override
    public String toString() {
        return "CoinFlipResult{" +
                "time=" + time +
                ", pickedSide=" + pickedSide +
                ", flipResault=" + flipResault +
                '}';
    }

}