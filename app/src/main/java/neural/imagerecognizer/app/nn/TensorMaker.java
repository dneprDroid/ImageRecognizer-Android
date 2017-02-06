package neural.imagerecognizer.app.nn;


import android.graphics.Bitmap;

import java.nio.ByteBuffer;
import java.util.Map;

public final class TensorMaker {
    private static final int SHORTER_SIDE = 256;
    private static final int DESIRED_SIDE = 224; // default image side for input in inception-bn network

    private TensorMaker(){}

    public static float[] convertBitmapToTensor(Bitmap bitmap, Map<String, Float> mean) {
        Bitmap processedBitmap = processBitmap(bitmap);
        ByteBuffer byteBuffer = ByteBuffer.allocate(processedBitmap.getByteCount());
        processedBitmap.copyPixelsToBuffer(byteBuffer);
        byte[] bytes = byteBuffer.array();
        float[] colors = new float[bytes.length / 4 * 3];

        float mean_b = mean.get("b");
        float mean_g = mean.get("g");
        float mean_r = mean.get("r");
        for (int i = 0; i < bytes.length; i += 4) {
            int j = i / 4;
            colors[0 * DESIRED_SIDE * DESIRED_SIDE + j] = (float) (((int) (bytes[i + 0])) & 0xFF) - mean_r; // red
            colors[1 * DESIRED_SIDE * DESIRED_SIDE + j] = (float) (((int) (bytes[i + 1])) & 0xFF) - mean_g; // green
            colors[2 * DESIRED_SIDE * DESIRED_SIDE + j] = (float) (((int) (bytes[i + 2])) & 0xFF) - mean_b; // blue
        }
        return colors;
    }

    private static Bitmap processBitmap(final Bitmap origin) {
        final int originWidth = origin.getWidth();
        final int originHeight = origin.getHeight();
        int height = SHORTER_SIDE;
        int width = SHORTER_SIDE;
        if (originWidth < originHeight) {
            height = (int) ((float) originHeight / originWidth * width);
        } else {
            width = (int) ((float) originWidth / originHeight * height);
        }
        final Bitmap scaled = Bitmap.createScaledBitmap(origin, width, height, false);
        int y = (height - DESIRED_SIDE) / 2;
        int x = (width - DESIRED_SIDE) / 2;
        return Bitmap.createBitmap(scaled, x, y, DESIRED_SIDE, DESIRED_SIDE);

    }

}
