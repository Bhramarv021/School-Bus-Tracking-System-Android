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

import com.example.schoolbustrackingsystem.Model.BusAttendantModel;
import com.example.schoolbustrackingsystem.Model.UsersModel;
import com.example.schoolbustrackingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class AddBusAttendantActivity extends AppCompatActivity {

    EditText attendantName;
    EditText attendantId;
    EditText attendantContactNumber;
    EditText attendantRouteNumber;
    EditText attendantBusNumber;
    EditText attendantEmail;
    EditText attendantPass;
    ProgressBar progressBar;

    Button submitBusAttendantDetails;

    String schoolId;
    String userType;

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mSchoolCollectionReference;
    CollectionReference mUserCollectionReference;
    CollectionReference mBusAttendantCollection;

    BusAttendantModel busAttendantModel;
    UsersModel usersModelForBusAttendant;

    private static final String TAG = "AddBusAttendantActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus_attendant);

        Intent intentToGetSchoolId = getIntent();
        schoolId = intentToGetSchoolId.getStringExtra("schoolId");

        Intent intentToGetUserType = getIntent();
        userType = intentToGetUserType.getStringExtra("userType");

        Log.d(TAG, "SchoolId and userType : "+schoolId+ " " +userType);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mSchoolCollectionReference = mFirebaseFirestore.collection("Schools");
        mUserCollectionReference = mFirebaseFirestore.collection("Users");

        attendantName = findViewById(R.id.attendantName);
        attendantId = findViewById(R.id.attendantId);
        attendantContactNumber = findViewById(R.id.attendantContactNumber);
        attendantRouteNumber = findViewById(R.id.attendantRouteNumber);
        attendantBusNumber = findViewById(R.id.attendantBusNumber);
        attendantEmail = findViewById(R.id.attendantEmail);
        attendantPass = findViewById(R.id.attendantPassword);
        progressBar = findViewById(R.id.progressBarForAddBusAttendant);

        submitBusAttendantDetails = findViewById(R.id.btnSubmitBusAttendantDetails);

        submitBusAttendantDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String name = attendantName.getText().toString();
                String busAttendantId = attendantId.getText().toString();
                String contactNumber = attendantContactNumber.getText().toString();
                String routeNumber = attendantContactNumber.getText().toString();
                String busNumber = attendantBusNumber.getText().toString();
                String email = attendantEmail.getText().toString();
                String pass = attendantPass.getText().toString();

                busAttendantModel = new BusAttendantModel(busAttendantId, busNumber, contactNumber, email, name, routeNumber);

                usersModelForBusAttendant = new UsersModel(email, userType, schoolId);

                addBusAttendantForAuthentication(email, pass);
            }
        });
    }

    private void addBusAttendantForAuthentication(String email, String pass) {
        mFirebaseAuth
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "Sign in successful");
                            Toast.makeText(AddBusAttendantActivity.this, "signInWithEmailPass : Successful", Toast.LENGTH_SHORT).show();
                            addUserInfoToFirebaseUsersCollection(usersModelForBusAttendant);
                            addUserInfoToFirestoreBusAttendantCollection(busAttendantModel);
                            clearAllFields();
                        }
                        else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.w(TAG, "SignInWithEmailPass : Failure");
                            Toast.makeText(AddBusAttendantActivity.this, "signInWithEmailPass : Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, e.getMessage());
                Toast.makeText(AddBusAttendantActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserInfoToFirestoreBusAttendantCollection(BusAttendantModel busAttendantModel) {
        mBusAttendantCollection = mSchoolCollectionReference.document(schoolId).collection("BusAttendant");
        mBusAttendantCollection
                .document(busAttendantModel.getAttendantId())
                .set(busAttendantModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Added data at School Successfully");
                        Toast.makeText(AddBusAttendantActivity.this, "Added data to school database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to add data in School : " + e.getMessage());
            }
        });
    }

    private void addUserInfoToFirebaseUsersCollection(UsersModel usersModelForBusAttendant) {
        mUserCollectionReference
                .document(usersModelForBusAttendant.getEmailId())
                .set(usersModelForBusAttendant)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DATA Added to users collection");
                        Toast.makeText(AddBusAttendantActivity.this, "Data added to users collection", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to add data to Users Collection : " + e.getMessage());
            }
        });
    }

    private void clearAllFields() {
        attendantName.setText("");
        attendantId.setText("");
        attendantContactNumber.setText("");
        attendantRouteNumber.setText("");
        attendantBusNumber.setText("");
        attendantEmail.setText("");
        attendantPass.setText("");

        busAttendantModel = new BusAttendantModel();
        usersModelForBusAttendant = new UsersModel();
    }
}