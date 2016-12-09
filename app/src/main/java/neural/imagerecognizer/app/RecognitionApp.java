package neural.imagerecognizer.app;

import android.app.Application;
import neural.imagerecognizer.app.util.AppUncaughtExceptionHandler;
import neural.imagerecognizer.app.util.ThreadManager;
import neural.imagerecognizer.app.util.Tool;
import org.dmlc.mxnet.Predictor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecognitionApp extends Application {
    private static Predictor predictor;
    public static ThreadManager tm;
    private static RecognitionApp instance;

    public static Predictor getPredictor() {
        return predictor;
    }

    private static List<String> dict;
    private static Map<String, Float> mean;

    public static String getName(int i) {
        if (i >= dict.size()) {
            return instance.getString(R.string.text_image_not_recognized);
        }
        return dict.get(i);
    }

    public static Map<String, Float> getMean() {
        return mean;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        tm = ThreadManager.getInstance();
        //Thread.setDefaultUncaughtExceptionHandler(new AppUncaughtExceptionHandler(this));

        initMxNet();
    }

    private void initMxNet() {
        final byte[] symbol = Tool.readRawFile(this, R.raw.symbol);
        final byte[] params = Tool.readRawFile(this, R.raw.params);

        final Predictor.Device device = new Predictor.Device(Predictor.Device.Type.CPU, 0);
        //3 channel image on input
        final int[] shape = {1, 3, 224, 224};
        final String key = "data";
        final Predictor.InputNode node = new Predictor.InputNode(key, shape);

        predictor = new Predictor(symbol, params, device, new Predictor.InputNode[]{node});
        dict = Tool.readRawTextFile(this, R.raw.synset);
        try {
            final StringBuilder sb = new StringBuilder();
            final List<String> lines = Tool.readRawTextFile(this, R.raw.mean);
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


    public static RecognitionApp getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        tm.end();
    }
}
