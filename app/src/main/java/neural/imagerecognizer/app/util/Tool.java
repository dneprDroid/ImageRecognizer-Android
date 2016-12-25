package neural.imagerecognizer.app.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;
import neural.imagerecognizer.app.RecognitionApp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public static byte[] readRawFile(@RawRes int resId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int size = 0;
        byte[] buffer = new byte[1024];
        try {
            InputStream ins = RecognitionApp.getInstance().getApplicationContext().getResources().openRawResource(resId);
            while ((size = ins.read(buffer, 0, 1024)) >= 0) {
                outputStream.write(buffer, 0, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }


    public static String readRawTextFile(@RawRes int resId) {
        StringBuilder result = new StringBuilder();
        InputStream inputStream = RecognitionApp.getInstance().getApplicationContext().getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        try {
            while ((line = buffreader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static List<String> readRawTextFileAsList(@RawRes int resId) {
        List<String> result = new ArrayList<>();
        InputStream inputStream = RecognitionApp.getInstance().getApplicationContext().getResources().openRawResource(resId);

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

    //Debug
    public static void saveBitmap(Bitmap bitmap) {
        File myDir = new File("/sdcard/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = String.format("Image-%s.jpg", n);
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareText(Context context, @NonNull String str) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static String generateGooglePlayLink() {

        return String.format("https://play.google.com/store/apps/details?id=%s",
                RecognitionApp.getInstance().getPackageName());
    }

    public static int getToolbarHeight() {
        Context ctx = RecognitionApp.getInstance().getApplicationContext();
        TypedValue tv = new TypedValue();
        if (ctx.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,
                    ctx.getResources().getDisplayMetrics());
        }
        return 0;
    }
}
