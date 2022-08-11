package com.example.namaprojectfirebase;

public class User {
    public String fullName, licenseNum, email, phoneNum, address;
    public int salary, permission;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    //    public User(String name, String email) {
//
//    }
    public User (){};
    public User(String fullName, String license, String email, String phoneNum, String address, int salary, int permission){
        this.fullName = fullName;
        this.licenseNum = license;
        this.email = email;
        this.phoneNum = phoneNum;
        this.address = address;
        if(permission == 5){
            this.salary = 0;
        }
        else
        {
            this.salary = salary;
        }

        if(permission == 5){
            this.permission = 5;
        }
        else
        {
            this.permission = permission;
        }

    }
}
