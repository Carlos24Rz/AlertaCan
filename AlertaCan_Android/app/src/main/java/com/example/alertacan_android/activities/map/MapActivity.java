package com.example.alertacan_android.activities.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.alertacan_android.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapActivity extends AppCompatActivity

        implements OnMapReadyCallback {

    private double latitudeUser;
    private double longitudeUser;
    private LocationRequest locationRequest;

    private Double dogLat;
    private Double dogLon;
    private PlacesClient placesClient;


    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);

        //
        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), "AIzaSyAc9EcgUw9i8qLE7nHtxhBj6C6rbAT7Uo0");
        }
        placesClient = Places.createClient(this);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            latitudeUser = extras.getDouble("lat");
            longitudeUser = extras.getDouble("lon");
        }

        LatLng userLocation = new LatLng(latitudeUser, longitudeUser);
        googleMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("Estás aquí"));

        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeUser, longitudeUser), 15.0f) );

        // Adding markers for dogs
        db.collection("dogs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String dogName = (String) document.getData().get("name");
                                String dogBreed = (String) document.getData().get("breed");

                                String placeId = (String) document.getData().get("placeID");
                                final List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                                final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId,placeFields);

                                placesClient.fetchPlace(request).addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
                                    @Override
                                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                                        if (task.isSuccessful()){
                                            Place place = task.getResult().getPlace();
                                            dogLat = place.getLatLng().latitude;
                                            dogLon = place.getLatLng().longitude;



                                            // creating marker
                                            LatLng dogLocation = new LatLng(dogLat, dogLon);
                                            googleMap.addMarker(new MarkerOptions()
                                                    .position(dogLocation)
                                                    .title(dogName + " - " + dogBreed)
                                                    .icon(BitmapDescriptorFactory
                                                            .fromResource(R.drawable.dog_marker)));

                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (ApiException e){
                                                Log.e("Places",e.getMessage());
                                            } catch (Exception e){
                                                Log.e("Error Places",e.getMessage());
                                            }
                                        }
                                    }
                                });



                            }
                        } else {
                            Log.d("tag_error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}


