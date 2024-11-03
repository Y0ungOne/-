package com.example.missingapp;

// 사용자 정보 조회 응답 객체
public class UserProfileResponse {
    private String email;
    private String userName;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
