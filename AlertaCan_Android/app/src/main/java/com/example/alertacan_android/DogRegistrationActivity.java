package com.example.alertacan_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DogRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_registration);

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
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(this,
                R.array.sizes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSize.setAdapter(adapterSize);

        // Spinner color
        Spinner spinnerColor = (Spinner) findViewById(R.id.id_spinner_color);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerColor.setAdapter(adapterColor);

        // Spinner sex
        Spinner spinnerSex = (Spinner) findViewById(R.id.id_spinner_sex);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSex.setAdapter(adapterSex);

    }
}