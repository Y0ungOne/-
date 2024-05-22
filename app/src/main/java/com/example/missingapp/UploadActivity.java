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

    private static final String TAG = "UploadActivity";
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

        btnConfirm.setOnClickListener(v -> uploadImageToServer());
    }

    private void uploadImageToServer() {
        String name = etName.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());

        // createDto 객체 생성
        CreateDto createDto = new CreateDto(name, age);

        // Gson을 사용하여 createDto 객체를 JSON으로 변환
        Gson gson = new Gson();
        String createDtoJson = gson.toJson(createDto);
        RequestBody requestBodyCreateDto = RequestBody.create(createDtoJson, MediaType.parse("application/json"));

        // 이미지 파일을 1MB 이하로 압축
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
                Toast.makeText(UploadActivity.this, "이미지 업로드 중 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File compressImageFile(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int quality = 100; // 초기 품질
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

        // 파일 크기가 1MB 이하가 될 때까지 품질을 줄임
        while (outputStream.toByteArray().length / 1024 > 1024) {
            outputStream.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        }

        // 압축된 이미지를 파일로 저장
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
