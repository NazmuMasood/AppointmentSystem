package com.example.appointmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

public class SignupActivity extends AppCompatActivity {
    EditText phoneNoET; Button sendCodeButton;
    Spinner spinner; EditText countryCodeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initializing content
        phoneNoET = findViewById(R.id.phoneNoET);

        sendCodeButton = findViewById(R.id.sendCodeButton);

        countryCodeET = findViewById(R.id.countryCodeET);
        countryCodeET.setFocusable(false);

        spinner = findViewById(R.id.countryCodeSpinner);

        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
        spinner.setSelection(13);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String countryCode = CountryData.countryAreaCodes[position];
                countryCodeET.setText("+"+countryCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        phoneNoET.setText("1555555555");

        //Signup button onClick listener
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makeToast("Phone number is "+phoneNo.getText().toString());
                signupHandler();
            }
        });


    }

    //Useful method for making toast
    private void makeToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT)
                .show();
    }

    //Signup handler when signup button is clicked
    private void signupHandler(){
        String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

        String number = phoneNoET.getText().toString().trim();

        if (number.length() != 10){
            phoneNoET.setError("Valid Number is required");
            phoneNoET.requestFocus();
            return;
        }

        String phoneNumber = "+" + code + number;

        Intent intent = new Intent(SignupActivity.this, VerifyPhoneActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(this, SignedupActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
}
