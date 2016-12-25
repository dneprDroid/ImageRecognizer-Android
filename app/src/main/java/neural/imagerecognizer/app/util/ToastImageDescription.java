package neural.imagerecognizer.app.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import neural.imagerecognizer.app.R;

public class ToastImageDescription {
    public static void show(final Context context, final String message) {

        Tool.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.TOP, 0, (int) (Tool.getToolbarHeight() * 1.5));
                toast.setDuration(Toast.LENGTH_LONG);
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.toast_image_description, null);
                tv.setText(message);
                toast.setView(tv);
                toast.show();
            }
        });
    }
}
