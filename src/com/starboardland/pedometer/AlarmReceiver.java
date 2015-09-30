package com.starboardland.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        int steps = FragmentTab1.getSteps();
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (steps < 10000 * hour / 24){
            String toastString = "Go for a walk, you are falling behind on your goal!";
            Toast.makeText(arg0, toastString, Toast.LENGTH_SHORT).show();
        }
    }

}
