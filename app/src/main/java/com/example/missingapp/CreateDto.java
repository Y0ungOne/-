package com.example.missingapp;

public class CreateDto {
    private String name;
    private int age;
    private String imageFilePath;

    public CreateDto(String name, int age,String imageFilePath) {
        this.name = name;
        this.age = age;
        this.imageFilePath = imageFilePath;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    //임시
    public String getImageFilePath() {
        return imageFilePath;
    }
}
