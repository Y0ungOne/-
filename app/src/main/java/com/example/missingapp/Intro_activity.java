package com.example.missingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Intro_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Intro_activity.this, SignIn.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
