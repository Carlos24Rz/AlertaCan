package com.example.alertacan_android.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.alertacan_android.R;
import com.example.alertacan_android.activities.dogRegistration.DogRegistrationActivity;
import com.example.alertacan_android.adapters.DogListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TabLayout tabLayout;
    ViewPager2 dogsViewPager;
    DogListAdapter dogListAdapter;
    BottomNavigationView bottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        dogListAdapter = new DogListAdapter(this, mAuth.getCurrentUser().getEmail());

        tabLayout = findViewById(R.id.tabLayout);
        dogsViewPager = findViewById(R.id.viewpager);
        dogsViewPager.setAdapter(dogListAdapter);
        new TabLayoutMediator(tabLayout,dogsViewPager,
                (tab,position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Encontrados");
                            break;
                        case 1:
                            tab.setText("Perdidos");
                            break;
                        default:
                            tab.setText("Tus Mascotas");
                            break;
                    }
                }).attach();

        bottomMenu = findViewById(R.id.bottomNavigationView);

        Toast.makeText(HomeActivity.this, mAuth.getCurrentUser().getEmail(),
                Toast.LENGTH_SHORT).show();

        bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getTitle().toString()){
                    case ("Añadir Perro") :
                        Intent intent = new Intent(HomeActivity.this, DogRegistrationActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}
