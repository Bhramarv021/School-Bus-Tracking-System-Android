package com.example.schoolbustrackingsystem.Model;

public class BusAttendantModel {

    String attendantId;
    String busNumber;
    String contactNumber;
//    String currentLocation;
    String emailId;
    String name;
    String routeNumber;

    public BusAttendantModel() {
    }

    public BusAttendantModel(String attendantId,
                             String busNumber,
                             String contactNumber,
                             String emailId,
                             String name,
                             String routeNumber) {
        this.attendantId = attendantId;
        this.busNumber = busNumber;
        this.contactNumber = contactNumber;
        this.emailId = emailId;
        this.name = name;
        this.routeNumber = routeNumber;
    }

    public String getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(String attendantId) {
        this.attendantId = attendantId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

//    public String getCurrentLocation() {
//        return currentLocation;
//    }
//
//    public void setCurrentLocation(String currentLocation) {
//        this.currentLocation = currentLocation;
//    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }
}
