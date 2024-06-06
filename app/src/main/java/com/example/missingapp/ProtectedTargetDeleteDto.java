package com.example.missingapp;

public class ProtectedTargetDeleteDto {
    private int protectedTargetId;

    public ProtectedTargetDeleteDto(int id) {
        this.protectedTargetId = id;
    }

    public int getId() {
        return protectedTargetId;
    }

    public void setId(int id) {
        this.protectedTargetId = id;
    }
}
