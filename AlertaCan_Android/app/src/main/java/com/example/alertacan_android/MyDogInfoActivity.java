package com.example.alertacan_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class MyDogInfoActivity extends AppCompatActivity {

    TextView stateTextView;
    TextView nameTextView;
    TextView breedTextView;
    TextView sexTextView;
    TextView sizeTextView;
    TextView colorTextView;
    TextView dateMissingTextView;
    TextView dateRegistrationTextView;
    TextView lasTimeLocationTextView;
    TextView descriptionTextView;
    TextView ownerPhoneTextView;

    String DOG_ID;

    public String formatDate(int day, int dayOfMonth, int month, int year){
        String[] days = new String[]{"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado" };
        String[] months = new String[]{ "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre" };
        return days[day] + " " +  Integer.toString(dayOfMonth)  + " de " + months[month] + " de " + Integer.toString(1900+year);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dog_info);

        stateTextView = findViewById(R.id.id_state);
        nameTextView = findViewById(R.id.id_name);
        breedTextView = findViewById(R.id.id_breed);
        sexTextView = findViewById(R.id.id_sex);
        sizeTextView = findViewById(R.id.id_size);
        colorTextView = findViewById(R.id.id_color);
        dateMissingTextView = findViewById(R.id.id_date_missing);
        dateRegistrationTextView = findViewById(R.id.id_date_registration);
        lasTimeLocationTextView = findViewById(R.id.id_last_time_location);
        descriptionTextView = findViewById(R.id.id_description);
        ownerPhoneTextView = findViewById(R.id.id_owner_phone);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            DOG_ID = extras.getString("dogId");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("dogs").document(DOG_ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("tag", "DocumentSnapshot data: " + document.getData());

                        Map<String, Object> dataObj = document.getData();
                        String dogName = dataObj.get("name").toString();
                        String dogStatus = dataObj.get("state").toString();
                        String dogBreed = dataObj.get("breed").toString();
                        String dogSex = dataObj.get("sex").toString();
                        String dogSize = dataObj.get("size").toString();
                        String dogColor = dataObj.get("color").toString();

                        Date dateMissing = document.getTimestamp("date_missing").toDate();
                        String dogDateMissing = formatDate(dateMissing.getDay(), dateMissing.getDate(), dateMissing.getMonth(), dateMissing.getYear());

                        Date dateRegistration = document.getTimestamp("date_registration").toDate();
                        String dogDateRegistration = formatDate(dateRegistration.getDay(), dateRegistration.getDate(), dateRegistration.getMonth(), dateRegistration.getYear());

                        String dogLastTimeLocation = dataObj.get("last_time_location").toString();
                        String dogDescription = dataObj.get("description").toString();
                        String dogOwnerPhone = dataObj.get("owner_phone").toString();

                        nameTextView.setText(dogName);
                        stateTextView.setText((dogStatus));
                        breedTextView.setText(dogBreed);
                        sexTextView.setText(dogSex);
                        sizeTextView.setText(dogSize);
                        colorTextView.setText(dogColor);
                        dateMissingTextView.setText(dogDateMissing);
                        dateRegistrationTextView.setText(dogDateRegistration);
                        lasTimeLocationTextView.setText(dogLastTimeLocation);
                        descriptionTextView.setText(dogDescription);
                        ownerPhoneTextView.setText(dogOwnerPhone);
                    } else {
                        Log.d("tag2", "No such document");
                    }
                } else {
                    Log.d("tag3", "get failed with ", task.getException());
                }
            }
        });


    }




}
