package com.example.missingapp;

public class CreateDto {
    private String name;
    private int age;

    public CreateDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
