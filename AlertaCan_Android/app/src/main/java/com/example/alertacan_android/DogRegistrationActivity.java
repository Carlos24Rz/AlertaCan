package com.example.alertacan_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class DogRegistrationActivity extends AppCompatActivity {

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
//    private String dogName = "";



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


//        EditText nameEditTex = findViewById(R.id.id_dog_name);
        Button btnSubmit = findViewById(R.id.id_btn_submit);

        submitDog(btnSubmit);



    }



    public void submitDog(Button btnSubmit){

        btnSubmit.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        // Getting dog is yours
                        String ansRadioGObj = ansRadioG;

                        // Getting dog name
                        EditText text = (EditText)findViewById(R.id.id_dog_name);
                        dogNameObj = text.getText().toString();

                        




                    }
                });
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
//                month = month + 1;
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