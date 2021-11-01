package cmpt276.phosphorus.childapp.model;

import java.time.LocalDateTime;

//Stored the result of a coin flip
public class CoinFlipResult {
    private String sidePicker;
    private LocalDateTime time;
    private boolean isHeads;
    private boolean didWin;

    public CoinFlipResult(String pick, boolean heads, boolean win) {
        this.sidePicker = pick;
        this.time = LocalDateTime.now();
        this.isHeads = heads;
        this.didWin = win;
    }

    public CoinFlipResult(boolean heads){
        this.sidePicker = "";
        this.time = LocalDateTime.now();
        this.isHeads = heads;
        this.didWin = false;
    }

    public String getSidePicker(){return this.sidePicker;}

    public boolean getDidWin(){return this.didWin;}

    public LocalDateTime getTime(){return this.time;}

    //gets date/time in format YYYY/MM/DD HH:mm
    public String getFormattedTime(){
        String result = "" + time.getYear();
        result += "/" + time.getMonth() + "/" + time.getDayOfYear();
        result += " " + time.getHour() + ":" + time.getMinute();
        return result;
    }

    public boolean isHeads(){return this.isHeads;}
}
