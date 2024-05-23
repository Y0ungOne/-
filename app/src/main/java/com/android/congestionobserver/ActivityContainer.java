package com.android.congestionobserver;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.congestionobserver.databinding.ActivityContainerBinding;


public class ActivityContainer extends AppCompatActivity {

    private ActivityContainerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_live) {
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, new LiveFragment()).commit();
            } else if (itemId == R.id.nav_month) {
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, new MonthFragment()).commit();
            } else if (itemId == R.id.nav_week) {
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, new WeekFragment()).commit();
            } else if (itemId == R.id.nav_find) {
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, new HourFragment()).commit();
            }
            return true;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, new LiveFragment()).commit();

    }
}
