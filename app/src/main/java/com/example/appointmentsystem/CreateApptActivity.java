package com.example.appointmentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CreateApptActivity extends Activity implements View.OnTouchListener{

    LinearLayout root_layout;
    Button slidingButton; CardView cardView;
    ScrollView scrollView; LinearLayout scrollViewLinearLayout; TextView infoView;
    LinearLayout bottomLinearLayout; Button createApptButton, closeApptDialogButton;
    Window window;
    EditText patientNameET, patientPhoneET,
            apptDateET, startTimeET, endTimeET,
            tokenET, reasonET;
    int year,day,month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        try
//        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setFinishOnTouchOutside(false);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_appt);
            initializeContent();
            setupEditTextFont();

            System.out.println("PopupDialogue started");
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            if (Build.VERSION.SDK_INT >= 27) {
                setShowWhenLocked(true);
            }
            else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                );
            }

            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setGravity(Gravity.CENTER);
            lp.copyFrom(window.getAttributes());
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            String info = "Friend is calling you"
                    +"\nCaller type: Customer" +
                    "\nProducts List:\nLamp\nTable-Tennis Bat" +
                    "\nLa\nLa\nLa\nLa\nLa\nLa";
            //infoView.setText(info);

            createApptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkETFields()){
                        makeToast("Success");
                        CreateApptActivity.this.finish();
                    }
                }
            });

            closeApptDialogButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CreateApptActivity.this.finish();
                    //System.exit(0);
                }
            });
//        }
//        catch (Exception e)
//        {
//            Log.d("Exception", e.toString());
//            e.printStackTrace();
//        }

        defineTouchListeners();

        MyEditTextDatePicker DatePicker = new MyEditTextDatePicker(this, R.id.apptDateET);
        setDefaultDateOnET();
        TimePickerUniversal startTimePicker = new TimePickerUniversal(
                startTimeET,true);
        TimePickerUniversal endTimePicker = new TimePickerUniversal(
                endTimeET,true);
    }

    private boolean checkETFields() {
        String errorMsg = "This field cannot be empty";
        if(TextUtils.isEmpty(patientNameET.getText().toString())) {
            patientNameET.setError(errorMsg);
            return false;
        }
        else if(TextUtils.isEmpty(patientNameET.getText().toString())) {
            patientNameET.setError(errorMsg);
            return false;
        }
        else if(TextUtils.isEmpty(patientPhoneET.getText().toString())) {
            patientPhoneET.setError(errorMsg);
            return false;
        }
        else if(TextUtils.isEmpty(apptDateET.getText().toString())) {
            apptDateET.setError(errorMsg);
            return false;
        }
        else if(TextUtils.isEmpty(startTimeET.getText().toString())) {
            startTimeET.setError(errorMsg);
            return false;
        }
        else if(TextUtils.isEmpty(endTimeET.getText().toString())) {
            endTimeET.setError(errorMsg);
            return false;
        }
        else {return true;}
    }

    private void initializeContent() {
        root_layout = findViewById(R.id.root_layout);
        cardView = findViewById(R.id.cardView);
        //infoView = findViewById(R.id.kiArKorboTextView);
        scrollView = findViewById(R.id.scroll_view);
        //scrollViewLinearLayout = findViewById(R.id.scrollView_linear_layout);
        slidingButton = findViewById(R.id.sliding_button);
        //bottomLinearLayout = findViewById(R.id.bottom_linear_layout);
        createApptButton = findViewById(R.id.createApptButton);
        closeApptDialogButton = findViewById(R.id.closeApptDialogButton);

        patientNameET = findViewById(R.id.patientNameET);
        patientPhoneET = findViewById(R.id.patientPhoneET);
        apptDateET = findViewById(R.id.apptDateET);
        startTimeET = findViewById(R.id.startTimeET);
        endTimeET = findViewById(R.id.endTimeET);
        tokenET = findViewById(R.id.tokenET);
        reasonET = findViewById(R.id.reasonET);
    }

    private void setupEditTextFont() {
        Typeface tf = ResourcesCompat.getFont(this, R.font.open_sans_light);
        patientNameET.setTypeface(tf);
        patientPhoneET.setTypeface(tf);
        apptDateET.setTypeface(tf);
        startTimeET.setTypeface(tf);
        endTimeET.setTypeface(tf);
        tokenET.setTypeface(tf);
        reasonET.setTypeface(tf);
    }

    private void defineTouchListeners() {
        root_layout.setOnTouchListener(touchListener);
        slidingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchListener.onTouch(v, event);
                return false;
            }
        });
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchListener.onTouch(v, event);
                return false;
            }
        });
    }

    private void setDefaultDateOnET() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        apptDateET.setText(new StringBuilder()
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));
    }

    private final View.OnTouchListener touchListener = new View.OnTouchListener() {

        private int initY , yDelta;

        public boolean onTouch(View view, MotionEvent event) {
            //------G A P------//
            /*Fling detected*/
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());

            final int y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    initY = lp.y;
                    yDelta = y - initY;
                    break;

                case MotionEvent.ACTION_UP:
                    break;

                case MotionEvent.ACTION_MOVE:
                    lp.y = y - yDelta;
                    window.setAttributes(lp);
                    break;
            }
            //----G A P----//

            return true;
        }


    };

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT) .show();}

    /*public void onBackPressed()
    {
        PopupDialogueActivity.this.finish();
        super.onBackPressed();
    }*/

}
