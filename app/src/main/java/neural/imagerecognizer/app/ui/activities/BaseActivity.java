package neural.imagerecognizer.app.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import neural.imagerecognizer.app.util.Tool;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;

    private static final int REQUEST_CODE = 101;

    private CallbackResult callback;
    private PermissionCallback permissionCallback;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        bind();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        bind();
    }

    private void bind() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void startActivityForResult(Intent intent, CallbackResult callback) {
        this.callback = callback;
        super.startActivityForResult(intent, REQUEST_CODE);
    }

    public void requestPermission(PermissionCallback permissionCallback) {
        final String permission = permissionCallback.getPermissionName();// Manifest.permission.CAMERA;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                permissionCallback.onFail();
            } else {
                // Handle the result in Activity#onRequestPermissionResult(int, String[], int[])
                this.permissionCallback = permissionCallback;
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION);
            }
        } else {
            permissionCallback.onPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_PERMISSION || permissionCallback == null)
            return;
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;

            if (permission.equals(permissionCallback.getPermissionName())) {
                if (granted)
                    permissionCallback.onPermissionGranted();
                else
                    permissionCallback.onFail();
                break;
            }
        }
        permissionCallback = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean resultOk = resultCode == RESULT_OK && requestCode == REQUEST_CODE && callback != null && data != null;
        if (resultOk)
            callback.onResult(data);
        else
            Tool.log("data nullable is %s", data == null);
        callback = null;
    }

    public interface PermissionCallback {
        void onPermissionGranted();

        void onFail();

        @NonNull
        String getPermissionName();
    }

    public interface CallbackResult {
        void onResult(@NonNull Intent data);
    }
}
