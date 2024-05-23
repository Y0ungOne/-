package com.example.missingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoDetailActivity extends AppCompatActivity {

    private ImageView photoView;
    private EditText photoName, photoAge;
    private UserService userService;
    private String accessToken;
    private int photoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("상세보기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoView = findViewById(R.id.photoView);
        photoName = findViewById(R.id.photoName);
        photoAge = findViewById(R.id.photoAge);
        Button updateButton = findViewById(R.id.updateButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("token", null);

        if (accessToken == null || accessToken.isEmpty()) {
            Toast.makeText(this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        userService = RetrofitClient.getClient(accessToken).create(UserService.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("photoId")) {
            photoId = intent.getIntExtra("photoId", -1);
            loadPhotoDetails(photoId);
        }

        updateButton.setOnClickListener(v -> updatePhoto());
        deleteButton.setOnClickListener(v -> deletePhoto());
    }

    private void loadPhotoDetails(int id) {
        Call<Photo> call = userService.getPhoto(id);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Photo photo = response.body();
                    photoName.setText(photo.getName());
                    photoAge.setText(String.valueOf(photo.getAge()));

                    if (photo.getImage() != null && !photo.getImage().isEmpty()) {
                        String base64Image = photo.getImage().get(0);
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        photoView.setImageBitmap(decodedByte);
                    } else {
                        photoView.setImageResource(R.drawable.image_button);
                    }
                } else {
                    Toast.makeText(PhotoDetailActivity.this, "사진 정보를 가져오지 못했습니다. 상태 코드: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Toast.makeText(PhotoDetailActivity.this, "서버와의 연결에 실패했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePhoto() {
        String name = photoName.getText().toString();
        int age = Integer.parseInt(photoAge.getText().toString());

        Photo updateRequest = new Photo(photoId, name, age, null);
        Call<Photo> call = userService.updatePhoto(updateRequest);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PhotoDetailActivity.this, "사진 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PhotoDetailActivity.this, "업데이트 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Toast.makeText(PhotoDetailActivity.this, "업데이트 중 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePhoto() {
        Call<Void> call = userService.deletePhoto(photoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PhotoDetailActivity.this, "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PhotoDetailActivity.this, "삭제 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PhotoDetailActivity.this, "삭제 중 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
