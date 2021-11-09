package cmpt276.phosphorus.childapp.timeout.utils;

import java.util.Locale;

public class TimeConversionUtils {
    // Interval in milliseconds the timer updates its countdown
    public static final int COUNT_DOWN_INTERVAL = 1000;
    public static final long NUM_TO_MULTI_TO_CONVERT_MIN_TO_MILLISECONDS = 60000L;

    public static String timeLeftFormatter(long timeLeft) {
        final int CONVERT_MILLISECONDS_TO_SECONDS = 1000;
        final int CONVERT_SECONDS_TO_MINUTES = 60;

        int minutes = (int) ((timeLeft / CONVERT_MILLISECONDS_TO_SECONDS) / CONVERT_SECONDS_TO_MINUTES);
        int seconds = (int) ((timeLeft / CONVERT_MILLISECONDS_TO_SECONDS) % CONVERT_SECONDS_TO_MINUTES);

        return String.format(Locale.getDefault(),
                "%02d:%02d", minutes, seconds);
    }

}
