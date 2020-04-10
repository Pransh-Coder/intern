package com.example.intern.socialnetwork;

public class PersonDetails_pojo {
    String id;
    String Name;
    String Occupation;
    String Age;

    public PersonDetails_pojo(String id, String name, String occupation, String age) {
        this.id = id;
        Name = name;
        Occupation = occupation;
        Age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }
}
