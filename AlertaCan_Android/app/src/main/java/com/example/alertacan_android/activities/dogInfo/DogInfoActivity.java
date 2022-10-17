package com.example.alertacan_android.activities.dogInfo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alertacan_android.R;
import com.example.alertacan_android.activities.dogRegistration.DogRegistrationActivity;
import com.example.alertacan_android.activities.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DogInfoActivity extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    String dogPlaceId;
    String dogLastTimeLocation;
    String dogDescription;
    String dogOwnerPhone;

    Date dateMissing;


    Button btnEdit;
    Button btnDelete;
    Button btnSeen;
    Button btnNot;

    Dialog dialogSeen;
    ImageView imgDog;
    TextView inputPhone;
    TextView inputLastTime;
    TextView inputMsg;
    Button btnDialogSubmit;

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
        btnSeen = findViewById(R.id.id_btn_seen);
        btnNot = findViewById(R.id.id_btn_not);

        // Creating dialog
        dialogSeen = new Dialog(this);

        btnSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSeen();

            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            DOG_ID = extras.getString("dogId");
            FRAGMENT_VALUE = extras.getString("fragmentValue");
        }

        if(!FRAGMENT_VALUE.equals("2")){
            btnEdit.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
            btnSeen.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.INVISIBLE);
        }
        else{
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnSeen.setVisibility(View.INVISIBLE);
            btnNot.setVisibility(View.VISIBLE);
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
                        dogPlaceId = dataObj.get("placeID").toString();
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

                        Picasso.get()
                                .load(dogImageUrl)
                                .fit()
                                .centerCrop()
                                .placeholder( R.drawable.progress_animation )
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
                myIntent.putExtra("dog_placeId_intent", dogPlaceId);
                myIntent.putExtra("dog_last_time_intent", dogLastTimeLocation);
                myIntent.putExtra("dog_description_intent", dogDescription);
                myIntent.putExtra("dog_phone_intent", dogOwnerPhone);
                myIntent.putExtra("dog_date_missing_intent", dateMissing);


                DogInfoActivity.this.startActivity(myIntent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DogInfoActivity.this)
                        .setTitle("Eliminar perro")
                        .setMessage("¿Deseas borrar a " + dogName + " de tu cuenta?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                db.collection("dogs").document(DOG_ID)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(DogInfoActivity.this, dogName + " ha sido borrado exitosamente", Toast.LENGTH_SHORT).show();
                                                Intent myIntent = new Intent(DogInfoActivity.this, HomeActivity.class);
                                                DogInfoActivity.this.startActivity(myIntent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(DogInfoActivity.this, "Ocurrió un error al borrar al perro", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }




    private void showDialogSeen(){
        dialogSeen.setContentView(R.layout.dialog_dog_seen);

        imgDog = dialogSeen.findViewById(R.id.id_dialog_img);
        Picasso.get()
                .load(dogImageUrl)
                .placeholder( R.drawable.progress_animation )
                .fit()
                .centerCrop()
                .into(imgDog);


        inputPhone = dialogSeen.findViewById(R.id.id_dialog_name);
        inputLastTime = dialogSeen.findViewById(R.id.id_dialog_id_seen);
        inputMsg = dialogSeen.findViewById(R.id.id_dialog_msg);
        btnDialogSubmit = dialogSeen.findViewById(R.id.id_dialog_btn_submit);

        btnDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneObj = inputPhone.getText().toString();
                String lastTimeObj = inputLastTime.getText().toString();
                String msgObj = inputMsg.getText().toString();

                if(lastTimeObj.equals("") || msgObj.equals("")){
                    Toast.makeText(DogInfoActivity.this, "Por favor, llena todos los campos obligatorios", Toast.LENGTH_SHORT).show();
                }else{

                    Map<String, Object> newSighting = new HashMap<>();

                    if(phoneObj.equals("")){
                        phoneObj = "Anónimo";
                    }

                    newSighting.put("phone", phoneObj);
                    newSighting.put("lastLocation", lastTimeObj);
                    newSighting.put("msg", msgObj);

                    Timestamp sightingObj = new Timestamp(System.currentTimeMillis());
                    newSighting.put("date_sighting", sightingObj);

                    newSighting.put("dogId", DOG_ID);
                    newSighting.put("dogName", dogName);

                    db.collection("sightings")
                            .add(newSighting)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(DogInfoActivity.this, "Avistamiento registrado", Toast.LENGTH_SHORT).show();
                                    dialogSeen.dismiss();
//                                    Intent myIntent = new Intent(DogRegistrationActivity.this, HomeActivity.class);
//                                    DogRegistrationActivity.this.startActivity(myIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DogInfoActivity.this, "Error al registrar avistamiento", Toast.LENGTH_SHORT).show();
                                }
                            });



                }
            }
        });



        dialogSeen.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSeen.show();




    }




}
