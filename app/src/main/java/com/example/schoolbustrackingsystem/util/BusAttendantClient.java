package com.example.schoolbustrackingsystem.util;

import android.app.Application;

import com.example.schoolbustrackingsystem.Model.BusAttendantModel;

public class BusAttendantClient extends Application {

    private BusAttendantModel user = null;

    public BusAttendantModel getUser() {
        return user;
    }

    public void setUser(BusAttendantModel user) {
        this.user = user;
    }

}
