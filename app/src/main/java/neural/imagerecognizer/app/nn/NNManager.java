package neural.imagerecognizer.app.nn;

import android.graphics.Bitmap;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import neural.imagerecognizer.app.R;
import neural.imagerecognizer.app.RecognitionApp;
import neural.imagerecognizer.app.util.ThreadManager;
import neural.imagerecognizer.app.util.Tool;
import org.dmlc.mxnet.Predictor;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NNManager {


    private List<String> dict;
    private Map<String, Float> mean;

    private Predictor predictor;

    private static final int SHORTER_SIDE = 256;
    private static final int DESIRED_SIDE = 224;
    private static NNManager shared;

    private NNManager() {
    }

    public static synchronized NNManager shared() {
        if (shared == null) {
            shared = new NNManager();
            shared.initMxNet();
        }
        return shared;
    }

    private void initMxNet() {
        final byte[] symbol = Tool.readRawFile(R.raw.symbol);
        final byte[] params = Tool.readRawFile(R.raw.params);

        final Predictor.Device device = new Predictor.Device(Predictor.Device.Type.CPU, 0);
        //3 channel image on input
        final int[] shape = {1, 3, 224, 224};
        final String key = "data";
        final Predictor.InputNode node = new Predictor.InputNode(key, shape);

        predictor = new Predictor(symbol, params, device, new Predictor.InputNode[]{node});
        dict = Tool.readRawTextFile(R.raw.synset);
        try {
            final StringBuilder sb = new StringBuilder();
            final List<String> lines = Tool.readRawTextFile(R.raw.mean);
            for (final String line : lines) {
                sb.append(line);
            }
            final JSONObject meanJson = new JSONObject(sb.toString());
            mean = new HashMap<>();
            mean.put("b", (float) meanJson.optDouble("b"));
            mean.put("g", (float) meanJson.optDouble("g"));
            mean.put("r", (float) meanJson.optDouble("r"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void identifyImage(final Bitmap bitmap, final Callback callback) {
        RecognitionApp.tm.execute(new ThreadManager.Executor<String>() {
            @Nullable
            @Override
            public String onExecute() throws Exception {
                Bitmap processedBitmap = processBitmap(bitmap);
                ByteBuffer byteBuffer = ByteBuffer.allocate(processedBitmap.getByteCount());
                processedBitmap.copyPixelsToBuffer(byteBuffer);
                byte[] bytes = byteBuffer.array();
                float[] colors = new float[bytes.length / 4 * 3];

                float mean_b = getMean().get("b");
                float mean_g = getMean().get("g");
                float mean_r = getMean().get("r");
                for (int i = 0; i < bytes.length; i += 4) {
                    int j = i / 4;
                    colors[0 * DESIRED_SIDE * DESIRED_SIDE + j] = (float) (((int) (bytes[i + 0])) & 0xFF) - mean_r;
                    colors[1 * DESIRED_SIDE * DESIRED_SIDE + j] = (float) (((int) (bytes[i + 1])) & 0xFF) - mean_g;
                    colors[2 * DESIRED_SIDE * DESIRED_SIDE + j] = (float) (((int) (bytes[i + 2])) & 0xFF) - mean_b;
                }
                Predictor predictor = getPredictor();
                predictor.forward("data", colors);
                final float[] result = predictor.getOutput(0);

                int index = 0;
                for (int i = 0; i < result.length; ++i) {
                    if (result[index] < result[i]) index = i;
                }
                Arrays.sort(result);
                String tag = getName(index);
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
                Tool.showToast(R.string.toast_recognition_error);
            }
        });
    }

    public static void init() {
        shared();
    }

    private Bitmap processBitmap(final Bitmap origin) {
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


    private String getName(int i) {
        if (i >= dict.size()) {
            return RecognitionApp.getInstance().getString(R.string.text_image_not_recognized);
        }
        return dict.get(i);
    }

    private Predictor getPredictor() {
        return predictor;
    }

    private Map<String, Float> getMean() {
        return mean;
    }

    public interface Callback {
        @MainThread
        void onResult(@NonNull String description);
    }

}
