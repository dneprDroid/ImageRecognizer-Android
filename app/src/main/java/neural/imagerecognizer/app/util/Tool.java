package neural.imagerecognizer.app.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

public final class Tool {

    private Tool() {
    }


    public static void log(String s, Object... args) {
        log(String.format(s, args));
    }

    public static void log(String s) {
        Log.v("ImageRecognizer", s);
    }

    public static void showToast(final Context context, final String message) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showToast(Context context, @StringRes int message) {
        showToast(context, context.getString(message));
    }

    private static void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
