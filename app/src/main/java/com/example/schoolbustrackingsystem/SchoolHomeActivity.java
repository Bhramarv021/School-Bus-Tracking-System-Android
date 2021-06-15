package com.example.schoolbustrackingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.schoolbustrackingsystem.addmodule.AddStudentActivity;
import com.example.schoolbustrackingsystem.addmodule.AddBusActivity;
import com.example.schoolbustrackingsystem.addmodule.AddBusAttendantActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SchoolHomeActivity extends AppCompatActivity {

    Button addStudent;
    Button viewStudent;
    Button addBus;
    Button viewBus;
    Button addBusAttendant;
    Button viewBusAttendant;
    Button signOut;

    String schoolId;

    private static final String TAG = "SchoolHomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_home_activity);

        addStudent = findViewById(R.id.btnAddStudent);
        viewStudent = findViewById(R.id.btnViewStudent);

        addBus = findViewById(R.id.btnAddBus);
        viewBus = findViewById(R.id.btnViewBus);

        addBusAttendant = findViewById(R.id.btnAddBusAttendant);
        viewBusAttendant = findViewById(R.id.btnViewBusAttendant);

        signOut = findViewById(R.id.logoutSchool);


        Intent intent = getIntent();
        schoolId = intent.getStringExtra("enteredSchoolId");
        Log.d(TAG, "School id through intent is : "+schoolId);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SchoolHomeActivity.this, LoginActivity.class));
                finish();
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAddStudentActivity = new Intent(SchoolHomeActivity.this, AddStudentActivity.class);
                intentToAddStudentActivity.putExtra("schoolId", schoolId);
                startActivity(intentToAddStudentActivity);
            }
        });

        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAddBusActivity = new Intent(SchoolHomeActivity.this, AddBusActivity.class);
                intentToAddBusActivity.putExtra("schoolId", schoolId);
                intentToAddBusActivity.putExtra("userType", "School");
                intentToAddBusActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentToAddBusActivity);
            }
        });

        viewBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addBusAttendant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAddBusAttendantActivity = new Intent(SchoolHomeActivity.this, AddBusAttendantActivity.class);
                intentToAddBusAttendantActivity.putExtra("schoolId", schoolId);
                intentToAddBusAttendantActivity.putExtra("userType", "BusAttendant");
                intentToAddBusAttendantActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentToAddBusAttendantActivity);
            }
        });

        viewBusAttendant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }
}