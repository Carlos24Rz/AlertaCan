package com.example.alertacan_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alertacan_android.R;
import com.example.alertacan_android.models.Dog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FilterDogsFabFragment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;

    private Spinner sexSpinner;
    private Spinner breedSpinner;
    private Spinner colorSpinner;
    private Spinner sizeSpinner;
    private Button filterButton;

    private String mSex = "Todos";
    private String mBreed = "Todos";
    private String mColor = "Todos";
    private String mSize = "Todos";

    private String userEmail;
    private int fragmentValue;

    private List<Dog> mDogs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_dog_filter, container, false);

        assert this.getArguments() != null;
        userEmail = this.getArguments().getString("user");
        fragmentValue = this.getArguments().getInt("fragmentVal");


        sexSpinner = v.findViewById(R.id.filter_sex);
        breedSpinner = v.findViewById(R.id.filter_breed);
        colorSpinner = v.findViewById(R.id.filter_color);
        sizeSpinner = v.findViewById(R.id.filter_size);
        Button resetButton = v.findViewById(R.id.resetFilterButton);
        filterButton = v.findViewById(R.id.filterButton);

        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(v.getContext(),
                R.array.sex_array_filter, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterBreed = ArrayAdapter.createFromResource(v.getContext(),
                R.array.breeds_array_filter, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(v.getContext(),
                R.array.colors_array_filter, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(v.getContext(),
                R.array.sizes_array_filter, android.R.layout.simple_spinner_item);

        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterBreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sexSpinner.setAdapter(adapterSex);
        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSex = adapterView.getItemAtPosition(i).toString();
                filterButton.setEnabled(isValid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        breedSpinner.setAdapter(adapterBreed);
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mBreed = adapterView.getItemAtPosition(i).toString();
                filterButton.setEnabled(isValid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        colorSpinner.setAdapter(adapterColor);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mColor = adapterView.getItemAtPosition(i).toString();
                filterButton.setEnabled(isValid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        sizeSpinner.setAdapter(adapterSize);
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSize = adapterView.getItemAtPosition(i).toString();
                filterButton.setEnabled(isValid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        filterButton.setEnabled(false);

        resetButton.setOnClickListener(view -> resetParam());

        filterButton.setOnClickListener(view -> filterSearch());
        return v;
    }
    private void resetParam() {
        sexSpinner.setSelection(0);
        breedSpinner.setSelection(0);
        colorSpinner.setSelection(0);
        sizeSpinner.setSelection(0);
    }

    private void filterSearch(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notebookRef = db.collection("dogs");

        Query query;

        switch(fragmentValue) {
            case 0:
                //get "encontrados" dogs
                query = notebookRef
                        .whereEqualTo("state","Encontrado")
                        .whereNotEqualTo("user", userEmail);
                break;
            case 1:
                //get "perdidos" dogs
                query = notebookRef
                        .whereEqualTo("state","Perdido")
                        .whereNotEqualTo("user", userEmail);
                break;
            default:
                //get user dogs
                query = notebookRef
                        .whereEqualTo("user", userEmail);
                break;
        }

       if (!mSize.equals("Todos")) query = query.whereEqualTo("size",mSize);
       if (!mBreed.equals("Todos")) query = query.whereEqualTo("breed", mBreed);
       if (!mColor.equals("Todos")) query = query.whereEqualTo("color",mColor);
       if (!mSex.equals("Todos")) query = query.whereEqualTo("sex",mSex);

       query.get()
               .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       mDogs = new ArrayList<>();
                       for (QueryDocumentSnapshot document : task.getResult()){
                           Dog dog = new Dog(document.getId()
                                   , document.getString("name")
                                   , document.getString("breed")
                                   , document.getString("imageUrl")
                           );

                           mDogs.add(dog);
                       }
                       mListener.onFilteredClicked(mDogs);
                       dismiss();
                   } else {
                       Log.e("FilterDogs", "Error getting documents: ", task.getException());
                   }
               });
    }

    public interface BottomSheetListener {
        void onFilteredClicked(List<Dog> dogs);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (BottomSheetListener) getParentFragment();
    }

    private boolean isValid(){
        return !mSex.equals("Todos") || !mBreed.equals("Todos") || !mColor.equals("Todos") || !mSize.equals("Todos");
    }
}
