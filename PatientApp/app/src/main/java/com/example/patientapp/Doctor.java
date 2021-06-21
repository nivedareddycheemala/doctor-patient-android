package com.example.patientapp;

public class Doctor {
    private String name,email,phone,age,password,locationaddress,dateofbirth,gend,speciality,imageURL,status;

    public Doctor(String name, String email, String phone, String age, String password, String locationaddress, String dateofbirth,String gend,String speciality,String imageURL,String status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.password = password;
        this.locationaddress = locationaddress;
        this.dateofbirth=dateofbirth;
        this.gend=gend;
        this.speciality=speciality;
        this.imageURL=imageURL;
        this.status=status;
    }

    public Doctor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocationaddress() {
        return locationaddress;
    }

    public void setLocationaddress(String locationaddress) {
        this.locationaddress = locationaddress;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getGend() {
        return gend;
    }

    public void setGend(String gend) {
        this.gend = gend;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}