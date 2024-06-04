package com.example.missingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    private EditText etName, etAge;
    private ImageView ivCapturedImage;
    private String imageFilePath;
    private UserService userService;
    private String accessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);

        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        ivCapturedImage = findViewById(R.id.iv_captured_image);
        Button btnConfirm = findViewById(R.id.btn_confirm);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("token", null);

        if (accessToken == null || accessToken.isEmpty()) {
            Toast.makeText(this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        userService = RetrofitClient.getClient(accessToken).create(UserService.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("imageFilePath")) {
            imageFilePath = intent.getStringExtra("imageFilePath");
            ivCapturedImage.setImageBitmap(BitmapFactory.decodeFile(imageFilePath));
        }

        //btnConfirm.setOnClickListener(v -> uploadImageToServer());
        //임시로 로컬에 저장하기
        btnConfirm.setOnClickListener(v -> saveImageLocally());
    }

    /*private void uploadImageToServer() {
        String name = etName.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());

        CreateDto createDto = new CreateDto(name, age);

        Gson gson = new Gson();
        String createDtoJson = gson.toJson(createDto);
        RequestBody requestBodyCreateDto = RequestBody.create(createDtoJson, MediaType.parse("application/json"));

        File compressedImageFile = compressImageFile(imageFilePath);

        if (compressedImageFile == null) {
            Toast.makeText(this, "이미지 압축 실패", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestBodyFile = RequestBody.create(compressedImageFile, MediaType.parse("image/jpeg"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", compressedImageFile.getName(), requestBodyFile);

        Call<Void> call = userService.registerProtectedTarget(requestBodyCreateDto, body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "보호 대상이 등록되었습니다!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadActivity.this, Mypage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(UploadActivity.this, "이미지 업로드 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UploadActivity.this, "이미지 업로드 중 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private void saveImageLocally() {
        String name = etName.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());

        File compressedImageFile = compressImageFile(imageFilePath);

        if (compressedImageFile == null) {
            Toast.makeText(this, "이미지 압축 실패", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File dataDir = new File(getFilesDir(), "ProtectedTargets");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            // Save image
            File imageFile = new File(dataDir, name + "_" + age + "_image.jpg");
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                Bitmap bitmap = BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            }

            // Save data
            CreateDto createDto = new CreateDto(name, age, imageFile.getAbsolutePath());
            Gson gson = new Gson();
            String createDtoJson = gson.toJson(createDto);
            File dataFile = new File(dataDir, name + "_" + age + "_data.json");
            try (FileOutputStream fos = new FileOutputStream(dataFile)) {
                fos.write(createDtoJson.getBytes());
                fos.flush();
            }

            Toast.makeText(this, "데이터가 어플 내에 저장되었습니다", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "데이터 저장 실패", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(UploadActivity.this, PhotoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private File compressImageFile(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

        while (outputStream.toByteArray().length / 1024 > 1024) {
            outputStream.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        }

        File compressedFile = new File(getCacheDir(), "compressed_image.jpg");
        try (FileOutputStream fos = new FileOutputStream(compressedFile)) {
            fos.write(outputStream.toByteArray());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return compressedFile;
    }
}
