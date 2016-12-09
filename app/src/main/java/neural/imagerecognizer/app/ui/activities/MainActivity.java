package neural.imagerecognizer.app.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import neural.imagerecognizer.app.R;
import neural.imagerecognizer.app.ui.views.WhatisButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @OnClick(R.id.btnWhatis)
    public void whatisClick() {
        btnWhatis.startAnimation();

    }

    @OnClick(R.id.ivGallery)
    public void selectFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, new CallbackResult() {
            @Override
            public void onResult(@NonNull Intent data) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);

                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
                        Uri photoUri = data.getData();
                        Bitmap bitmap = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                });
            }

            @Override
            public void onFailGrant() {
                Tool.showToast(MainActivity.this, "Please give camera permission!");
            }

            @NonNull
            @Override
            public String getPermissionName() {
                return Manifest.permission.CAMERA;
            }
        });
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
