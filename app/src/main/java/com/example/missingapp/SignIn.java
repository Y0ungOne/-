package com.example.missingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {
    public static String token = null;

    private EditText editTextID;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextID = findViewById(R.id.IDText);
        editTextPassword = findViewById(R.id.Password);
        buttonSignIn = findViewById(R.id.login);
        buttonSignUp = findViewById(R.id.sign_up);

        buttonSignIn.setOnClickListener(v -> {
            String email = editTextID.getText().toString();
            String password = editTextPassword.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                signIn(email, password);
            } else {
                Toast.makeText(SignIn.this, "입력되지 않은 정보가 남아 있습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
        });
    }

    private void signIn(String email, String password) {
        UserService service = RetrofitClient.getClient("http://192.168.219.111:8080").create(UserService.class);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<LoginResponse> call = service.signIn(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    token = accessToken;
                    saveToken(accessToken);

                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginError", "로그인 실패: " + response.code() + " " + response.message());
                    Toast.makeText(SignIn.this, "로그인 실패: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("NetworkError", "네트워크 연결 실패", t);
                Toast.makeText(SignIn.this, "네트워크 연결이 불안정합니다. 인터넷 연결을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
}