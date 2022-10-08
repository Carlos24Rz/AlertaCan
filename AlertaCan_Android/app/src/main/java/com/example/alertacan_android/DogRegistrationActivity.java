package com.example.alertacan_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DogRegistrationActivity extends AppCompatActivity  {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public StorageReference reference;
    private Uri imageUri;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    // variables that store the form
    private String ansRadioG = "Sí";
    private String dogNameObj = "";
    private String dogBreedObj = "";
    private String dogSizeObj = "";
    private String dogColorObj = "";
    private String dogSexObj = "";
    private String dogLastLocationSeenObj = "";
    private Timestamp dogDateMissing;
    private String dogPhoneObj = "";
    private String dogDescriptionObj = "";


    // for the image
    private ImageView imageViewDog;
    private ProgressBar progressBarImg;

    private String dogUrlObj;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_registration);

        // initialize date picker
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        // load four spinners
        loadSpinners();

        // Listen to radio group
        listenRadioGroup();

        // for the image
        imageViewDog = findViewById(R.id.id_image_dog);
        progressBarImg = findViewById(R.id.progressBar);
        progressBarImg.setVisibility(View.INVISIBLE);

         reference = FirebaseStorage.getInstance().getReference();

        imageViewDog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });


        // EditText nameEditTex = findViewById(R.id.id_dog_name);
        Button btnSubmit = findViewById(R.id.id_btn_submit);

        submitDog(btnSubmit);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            imageViewDog.setImageURI(imageUri);
        }
    }

    public void submitDog(Button btnSubmit){

        btnSubmit.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Map<String, Object> newDog = new HashMap<>();

                        // Getting dog is yours
                        String ansRadioGObj = ansRadioG;
//                        newDog.put("is_yours", ansRadioGObj);

                        if (ansRadioG == "Sí"){
                            newDog.put("state", "Perdido");
                        }else {
                            newDog.put("state", "Encontrado");
                        }

                        // Getting dog name
                        EditText textName = (EditText)findViewById(R.id.id_dog_name);
                        dogNameObj = textName.getText().toString();
                        newDog.put("name", dogNameObj);

                        // Getting breed
                        Spinner spinnerBreed = (Spinner) findViewById(R.id.id_spinner_breeds);
                        dogBreedObj = spinnerBreed.getSelectedItem().toString();
                        newDog.put("breed", dogBreedObj);

                        // Getting breed
                        Spinner spinnerSize = (Spinner) findViewById(R.id.id_spinner_size);
                        dogSizeObj = spinnerSize.getSelectedItem().toString();
                        newDog.put("size", dogSizeObj);

                        // Getting color
                        Spinner spinnerColor = (Spinner) findViewById(R.id.id_spinner_color);
                        dogColorObj = spinnerColor.getSelectedItem().toString();
                        newDog.put("color", dogColorObj);

                        // Getting sex
                        Spinner spinnerSex = (Spinner) findViewById(R.id.id_spinner_sex);
                        dogSexObj = spinnerSex.getSelectedItem().toString();
                        newDog.put("sex", dogSexObj);

                        // Getting dog last time location
                        EditText textLastTime = (EditText)findViewById(R.id.id_last_location);
                        dogLastLocationSeenObj = textLastTime.getText().toString();
                        newDog.put("last_time_location", dogLastLocationSeenObj);

                        // Getting date missing
                        Timestamp dogDateMissingObj = dogDateMissing;
                        newDog.put("date_missing", dogDateMissingObj);

                        // Getting date registration
                        Timestamp dogDateRegistrationObj = new Timestamp(System.currentTimeMillis());
                        newDog.put("date_registration", dogDateRegistrationObj);

                        // Getting phone
                        EditText textPhone = (EditText)findViewById(R.id.id_phone);
                        dogPhoneObj = textPhone.getText().toString();
                        newDog.put("owner_phone", dogPhoneObj);

                        // Getting description
                        EditText textDescription = (EditText)findViewById(R.id.id_dog_description);
                        dogDescriptionObj = textDescription.getText().toString();
                        newDog.put("description", dogDescriptionObj);

                        // image
                        if (imageUri != null){
                            uploadToFirebase(imageUri);
                            newDog.put("imageUrl", dogUrlObj);
                        } else{
                            Toast.makeText(DogRegistrationActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                        }

                        db.collection("dogs")
                                .add(newDog)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Toast.makeText(DogRegistrationActivity.this, "Perro registrado", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DogRegistrationActivity.this, "Error al registrar el perro", Toast.LENGTH_SHORT).show();
                                    }
                                });


                        Log.d("newDog", newDog.toString());
                    }
                });
    }

    private void uploadToFirebase(Uri  uri){
        final StorageReference fileRef = reference.child( "uploads/" + System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dogUrlObj = uri.toString();
                        Toast.makeText(DogRegistrationActivity.this, "Uploading successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBarImg.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarImg.setVisibility(View.INVISIBLE);
                Toast.makeText(DogRegistrationActivity.this, "Uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }


    // Function to listen the value of the radio group
    public void listenRadioGroup(){
        RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio1:
                        RadioButton radioButton = (RadioButton) findViewById(R.id.radio1);
                        ansRadioG = radioButton.getText().toString();

                        break;
                    case R.id.radio2:
                        radioButton = (RadioButton) findViewById(R.id.radio2);
                        ansRadioG = radioButton.getText().toString();
                        break;
                }
                Log.d("RADIOOO", ansRadioG);
            }
        });
    }





    // Function to load all four different spinners
    public void loadSpinners(){
        // Spinner breed
        Spinner spinnerBreeds = (Spinner) findViewById(R.id.id_spinner_breeds);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterBreeds = ArrayAdapter.createFromResource(this,
                R.array.breeds_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBreeds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBreeds.setAdapter(adapterBreeds);

        // Spinner size
        Spinner spinnerSize = (Spinner) findViewById(R.id.id_spinner_size);
        ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(this,
                R.array.sizes_array, android.R.layout.simple_spinner_item);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapterSize);

        // Spinner color
        Spinner spinnerColor = (Spinner) findViewById(R.id.id_spinner_color);
        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapterColor);

        // Spinner sex
        Spinner spinnerSex = (Spinner) findViewById(R.id.id_spinner_sex);
        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(adapterSex);
    }

    // Functions to work with the date picker
    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                // updating the variable that saves the timestamnp
                String dateStr = Integer.toString(year) + "-" + Integer.toString(month+1) + "-" + Integer.toString(day) + " 00:00:00.000";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                Date parsedDate = null;
                try {
                    parsedDate = dateFormat.parse(dateStr);
                    dogDateMissing = new java.sql.Timestamp(parsedDate.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        String[] months = new String[]{ "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre" };
        return months[month];
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

}