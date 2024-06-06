package com.example.missingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordCheck;
    private EditText editNickname;
    private Button buttonComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle("어플이름");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTextEmail = findViewById(R.id.EmailAddress);
        editTextPassword = findViewById(R.id.pwd_insert);
        editTextPasswordCheck = findViewById(R.id.pwd_check);
        editNickname = findViewById(R.id.Nickname);
        buttonComplete = findViewById(R.id.complete);

        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        if (!validateInputs()) {
            return;
        }

        UserService service = RetrofitClient.getClient("http://192.168.219.111:8080").create(UserService.class);
        User request = new User(editNickname.getText().toString().trim(), editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim(), "user");


        Call<UserSignUpResponse> call = service.signUp(request);
        call.enqueue(new Callback<UserSignUpResponse>() {
            @Override
            public void onResponse(Call<UserSignUpResponse> call, Response<UserSignUpResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUp.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("SignUp", "회원가입 실패: " + response.code() + " " + response.message());
                    Toast.makeText(SignUp.this, "회원가입 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserSignUpResponse> call, Throwable t) {
                Log.e("SignUp", "네트워크 오류", t);
                Toast.makeText(SignUp.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private boolean validateInputs() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordCheck = editTextPasswordCheck.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
            Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(passwordCheck)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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