package com.example.alertacan_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("dogs");

    FirebaseAuth mAuth;
    private DogCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        Toast.makeText(HomeActivity.this, mAuth.getCurrentUser().getEmail(),
                Toast.LENGTH_SHORT).show();

        populateRecyclerView();
    }

    private void populateRecyclerView(){
        Query query = notebookRef.orderBy("date_missing", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Dog> options = new FirestoreRecyclerOptions.Builder<Dog>()
                .setQuery(query, new SnapshotParser<Dog>() {
                    @NonNull
                    @Override
                    public Dog parseSnapshot(@NonNull DocumentSnapshot snapshot) {

                        String id = snapshot.getId();
                        String dogName = snapshot.getString("name");
                        String breed = snapshot.getString("breed");
                        String imageUrl = snapshot.getString("imageUrl");

                        return new Dog(id,dogName,breed,imageUrl);
                    }
                })
                .build();

        adapter = new DogCardAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.lostDogsRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
