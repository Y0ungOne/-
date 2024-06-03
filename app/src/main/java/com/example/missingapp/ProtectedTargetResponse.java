package com.example.missingapp;

import java.util.List;

public class ProtectedTargetResponse {

    private boolean success;
    private String message;
    private ProtectedTargetReadDto data;

    // Getter 및 Setter 메서드
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProtectedTargetReadDto getData() {
        return data;
    }

    public void setData(ProtectedTargetReadDto data) {
        this.data = data;
    }
    private List<ProtectedTargetReadDto> protectedTargetReadDtos;

    public List<ProtectedTargetReadDto> getProtectedTargetReadDtos() {
        return protectedTargetReadDtos;
    }

    public void setProtectedTargetReadDtos(List<ProtectedTargetReadDto> protectedTargetReadDtos) {
        this.protectedTargetReadDtos = protectedTargetReadDtos;
    }
}
