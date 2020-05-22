package com.example.tourodirectory.classes;

public class Contact {

    private String fullName;
    private String school;
    private String department;
    private String email;
    private String number;

    public Contact(String fullName, String school, String department, String email, String number) {
        this.fullName = fullName;
        this.school = school;
        this.department = department;
        this.email = email;
        this.number = number;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSchool() {
        return school;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
