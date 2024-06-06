package com.example.missingapp;

import java.util.List;

public class ProtectedTargetReadDto {
    private int id;
    private String name;
    private int age;
    private String image;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProtectedTargetReadDto() {
    }

    public ProtectedTargetReadDto(int id,  String name, int age,String image) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.image = image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
