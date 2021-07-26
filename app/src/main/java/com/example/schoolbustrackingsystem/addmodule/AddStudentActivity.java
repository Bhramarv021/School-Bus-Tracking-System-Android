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

import com.example.schoolbustrackingsystem.Model.StudentModel;
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

public class AddStudentActivity extends AppCompatActivity {

    EditText studentName;
    EditText studentRollNumber;
    EditText studentAddress;
    EditText studentCity;
    EditText studentState;
    EditText studentCountry;
    EditText studentPicDropLoc;
    EditText studentParentName;
    EditText studentParentContactNumber;
    EditText studentParentEmail;
    EditText studentParentEmailPassword;
    EditText studentBusRouteNumber;
    EditText studentBusNumber;
    Button submitStudentDetails;
    ProgressBar progressBar;

    String userType;
    String schoolId;

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mSchoolCollection;
    CollectionReference mUserCollectionRef;
    CollectionReference mStudentDocRef;

    StudentModel studentModelForStudentDatabase;
    UsersModel studentModelForUserDatabase;

    private static final String TAG = "AddStudentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        studentName = findViewById(R.id.studentName);
        studentRollNumber = findViewById(R.id.studentRollNumber);
        studentAddress = findViewById(R.id.studentAddress);
        studentCity = findViewById(R.id.studentCity);
        studentState = findViewById(R.id.studentState);
        studentCountry = findViewById(R.id.studentCountry);
        studentPicDropLoc = findViewById(R.id.studentPicDropLoc);
        studentParentName = findViewById(R.id.studentParentName);
        studentParentContactNumber = findViewById(R.id.studentParentContactNumber);
        studentParentEmail = findViewById(R.id.studentParentEmail);
        studentParentEmailPassword = findViewById(R.id.studentParentPassword);
        studentBusRouteNumber = findViewById(R.id.studentBusRouteNumber);
        studentBusNumber = findViewById(R.id.studentBusNumber);
        progressBar = findViewById(R.id.progressBarForAddStudent);

        submitStudentDetails = findViewById(R.id.btnSubmitStudentDetails);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mSchoolCollection = mFirebaseFirestore.collection("Schools");
        mUserCollectionRef = mFirebaseFirestore.collection("Users");

        Intent intentToGetUserType = getIntent();
        userType = intentToGetUserType.getStringExtra("userType");

        Intent intentToGetSchoolId = getIntent();
        schoolId = intentToGetSchoolId.getStringExtra("schoolId");

        Log.d(TAG, "UseType and SchoolId Through intent is : " + userType + " " + schoolId);

        submitStudentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String parentEmail = studentParentEmail.getText().toString().trim();
                String parentPass = studentParentEmailPassword.getText().toString();

                String name = studentName.getText().toString();
                String rollNo = studentRollNumber.getText().toString();
                String address = studentAddress.getText().toString();
                String city = studentCity.getText().toString();
                String state = studentState.getText().toString();
                String country = studentCountry.getText().toString();
                String picDrop = studentPicDropLoc.getText().toString();
                String parentName = studentParentName.getText().toString();
                String parentContactNum = studentParentContactNumber.getText().toString();
                String busRouteNum = studentBusRouteNumber.getText().toString();
                String busNumber = studentBusNumber.getText().toString();

                studentModelForStudentDatabase =
                        new StudentModel(name, rollNo, address, city, state, country, picDrop,
                                parentName, parentContactNum, parentEmail, busRouteNum, busNumber);

                studentModelForUserDatabase =
                        new UsersModel(parentEmail, userType, schoolId);

                addUserForAuthentication(parentEmail, parentPass);
            }
        });
    }

    private void addUserForAuthentication(String parentEmail, String parentPass) {
        mFirebaseAuth.createUserWithEmailAndPassword(parentEmail, parentPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "Sign in successful");
                            Toast.makeText(AddStudentActivity.this, "signInWithEmailPass : Successful", Toast.LENGTH_SHORT).show();
                            addUserInfoToFirebaseUsersCollection(studentModelForUserDatabase);
                            addUserInfoToFirestoreStudentCollection(studentModelForStudentDatabase);
                            clearAllFields();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.w(TAG, "SignInWithEmailPass : Failure");
                            Toast.makeText(AddStudentActivity.this, "signInWithEmailPass : Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, e.getMessage());
                Toast.makeText(AddStudentActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserInfoToFirebaseUsersCollection(UsersModel studentModelForUserDatabase) {
        mUserCollectionRef
                .document(studentModelForUserDatabase.getEmailId())
                .set(studentModelForUserDatabase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DATA Added to users collection");
                        Toast.makeText(AddStudentActivity.this, "Data added to users collection", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e(TAG, "Failed to add data to Users Collection : " + e.getMessage());
            }
        });
    }

    private void addUserInfoToFirestoreStudentCollection(StudentModel studentModelForStudentDatabase) {
        mStudentDocRef = mSchoolCollection.document(schoolId).collection("Students");
        mStudentDocRef
                .document(studentModelForStudentDatabase.getRollNo())
                .set(studentModelForStudentDatabase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Added data at School Successfully");
                        Toast.makeText(AddStudentActivity.this, "Added data to school database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e(TAG, "Failed to add data in School : " + e.getMessage());
            }
        });
    }

    private void clearAllFields() {
        studentName.setText("");
        studentRollNumber.setText("");
        studentAddress.setText("");
        studentCity.setText("");
        studentState.setText("");
        studentCountry.setText("");
        studentPicDropLoc.setText("");
        studentParentName.setText("");
        studentParentContactNumber.setText("");
        studentParentEmail.setText("");
        studentParentEmailPassword.setText("");
        studentBusRouteNumber.setText("");
        studentBusNumber.setText("");

        studentModelForStudentDatabase = new StudentModel();
        studentModelForUserDatabase = new UsersModel();
    }

}