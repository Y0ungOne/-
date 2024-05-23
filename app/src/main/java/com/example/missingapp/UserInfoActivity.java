package com.example.missingapp;

/*import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Retrofit;

public class UserInfoActivity extends AppCompatActivity {

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);
        userService = RetrofitClient.getRetrofitInstance().create(UserService.class);
    }

    private void updatePassword(String currentPassword, String newPassword) {
        PasswordUpdateRequest passwordUpdate = new PasswordUpdateRequest(currentPassword, newPassword);
        userService.updatePassword(passwordUpdate).enqueue(new GenericCallback<>(this, "비밀번호"));
    }

    private void updateNickName(String nickName) {
        NickNameUpdate nickNameUpdate = new NickNameUpdate(nickName);
        userService.updateNickName(nickNameUpdate).enqueue(new GenericCallback<>(this, "닉네임"));
    }

    private void deleteUserProfile() {
        userService.deleteUser().enqueue(new GenericCallback<>(this, "회원 정보 삭제"));
    }

    // 내부 클래스로 데이터 모델 정의
    public class PasswordUpdateRequest {
        String currentPassword;
        String newPassword;

        public PasswordUpdateRequest(String currentPassword, String newPassword) {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }
    }

    public class NickNameUpdate {
        String nickName;

        public NickNameUpdate(String nickName) {
            this.nickName = nickName;
        }
    }
}*/
