package com.starboardland.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        int steps = CounterActivity.getSteps();
        Log.i("debug", "steps = " + steps);

        if (steps < CounterActivity.getGoal()){

            Toast.makeText(arg0, "Time to go for a walk!",
                    Toast.LENGTH_LONG).show();
            // Vibrate the mobile phone
            Vibrator vibrator = (Vibrator) arg0
                    .getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);


            /*Intent newIntent = new Intent(arg0, PopupActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            arg0.startActivity(newIntent);*/
        }

        /*Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (steps < 10000 * hour / 24){
            String toastString = "Go for a walk, you are falling behind on your goal!";
            Toast.makeText(arg0, toastString, Toast.LENGTH_SHORT).show();
        }*/
    }

}
