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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class ViewAllBusesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static String TAG = "ViewAllBusesActivity";

    //    private SupportMapFragment supportMapFragment;
    private ArrayList<BusAttendantLiveLocationModel> mAttendantsLocation = new ArrayList<>();
  
    private ListenerRegistration mAttendantListEventListener;

    private String schoolId;

    private GoogleMap mMap;

    private ArrayList<LatLng> latLngArrayList;


    LatLng sydney = new LatLng(-34, 151);
    LatLng TamWorth = new LatLng(-31.083332, 150.916672);
    LatLng NewCastle = new LatLng(-32.916668, 151.750000);
    LatLng Brisbane = new LatLng(-27.470125, 153.021072);

    // creating array list for adding all our locations.
    private ArrayList<LatLng> locationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_buses);

        Intent intent = getIntent();
        schoolId = intent.getStringExtra("schoolId");

//        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.attendantMapsFragment);
//        supportMapFragment.getMapAsync(this);

        getAttendantsDetail();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.allBusesMapsFragment);

        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.
        locationArrayList.add(sydney);
        locationArrayList.add(TamWorth);
        locationArrayList.add(NewCastle);
        locationArrayList.add(Brisbane);

        mapFragment.getMapAsync(this);
//        getAttendantsDetail();
    }

    private void getAttendantsDetail() {
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

                if (value != null) {

                    for (QueryDocumentSnapshot doc : value) {

                        BusAttendantLiveLocationModel busAttendantLiveLocationModel = doc.toObject(BusAttendantLiveLocationModel.class);
                        mAttendantsLocation.add(busAttendantLiveLocationModel);

                        LatLng latLng = new LatLng(busAttendantLiveLocationModel.getGeoPoint().getLatitude(),
                                busAttendantLiveLocationModel.getGeoPoint().getLongitude());

//                        latLngArrayList.add(latLng);
                    }
//                    Log.d(TAG, "onEvent: user list size: " + mAttendantsLocation.size() + " , " + latLngArrayList.size());
//                    getAttendantsLocation();
                }
            }
        });
    }

//    private void getAttendantsLocation(){
////        CollectionReference documentReference = FirebaseFirestore.getInstance()
////                .collection("Schools")
////                .document(schoolId)
////                .collection("BusAttendantLocation");
////                .document(mAttendantsLocation.get().getBusAttendantModel().getAttendantId());
//
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    Log.d(TAG, "Attendant name and location is : "+mAttendantsLocation.get(i).getBusAttendantModel().getAttendantId()
//                    + " " +mAttendantsLocation.get(i).getGeoPoint().getLatitude() + mAttendantsLocation.get(i).getGeoPoint().getLongitude());
//                }
//            }
//        });
//    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
//        LatLng latLng = new LatLng(28.6570681, 77.1627629);

//        getAttendantsDetail();
//        for (int i = 0; i < latLngArrayList.size(); i++) {
//
//            // below line is use to add marker to each location of our array list.
//            mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(i)).title("Marker"));
//
//            // below lin is use to zoom our camera on map.
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
//
//            // below line is use to move our camera to the specific location.
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngArrayList.get(i)));
//
//        }

        for (int i = 0; i < locationArrayList.size(); i++) {

            // below line is use to add marker to each location of our array list.
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));

            // below lin is use to zoom our camera on map.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));

//        googleMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("You"));

        }
    }
}