package neural.imagerecognizer.app.util;

import android.graphics.Bitmap;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import neural.imagerecognizer.app.RecognitionApp;
import org.dmlc.mxnet.Predictor;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class MxNetUtils {
    private MxNetUtils() {
    }

    public static void identifyImage(final Bitmap bitmap, final Callback callback) {
        RecognitionApp.tm.execute(new ThreadManager.Executor<String>() {
            @Nullable
            @Override
            public String onExecute() throws Exception {
                ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
                bitmap.copyPixelsToBuffer(byteBuffer);
                byte[] bytes = byteBuffer.array();
                float[] colors = new float[bytes.length / 4 * 3];

                float mean_b = RecognitionApp.getMean().get("b");
                float mean_g = RecognitionApp.getMean().get("g");
                float mean_r = RecognitionApp.getMean().get("r");
                for (int i = 0; i < bytes.length; i += 4) {
                    int j = i / 4;
                    colors[0 * 224 * 224 + j] = (float) (((int) (bytes[i + 0])) & 0xFF) - mean_r;
                    colors[1 * 224 * 224 + j] = (float) (((int) (bytes[i + 1])) & 0xFF) - mean_g;
                    colors[2 * 224 * 224 + j] = (float) (((int) (bytes[i + 2])) & 0xFF) - mean_b;
                }
                Predictor predictor = RecognitionApp.getPredictor();
                predictor.forward("data", colors);
                final float[] result = predictor.getOutput(0);

                int index = 0;
                for (int i = 0; i < result.length; ++i) {
                    if (result[index] < result[i]) index = i;
                }
                Arrays.sort(result);
                String tag = RecognitionApp.getName(index);
                Tool.log("recognition competed: %s", tag);
                String[] arr = tag.split(" ", 2);
                return arr[1];
            }

            @Override
            public void onCallback(@NonNull String data) {
                callback.onResult(data);
            }

            @Override
            public void onError(Exception e) {
                Tool.log("error of img recogn. :  %s", e);
            }
        });
    }

    public interface Callback {
        @MainThread
        void onResult(@NonNull String description);
    }

}
