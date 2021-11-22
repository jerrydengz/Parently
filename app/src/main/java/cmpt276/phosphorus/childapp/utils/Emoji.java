package cmpt276.phosphorus.childapp.utils;


// ==============================================================================================
//
// A list of possible emojis through their unicode
//
// ==============================================================================================
public enum Emoji {

    SAD(0x1F627),
    HAPPY(0x1F600);

    private final int unicode;

    Emoji(int unicode) {
        this.unicode = unicode;
    }

    public String get() {
        return new String(Character.toChars(this.unicode));
    }

}
