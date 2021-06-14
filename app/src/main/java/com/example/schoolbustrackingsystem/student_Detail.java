package com.example.schoolbustrackingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class student_Detail extends AppCompatActivity {

    TextView sName,
            sRollno,
            sAdd,
            sCity,
            sState,
            sCountry,
            sPicknDrop,
            sPrntname,
            sPrntno,
            sPrntemail,
            sBusroute,
            sBusno;

    Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        sName = findViewById(R.id.stdname);
        sRollno = findViewById(R.id.rllno);
        sAdd = findViewById(R.id.address);
        sCity = findViewById(R.id.city);
        sState = findViewById(R.id.state);
        sCountry = findViewById(R.id.country);
        sPicknDrop = findViewById(R.id.PnD);
        sPrntname = findViewById(R.id.parentname);
        sPrntno = findViewById(R.id.parentno);
        sPrntemail = findViewById(R.id.parentemail);
        sBusroute = findViewById(R.id.busroute);
        sBusno = findViewById(R.id.busno);

        Back = findViewById(R.id.back);

        sName.setText(getIntent().getStringExtra("sname").toString());
        sRollno.setText(getIntent().getStringExtra("srollno").toString());
        sAdd.setText(getIntent().getStringExtra("sadr").toString());
        sCity.setText(getIntent().getStringExtra("scity").toString());
        sState.setText(getIntent().getStringExtra("sstate").toString());
        sCountry.setText(getIntent().getStringExtra("scountry").toString());
        sPicknDrop.setText(getIntent().getStringExtra("spickndrp").toString());
        sPrntname.setText(getIntent().getStringExtra("sprtname").toString());
        sPrntno.setText(getIntent().getStringExtra("sprtno").toString());
        sPrntemail.setText(getIntent().getStringExtra("sprtemail").toString());
        sBusroute.setText(getIntent().getStringExtra("sbusrte").toString());
        sBusno.setText(getIntent().getStringExtra("sbusno").toString());

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), school_HomePage.class));
                finish();
            }
        });

    }
}