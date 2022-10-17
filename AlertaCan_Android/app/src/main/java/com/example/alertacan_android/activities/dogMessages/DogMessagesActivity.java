package com.example.alertacan_android.activities.dogMessages;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertacan_android.R;
import com.example.alertacan_android.adapters.MessageAdapter;
import com.example.alertacan_android.interfaces.RecyclerViewInterface;
import com.example.alertacan_android.models.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DogMessagesActivity extends AppCompatActivity implements RecyclerViewInterface {

    private static String DOG_ID;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("sightings");

    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private List<Message> mMessages;
    private Dialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_messages);

        Bundle extras = getIntent().getExtras();
        DOG_ID = extras.getString("dog_id_intent");

        Toolbar toolbar = findViewById(R.id.messageToolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Messages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messageDialog = new Dialog(this);
        recyclerView = findViewById(R.id.messagesRecylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMessages();

    }

    private void loadMessages(){
        mMessages = new ArrayList<>();

        notebookRef.whereEqualTo("dogId", DOG_ID)
                .orderBy("date_sighting")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Message message = new Message(
                                        document.getString("phone")
                                        , document.getString("lastLocation")
                                        , document.getString("msg")
                                );
                                mMessages.add(message);
                            }

                            adapter = new MessageAdapter(DogMessagesActivity.this,mMessages,DogMessagesActivity.this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.e("DogMessagesActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        showMessageDialog(position);
        Log.i("DogMessagesActivity", mMessages.get(position).getMessage());
    }

    private void showMessageDialog(int position){
        messageDialog.setContentView(R.layout.dialog_message);
        TextView phoneTv = messageDialog.findViewById(R.id.messagePhoneTextView);
        TextView addressTv = messageDialog.findViewById(R.id.messageAddressTextView);
        TextView messageTv = messageDialog.findViewById(R.id.messageMessageTextView);

        phoneTv.setText(mMessages.get(position).getPhone());
        addressTv.setText(mMessages.get(position).getAddress());
        messageTv.setText(mMessages.get(position).getMessage());

        messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }

}