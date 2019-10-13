package com.example.appointmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class SignupActivity extends AppCompatActivity {
    EditText phoneNo; Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initializing content
        phoneNo = findViewById(R.id.phoneNoET);
        signupButton = findViewById(R.id.signupButton);

        //Signup button onClick listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast("Phone number is "+phoneNo.getText().toString());
            }
        });


    }

    //Useful method for making toast
    private void makeToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT)
                .show();
    }

}
