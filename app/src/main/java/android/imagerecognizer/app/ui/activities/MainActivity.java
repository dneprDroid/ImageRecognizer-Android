package android.imagerecognizer.app.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.imagerecognizer.app.R;
import android.imagerecognizer.app.ui.views.WhatisButton;
import android.imagerecognizer.app.util.Tool;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends BaseActivity {

    @Bind(R.id.btnWhatis)
    protected WhatisButton btnWhatis;

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
        startActivityForResult(photoPickerIntent, new Callback() {
            @Override
            public void onCallback(@NonNull Intent data) {
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
