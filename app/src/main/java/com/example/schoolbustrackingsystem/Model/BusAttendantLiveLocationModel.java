package com.example.schoolbustrackingsystem.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class BusAttendantLiveLocationModel implements Parcelable {

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

    protected BusAttendantLiveLocationModel(Parcel in) {
    }

    public static final Creator<BusAttendantLiveLocationModel> CREATOR = new Creator<BusAttendantLiveLocationModel>() {
        @Override
        public BusAttendantLiveLocationModel createFromParcel(Parcel in) {
            return new BusAttendantLiveLocationModel(in);
        }

        @Override
        public BusAttendantLiveLocationModel[] newArray(int size) {
            return new BusAttendantLiveLocationModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
