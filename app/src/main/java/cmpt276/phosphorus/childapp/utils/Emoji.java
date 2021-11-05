package cmpt276.phosphorus.childapp.utils;

public enum Emoji {

    STAR(0x2B50);

    private final int unicode;

    Emoji(int unicode) {
        this.unicode = unicode;
    }

    public String get() {
        return new String(Character.toChars(this.unicode));
    }

}
