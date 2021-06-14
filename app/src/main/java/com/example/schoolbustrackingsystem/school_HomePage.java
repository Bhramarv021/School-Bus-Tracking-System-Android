package com.example.schoolbustrackingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class school_HomePage extends AppCompatActivity {

    Button mbtnaddstud, mbtnaddbusatt, mbtnaddbus, mbtnviewstu, mbtnviewbusatt, mbtnviewbus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_home_page);

        mbtnaddstud = findViewById(R.id.btnAddStudent);
        mbtnaddbusatt = findViewById(R.id.btnAddBusAttendent);
        mbtnaddbus = findViewById(R.id.btnAddBus);
        mbtnviewstu = findViewById(R.id.btnViewStudent);
        mbtnviewbusatt = findViewById(R.id.btnViewBusAttendent);
        mbtnviewbus = findViewById(R.id.btnViewBus);

        //        ADD STUDENT BUTTON JUMP TO ADD STUDENT PAGE
        mbtnaddstud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        //        VIEW STUDENT BUTTON JUMP TO VIEW STUDENT PAGE
        mbtnviewstu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), view_Student_data.class));
            }
        });


        //        ADD BUS ATTENDENT BUTTON JUMP TO ADD BUS ATTENDENT PAGE
        mbtnaddbusatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        //        VIEW BUS ATTENDENT BUTTON JUMP TO VIEW BUS ATTENDENT PAGE
        mbtnviewbusatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        //        ADD BUS BUTTON JUMP TO ADD BUS PAGE
        mbtnaddbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        //        VIEW BUS BUTTON JUMP TO VIEW BUS PAGE
        mbtnviewbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }


}