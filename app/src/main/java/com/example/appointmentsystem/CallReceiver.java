package com.example.appointmentsystem;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.os.Handler;
import java.util.Date;

public class CallReceiver extends PhoneCallReceiver
{
    Context context;

    @Override
    protected void onIncomingCallStarted(final Context ctx, final String number, Date start)
    {
        Toast.makeText(ctx,"Nazmu Incoming Call"+ number,Toast.LENGTH_LONG).show();

        context =   ctx;

        final Intent intent = new Intent(context, PopupDialogueActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("phone_no",number);
        intent.putExtra("turnScreenOff", "no");

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    System.out.println("Starting AppointmentSystem");
                    context.startActivity(intent);
                }
                catch (Exception e){e.printStackTrace();}
            }
        },1500);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        Toast.makeText(ctx,"Bye Bye"+ number,Toast.LENGTH_LONG).show();
        System.exit(0);
    }
}
