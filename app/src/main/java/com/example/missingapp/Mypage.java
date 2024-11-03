package com.example.missingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.android.congestionobserver.ActivityContainer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Mypage extends AppCompatActivity {

    private TextView textViewNickName;
    private TextView textViewEmail;
    private Button buttonWithdraw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

//        User email,nickname
//        textViewNickName = findViewById(R.id.textViewNickName);
//        textViewEmail = findViewById(R.id.textViewEmail);
//
//        // SharedPreferences에서 사용자 정보 불러오기
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String userJson = sharedPreferences.getString("user", null);
//
//        if (userJson != null) {
//            Gson gson = new Gson();
//            User user = gson.fromJson(userJson, User.class);
//            if (user != null) {
//                textViewNickName.setText(user.getNickName());
//                textViewEmail.setText(user.getEmail());
//            } else {
//                textViewNickName.setText("error");
//                textViewEmail.setText("error");
//            }
//        } else {
//            textViewNickName.setText("error");
//            textViewEmail.setText("error");
//        }

        // 탈퇴하기 버튼 초기화 및 클릭 이벤트 설정
        buttonWithdraw = findViewById(R.id.buttonWithdraw);
        buttonWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAccount();
            }
        });

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


        Button buttonCam = findViewById(R.id.Camera);
        buttonCam.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Camera_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        Button buttonManage = findViewById(R.id.buttonManage);
        buttonManage.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // BottomNavigationView 설정
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_mypage);
    }

    // 탈퇴하기 구현
    private void deleteUserAccount() {
        UserService service = RetrofitClient.getClient("http://192.168.219.111:8080").create(UserService.class);

        Call<Void> call = service.deleteUser();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Mypage.this, "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    // 탈퇴 완료 후 로그인 화면으로 이동
                    Intent intent = new Intent(Mypage.this, SignIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Mypage.this, "탈퇴 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Mypage.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
