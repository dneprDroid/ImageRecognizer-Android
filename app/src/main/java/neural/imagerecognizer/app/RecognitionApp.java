package neural.imagerecognizer.app;

import android.app.Application;
import neural.imagerecognizer.app.nn.NNManager;
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
    public static ThreadManager tm;
    private static RecognitionApp instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        tm = ThreadManager.getInstance();
        Thread.setDefaultUncaughtExceptionHandler(new AppUncaughtExceptionHandler(this));

        NNManager.init();
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
