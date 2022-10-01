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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dog_info);

        stateTextView = findViewById(R.id.id_state);
        nameTextView = findViewById(R.id.id_name);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("dogs").document("3lc69Hjz9aqfvuJlvqaV");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("tag", "DocumentSnapshot data: " + document.getData());
                        String myS = document.getData().get("name").toString();
                        nameTextView.setText(myS);

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
