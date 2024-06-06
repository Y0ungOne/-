package com.example.missingapp;

public class Photo {
    private int protectedTargetId;
    private String name;
    private int age;
    private String image;

    public Photo() {
    }

    public Photo(int id, String name, int age, String image) {
        this.protectedTargetId = id;
        this.name = name;
        this.age = age;
        this.image = image;
    }

    public int getId() {
        return protectedTargetId;
    }

    public void setId(int id) {
        this.protectedTargetId = id;
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
