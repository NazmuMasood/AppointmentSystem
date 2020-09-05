package com.example.appointmentsystem.views.auth;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentsystem.R;
import com.example.appointmentsystem.views.user_profile.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText emailLoginET, passwordLoginET;
    Button loginButton; ProgressBar loginPB;
    TextView signupRedirectTV, passwordResetTV;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLoginET = findViewById(R.id.emailLoginET);
        passwordLoginET = findViewById(R.id.passwordLoginET);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectTV = findViewById(R.id.signupRedirectTV);
        passwordResetTV = findViewById(R.id.passwordResetTV);
        loginPB = findViewById(R.id.loginPB);
        mAuth = FirebaseAuth.getInstance();
        setupPasswordResetHlink();
        setupSignupHlink();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginHandler();
            }
        });
    }

    private void loginHandler() {
        String email = emailLoginET.getText().toString().trim();
        String password = passwordLoginET.getText().toString();

        if (!isValidMail(email)){
            emailLoginET.setError("Valid email required");
            emailLoginET.requestFocus();
            return;
        }
        if (password.length()<6){
            passwordLoginET.setError("Password must be at least 6 characters in length");
            passwordLoginET.requestFocus();
            return;
        }

        loginPB.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            makeToast("Login Success");
                            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                            finish();
                        }
                        else {
                            makeToast("Invalid email or password");
                        }
                        loginPB.setVisibility(View.GONE);
                    }
                });
    }

    private boolean isValidMail(String email) {

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    private void makeToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void setupSignupHlink() {
        SpannableString ss = new SpannableString(getString(R.string.signup_redirect_hlink));
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(cs, 11, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        signupRedirectTV.setText(ss);
        signupRedirectTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupPasswordResetHlink() {
        SpannableString ss = new SpannableString(getString(R.string.password_reset_hlink));
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String email = emailLoginET.getText().toString().trim();
                if (!isValidMail(email)){
                    emailLoginET.setError("Please enter a valid email");
                    emailLoginET.requestFocus();
                    return;
                }
                loginPB.setVisibility(View.VISIBLE);
                checkEmailExist(email);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(cs, 0, 16, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        passwordResetTV.setText(ss);
        passwordResetTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void checkEmailExist(final String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            makeToast("Email address not registered");
                            loginPB.setVisibility(View.GONE);
                        }
                        else {
                            mAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                makeToast("Password reset link sent to your email");
                                            }
                                            else {makeToast("Error: "+task.getException().getMessage());}
                                            loginPB.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
    }

}