package com.starboardland.pedometer;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FragmentTab1 extends Fragment {

    //private PendingIntent pendingIntent;
    //private AlarmManager manager;
    private ProgressBar mProgress;
    private TextView count, alarmTimeText, alarmOnText, goalText;;
    private int steps, goal;
    private Button settings;
    private ToggleButton toggle;


    AlarmReceiver alarm = new AlarmReceiver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab1, container, false);
        Log.i("debug", "rootView");

        count = (TextView) rootView.findViewById(R.id.count);
        Log.i("debug", "count = " + count.toString());
        count.setText("10000");
        Log.i("debug", "count set text");


        mProgress = (ProgressBar) rootView.findViewById(R.id.circle_progress_bar);
        Log.i("debug", "mProgress = " + mProgress.toString());


        alarmTimeText = (TextView) rootView.findViewById(R.id.alarm_time_text);
        alarmOnText = (TextView) rootView.findViewById(R.id.alarm_on_text);
        goalText = (TextView) rootView.findViewById(R.id.goal_text);

        // Retrieve a PendingIntent that will perform a broadcast

        //Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("debug", "onResume FT1");
        update();
    }

    public void update() {
        steps = CounterActivity.getSteps();
        goal = CounterActivity.getGoal();
        Log.i("debug", "SetText:steps = " + steps);
        Log.i("debug", "count = " + count.toString());
        Log.i("debug", "goal = " + goal);

        count.setText(String.valueOf(steps));
        Log.i("debug", "setProgress:progress = " + steps * 100 / goal);
        mProgress.setProgress(steps * 100 / goal);
    }

    public void updateGoal(int goal){
        Log.i("debug", "goal = " + goal);
        Log.i("debug", goalText.toString());
        goalText.setText(Integer.toString(goal));
        Log.i("debug", "goal text set");
    }
    public void updateAlarmTime(String alarmTime){
        Log.i("debug", alarmTime);
        Log.i("debug", alarmTimeText.toString());
        alarmTimeText.setText(alarmTime);
    }
    public void updateAlarmOn(boolean alarmOn){
        Log.i("debug", "alarmOn = " + alarmOn);
        Log.i("debug", alarmOnText.toString());
        if (alarmOn){
            alarmOnText.setText(getResources().getString(R.string.alarm_on));
        }
        else{
            alarmOnText.setText(getResources().getString(R.string.alarm_off));
        }
    }



}