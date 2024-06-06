package com.example.missingapp;

// 회원가입 요청 데이터 모델
public class User {
    private String nickName;
    private String email;
    private String password;
    private String role;

    // Constructor
    public User(String nickName, String email, String password, String role) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public User(String nickName, String email) {
        this.nickName = nickName;
        this.email = email;
    }

    // Getter methods
    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Setter methods
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
