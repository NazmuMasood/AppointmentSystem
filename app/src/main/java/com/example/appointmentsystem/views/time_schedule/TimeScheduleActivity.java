package com.example.appointmentsystem.views.time_schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentsystem.R;
import com.example.appointmentsystem.models.Doctor;
import com.example.appointmentsystem.models.TimeSlot;
import com.example.appointmentsystem.views.auth.LoginActivity;
import com.example.appointmentsystem.views.user_profile.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TimeScheduleActivity extends AppCompatActivity {

    Button logoutButton;
    TextView profileTV;
    SwipeRefreshLayout swipeLayout;
    ProgressBar progressBar;
    FirebaseUser fbUser;
    private DatabaseReference dbRef;

    ArrayList<TimeSlot> monTimeSlots;
    ArrayList<TimeSlot> tueTimeSlots;
    ArrayList<TimeSlot> wedTimeSlots;
    ArrayList<TimeSlot> thuTimeSlots;
    ArrayList<TimeSlot> friTimeSlots;
    ArrayList<TimeSlot> satTimeSlots;
    ArrayList<TimeSlot> sunTimeSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_schedule);

        setupViewComponents();
        progressBar.setVisibility(View.GONE);
    }

    private void setupViewComponents() {
        swipeLayout = findViewById(R.id.swipe_time_schdl);
        swipeLayout.setOnRefreshListener(swipeListener);
        logoutButton = findViewById(R.id.logoutButtonTimeSchdl);
        progressBar = findViewById(R.id.timeSchdlPB);
        progressBar.setIndeterminate(true);
        logoutButton.setOnClickListener(logoutListener);

        profileTV = findViewById(R.id.view_profile_hlink);
        profileTV.setPaintFlags(profileTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        profileTV.setOnClickListener(viewProfileListener);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Monday recycler view
        RecyclerView monRV = findViewById(R.id.monRecyclerView);
        monTimeSlots = TimeSlot.createTimeSlotList(2);
        Log.d("mon", monTimeSlots.get(0).getStartTime());
        TimeScheduleAdapter monAdapter = new TimeScheduleAdapter(TimeScheduleActivity.this, "mon", monTimeSlots);
        monRV.setAdapter(monAdapter);
        monRV.setLayoutManager(new LinearLayoutManager(this));

        // Tuesday recycler view
        RecyclerView tueRV = findViewById(R.id.tueRecyclerView);
        tueTimeSlots = TimeSlot.createTimeSlotList(2);
        Log.d("tue", tueTimeSlots.get(0).getStartTime());
        TimeScheduleAdapter tueAdapter = new TimeScheduleAdapter(TimeScheduleActivity.this, "tue", tueTimeSlots);
        tueRV.setAdapter(tueAdapter);
        tueRV.setLayoutManager(new LinearLayoutManager(this));

        // wednesday recycler view
        RecyclerView wedRV = findViewById(R.id.wedRecyclerView);
        wedTimeSlots = TimeSlot.createTimeSlotList(1);
        Log.d("wed", wedTimeSlots.get(0).getStartTime());
        TimeScheduleAdapter wedAdapter = new TimeScheduleAdapter(TimeScheduleActivity.this, "wed", wedTimeSlots);
        wedRV.setAdapter(wedAdapter);
        wedRV.setLayoutManager(new LinearLayoutManager(this));

        // thursday recycler view
        RecyclerView thuRV = findViewById(R.id.thuRecyclerView);
        thuTimeSlots = TimeSlot.createTimeSlotList(2);
        Log.d("thu", thuTimeSlots.get(0).getStartTime());
        TimeScheduleAdapter thuAdapter = new TimeScheduleAdapter(TimeScheduleActivity.this, "thu", thuTimeSlots);
        thuRV.setAdapter(thuAdapter);
        thuRV.setLayoutManager(new LinearLayoutManager(this));

        // friday recycler view
        RecyclerView friRV = findViewById(R.id.friRecyclerView);
        friTimeSlots = TimeSlot.createTimeSlotList(1);
        Log.d("fri", friTimeSlots.get(0).getStartTime());
        TimeScheduleAdapter friAdapter = new TimeScheduleAdapter(TimeScheduleActivity.this, "fri", friTimeSlots);
        friRV.setAdapter(friAdapter);
        friRV.setLayoutManager(new LinearLayoutManager(this));

        // saturday recycler view
        RecyclerView satRV = findViewById(R.id.satRecyclerView);
        satTimeSlots = TimeSlot.createTimeSlotList(2);
        Log.d("sat", satTimeSlots.get(0).getStartTime());
        TimeScheduleAdapter satAdapter = new TimeScheduleAdapter(TimeScheduleActivity.this, "sat", satTimeSlots);
        satRV.setAdapter(satAdapter);
        satRV.setLayoutManager(new LinearLayoutManager(this));

        // sunday recycler view
        RecyclerView sunRV = findViewById(R.id.sunRecyclerView);
        sunTimeSlots = TimeSlot.createTimeSlotList(1);
        Log.d("sun", sunTimeSlots.get(0).getStartTime());
        TimeScheduleAdapter sunAdapter = new TimeScheduleAdapter(TimeScheduleActivity.this, "sun", sunTimeSlots);
        sunRV.setAdapter(sunAdapter);
        sunRV.setLayoutManager(new LinearLayoutManager(this));
    }

    //View_Profile Activity Link Click Listener
    View.OnClickListener viewProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(TimeScheduleActivity.this, UserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    };

    //Logout button click listener
    View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(TimeScheduleActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    };

    //Page swipeRefresh listener
    SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            progressBar.setVisibility(View.VISIBLE);
            new DemoAsync().execute();
        }
    };

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