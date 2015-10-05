package com.starboardland.pedometer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.preference.PreferenceScreen;


public class CounterActivity extends Activity implements SensorEventListener {


    ActionBar.Tab Tab1, Tab3;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab3 = new FragmentTab3();

    private SensorManager sensorManager;
    private static int steps;
    boolean activityRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("debug", "onCreate");
        ActionBar actionBar = getActionBar();

        // Hide Actionbar Icon
        //actionBar.setDisplayShowHomeEnabled(false);

        // Hide Actionbar Title
        //actionBar.setDisplayShowTitleEnabled(false);

        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        Tab1 = actionBar.newTab().setText("Pedometer");
        Tab3 = actionBar.newTab().setText("History");

        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(fragmentTab1));
        Tab3.setTabListener(new TabListener(fragmentTab3));

        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        //actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);
        Log.i("debug", "actionBar done");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

/*
        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                Context context = v.getContext();
                Intent intent = new Intent(context, AlarmSettings.class);
                context.startActivity(intent);
            }
        });

        toggle = (ToggleButton) findViewById(R.id.button);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startAlarm();
                } else {
                    cancelAlarm();
                }
            }
        });
        */
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.i("debug", "onResume");
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Log.i("debug", "countSensor");
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
        Log.i("debug", "onResume done");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("debug", "onPause done");
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this); 
    }

    /*
    public void startAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 60000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void openSettings(){
        Intent i = new Intent(this, MyPreferencesActivity.class);
        Log.i("Debug", "openSettings");
        startActivity(i);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            Log.i("debug", "SensorChanged:event = " + event.toString());
            steps = Math.round(event.values[0]);
            FragmentTab1 ft1 = (FragmentTab1) fragmentTab1;
            ft1.update();
            Log.i("debug", "done");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public static int getSteps() {
        return steps;
    }

}
