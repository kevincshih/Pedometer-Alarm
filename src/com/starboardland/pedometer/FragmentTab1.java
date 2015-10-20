package com.starboardland.pedometer;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;

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
        Log.i("idebug", "rootView = " + rootView.toString());

        count = (TextView) rootView.findViewById(R.id.count);
        Log.i("idebug", "count = " + count.toString());
        count.setText("10000");
        Log.i("idebug", "count set text");


        mProgress = (ProgressBar) rootView.findViewById(R.id.circle_progress_bar);
        Log.i("idebug", "mProgress = " + mProgress.toString());


        alarmTimeText = (TextView) rootView.findViewById(R.id.alarm_time_text);
        alarmOnText = (TextView) rootView.findViewById(R.id.alarm_on_text);
        goalText = (TextView) rootView.findViewById(R.id.goal_text);

        Log.i("idebug", "alarmTimeText = " + alarmTimeText.toString());


        // Retrieve a PendingIntent that will perform a broadcast

        //Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("idebug", "onResume FT1");
        update();
    }

    public void update() {
        steps = CounterActivity.getSteps();
        goal = CounterActivity.getGoal();
        Log.i("idebug", "SetText:steps = " + steps);
        Log.i("idebug", "count = " + count.toString());
        Log.i("idebug", "goal = " + goal);

        count.setText(String.valueOf(steps));
        Log.i("idebug", "setProgress:progress = " + steps * 100 / goal);
        mProgress.setProgress(steps * 100 / goal);
    }

    public void updateGoal(int goal){
        Log.i("idebug", "goal = " + goal);
        Log.i("idebug", goalText.toString());
        goalText.setText(Integer.toString(goal));
        Log.i("idebug", "goal text set");
    }
    public void updateAlarmTime(String alarmTime, boolean alarmOn){
        Log.i("idebug", alarmTime);
        Log.i("idebug", alarmTimeText.toString());
        if (alarmOn){
            alarmTimeText.setText(alarmTime);
        }
        else {
            alarmTimeText.setText("");
        }
    }
    public void updateAlarmOn(boolean alarmOn){
        Log.i("idebug", "alarmOn = " + alarmOn);
        Log.i("idebug", alarmOnText.toString());
        if (alarmOn){
            alarmOnText.setText(getResources().getString(R.string.alarm_on));
        }
        else{
            alarmOnText.setText("");
        }
    }

}