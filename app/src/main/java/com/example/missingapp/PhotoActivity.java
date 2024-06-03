package com.example.missingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    private RecyclerView photoRecyclerView;
    private PhotoAdapter photoAdapter;
    private UserService userService;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("token", null);

        if (accessToken == null || accessToken.isEmpty()) {
            Toast.makeText(this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        userService = RetrofitClient.getClient(accessToken).create(UserService.class);
        loadPhotos();*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMockPhotos();
    }


    /*
    private void loadPhotos() {
        Call<List<ProtectedTargetReadDto>> call = userService.getPhotos();
        call.enqueue(new Callback<List<ProtectedTargetReadDto>>() {
            @Override
            public void onResponse(Call<List<ProtectedTargetReadDto>> call, Response<List<ProtectedTargetReadDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProtectedTargetReadDto> photos = response.body();
                    Log.d(TAG, "불러온 사진 수: " + photos.size());
                    photoAdapter = new PhotoAdapter(PhotoActivity.this, photos);
                    photoRecyclerView.setAdapter(photoAdapter);
                } else {
                    Log.e(TAG, "사진 목록을 불러오지 못했습니다. 상태 코드: " + response.code());
                    Toast.makeText(PhotoActivity.this, "사진 목록을 불러오지 못했습니다. 상태 코드: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProtectedTargetReadDto>> call, Throwable t) {
                Log.e(TAG, "사진 목록을 불러오는 중 오류 발생: " + t.getMessage());
                Toast.makeText(PhotoActivity.this, "사진 목록을 불러오는 중 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    */

    private void loadMockPhotos() {
        List<ProtectedTargetReadDto> protectedTargets = new ArrayList<>();

        // Mock 데이터 생성
        List<String> imageList1 = new ArrayList<>();
        imageList1.add(encodeImageToBase64(R.drawable.sample_image1));
        List<String> imageList2 = new ArrayList<>();
        imageList2.add(encodeImageToBase64(R.drawable.sample_image2));

        protectedTargets.add(new ProtectedTargetReadDto(1, "John Doe", 30, imageList1));
        protectedTargets.add(new ProtectedTargetReadDto(2, "Jane Doe", 25, imageList2));

        List<Photo> photos = new ArrayList<>();
        for (ProtectedTargetReadDto target : protectedTargets) {
            photos.add(new Photo(target.getId(), target.getName(), target.getAge(), target.getImage()));
        }

        photoAdapter = new PhotoAdapter(this, photos);
        photoRecyclerView.setAdapter(photoAdapter);
    }

    private String encodeImageToBase64(int imageResId) {
        // Drawable을 Bitmap으로 변환
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        // Base64로 인코딩
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}