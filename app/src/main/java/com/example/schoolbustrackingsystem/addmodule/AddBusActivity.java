package com.example.schoolbustrackingsystem.addmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.schoolbustrackingsystem.Model.BusModel;
import com.example.schoolbustrackingsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class AddBusActivity extends AppCompatActivity {

    EditText busNumber;
    EditText busRegistrationNumber;
    EditText busRouteNumber;
    EditText busDriverName;
    EditText busDriverNumber;
    Button submitBusDetails;
    ProgressBar progressBar;

    private static final String TAG = "AddBusActivity";

    String schoolId;

    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mSchoolCollection;
    CollectionReference mBusDocReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        busNumber = findViewById(R.id.busNumber);
        busRegistrationNumber = findViewById(R.id.busRegistrationNumber);
        busRouteNumber = findViewById(R.id.busRouteNumber);
        busDriverName = findViewById(R.id.busDriverName);
        busDriverNumber = findViewById(R.id.busDriverContactNumber);
        progressBar = findViewById(R.id.progressBarForAddBus);
        submitBusDetails = findViewById(R.id.btnSubmitBusDetails);

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mSchoolCollection = mFirebaseFirestore.collection("Schools");

        Intent intentToGetSchoolId = getIntent();
        schoolId = intentToGetSchoolId.getStringExtra("schoolId");

        Log.d(TAG, "School id is : "+schoolId);

        submitBusDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busNum = busNumber.getText().toString();
                String busRegistrationNum = busRegistrationNumber.getText().toString();
                String busRouteNum = busRouteNumber.getText().toString();
                String schoolBusDriverName = busDriverName.getText().toString();
                String schoolBusDriverNumber = busDriverNumber.getText().toString();

                BusModel busModel = new BusModel(busNum, busRegistrationNum, busRouteNum, schoolBusDriverNumber, schoolBusDriverName);

                addBusDetailsToFirebase(busModel);
            }
        });

    }

    private void addBusDetailsToFirebase(BusModel busModel) {
        mBusDocReference = mSchoolCollection.document(schoolId).collection("Buses");
        mBusDocReference.document(busModel.getBusNumber())
                .set(busModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Bus details added");
                        Toast.makeText(AddBusActivity.this, "Bus details added successfully", Toast.LENGTH_SHORT).show();
                        clearAllTextFields();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NotNull Exception e) {
                Log.d(TAG, "Error Message : "+e.getMessage());
                Toast.makeText(AddBusActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAllTextFields(){
        busNumber.setText("");
        busRegistrationNumber.setText("");
        busRouteNumber.setText("");
        busDriverName.setText("");
        busDriverNumber.setText("");
    }
}