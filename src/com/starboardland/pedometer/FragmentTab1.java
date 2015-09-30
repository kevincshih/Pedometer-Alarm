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

public class FragmentTab1 extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView count;
    private static int steps;
    boolean activityRunning;

    //private PendingIntent pendingIntent;
    //private AlarmManager manager;
    private ProgressBar mProgress;

    private Button settings;
    private ToggleButton toggle;

    AlarmReceiver alarm = new AlarmReceiver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab1, container, false);
        count = (TextView) rootView.findViewById(R.id.count);
        mProgress = (ProgressBar) rootView.findViewById(R.id.circle_progress_bar);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        // Retrieve a PendingIntent that will perform a broadcast

        //Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getActivity(), "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            Log.i("SensorChanged", "event = " + event.toString());
            steps = Math.round(event.values[0]);
            Log.i("SetText", "steps = " + steps);
            count.setText(String.valueOf(steps));
            Log.i("setProgress", "progress = " + steps * 100 / 10000);
            mProgress.setProgress(steps * 100 / 10000);
            Log.i("Done", "done");
        }
    }

    public static int getSteps() {
        return steps;
    }

}