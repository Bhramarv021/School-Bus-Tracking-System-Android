package com.example.schoolbustrackingsystem.viewmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.schoolbustrackingsystem.Model.BusAttendantLiveLocationModel;
import com.example.schoolbustrackingsystem.Model.BusAttendantModel;
import com.example.schoolbustrackingsystem.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewAllBusesActivity extends AppCompatActivity {

    private final static String TAG = "ViewAllBusesActivity";

    private SupportMapFragment supportMapFragment;
    private ArrayList<BusAttendantLiveLocationModel> mAttendantsLocation = new ArrayList<>();

    private ListenerRegistration mAttendantListEventListener;

    private String schoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_buses);

        Intent intent = getIntent();
        schoolId = intent.getStringExtra("schoolId");

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.attendantMapsFragment);

        getAttendantsDetail();
    }

    private void getAttendantsDetail(){
        CollectionReference attendantsLocationCollection =
                FirebaseFirestore.getInstance()
                .collection("Schools")
                .document(schoolId)
                .collection("BusAttendantLocation");

        mAttendantListEventListener = attendantsLocationCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "onEvent: Listen failed.", error);
                }

                if(value != null){

                    for (QueryDocumentSnapshot doc : value) {
                        BusAttendantLiveLocationModel busAttendantLiveLocationModel = doc.toObject(BusAttendantLiveLocationModel.class);
                        mAttendantsLocation.add(busAttendantLiveLocationModel);
                        getAttendantsLocation(busAttendantLiveLocationModel);
                    }
                    Log.d(TAG, "onEvent: user list size: " + mAttendantsLocation.size());
                }
            }
        });
    }

    private void getAttendantsLocation(BusAttendantLiveLocationModel busAttendantLiveLocationModel){
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Schools")
                .document(schoolId)
                .collection("BusAttendantLocation")
                .document(busAttendantLiveLocationModel.getBusAttendantModel().getAttendantId());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Attendant name and location is : "+busAttendantLiveLocationModel.getBusAttendantModel().getAttendantId()
                    + " " +busAttendantLiveLocationModel.getGeoPoint().getLatitude() + busAttendantLiveLocationModel.getGeoPoint().getLongitude());
                }
            }
        });
    }
}