package com.example.missingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.congestionobserver.ActivityContainer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.missingapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btnExplore = findViewById(R.id.btn_explore);
        ImageView imageView = findViewById(R.id.imageView);
        TextView statusText = findViewById(R.id.status_text);

        btnExplore.setOnClickListener(v -> {
            // 탐색중 텍스트 설정
            statusText.setText("탐색중");

            // 임시로 더미 이미지 URL 사용
            String dummyImageUrl = "https://via.placeholder.com/100";
            Glide.with(this)
                    .load(dummyImageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // 로드 실패 시 텍스트 설정
                            statusText.setText("탐색 실패");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // 로드 성공 시 텍스트 설정
                            statusText.setText("탐색 완료!");
                            return false;
                        }
                    }).override(1000,1000)
                    .into(imageView);
        });

        fragmentManager = getSupportFragmentManager();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else if (itemId == R.id.navigation_people) {
                intent = new Intent(getApplicationContext(), ActivityContainer.class);
            } else if (itemId == R.id.navigation_mypage) {
                intent = new Intent(getApplicationContext(), Mypage.class);
            }
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            return true;
        });

        binding.bottomNavigation.setSelectedItemId(R.id.navigation_home);

        showDecisionPopup();
    }

    private void showDecisionPopup() {
        // AlertDialog 빌더 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // LayoutInflater를 사용하여 커스텀 레이아웃을 인플레이트
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);

        // 커스텀 타이틀과 메시지 설정
        TextView customTitle = dialogView.findViewById(R.id.customTitle);
        TextView customMessage = dialogView.findViewById(R.id.customMessage);

        // 버튼 설정
        builder.setNegativeButton("아니오", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("예", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(), Mypage.class);
            startActivity(intent);
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
