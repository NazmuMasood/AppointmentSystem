package com.example.appointmentsystem.views.user_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentsystem.R;
import com.example.appointmentsystem.models.Doctor;
import com.example.appointmentsystem.views.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeLayout;
    Button logoutButton, updateProfileButton;
    EditText fullNameET,
            serviceTypeET,
            designationET,
            agencyNameET,
            addressET,
            apptPhoneET,
            emailET,
            phoneET;
    TextView fullNameTV;
    ProgressBar progressBar;
    FirebaseUser fbUser;
    Doctor doctor;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setupViewComponents();
        progressBar.setVisibility(View.VISIBLE);
        loadUserData();
        logoutButton.setOnClickListener(logoutListener);
        updateProfileButton.setOnClickListener(updateProfileListener);
    }

    private void loadUserData() {
        //TODO: Actually create user instance by check against the uId..
        //..if you're using own rest api;
        dbRef.child("doctors").orderByChild("uId").equalTo(fbUser.getUid())
                .addListenerForSingleValueEvent(doctorProfileListener);
        emailET.setText(fbUser.getEmail());
        phoneET.setText(fbUser.getPhoneNumber());
    }

    ValueEventListener doctorProfileListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!dataSnapshot.exists()) {
                apptPhoneET.setText(fbUser.getPhoneNumber());
                progressBar.setVisibility(View.GONE);
                return;
            }
            doctor = dataSnapshot.getValue(Doctor.class);
            fullNameTV.setText(doctor.name);
            fullNameET.setText(doctor.name);
            serviceTypeET.setText(doctor.serviceType);
            designationET.setText(doctor.designation);
            agencyNameET.setText(doctor.agencyName);
            addressET.setText(doctor.address);
            apptPhoneET.setText(doctor.appointmentPhone);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("loadDocProf:onCancelled", "loadDocProfile:onCancelled", databaseError.toException());
            Toast.makeText(getApplicationContext(), "Data fetching error. Please try again...",
                    Toast.LENGTH_LONG).show();
        }
    };

    public void updateProfile() {
        fullNameET.setError(null);
        serviceTypeET.setError(null);
        designationET.setError(null);
        agencyNameET.setError(null);
        addressET.setError(null);
        apptPhoneET.setError(null);
        Toast.makeText(getApplicationContext(), "Update!", Toast.LENGTH_SHORT).show();
    }

    private void setupViewComponents() {
        swipeLayout = findViewById(R.id.swipe_profile);
        swipeLayout.setOnRefreshListener(swipeListener);
        logoutButton = findViewById(R.id.logoutButton);
        fullNameTV = findViewById(R.id.full_name_field);
        progressBar = findViewById(R.id.profilePB);
        progressBar.setIndeterminate(true);
        updateProfileButton = findViewById(R.id.updateProfileButton);

        fullNameET = findViewById(R.id.full_name_profile);
        serviceTypeET = findViewById(R.id.service_type_profile);
        designationET = findViewById(R.id.designation_profile);
        agencyNameET = findViewById(R.id.agency_name_profile);
        addressET = findViewById(R.id.address_profile);
        addressET.setOnTouchListener(locationListener);
        apptPhoneET = findViewById(R.id.appt_phone_profile);
        emailET = findViewById(R.id.email_profile);
        emailET.setEnabled(false);
        phoneET = findViewById(R.id.phone_profile);
        phoneET.setEnabled(false);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    //Page swipeRefresh listener
    SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            progressBar.setVisibility(View.VISIBLE);
            new DemoAsync().execute();
        }
    };

    //Location icon tap listener
    View.OnTouchListener locationListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (addressET.getRight() - addressET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    Toast.makeText(getApplicationContext(), "Getting location!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return false;
        }
    };

    //Update button click listener
    View.OnClickListener updateProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard();
            if (fullNameET.getText().toString().trim().isEmpty()) {
                fullNameET.setError("Please enter your name");
                fullNameET.requestFocus();
            } else if (serviceTypeET.getText().toString().trim().isEmpty()) {
                serviceTypeET.setError("Enter type of service that you offer");
                serviceTypeET.requestFocus();
            } else if (designationET.getText().toString().trim().isEmpty()) {
                designationET.setError("Please enter your designation");
                designationET.requestFocus();
            } else if (agencyNameET.getText().toString().trim().isEmpty()) {
                agencyNameET.setError("Please enter your company/agency name");
                agencyNameET.requestFocus();
            } else if (addressET.getText().toString().trim().isEmpty()) {
                addressET.setError("Please enter your address");
                addressET.requestFocus();
            } else if (apptPhoneET.getText().toString().trim().isEmpty()) {
                apptPhoneET.setError("Appointment phone number can't be empty");
                apptPhoneET.requestFocus();
            } else {
                updateProfile();
            }
        }
    };

    //Logout button click listener
    View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    };

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public class DemoAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Page refreshed!",
                    Toast.LENGTH_LONG).show();

            // Notify swipeRefreshLayout that the refresh has finished
            swipeLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }

    }

}
