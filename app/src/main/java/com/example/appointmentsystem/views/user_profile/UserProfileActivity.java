package com.example.appointmentsystem.views.user_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

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
    TextView fullNameTV, timeScheduleTV;
    ProgressBar progressBar;
    FirebaseUser fbUser;
    Doctor doctor;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setupViewComponents();
        loadUserData();
    }

    private void loadUserData() {
        progressBar.setVisibility(View.VISIBLE);
        //TODOO: Actually create user instance by check against the uId..
        //..if you're using own rest api;
        dbRef.child("users-profile").orderByChild("uId").equalTo(fbUser.getUid())
                .addListenerForSingleValueEvent(doctorProfileListener);
        emailET.setText(fbUser.getEmail());
        phoneET.setText(fbUser.getPhoneNumber());
    }

    ValueEventListener doctorProfileListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!dataSnapshot.exists()) {
                apptPhoneET.setText(fbUser.getPhoneNumber());
                timeScheduleTV.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                return;
            }
            Log.d("dataSnapshot", dataSnapshot.getValue().toString());
            //Doctor doctor = dataSnapshot.getValue(Doctor.class);

            for (DataSnapshot docSnapshot: dataSnapshot.getChildren()) {
                Doctor doctor = new Doctor((String) docSnapshot.child("name").getValue(),
                        (String) docSnapshot.child("serviceType").getValue(),
                        (String) docSnapshot.child("designation").getValue(),
                        (String) docSnapshot.child("agencyName").getValue(),
                        (String) docSnapshot.child("address").getValue(),
                        (String) docSnapshot.child("appointmentPhone").getValue(),
                        (String) docSnapshot.child("uId").getValue());
                Log.d("docName", doctor.name);
                fullNameTV.setText(doctor.name);
                fullNameET.setText(doctor.name);
                serviceTypeET.setText(doctor.serviceType);
                designationET.setText(doctor.designation);
                agencyNameET.setText(doctor.agencyName);
                addressET.setText(doctor.address);
                apptPhoneET.setText(doctor.appointmentPhone);
                progressBar.setVisibility(View.GONE);
            }
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

        String name, serviceType, designation, agencyName, address, apptPhone;
        name = fullNameET.getText().toString().trim();
        serviceType = serviceTypeET.getText().toString().trim();
        designation = designationET.getText().toString().trim();
        agencyName = agencyNameET.getText().toString().trim();
        address = addressET.getText().toString().trim();
        apptPhone = apptPhoneET.getText().toString().trim();

        Doctor newDoctorProfile = new Doctor(name, serviceType, designation, agencyName, address, apptPhone, fbUser.getUid());
        //Map<String, Object> DoctorProfileValues = newDoctorProfile.toMap();
        progressBar.setVisibility(View.VISIBLE);
        String key = dbRef.child("users-profile").push().getKey();
        dbRef.child("users-profile").child(key).setValue(newDoctorProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Update failed. Please try again...", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setupViewComponents() {
        swipeLayout = findViewById(R.id.swipe_profile);
        swipeLayout.setOnRefreshListener(swipeListener);
        logoutButton = findViewById(R.id.logoutButton);
        fullNameTV = findViewById(R.id.full_name_field);
        progressBar = findViewById(R.id.profilePB);
        progressBar.setIndeterminate(true);
        updateProfileButton = findViewById(R.id.updateProfileButton);
        logoutButton.setOnClickListener(logoutListener);
        updateProfileButton.setOnClickListener(updateProfileListener);

        timeScheduleTV = findViewById(R.id.time_schedule_profile);
        timeScheduleTV.setPaintFlags(timeScheduleTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        timeScheduleTV.setOnClickListener(timeScheduleListener);

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

    View.OnClickListener timeScheduleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), "Clicked!",
                    Toast.LENGTH_LONG).show();
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
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
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
