package neural.imagerecognizer.app.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import neural.imagerecognizer.app.ui.activities.BaseActivity;
import neural.imagerecognizer.app.ui.activities.MainActivity;

public class AppUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Context context;

    public AppUncaughtExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Tool.log("Exception: " + ex);

        Class<? extends BaseActivity> activityClass = MainActivity.class;

        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(intent), PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 15, pendingIntent);
        System.exit(2);
    }
}
