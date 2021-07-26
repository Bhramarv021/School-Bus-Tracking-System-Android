package com.example.schoolbustrackingsystem.Model;

public class BusModel {

    String busNumber;
    String busRegistrationNumber;
    String busRouteNumber;
    String driverContactNumber;
    String driverName;

    public BusModel(){

    }

    public BusModel(String busNumber,
                    String busRegistrationNumber,
                    String busRouteNumber,
                    String driverContactNumber,
                    String driverName) {
        this.busNumber = busNumber;
        this.busRegistrationNumber = busRegistrationNumber;
        this.busRouteNumber = busRouteNumber;
        this.driverContactNumber = driverContactNumber;
        this.driverName = driverName;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusRegistrationNumber() {
        return busRegistrationNumber;
    }

    public void setBusRegistrationNumber(String busRegistrationNumber) {
        this.busRegistrationNumber = busRegistrationNumber;
    }

    public String getBusRouteNumber() {
        return busRouteNumber;
    }

    public void setBusRouteNumber(String busRouteNumber) {
        this.busRouteNumber = busRouteNumber;
    }

    public String getDriverContactNumber() {
        return driverContactNumber;
    }

    public void setDriverContactNumber(String driverContactNumber) {
        this.driverContactNumber = driverContactNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
