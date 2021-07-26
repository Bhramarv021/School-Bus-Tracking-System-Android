package com.example.schoolbustrackingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ParentActivity extends AppCompatActivity {

    private static final String TAG = "ParentActivity";
    private final int REQUEST_CODE = 101;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;

    private String userEmail;
    private String schoolId;

    Geocoder geocoder;
    List<Address> addresses;

    String studentRouteNumber;
    String studentDropLocation;
    String attendantId;
    GeoPoint attendantLiveLocation;

    String busLocation;
    String dropLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ParentActivity.this);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("parentEmail");
        schoolId = intent.getStringExtra("enteredSchoolId");
        Log.d(TAG, "Parent Login email and school id is : " + userEmail + " " + schoolId);


        if (ActivityCompat.checkSelfPermission(ParentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();

            CollectionReference collectionReferenceToParent = FirebaseFirestore.getInstance()
                    .collection("Schools")
                    .document(schoolId)
                    .collection("Students");

            collectionReferenceToParent
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getString("parentEmail").equals(userEmail)) {
                                        studentRouteNumber = document.getString("routeNumber");
                                        studentDropLocation = document.getString("picDropLocation");
                                        dropLocation = studentDropLocation;
                                        Log.d(TAG, "Student route number and drop loc is : " + studentRouteNumber
                                                + " " + studentDropLocation);
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents : " + task.getException());
                            }
                        }
                    });


            CollectionReference collectionReferenceToAttendant = FirebaseFirestore.getInstance()
                    .collection("Schools")
                    .document(schoolId)
                    .collection("BusAttendant");

            collectionReferenceToAttendant
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    if (doc.getString("routeNumber").equals(studentRouteNumber)) {
                                        attendantId = doc.getString("attendantId");
                                        Log.d(TAG, "Attendant Id with student is : " + attendantId + " At route number " +
                                                doc.getString("routeNumber"));
                                    }
                                }
                            }
                        }
                    });

            CollectionReference collectionReferenceToAttendantLocation = FirebaseFirestore.getInstance()
                    .collection("Schools")
                    .document(schoolId)
                    .collection("BusAttendantLocation");

            collectionReferenceToAttendantLocation
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    if (document.getString("busAttendantModel.attendantId").equals(attendantId)) {
                                        attendantLiveLocation = document.getGeoPoint("geoPoint");
                                        Log.d(TAG, "atndnt id's and geoPoint is : " +
                                                document.getString("busAttendantModel.emailId") + " " +
                                                attendantLiveLocation);
                                        busLocation = GetAddressByLatLng(attendantLiveLocation.getLatitude(), attendantLiveLocation.getLongitude());
                                        Log.d(TAG, "Returned String address is : " + busLocation);
                                        getBusLocationAndDirectionToHome(busLocation, dropLocation);
                                    }
                                }
                            }
                        }
                    });

            Log.d(TAG, "HELLO EVERYONE");
            Log.d(TAG, "Bus And student drop loc is : " + busLocation + " " + dropLocation);

//            getBusLocationAndDirectionToHome(busLocation, dropLocation);

        } else {
            ActivityCompat.requestPermissions(ParentActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private String GetAddressByLatLng(double mLat, double mLng) {
        geocoder = new Geocoder(ParentActivity.this, Locale.getDefault());
        String address = null;

        if (mLat != 0) {
            try {
                addresses = geocoder.getFromLocation(mLat, mLng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            Log.d(TAG, "GEOCODER Location is : " + addresses);

        }
        return address;
    }

    private void getBusLocationAndDirectionToHome(String busLocation, String HomeLocation) {
        //If the device does not have a map installed, then redirect it to play store
        try {
            //when google maps is installed
            //Initialize uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + busLocation + "/"
                    + dropLocation);
            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //Set Package
            intent.setPackage("com.google.android.apps.maps");
            //Set Flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start Activity
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //When google maps is not installed
            //Initialize uri
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id-com.google.android.apps.maps");
            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //Set Flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start Activity
            startActivity(intent);
        }
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            MarkerOptions markerOptions
                                    = new MarkerOptions().position(latLng).title("You are here");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                            googleMap.addMarker(markerOptions).showInfoWindow();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Fail to get last location " + e.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(ParentActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}