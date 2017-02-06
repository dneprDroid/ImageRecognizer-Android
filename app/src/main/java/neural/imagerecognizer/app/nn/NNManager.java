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
        dict = Tool.readRawTextFileAsList(R.raw.synset);
        try {
            final String meanStr = Tool.readRawTextFile(R.raw.mean);
            final JSONObject meanJson = new JSONObject(meanStr);
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
                float[] colors = TensorMaker.convertBitmapToTensor(bitmap, mean);
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


    private String getName(int i) {
        if (i >= dict.size()) {
            return RecognitionApp.getInstance().getString(R.string.text_image_not_recognized);
        }
        return dict.get(i);
    }

    public interface Callback {
        @MainThread
        void onResult(@NonNull String description);
    }

}
