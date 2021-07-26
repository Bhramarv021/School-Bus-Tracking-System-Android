package com.example.schoolbustrackingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.schoolbustrackingsystem.Model.BusAttendantLiveLocationModel;
import com.example.schoolbustrackingsystem.Model.BusAttendantModel;
import com.example.schoolbustrackingsystem.Model.StudentModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BusAttendantActivity extends AppCompatActivity {

    private static final String TAG = "BusAttendantActivity";
    private final int REQUEST_CODE = 101;
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;

    private Button makeAttendance;
    private Button showRoute;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;
    private boolean ismLocationPermissionGranted = false;
    private BusAttendantLiveLocationModel busAttendantLiveLocationModel;
    private FirebaseFirestore mFirebaseFirestore;
    private CollectionReference mSchoolCollectionReference;

    String schoolId;
    String attendantEmail;
    String attendantId;

    Geocoder geocoder;
    List<Address> addresses;
    String selectedAddress;
    String busLoc;

    ArrayList<BusAttendantModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_attendant);

        arrayList = new ArrayList<>();

        Intent intent = getIntent();
        schoolId = intent.getStringExtra("enteredSchoolId");
        attendantEmail = intent.getStringExtra("attendantsLoginId");
        Log.d(TAG, "School id through intent is : " + schoolId + " Attendant login id is : " + attendantEmail);

//        makeAttendance = findViewById(R.id.btnMarkAttendance);
        showRoute = findViewById(R.id.btnShowRoute);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.attendantMapsFragment);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(BusAttendantActivity.this);

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mSchoolCollectionReference = mFirebaseFirestore
                .collection("Schools")
                .document(schoolId).collection("BusAttendantLocation");

        if (checkMapServices()) {
            if (ismLocationPermissionGranted) {
                getLastKnownLocation();
            } else {
                getLocationPermission();
            }
        }
    }

    private void getUserDetails() {
        if (busAttendantLiveLocationModel == null) {
            busAttendantLiveLocationModel = new BusAttendantLiveLocationModel();

            mFirebaseFirestore.collection("Schools")
                    .document(schoolId)
                    .collection("BusAttendant")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                BusAttendantModel busAttendantModel = d.toObject(BusAttendantModel.class);
                                arrayList.add(busAttendantModel);
                                Log.d(TAG, busAttendantModel.getAttendantId());
//                                getCurrentLocation();
                                if (busAttendantModel.getEmailId().equals(attendantEmail)) {
                                    busAttendantLiveLocationModel.setBusAttendantModel(busAttendantModel);
                                    attendantId = busAttendantModel.getAttendantId();
                                    getLastKnownLocation();
                                    Log.d(TAG, "Email match found " + busAttendantModel.getEmailId());
                                }
                            }
                        }
                    });
        } else {
            getLastKnownLocation();
        }
    }

    private void saveUserLocation() {

        if (busAttendantLiveLocationModel != null) {
            DocumentReference documentReference = mSchoolCollectionReference
                    .document(attendantId);

            documentReference.set(busAttendantLiveLocationModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully bus Attendant Live Location Data inserted");
                                Log.d(TAG, "savedUserLocation : " + "Lat : " +
                                        busAttendantLiveLocationModel.getGeoPoint().getLatitude() + " Long : " +
                                        busAttendantLiveLocationModel.getGeoPoint().getLongitude());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d(TAG, "Failed to add bus attendant location");
                }
            });
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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
                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                            busLoc = GetAddressByLatLng(latLng.latitude, latLng.longitude);

                            MarkerOptions markerOptions
                                    = new MarkerOptions().position(latLng).title("You are here");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                            googleMap.addMarker(markerOptions).showInfoWindow();

                            //trying

//                            addStudentsLocationMarker();
//                            googleMap.animateCamera();
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
                                                    String completeAddress =
                                                            document.getString("picDropLocation")
                                                                    .concat(",")
                                                                    .concat(document.getString("state"))
                                                                    .concat(",")
                                                                    .concat(document.getString("city"))
                                                                    .concat(",")
                                                                    .concat(document.getString("country"));
                                                    selectedAddress = completeAddress;
                                                    LatLng latLng = getLocationFromAddress(BusAttendantActivity.this, completeAddress);
                                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(document.getString("studentName"));
                                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                                                    googleMap.addMarker(markerOptions).showInfoWindow();
                                                }
                                            }
                                        }
                                    });

//                            String add = "Rajendra place, baljeet nagar, new delhi - 110008";
//                            LatLng addLatLng = getLocationFromAddress(BusAttendantActivity.this, add);
//                            Log.d(TAG, "Lat Lng from location is : " + addLatLng.latitude + " " + addLatLng.longitude);
//                            MarkerOptions markerOptions3 = new MarkerOptions()
//                                    .position(addLatLng)
//                                    .title("New Point Added");
//                            googleMap.addMarker(markerOptions3).showInfoWindow();
//
//                            LatLng latLng2 = new LatLng(28.6270681, 77.1827629);
//                            GeoPoint geoPoint2 = new GeoPoint(28.6270681, 77.1827629);
//                            MarkerOptions markerOptions2
//                                    = new MarkerOptions().position(latLng2).title("Your stop");
//
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 14));
//                            googleMap.addMarker(markerOptions2).showInfoWindow();

                            busAttendantLiveLocationModel.setGeoPoint(geoPoint);
                            busAttendantLiveLocationModel.setTimestamp(null);
                            saveUserLocation();

                            showRoute.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (busLoc != null && selectedAddress != null) {

//                                        GetAddressByLatLng(latLng.latitude, latLng.longitude);
//                                        geocoder = new Geocoder(BusAttendantActivity.this, Locale.getDefault());

//                                        try {
//                                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                                        String city = addresses.get(0).getLocality();
//                                        String state = addresses.get(0).getAdminArea();
//                                        String country = addresses.get(0).getCountryName();
//                                        String postalCode = addresses.get(0).getPostalCode();
//                                        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//
//                                        Log.d(TAG, "GEOCODER Location is : " + addresses);

//                                        String start = "lal mandir, baljeet nagar, new delhi - 110008, india";
                                        String start = busLoc;
                                        String end = selectedAddress;
//                                        DisplayRoute(latLng, latLng2);
                                        DisplayRoute(start, end);
                                    }
                                }
                            });
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

        // Another way to get current location
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<Location> task) {
//                Location location = task.getResult();
//                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//                Log.d(TAG, "Lat is : " + geoPoint.getLatitude());
//                Log.d(TAG, "Long is : " + geoPoint.getLongitude());
//            }
//        });
    }

//    private void addStudentsLocationMarker() {
//        CollectionReference collectionReferenceToParent = FirebaseFirestore.getInstance()
//                .collection("Schools")
//                .document(schoolId)
//                .collection("Students");
//
//        collectionReferenceToParent
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String completeAddress =
//                                        document.getString("picDropLocation")
//                                                .concat(document.getString("city"))
//                                                .concat(document.getString("country"));
//                                LatLng latLng = getLocationFromAddress(BusAttendantActivity.this, completeAddress);
//                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(document.getString("studentName"));
//
//                            }
//                        }
//                    }
//                });
//    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }

    private String GetAddressByLatLng(double mLat, double mLng) {
        geocoder = new Geocoder(BusAttendantActivity.this, Locale.getDefault());
        String comAdd = null;

        if (mLat != 0) {
            try {
                addresses = geocoder.getFromLocation(mLat, mLng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            if (address.contains("Baljeet") || address.contains("Baljit") || address.contains("baljeet") || address.contains("baljit")) {
                comAdd = "Lal mandir, baljeet nagar, new delhi";
            } else
                comAdd = address.concat(",").concat(state).concat(",").concat(city).concat(",").concat(country);
            Log.d(TAG, "GEOCODER Location is : " + addresses);

        }
        return comAdd;
    }

//    private String GetAddressByLatLng(double mLat, double mLng) {
//        geocoder = new Geocoder(BusAttendantActivity.this, Locale.getDefault());
//        String mAddress = null;
//
//        if (mLat != 0) {
//            try {
//                addresses = geocoder.getFromLocation(mLat, mLng, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (addresses != null) {
//                // To get direct address by click on map
//                mAddress = addresses.get(0).getAddressLine(0);
//
//                // To create Custom Address
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String county = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//                String district = addresses.get(0).getSubAdminArea();
//
//                selectedAddress = mAddress.concat(",").concat(state).concat(",").concat(city).concat(",").concat(county);
//
//                if (mAddress != null) {
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    LatLng latLng = new LatLng(mLat, mLng);
//                    markerOptions.position(latLng).title(selectedAddress);
////                    googleMaps.addMarker(markerOptions).showInfoWindow();
//                } else {
//                    Toast.makeText(this, "Something went wrong to set marker", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Something went wrong in getAddressByLatLng", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "LatLng Null", Toast.LENGTH_SHORT).show();
//        }
//        return mAddress;
//    }

    //    private void DisplayRoute(LatLng latLng, LatLng latLng2) {
    private void DisplayRoute(String str, String end) {
        Log.d(TAG, "str and end points is : " + str + " " + end);
        //If the device does not have a map installed, then redirect it to play store
        try {
            //when google maps is installed
            //Initialize uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + str + "/"
                    + end);
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

    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    // Called by checkMapServices() method to check the services
// To check google play services is installed on device
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(BusAttendantActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(BusAttendantActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // To check GPS is enabled on device
    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    // Called by isMapsEnabled() function
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // Called by startActivityForResult in buildAlertMessageNoGps
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        if (requestCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
            if (ismLocationPermissionGranted) {
//                getCurrentLocation();
                getUserDetails();
            } else {
                getLocationPermission();
            }
        }
    }

    // Called by onActivityResult if user denied location permission
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ismLocationPermissionGranted = true;
//            getCurrentLocation();
            getUserDetails();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // Called by getLocationPermission() when function calls else part
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ismLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ismLocationPermissionGranted = true;
//                getCurrentLocation();
                getUserDetails();
            }
        }
    }
}