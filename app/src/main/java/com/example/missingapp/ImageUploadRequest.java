package com.example.missingapp;

public class ImageUploadRequest {
    private CreateDto createDto;
    private String image;

    public ImageUploadRequest(CreateDto createDto, String image) {
        this.createDto = createDto;
        this.image = image;
    }


    public CreateDto getCreateDto() {
        return createDto;
    }

    public void setCreateDto(CreateDto createDto) {
        this.createDto = createDto;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
