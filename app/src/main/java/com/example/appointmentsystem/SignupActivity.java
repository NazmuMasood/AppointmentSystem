package com.example.appointmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

public class SignupActivity extends AppCompatActivity {
    EditText phoneNoET; Button signupButton; Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initializing content
        phoneNoET = findViewById(R.id.phoneNoET);
        signupButton = findViewById(R.id.signupButton);
        spinner = findViewById(R.id.countryCodeSpinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
        spinner.setSelection(13);

        phoneNoET.setText("1521328932");

        //Signup button onClick listener
        signupButton.setOnClickListener(new View.OnClickListener() {
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

        if (number.isEmpty() || number.length() < 10){
            phoneNoET.setError("Valid Number is required");
            phoneNoET.requestFocus();
            return;
        }

        String phoneNumber = "+" + code + number;

        Intent intent = new Intent(SignupActivity.this, VerifyPhoneActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }
}
