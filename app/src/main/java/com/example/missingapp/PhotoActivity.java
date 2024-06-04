package com.example.missingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    private RecyclerView photoRecyclerView;
    private UserService userService;
    private PhotoAdapter photoAdapter;
    private String accessToken;

    //임시
    private List<ProtectedTargetReadDto> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        photoRecyclerView = findViewById(R.id.photoRecyclerView);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("token", null);

        if (accessToken == null) {
            Toast.makeText(this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        userService = RetrofitClient.getClient(accessToken).create(UserService.class);

        //loadPhotos();

        //임시
        photoList = loadLocalData();

        photoAdapter = new PhotoAdapter(this, photoList);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoRecyclerView.setAdapter(photoAdapter);
    }

    /*private void loadPhotos() {
        Call<ProtectedTargetResponse> call = userService.getPhotos();
        call.enqueue(new Callback<ProtectedTargetResponse>() {
            @Override
            public void onResponse(Call<ProtectedTargetResponse> call, Response<ProtectedTargetResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProtectedTargetReadDto> photos = response.body().getProtectedTargetReadDtos();
                    Log.d(TAG, "불러온 사진 수: " + photos.size());
                    for (ProtectedTargetReadDto photo : photos) {
                        Log.d(TAG, "사진 정보: " + photo.getName() + ", " + photo.getAge() + ", " + photo.getImage());
                    }
                    if (photos.isEmpty()) {
                        Log.d(TAG, "불러온 사진 데이터가 없습니다.");
                    }
                    photoAdapter = new PhotoAdapter(PhotoActivity.this, photos);
                    photoRecyclerView.setLayoutManager(new LinearLayoutManager(PhotoActivity.this));
                    photoRecyclerView.setAdapter(photoAdapter);
                } else {
                    Log.e(TAG, "응답 코드: " + response.code());
                    Log.e(TAG, "응답 메시지: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "응답 본문: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(PhotoActivity.this, "사진 목록을 불러오지 못했습니다. 상태 코드: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProtectedTargetResponse> call, Throwable t) {
                Toast.makeText(PhotoActivity.this, "사진 목록을 불러오는 중 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "사진 목록을 불러오는 중 오류 발생", t);
            }
        });
    }*/
    private List<ProtectedTargetReadDto> loadLocalData() {
        List<ProtectedTargetReadDto> photos = new ArrayList<>();
        File dataDir = new File(getFilesDir(), "ProtectedTargets");
        if (dataDir.exists() && dataDir.isDirectory()) {
            File[] dataFiles = dataDir.listFiles((dir, name) -> name.endsWith("_data.json"));
            if (dataFiles != null) {
                Gson gson = new Gson();
                for (File dataFile : dataFiles) {
                    try (FileInputStream fis = new FileInputStream(dataFile);
                         Scanner scanner = new Scanner(fis)) {
                        StringBuilder json = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            json.append(scanner.nextLine());
                        }
                        Type type = new TypeToken<CreateDto>() {}.getType();
                        CreateDto createDto = gson.fromJson(json.toString(), type);
                        photos.add(new ProtectedTargetReadDto(createDto.getName(), createDto.getAge(), createDto.getImageFilePath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Log.e(TAG, "데이터 디렉토리가 존재하지 않습니다.");
        }
        return photos;
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
