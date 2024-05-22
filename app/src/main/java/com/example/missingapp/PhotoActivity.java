package com.example.missingapp;

/*import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoActivity extends AppCompatActivity {

    private ListView photoListView;
    private UserService userService;
    private PhotoAdapter photoAdapter;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoListView = findViewById(R.id.photoListView);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("token", null);

        if (accessToken == null) {
            Toast.makeText(this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        userService = RetrofitClient.getClient(accessToken).create(UserService.class);

        loadPhotos();

        photoListView.setOnItemClickListener((parent, view, position, id) -> {
            Photo photo = (Photo) parent.getItemAtPosition(position);
            Intent intent = new Intent(PhotoActivity.this, PhotoDetailActivity.class);
            intent.putExtra("photoId", photo.getId());
            startActivity(intent);
        });
    }

    private void loadPhotos() {
        Call<List<Photo>> call = userService.getPhotos();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Photo> photos = response.body();
                    photoAdapter = new PhotoAdapter(PhotoActivity.this, photos);
                    photoListView.setAdapter(photoAdapter);
                } else {
                    Toast.makeText(PhotoActivity.this, "사진 목록을 불러오지 못했습니다. 상태 코드: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(PhotoActivity.this, "사진 목록을 불러오는 중 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}*/
