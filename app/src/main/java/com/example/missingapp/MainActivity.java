package com.example.missingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

        // Initialize the fragment manager
        fragmentManager = getSupportFragmentManager();

        // Set up the BottomNavigationView
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

        // Set default selection
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_home);

        // Show the decision popup
        showDecisionPopup();
    }

    private void showDecisionPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진 등록하기");
        builder.setMessage("추적대상 사진을 등록하시겠습니까?");
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
