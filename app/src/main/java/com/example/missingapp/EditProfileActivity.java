package com.example.missingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        setContentView(R.layout.edit_info);  // edit_info.xml 사용

        // JWT 토큰 가져오기
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

            // 입력한 새 비밀번호와 확인 비밀번호가 일치하는지 확인
            if (!newPassword.equals(newPasswordConfirm)) {
                Toast.makeText(this, "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 새 비밀번호가 현재 비밀번호와 다른지 확인
            if (newPassword.equals(currentPassword)) {
                Toast.makeText(this, "새 비밀번호는 현재 비밀번호와 달라야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 서버로 비밀번호와 닉네임 업데이트 요청
            updateUserInfo(userName, currentPassword, newPassword);
        });
    }

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
                    // MyPage로 이동
                    Intent intent = new Intent(EditProfileActivity.this, Mypage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
