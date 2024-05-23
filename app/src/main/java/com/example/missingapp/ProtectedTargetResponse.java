package com.example.missingapp;

import java.util.List;

public class ProtectedTargetResponse {
    private List<ProtectedTargetReadDto> protectedTargetReadDtos;

    public List<ProtectedTargetReadDto> getProtectedTargetReadDtos() {
        return protectedTargetReadDtos;
    }

    public void setProtectedTargetReadDtos(List<ProtectedTargetReadDto> protectedTargetReadDtos) {
        this.protectedTargetReadDtos = protectedTargetReadDtos;
    }
}
