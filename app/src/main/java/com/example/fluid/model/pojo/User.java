package com.example.fluid.model.pojo;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("email")
    private String email;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("familyName")
    private String familyName;
    @SerializedName("gender")
    private String gender;
    @SerializedName("id")
    private String id;

    public User(String email, String firstName, String familyName) {
        this.email = email;
        this.firstName = firstName;
        this.familyName = familyName;
    }

    public User() {
    }

    public User(String email, String firstName, String familyName, String gender) {
        this.email = email;
        this.firstName = firstName;
        this.familyName = familyName;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
