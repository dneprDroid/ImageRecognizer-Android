package neural.imagerecognizer.app.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import neural.imagerecognizer.app.R;
import neural.imagerecognizer.app.ui.views.PaintView;
import neural.imagerecognizer.app.ui.views.WhatisButton;
import neural.imagerecognizer.app.util.MxNetUtils;
import neural.imagerecognizer.app.util.ToastImageDescription;
import neural.imagerecognizer.app.util.Tool;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.OnClick;
import com.desmond.squarecamera.CameraActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends BaseActivity {

    @Bind(R.id.btnWhatis)
    WhatisButton btnWhatis;

    @Bind(R.id.paintView)
    PaintView paintView;

    @Nullable
    private Bitmap recognBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @OnClick(R.id.btnWhatis)
    public void whatisClick() {

        if (paintView.isModePaint()) {
            recognBitmap = paintView.getPaintedBitmap();
        } else if (paintView.isModePhoto())
            if (recognBitmap == null)
                return;

        btnWhatis.startAnimation();
        MxNetUtils.identifyImage(recognBitmap, new MxNetUtils.Callback() {
            @Override
            public void onResult(@NonNull String description) {
                btnWhatis.endAnimation();
                //set image description....
                ToastImageDescription.show(MainActivity.this, description);
            }
        });

    }

    @OnClick(R.id.ivErse)
    public void erse() {
        paintView.clearBitmap();
    }

    @OnClick(R.id.ivPencil)
    public void enablePaintMode() {
        paintView.setModePaint();
    }

    @OnClick(R.id.ivGallery)
    public void selectFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, new CallbackResult() {
            @Override
            public void onResult(@NonNull Intent data) {
                setImageFromIntent(data);
            }

        });
    }

    @OnClick(R.id.ivCamera)
    public void selectFromCamera() {
        requestPermission(new PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Intent startCustomCameraIntent = new Intent(MainActivity.this, CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, new CallbackResult() {
                    @Override
                    public void onResult(@NonNull Intent data) {
                        setImageFromIntent(data);
                    }
                });
            }

            @Override
            public void onFail() {
                Tool.showToast(MainActivity.this, "Please give camera permission!");
            }

            @NonNull
            @Override
            public String getPermissionName() {
                return Manifest.permission.CAMERA;
            }
        });
    }

    private void setImageFromIntent(Intent data) {
        try {
            Uri imageUri = data.getData();
            InputStream imageStream = getContentResolver().openInputStream(imageUri);

            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            this.recognBitmap = bitmap;
            paintView.setPhoto(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
