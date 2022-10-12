package com.example.alertacan_android.activities.dogInfo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alertacan_android.R;
import com.example.alertacan_android.activities.dogRegistration.DogRegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Map;

public class DogInfoActivity extends AppCompatActivity {

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
    String FRAGMENT_VALUE;

    ImageView imageDogView;

    String dogName;
    String dogStatus;
    String dogBreed;
    String dogSex;
    String dogSize;
    String dogColor;
    String dogImageUrl;
    String dogLastTimeLocation;
    String dogDescription;
    String dogOwnerPhone;

    Date dateMissing;


    Button btnEdit;
    Button btnDelete;

    public String formatDate(int day, int dayOfMonth, int month, int year){
        String[] days = new String[]{"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado" };
        String[] months = new String[]{ "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre" };
        return days[day] + " " +  Integer.toString(dayOfMonth)  + " de " + months[month] + " de " + Integer.toString(1900+year);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_info);

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

        imageDogView = findViewById(R.id.id_image);

        btnEdit = findViewById(R.id.id_btn_edit);
        btnDelete = findViewById(R.id.id_btn_delete);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            DOG_ID = extras.getString("dogId");
            FRAGMENT_VALUE = extras.getString("fragmentValue");
        }

        if(!FRAGMENT_VALUE.equals("2")){
            btnEdit.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
        }
        else{
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
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
                        dogName = dataObj.get("name").toString();
                        dogStatus = dataObj.get("state").toString();
                        dogBreed = dataObj.get("breed").toString();
                        dogSex = dataObj.get("sex").toString();
                        dogSize = dataObj.get("size").toString();
                        dogColor = dataObj.get("color").toString();

                        dateMissing = document.getTimestamp("date_missing").toDate();
                        String dogDateMissing = formatDate(dateMissing.getDay(), dateMissing.getDate(), dateMissing.getMonth(), dateMissing.getYear());

                        Date dateRegistration = document.getTimestamp("date_registration").toDate();
                        String dogDateRegistration = formatDate(dateRegistration.getDay(), dateRegistration.getDate(), dateRegistration.getMonth(), dateRegistration.getYear());

                        dogLastTimeLocation = dataObj.get("last_time_location").toString();
                        dogDescription = dataObj.get("description").toString();
                        dogOwnerPhone = dataObj.get("owner_phone").toString();

                        dogImageUrl = dataObj.get("imageUrl").toString();

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

                        Log.d("PPPPPPP", dogImageUrl);
                        Picasso.get()
                                .load(dogImageUrl)
                                .fit()
                                .centerCrop()
                                .into(imageDogView);
                    } else {
                        Log.d("tag2", "No such document");
                    }
                } else {
                    Log.d("tag3", "get failed with ", task.getException());
                }
            }
        });


        // BTN LISTENERS
        btnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(DogInfoActivity.this, DogRegistrationActivity.class);
                myIntent.putExtra("dog_id_intent", DOG_ID); //Optional parameters
                myIntent.putExtra("dog_img_intent", dogImageUrl);
                myIntent.putExtra("dog_name_intent", dogName);
                myIntent.putExtra("dog_breed_intent", dogBreed);
                myIntent.putExtra("dog_size_intent", dogSize);
                myIntent.putExtra("dog_color_intent", dogColor);
                myIntent.putExtra("dog_sex_intent", dogSex);
                myIntent.putExtra("dog_last_time_intent", dogLastTimeLocation);
                myIntent.putExtra("dog_description_intent", dogDescription);
                myIntent.putExtra("dog_phone_intent", dogOwnerPhone);
                myIntent.putExtra("dog_date_missing_intent", dateMissing);


                DogInfoActivity.this.startActivity(myIntent);
            }
        });


    }




}
