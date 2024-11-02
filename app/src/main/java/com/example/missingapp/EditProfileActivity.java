package com.example.missingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private UserService userService;
    private String jwtToken;
    private EditText editUserName, editCurrentPassword, editNewPassword, editNewPasswordConfirm;
    private TextView textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);  // 기존 edit_info 레이아웃을 사용

        // 토큰 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("token", null);

        if (jwtToken == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userService = RetrofitClient.getLocalClient(jwtToken).create(UserService.class);

        textEmail = findViewById(R.id.textEmail);
        editUserName = findViewById(R.id.editUserName);
        editCurrentPassword = findViewById(R.id.editCurrentPassword);
        editNewPassword = findViewById(R.id.editNewPassword);
        editNewPasswordConfirm = findViewById(R.id.editNewPasswordConfirm);
        Button buttonUpdate = findViewById(R.id.buttonUpdate);

        // 사용자 정보 불러오기
        loadUserInfo();

        // 정보 수정 버튼 클릭 시 업데이트 요청
        buttonUpdate.setOnClickListener(v -> {
            String userName = editUserName.getText().toString();
            String currentPassword = editCurrentPassword.getText().toString();
            String newPassword = editNewPassword.getText().toString();
            String newPasswordConfirm = editNewPasswordConfirm.getText().toString();

            if (newPassword.equals(newPasswordConfirm)) {
                updateUserInfo(userName, currentPassword, newPassword);
            } else {
                Toast.makeText(this, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 서버에서 사용자 정보를 불러오는 메서드
    private void loadUserInfo() {
        userService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse userProfile = response.body();
                    textEmail.setText("사용자 이메일: " + userProfile.getEmail());
                    editUserName.setText(userProfile.getUserName());
                } else {
                    Toast.makeText(EditProfileActivity.this, "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 서버에 사용자 정보 업데이트 요청을 보내는 메서드
    private void updateUserInfo(String userName, String currentPassword, String newPassword) {
        // 닉네임 업데이트
        NickNameUpdate nickNameUpdate = new NickNameUpdate(userName);
        userService.updateNickName(nickNameUpdate).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "닉네임 업데이트 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });

        // 비밀번호 업데이트
        PasswordUpdate passwordUpdate = new PasswordUpdate(currentPassword, newPassword);
        userService.updatePassword(passwordUpdate).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "정보가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "비밀번호 업데이트 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
