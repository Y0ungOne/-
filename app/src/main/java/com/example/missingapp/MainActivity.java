package com.example.missingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.congestionobserver.ActivityContainer;
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

        // AlertDialog 생성 및 표시
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
