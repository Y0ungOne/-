package com.example.missingapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Camera_Activity extends AppCompatActivity {

    private static final String TAG = "Camera_Activity";
    private String imageFilePath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        Button buttonConfirm = findViewById(R.id.btn_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                intent.putExtra("imageFilePath", imageFilePath);
                startActivity(intent);
            }
        });

    }

    private final ActivityResultLauncher<Intent> startActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        processCapturedPhoto();
                    } else {
                        Log.e(TAG, "Failed to capture image, resultCode: " + result.getResultCode());
                    }
                }
            }
    );

    private void launchCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, "Error creating image file", e);
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityResultLauncher.launch(intent);
            } else {
                Log.e(TAG, "Photo file is null");
            }
        } else {
            Log.e(TAG, "No activity found to handle camera intent. Ensure a camera application is installed.");
        }
    }

    private void processCapturedPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(imageFilePath);
        } catch (IOException e) {
            Log.e(TAG, "Error reading Exif data", e);
        }

        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }

        String resultPath = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        String filename = formatter.format(curDate);

        String strFolderName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "HONGDROID" + File.separator;
        File file = new File(strFolderName);
        if (!file.exists()) file.mkdirs();

        File f = new File(strFolderName + "/" + filename + ".png");
        resultPath = f.getPath();

        try (FileOutputStream fOut = new FileOutputStream(f)) {
            rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.PNG, 70, fOut);
            fOut.flush();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found", e);
            resultPath = "Save Error fOut";
        } catch (IOException e) {
            Log.e(TAG, "Error saving file", e);
            resultPath = "File close Error";
        }

        ((ImageView) findViewById(R.id.iv_result)).setImageBitmap(rotate(bitmap, exifDegree));
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) return 90;
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) return 180;
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) return 270;
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            launchCameraIntent();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨: " + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Permission denied: " + deniedPermissions.toString());
        }
    };
}
