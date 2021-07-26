package com.example.schoolbustrackingsystem.Model;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class BusAttendantLiveLocationModel{

    private GeoPoint geoPoint;
    private @ServerTimestamp Date timestamp;
    private BusAttendantModel busAttendantModel;

    public BusAttendantLiveLocationModel(GeoPoint geoPoint, Date timestamp, BusAttendantModel busAttendantModel) {
        this.geoPoint = geoPoint;
        this.timestamp = timestamp;
        this.busAttendantModel = busAttendantModel;
    }

    public BusAttendantLiveLocationModel() {
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public BusAttendantModel getBusAttendantModel() {
        return busAttendantModel;
    }

    public void setBusAttendantModel(BusAttendantModel busAttendantModel) {
        this.busAttendantModel = busAttendantModel;
    }
}
