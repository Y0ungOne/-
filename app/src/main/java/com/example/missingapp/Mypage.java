package com.example.missingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.congestionobserver.ActivityContainer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Mypage extends AppCompatActivity {

    private TextView textViewNickName;
    private TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        // 닉네임 및 이메일 TextView 초기화
        textViewNickName = findViewById(R.id.textViewNickName);
        textViewEmail = findViewById(R.id.textViewEmail);

        // SharedPreferences에서 사용자 정보 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", null);

        if (userJson != null) {
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            if (user != null) {
                textViewNickName.setText(user.getNickName());
                textViewEmail.setText(user.getEmail());
            } else {
                textViewNickName.setText("닉네임 로드 실패");
                textViewEmail.setText("이메일 로드 실패");
            }
        } else {
            textViewNickName.setText("닉네임 없음");
            textViewEmail.setText("이메일 없음");
        }

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle("어플이름");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 윈도우 인셋 적용
        View mainLayout = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 버튼 초기화 및 클릭 리스너 설정
        Button buttonEditProfile = findViewById(R.id.buttonEditProfile);
        buttonEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(Mypage.this, EditProfileActivity.class);
            startActivity(intent);
        });

        Button buttonCam = findViewById(R.id.Camera);
        buttonCam.setOnClickListener(view -> {
            Intent intent = new Intent(Mypage.this, Camera_Activity.class);
            startActivity(intent);
        });

        Button buttonManage = findViewById(R.id.buttonManage);
        buttonManage.setOnClickListener(view -> {
            Intent intent = new Intent(Mypage.this, PhotoActivity.class);
            startActivity(intent);
        });

        // BottomNavigationView 설정
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.navigation_home) {
                intent = new Intent(Mypage.this, MainActivity.class);
            } else if (itemId == R.id.navigation_people) {
                intent = new Intent(Mypage.this, ActivityContainer.class);
            } else if (itemId == R.id.navigation_mypage) {
                return true;
            }
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // 현재 탭 선택
        bottomNavigationView.setSelectedItemId(R.id.navigation_mypage);
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