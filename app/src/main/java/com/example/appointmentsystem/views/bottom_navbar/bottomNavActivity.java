package com.example.appointmentsystem.views.bottom_navbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.appointmentsystem.R;
import com.example.appointmentsystem.fragments.AppointmentsFragment;
import com.example.appointmentsystem.fragments.HomeFragment;
import com.example.appointmentsystem.fragments.ProfileFragment;
import com.example.appointmentsystem.fragments.SettingsFragment;
import com.example.appointmentsystem.views.book_appointment.CreateApptActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class bottomNavActivity extends AppCompatActivity {

    FloatingActionButton createApptFAB; BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        createApptFAB = findViewById(R.id.createApptFAB);
        createApptFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateApptActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        // Find the menu item and then disable it
        //navView.menu.findItem(R.id.navigation_home).isEnabled = false

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_appointments:
                        selectedFragment = new AppointmentsFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                    case R.id.nav_settings:
                        selectedFragment = new SettingsFragment();
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,selectedFragment).commit();

                return true;
            }
        };
}