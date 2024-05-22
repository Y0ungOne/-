package com.example.missingapp;

/*import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextNickName, editTextNewPassword, editTextConfirmNewPassword;
    private Button buttonSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);

        editTextEmail = findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        /*editTextNickName = findViewById(R.id.editNickName);
        editTextNewPassword = findViewById(R.id.editNewPassword);
        editTextConfirmNewPassword = findViewById(R.id.editConfirmNewPassword);
        buttonSaveChanges = findViewById(R.id.btnSaveChanges);

        loadUserInfo();

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndUploadNewPassword();
            }
        });
    }

    private void loadUserInfo() {
        EditText editTextEmailInput = findViewById(R.id.editEmail); // 사용자 입력 이메일 필드
        String email = editTextEmailInput.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        UserService service = RetrofitClient.getClient("https://yourapi.com/").create(UserService.class);
        Call<UserProfileResponse> call = service.getUserInfo(email);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful()) {
                    UserProfileResponse userProfile = response.body();
                    editTextEmail.setText(userProfile.email);
                    // editTextPassword 필드는 기존 비밀번호를 표시하지 않도록 합니다.
                    editTextNickName.setText(userProfile.nickName);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 비밀번호 업데이트 로직
    private void updateUserInfo() {
        String email = editTextEmail.getText().toString().trim();
        String oldPassword = editTextPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(EditProfileActivity.this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        UserService service = RetrofitClient.getClient("223.130.152.183:8080").create(UserService.class);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest(email, oldPassword, newPassword);
        Call<PasswordUpdateResponse> call = service.updateUserPassword(passwordUpdateRequest);
        call.enqueue(new Callback<PasswordUpdateResponse>() {
            @Override
            public void onResponse(Call<PasswordUpdateResponse> call, Response<PasswordUpdateResponse> response) {
                if (response.isSuccessful() && response.body().success) {
                    Toast.makeText(EditProfileActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PasswordUpdateResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validateAndUploadNewPassword() {
        String email = editTextEmail.getText().toString().trim();
        String oldPassword = editTextPassword.getText().toString().trim();
        String newNickName = editTextNickName.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        UserService service = RetrofitClient.getClient("223.130.152.183:8080").create(UserService.class);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest(email, oldPassword, newPassword);
        Call<PasswordUpdateResponse> call = service.updateUserPassword(passwordUpdateRequest);
        call.enqueue(new Callback<PasswordUpdateResponse>() {
            @Override
            public void onResponse(Call<PasswordUpdateResponse> call, Response<PasswordUpdateResponse> response) {
                if (response.isSuccessful() && response.body().success) {
                    Toast.makeText(EditProfileActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PasswordUpdateResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}*/


