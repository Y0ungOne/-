package com.example.missingapp;

import java.util.List;

public class ProtectedTargetReadDto {
    private int id;
    private String name;
    private int age;
    private String imageFilePath;
    private List<String> image;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProtectedTargetReadDto() {
    }

    public ProtectedTargetReadDto(/*int id,*/ String name, int age, /*List<String> image,*/String imageFilePath) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.image = image;
        this.imageFilePath = imageFilePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    //임시
    public String getImageFilePath() {
        return imageFilePath;
    }
}
