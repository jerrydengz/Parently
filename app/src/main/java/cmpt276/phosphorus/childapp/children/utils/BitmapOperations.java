package cmpt276.phosphorus.childapp.children.utils;

import android.graphics.Bitmap;

public class BitmapOperations {

    // todo: Refactor this according to UI if needed
    public static final int MAX_WIDTH = 200;
    public static final int MAX_HEIGHT = 200;

    // Code from https://stackoverflow.com/questions/15124179/resizing-a-bitmap-to-a-fixed-value-but-without-changing-the-aspect-ratio/22882877#22882877

    public static Bitmap scaleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) width / MAX_WIDTH;
            width = MAX_WIDTH;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / MAX_HEIGHT;
            height = MAX_HEIGHT;
            width = (int)(width / ratio);
        } else {
            // square
            height = MAX_HEIGHT;
            width = MAX_WIDTH;
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}