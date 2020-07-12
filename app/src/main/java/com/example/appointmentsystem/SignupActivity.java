package com.example.appointmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

public class SignupActivity extends AppCompatActivity {
    EditText phoneNoET; Button signupButton;
    Spinner spinner; EditText countryCodeET;
    EditText emailSignupET, passwordSignupET; String password;
    ProgressBar signupPB;
    TextView loginRedirectTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initializing content
        phoneNoET = findViewById(R.id.phoneNoET);
        emailSignupET = findViewById(R.id.emailSignupET);
        passwordSignupET = findViewById(R.id.passwordSignupET);

        loginRedirectTV = findViewById(R.id.loginRedirectTV);
        setupLoginHlink();

        signupButton = findViewById(R.id.signupButton);
        signupPB = findViewById(R.id.signupPB);

        countryCodeET = findViewById(R.id.countryCodeET);
        countryCodeET.setFocusable(false);

        spinner = findViewById(R.id.countryCodeSpinner);
        setupSpinner();

        phoneNoET.setText("1555555555");

        //Signup button onClick listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupHandler();
            }
        });


    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames){
            @Override
            public int getCount() {
                return CountryData.countryNames.length-1;
            }
        };
        spinner.setAdapter(adapter_country);
        //spinner.setSelection(CountryData.countryNames.length-1);
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
    }

    private void setupLoginHlink() {
        SpannableString ss = new SpannableString(getString(R.string.login_redirect_hlink));
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(cs, 19, 24, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        loginRedirectTV.setText(ss);
        loginRedirectTV.setMovementMethod(LinkMovementMethod.getInstance());
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
        String email = emailSignupET.getText().toString().trim();
        password = passwordSignupET.getText().toString();

        if (spinner.getSelectedItem().equals("Select a country")){
            makeToast("Please select a country");
            return;
        }
        if (!isValidMobile(number)){
            phoneNoET.setError("Valid number required");
            phoneNoET.requestFocus();
            return;
        }
        if (!isValidMail(email)){
            emailSignupET.setError("Valid email required");
            emailSignupET.requestFocus();
            return;
        }
        if (password.length()<6){
            passwordSignupET.setError("Password must be at least 6 characters in length");
            passwordSignupET.requestFocus();
            return;
        }

        String phoneNumber = "+" + code + number;
        signupPB.setVisibility(View.VISIBLE);
        checkUserAlreadyExist(phoneNumber, email);
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

    private void checkUserAlreadyExist(final String phone, final String email){
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.orderByChild("phone").equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    makeToast("Phone number already registered. Use a different number or login instead");
                    signupPB.setVisibility(View.GONE);
                }
                else {
                    usersRef.orderByChild("email").equalTo(email)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                makeToast("Email already registered. Use a different email account or login instead");
                                signupPB.setVisibility(View.GONE);
                            }
                            else {
                                Intent intent = new Intent(SignupActivity.this, VerifyPhoneActivity.class);
                                intent.putExtra("phoneNumber", phone);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                signupPB.setVisibility(View.GONE);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isValidMail(String email) {

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

}
