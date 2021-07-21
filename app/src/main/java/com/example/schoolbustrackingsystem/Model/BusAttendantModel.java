package com.example.schoolbustrackingsystem.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class BusAttendantModel implements Parcelable {

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

    protected BusAttendantModel(Parcel in) {
        attendantId = in.readString();
        busNumber = in.readString();
        contactNumber = in.readString();
        emailId = in.readString();
        name = in.readString();
        routeNumber = in.readString();
    }

    public static final Creator<BusAttendantModel> CREATOR = new Creator<BusAttendantModel>() {
        @Override
        public BusAttendantModel createFromParcel(Parcel in) {
            return new BusAttendantModel(in);
        }

        @Override
        public BusAttendantModel[] newArray(int size) {
            return new BusAttendantModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(attendantId);
        dest.writeString(busNumber);
        dest.writeString(contactNumber);
        dest.writeString(emailId);
        dest.writeString(name);
        dest.writeString(routeNumber);
    }
}
