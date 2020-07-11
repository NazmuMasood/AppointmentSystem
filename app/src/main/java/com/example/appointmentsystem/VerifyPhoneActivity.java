package com.example.appointmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    EditText verificationCode; Button signupButton; ProgressBar progressBar;
    String phoneNumber, email, password;

    //FireBase phone auth variables
    String verificationId;
    FirebaseAuth mAuth; FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        verificationCode = findViewById(R.id.verificationCodeET);
        signupButton = findViewById(R.id.enterCodeButton);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        if (phoneNumber.equals("+8801555555555")){
            testSendVerificationCode();
        }
        else {
            sendVerificationCode(phoneNumber);
        }

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verificationCode.getText().toString().trim();
                if (code.isEmpty() || code.length()<6 ){
                    verificationCode.setError("Please enter code");
                    verificationCode.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                signInWithCredential(credential);
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //Linking phone auth with email & password auth
                            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                            mAuth.getCurrentUser().linkWithCredential(credential)
                                    .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                //makeToast("Email linked");
                                                //FirebaseUser user = task.getResult().getUser();

                                                FirebaseUser user = mAuth.getCurrentUser();

                                                //Saving user into firebase database 'users' node
                                                User newUser = new User(user.getPhoneNumber(), user.getEmail());
                                                firebaseDatabase.getReference("users")
                                                        .child(user.getUid())
                                                        .setValue(newUser)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                makeToast("Registration successful");
                                                                //Takes user into home screen
                                                                Intent intent = new Intent(VerifyPhoneActivity.this, SignedupActivity.class);
                                                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                makeToast("Error: "+e.getMessage());
                                                            }
                                                        });

                                            } else {
                                                makeToast("Authentication failed.");
                                                //updateUI(null);
                                            }

                                            // ...
                                        }
                                    });

                        }
                        else {
                            makeToast(task.getException().getMessage());

                            //verification unsuccessful.. display an error message

                            /*String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }*/

                        }
                    }
                });
    }

    private void sendVerificationCode(String number){

        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private void testSendVerificationCode(){
        progressBar.setVisibility(View.VISIBLE);

        final String phoneNum = "+8801555555555";
        final String testVerificationCode = "123456";

        // Whenever verification is triggered with the whitelisted number,
        // provided it is not set for auto-retrieval, onCodeSent will be triggered.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum, 30L /*timeout*/, TimeUnit.SECONDS,
                this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(String verificationID,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Save the verification id somewhere
                        verificationId = verificationID;

                        makeToast("A verification code has been sent to "+phoneNum);

                        verificationCode.setText(testVerificationCode);
                        progressBar.setVisibility(View.GONE);

                        // The corresponding whitelisted code above should be used to complete sign-in.
                        //MainActivity.this.enableUserManuallyInputCode();
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        makeToast(e.getMessage());
                    }

                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

            makeToast("A verification code has been sent to "+phoneNumber);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verificationCode.setText(code);
                signInWithCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            makeToast(e.getMessage());
        }
    };


    //Useful method for making toast
    private void makeToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT)
                .show();
    }
}
