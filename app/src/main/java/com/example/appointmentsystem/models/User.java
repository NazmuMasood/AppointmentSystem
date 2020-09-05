package com.example.appointmentsystem.models;

public class User {
    public String phone, email, createdAt;

    public User(String phone, String email, String createdAt) {
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }
}
