package com.example.schoolbustrackingsystem.Model;

public class StudentModel {

    String studentName;
    String rollNo;
    String address;
    String city;
    String state;
    String country;
    String picDropLocation;
    String parentName;
    String parentContactNumber;
    String parentEmail;
    String busRoute;
    String busNumber;

    public StudentModel() {
    }

    public StudentModel(String studentName,
                        String rollNo,
                        String address,
                        String city,
                        String state,
                        String country,
                        String picDropLocation,
                        String parentName,
                        String parentContactNumber,
                        String parentEmail,
                        String busRoute,
                        String busNumber) {
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.picDropLocation = picDropLocation;
        this.parentName = parentName;
        this.parentContactNumber = parentContactNumber;
        this.parentEmail = parentEmail;
        this.busRoute = busRoute;
        this.busNumber = busNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPicDropLocation() {
        return picDropLocation;
    }

    public void setPicDropLocation(String picDropLocation) {
        this.picDropLocation = picDropLocation;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentContactNumber() {
        return parentContactNumber;
    }

    public void setParentContactNumber(String parentContactNumber) {
        this.parentContactNumber = parentContactNumber;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }
}
