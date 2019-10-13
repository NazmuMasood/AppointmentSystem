package com.example.appointmentsystem;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PopupDialogueActivity extends Activity implements View.OnTouchListener{

    LinearLayout root_layout;
    Button slidingButton;
    ScrollView scrollView; LinearLayout scrollViewLinearLayout; TextView infoView;
    LinearLayout bottomLinearLayout; Button bottomButton1, bottomButton2, bottomButton3;
    Window window;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setFinishOnTouchOutside(false);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_popup_dialogue);
            initializeContent();

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
            infoView.setText(info);

            bottomButton3.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PopupDialogueActivity.this.finish();
                    //System.exit(0);
                }
            });
        }
        catch (Exception e)
        {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }

        root_layout.setOnTouchListener(touchListener);
        /*scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
        slidingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchListener.onTouch(v, event);
                return false;
            }
        });
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

    private void makeToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT)
                .show();}

    private void initializeContent()
    {
        root_layout = findViewById(R.id.root_layout);
        infoView = findViewById(R.id.kiArKorboTextView);
        scrollView = findViewById(R.id.scroll_view);
        //scrollViewLinearLayout = findViewById(R.id.scrollView_linear_layout);
        slidingButton = findViewById(R.id.sliding_button);
        //bottomLinearLayout = findViewById(R.id.bottom_linear_layout);
        //bottomButton1 = findViewById(R.id.bottomButton1);
        //bottomButton2 = findViewById(R.id.bottomButton2);
        bottomButton3 = findViewById(R.id.bottomButton3);
    }

    /*public void onBackPressed()
    {
        PopupDialogueActivity.this.finish();
        super.onBackPressed();
    }*/
}
