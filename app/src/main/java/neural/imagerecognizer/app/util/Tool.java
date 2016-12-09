package neural.imagerecognizer.app.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;
import neural.imagerecognizer.app.RecognitionApp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void showToast(@StringRes int message) {
        Context context = RecognitionApp.getInstance();
        showToast(context, context.getString(message));
    }

    public static void showToast(String message) {
        Context context = RecognitionApp.getInstance();
        showToast(context, message);
    }

    public static void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
    public static byte[] readRawFile(Context ctx, int resId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int size = 0;
        byte[] buffer = new byte[1024];
        try {
            InputStream ins = ctx.getResources().openRawResource(resId);
            while ((size = ins.read(buffer, 0, 1024)) >= 0) {
                outputStream.write(buffer, 0, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }


    public static List<String> readRawTextFile(Context ctx, int resId) {
        List<String> result = new ArrayList<>();
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        try {
            while ((line = buffreader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
