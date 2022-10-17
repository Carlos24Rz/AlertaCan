package com.example.alertacan_android.activities.dogRegistration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alertacan_android.R;
import com.example.alertacan_android.activities.home.HomeActivity;
import com.example.alertacan_android.adapters.AddressPredictionArrayAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DogRegistrationActivity extends AppCompatActivity  {

    private static final long START_TIME_IN_MILLIS = 1000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public StorageReference reference;
    private Uri imageUri;

    FirebaseAuth mAuth;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    // views
    EditText textName;
    Spinner spinnerBreed;
    Spinner spinnerSize;
    Spinner spinnerColor;
    Spinner spinnerSex;
    AutoCompleteTextView textLastTime;
    EditText textDescription;
    EditText textPhone;

    // variables that store the form
    private String ansRadioG = "Sí";
    private String dogNameObj = "";
    private String dogBreedObj = "";
    private String dogSizeObj = "";
    private String dogColorObj = "";
    private String dogSexObj = "";
    private String dogLastLocationSeenObj = null;
    private Timestamp dogDateMissing;
    private String dogPhoneObj = "";
    private String dogDescriptionObj = "";
    private String dogOwnerObj = "";
    private Date dogDateMissingObj;
    private String dogUrlObj;

    private PlacesClient placesClient;
    private String dogPlaceIdObj;

    // for the image
    private ImageView imageViewDog;
    private ProgressBar progressBarImg;

    private String DOG_ID;

    // adapters for spinners
    ArrayAdapter<CharSequence> adapterBreeds;
    ArrayAdapter<CharSequence> adapterSize;
    ArrayAdapter<CharSequence> adapterColor;
    ArrayAdapter<CharSequence> adapterSex;
    Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_registration);

        // views
        textName = findViewById(R.id.id_dog_name);
        spinnerBreed = findViewById(R.id.id_spinner_breeds);
        spinnerSize = findViewById(R.id.id_spinner_size);
        spinnerColor = findViewById(R.id.id_spinner_color);
        spinnerSex = findViewById(R.id.id_spinner_sex);
        textLastTime = findViewById(R.id.id_last_location);
        textDescription = findViewById(R.id.id_dog_description);
        textPhone = findViewById(R.id.id_phone);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),"AIzaSyAc9EcgUw9i8qLE7nHtxhBj6C6rbAT7Uo0");
        }

        placesClient = Places.createClient(this);

        // initialize date picker
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText("");

        // load four spinners
        loadSpinners();

        // Listen to radio group
        listenRadioGroup();

        // for the image
        imageViewDog = findViewById(R.id.id_image_dog);
        progressBarImg = findViewById(R.id.progressBar);
        progressBarImg.setVisibility(View.INVISIBLE);
        reference = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        imageViewDog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        // checking if there is a new dog or it is an update
        extras = getIntent().getExtras();
        if (extras != null) {
            DOG_ID = extras.getString("dog_id_intent");
            dogUrlObj = extras.getString("dog_img_intent");
            dogNameObj = extras.getString("dog_name_intent");
            dogBreedObj = extras.getString("dog_breed_intent");
            dogSizeObj = extras.getString("dog_size_intent");
            dogColorObj = extras.getString("dog_color_intent");
            dogSexObj = extras.getString("dog_sex_intent");
            dogPlaceIdObj = extras.getString("dog_placeId_intent");
            dogLastLocationSeenObj = extras.getString("dog_last_time_intent");
            dogDescriptionObj = extras.getString("dog_description_intent");
            dogPhoneObj = extras.getString("dog_phone_intent");
            dogDateMissingObj = (Date) extras.get("dog_date_missing_intent");

            // Setting values to update
            Picasso.get()
                    .load(dogUrlObj)
                    .fit()
                    .centerCrop()
                    .into(imageViewDog);

            textName.setText(dogNameObj);

            int spinnerBreedPosition = adapterBreeds.getPosition(dogBreedObj);
            spinnerBreed.setSelection(spinnerBreedPosition);

            int spinnerSizePosition = adapterSize.getPosition(dogSizeObj);
            spinnerSize.setSelection(spinnerSizePosition);

            int spinnerColorPosition = adapterColor.getPosition(dogColorObj);
            spinnerColor.setSelection(spinnerColorPosition);

            int spinnerSexPosition = adapterSex.getPosition(dogSexObj);
            spinnerSex.setSelection(spinnerSexPosition);

            textLastTime.setText(dogLastLocationSeenObj);

            textDescription.setText(dogDescriptionObj);

            textPhone.setText(dogPhoneObj);

            dateButton.setText(makeDateString(dogDateMissingObj.getDate(), dogDateMissingObj.getMonth()+1, dogDateMissingObj.getYear()+1900));
            dogDateMissing = java.sql.Timestamp.valueOf(makeDateTimeStamp(dogDateMissingObj.getDate(), dogDateMissingObj.getMonth()+1, dogDateMissingObj.getYear()+1900));
        }

        textLastTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!textLastTime.isPerformingCompletion()){
                    dogLastLocationSeenObj = null;
                    dogPlaceIdObj = "";

                    textLastTime.dismissDropDown();
                    if (mTimerRunning) {
                        mCountDownTimer.cancel();
                        mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    }
                    startTimer(charSequence.toString());
                } else {
                    Log.i("AutoComplete", "Address has been selected");
                    dogLastLocationSeenObj = textLastTime.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        textLastTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AutocompletePrediction prediction = (AutocompletePrediction) adapterView.getItemAtPosition(i);
                dogPlaceIdObj = prediction.getPlaceId();
            }
        });


        // event listener fot the submit button
        Button btnSubmit = findViewById(R.id.id_btn_submit);
        if(DOG_ID == null){
            Log.d("update", "submitting");
            submitDog(btnSubmit);
        } else{
            Log.d("update", "updating dog");
            updateDog(btnSubmit);
        }
    }

    private void startTimer(String query) {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMillis = l;
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                predictAddress(query);
            }
        }.start();

        mTimerRunning = true;
    }

    private void predictAddress(String query){
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {

            List<AutocompletePrediction> predictions = new ArrayList<>(response.getAutocompletePredictions());
            AddressPredictionArrayAdapter adapter = new AddressPredictionArrayAdapter(this,predictions);

            textLastTime.setAdapter(adapter);
            textLastTime.showDropDown();

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("Place Error", "Place not found: " + apiException.getStatusCode());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode== 2 && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            imageViewDog.setImageURI(imageUri);
        }
    }

    //for new Dog
    public void submitDog(Button btnSubmit){
        btnSubmit.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        if (imageUri!=null && dogNameObj!=null && dogLastLocationSeenObj!=null && dogPhoneObj!=null && dogDescriptionObj!=null && dogDateMissing!=null){
                            uploadToFirebase(imageUri);
                        } else{
                            Toast.makeText(DogRegistrationActivity.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //for existing Dog
    public void updateDog(Button btnSubmit){
        btnSubmit.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        if (dogNameObj!=null && dogLastLocationSeenObj!=null && dogPhoneObj!=null && dogDescriptionObj!=null){
                            updateFirebaseRecord();
                        } else{
                            Toast.makeText(DogRegistrationActivity.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateFirebaseRecord(){
        if (imageUri != null) {
            //agregar nueva imagen a la de base de datos
            updateRecordWithImage();
        } else {
            //modificar documento sin la imagen
            updateRecordNoImage();
        }
    }

    private void updateRecordWithImage(){
        final StorageReference fileRef = reference.child( "uploads/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    // the images was successfully submited to firebase
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> newDog = new HashMap<>();

                        // Getting dog is yours
                        String ansRadioGObj = ansRadioG;
                        if (ansRadioG == "Sí"){
                            newDog.put("state", "Perdido");
                        }else {
                            newDog.put("state", "Encontrado");
                        }

                        // Getting dog name
                        dogNameObj = textName.getText().toString();
                        newDog.put("name", dogNameObj);

                        // Getting breed
                        dogBreedObj = spinnerBreed.getSelectedItem().toString();
                        newDog.put("breed", dogBreedObj);

                        // Getting size
                        dogSizeObj = spinnerSize.getSelectedItem().toString();
                        newDog.put("size", dogSizeObj);

                        // Getting color
                        dogColorObj = spinnerColor.getSelectedItem().toString();
                        newDog.put("color", dogColorObj);

                        // Getting sex
                        dogSexObj = spinnerSex.getSelectedItem().toString();
                        newDog.put("sex", dogSexObj);

                        // Getting dog last time location
                        newDog.put("last_time_location", dogLastLocationSeenObj);

                        // Getting date missing
                        dogDateMissingObj = dogDateMissing;
                        newDog.put("date_missing", dogDateMissingObj);

                        // Getting date registration
                        Timestamp dogDateRegistrationObj = new Timestamp(System.currentTimeMillis());
                        newDog.put("date_registration", dogDateRegistrationObj);

                        // Getting phone
                        dogPhoneObj = textPhone.getText().toString();
                        newDog.put("owner_phone", dogPhoneObj);

                        // Getting description
                        dogDescriptionObj = textDescription.getText().toString();
                        newDog.put("description", dogDescriptionObj);

                        // Getting image url
                        dogUrlObj = uri.toString();
                        newDog.put("imageUrl", dogUrlObj);

                        newDog.put("placeID", dogPlaceIdObj);

                        db.collection("dogs").document(DOG_ID)
                                .set(newDog, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBarImg.setVisibility(View.INVISIBLE);
                                        Toast.makeText(DogRegistrationActivity.this, "Perro actualizado", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(DogRegistrationActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBarImg.setVisibility(View.INVISIBLE);
                                        Toast.makeText(DogRegistrationActivity.this, "Error al actualizar el perro", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                Toast.makeText(DogRegistrationActivity.this, "Error al subir la foto", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateRecordNoImage() {

        progressBarImg.setVisibility(View.VISIBLE);
        Map<String, Object> newDog = new HashMap<>();

        // Getting dog is yours
        if (ansRadioG == "Sí"){
            newDog.put("state", "Perdido");
        }else {
            newDog.put("state", "Encontrado");
        }

        // Getting dog name
        dogNameObj = textName.getText().toString();
        newDog.put("name", dogNameObj);

        // Getting breed
        dogBreedObj = spinnerBreed.getSelectedItem().toString();
        newDog.put("breed", dogBreedObj);

        // Getting size
        dogSizeObj = spinnerSize.getSelectedItem().toString();
        newDog.put("size", dogSizeObj);

        // Getting color
        dogColorObj = spinnerColor.getSelectedItem().toString();
        newDog.put("color", dogColorObj);

        // Getting sex
        dogSexObj = spinnerSex.getSelectedItem().toString();
        newDog.put("sex", dogSexObj);

        // Getting dog last time location
        newDog.put("last_time_location", dogLastLocationSeenObj);

        // Getting date missing
        dogDateMissingObj = dogDateMissing;
        newDog.put("date_missing", dogDateMissingObj);

        // Getting phone
        dogPhoneObj = textPhone.getText().toString();
        newDog.put("owner_phone", dogPhoneObj);

        // Getting description
        dogDescriptionObj = textDescription.getText().toString();
        newDog.put("description", dogDescriptionObj);

        newDog.put("placeID", dogPlaceIdObj);

        db.collection("dogs").document(DOG_ID)
                .set(newDog, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBarImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(DogRegistrationActivity.this, "Perro actualizado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DogRegistrationActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBarImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(DogRegistrationActivity.this, "Error al actualizar el perro", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadToFirebase(Uri  uri){
        final StorageReference fileRef = reference.child( "uploads/" + System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    // the images was successfully submited to firebase
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> newDog = new HashMap<>();

                        // Getting dog is yours
                        String ansRadioGObj = ansRadioG;
                        if (ansRadioG == "Sí"){
                            newDog.put("state", "Perdido");
                        }else {
                            newDog.put("state", "Encontrado");
                        }

                        // Getting dog name
                        dogNameObj = textName.getText().toString();
                        newDog.put("name", dogNameObj);

                        // Getting breed
                        dogBreedObj = spinnerBreed.getSelectedItem().toString();
                        newDog.put("breed", dogBreedObj);

                        // Getting size
                        dogSizeObj = spinnerSize.getSelectedItem().toString();
                        newDog.put("size", dogSizeObj);

                        // Getting color
                        dogColorObj = spinnerColor.getSelectedItem().toString();
                        newDog.put("color", dogColorObj);

                        // Getting sex
                        dogSexObj = spinnerSex.getSelectedItem().toString();
                        newDog.put("sex", dogSexObj);

                        // Getting dog last time location
                        newDog.put("last_time_location", dogLastLocationSeenObj);

                        // Getting date missing
                        dogDateMissingObj = dogDateMissing;
                        newDog.put("date_missing", dogDateMissingObj);

                        // Getting date registration
                        Timestamp dogDateRegistrationObj = new Timestamp(System.currentTimeMillis());
                        newDog.put("date_registration", dogDateRegistrationObj);

                        // Getting phone
                        dogPhoneObj = textPhone.getText().toString();
                        newDog.put("owner_phone", dogPhoneObj);

                        // Getting description
                        dogDescriptionObj = textDescription.getText().toString();
                        newDog.put("description", dogDescriptionObj);

                        // Getting image url
                        dogUrlObj = uri.toString();
                        newDog.put("imageUrl", dogUrlObj);

                        // Getting current user
                        dogOwnerObj = mAuth.getCurrentUser().getEmail();
                        newDog.put("user", dogOwnerObj);

                        newDog.put("placeID", dogPlaceIdObj);

                        db.collection("dogs")
                                .add(newDog)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(DogRegistrationActivity.this, "Perro registrado", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DogRegistrationActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DogRegistrationActivity.this, "Error al registrar el perro", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        progressBarImg.setVisibility(View.INVISIBLE);
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
                Toast.makeText(DogRegistrationActivity.this, "Error al subir la foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to get the file extension of the image
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
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterBreeds = ArrayAdapter.createFromResource(this,
                R.array.breeds_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBreeds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBreed.setAdapter(adapterBreeds);

        // Spinner size
        adapterSize = ArrayAdapter.createFromResource(this,
                R.array.sizes_array, android.R.layout.simple_spinner_item);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapterSize);

        // Spinner color
        adapterColor = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapterColor);

        // Spinner sex
        adapterSex = ArrayAdapter.createFromResource(this,
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

        if(extras == null){
            datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        }
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String makeDateTimeStamp(int day, int month, int year) {
        return year + "-" + month + "-" + day + " 10:10:10.0";
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