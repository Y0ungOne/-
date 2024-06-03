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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

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

        // userService = RetrofitClient.getClient(accessToken).create(UserService.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("photoId")) {
            photoId = intent.getIntExtra("photoId", -1);
            loadMockPhotoDetails(photoId); // Mock 데이터 로드
        }

        updateButton.setOnClickListener(v -> updateMockPhoto()); // Mock 데이터 업데이트
        deleteButton.setOnClickListener(v -> deleteMockPhoto()); // Mock 데이터 삭제
    }

    /*
    private void loadPhotoDetails(int id) {
        Call<ProtectedTargetReadDto> call = userService.getPhoto(id);
        call.enqueue(new Callback<ProtectedTargetReadDto>() {
            @Override
            public void onResponse(Call<ProtectedTargetReadDto> call, Response<ProtectedTargetReadDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProtectedTargetReadDto photo = response.body();
                    photoName.setText(photo.getName());
                    photoAge.setText(String.valueOf(photo.getAge()));

                    if (photo.getImage() != null && !photo.getImage().isEmpty()) {
                        String base64Image = photo.getImage().get(0);
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        photoView.setImageBitmap(decodedByte);
                    } else {
                        photoView.setImageResource(R.drawable.placeholder_image);
                    }
                } else {
                    Toast.makeText(PhotoDetailActivity.this, "사진 정보를 가져오지 못했습니다. 상태 코드: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProtectedTargetReadDto> call, Throwable t) {
                Toast.makeText(PhotoDetailActivity.this, "서버와의 연결에 실패했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    */

    private void loadMockPhotoDetails(int id) {
        String json = loadJSONFromAsset("protected_targets.json");
        if (json != null) {
            Gson gson = new Gson();
            Type responseType = new TypeToken<ProtectedTargetResponse>() {}.getType();
            ProtectedTargetResponse response = gson.fromJson(json, responseType);
            for (ProtectedTargetReadDto photo : response.getProtectedTargetReadDtos()) {
                if (photo.getId() == id) {
                    photoName.setText(photo.getName());
                    photoAge.setText(String.valueOf(photo.getAge()));

                    if (photo.getImage() != null && !photo.getImage().isEmpty()) {
                        String base64Image = photo.getImage().get(0);
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        photoView.setImageBitmap(decodedByte);
                    } else {
                        photoView.setImageResource(R.drawable.error);
                    }
                    return;
                }
            }
        } else {
            Toast.makeText(this, "로컬 JSON 파일을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void updateMockPhoto() {
        String name = photoName.getText().toString();
        int age = Integer.parseInt(photoAge.getText().toString());

        // 실제 서버 없이 업데이트하는 부분
        Toast.makeText(this, "사진 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void deleteMockPhoto() {
        // 실제 서버 없이 삭제하는 부분
        Toast.makeText(this, "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
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
