package com.example.schoolbustrackingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText userEmail;
    EditText userPass;
    Button userLoginBtn;
    ProgressBar progressBar;

    FirebaseAuth mFirebaseAuth;

    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mUserCollection;
    DocumentReference mDocumentUserReference;

    String mUserEmail;
    String mUserPass;

    String userType;
    String schoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.inputEmail);
        userPass = findViewById(R.id.inputPassword);
        userLoginBtn = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mUserCollection = mFirebaseFirestore.collection("Users");

        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "In Button", Toast.LENGTH_SHORT).show();
                mUserEmail = userEmail.getText().toString().trim();
                mUserPass = userPass.getText().toString();

                if (TextUtils.isEmpty(mUserEmail)) {
                    userEmail.setError("Email is required");
                }

                if (TextUtils.isEmpty(mUserPass)) {
                    userPass.setError("Password is Required.");
                    return;
                }

                if (mUserPass.length() < 6) {
                    userPass.setError("Password Must be >= 6 Characters");
                    return;
                }

                loginCheck(mUserEmail, mUserPass);
            }
        });
    }

    private void loginCheck(String email, String pass) {

        mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);

                mUserCollection
                        .document(email)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                userType = documentSnapshot.getString("userType");
                                schoolId = documentSnapshot.getString("schoolId");
                                Log.d("User Data", "User type is " + userType +"  "+ schoolId);
                                Toast.makeText(LoginActivity.this, "GETTING", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Not Found", "Failed to find doc");
                                Toast.makeText(LoginActivity.this, "Not GETTING", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Enter Valid Details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}