package com.starboardland.pedometer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CounterActivity extends Activity implements SensorEventListener {


    ActionBar.Tab Tab1, Tab3;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab3 = new FragmentTab3();

    private SensorManager sensorManager;
    private static int steps, goal;
    private static boolean activityRunning, alarmOn;
    private static String alarmTime;
    private long alarmTimeMs;
    private SharedPreferences preferences;
    private static SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private static final int DIALOG_ALERT = 10;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("debug", "onCreate");
        ActionBar actionBar = getActionBar();
        steps = 0;
        /*
        alarmTime = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.time), getString(R.string.default_time));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(alarmTime));
        calendar.set(Calendar.MINUTE, TimePreference.getMinute(alarmTime));
        calendar.set(Calendar.SECOND, 00);
        alarmTimeMs = calendar.getTimeInMillis();
        goal = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.goal), getString(R.string.default_goal)));
        alarmOn = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.alarm), false);
        Log.i("debug", "alarmOn = " + alarmOn);
        if (alarmOn) {
            Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTimeMs, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        */

        // Hide Actionbar Icon
        //actionBar.setDisplayShowHomeEnabled(false);

        // Hide Actionbar Title
        //actionBar.setDisplayShowTitleEnabled(false);

        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        Tab1 = actionBar.newTab().setText("Pedometer");
        //Tab3 = actionBar.newTab().setText("History");

        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(fragmentTab1));
        //Tab3.setTabListener(new TabListener(fragmentTab3));

        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        //actionBar.addTab(Tab2);
        //actionBar.addTab(Tab3);
        Log.i("debug", "actionBar done");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);



        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                FragmentTab1 ft1 = (FragmentTab1) fragmentTab1;
                Log.i("debug", "onSharedPreferenceChanged key = " + key);
                if (key.equals(getResources().getString(R.string.time))) {
                    alarmOn = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.alarm), false);
                    alarmTime = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.time), getString(R.string.default_time));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(alarmTime));
                    calendar.set(Calendar.MINUTE, TimePreference.getMinute(alarmTime));
                    calendar.set(Calendar.SECOND, 00);
                    alarmTimeMs = calendar.getTimeInMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                    ft1.updateAlarmTime(sdf.format(calendar.getTime()), alarmOn);
                    Log.i("debug", "alarmTimeMs = " + alarmTimeMs);

                } else if (key.equals(getResources().getString(R.string.goal))) {
                    goal = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.goal), getString(R.string.default_goal)));
                    if (goal < 1){
                        Toast.makeText(getApplicationContext(), "Goal must be greater than 0.", Toast.LENGTH_LONG).show();
                        goal = 1;
                    }
                    ft1.updateGoal(goal);
                    Log.i("debug", "goal = " + goal);

                } else if (key.equals(getResources().getString(R.string.alarm))) {
                    alarmOn = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.alarm), false);
                    ft1.updateAlarmOn(alarmOn);
                    Log.i("debug", "alarmOn = " + alarmOn);
                    if (alarmOn) {
                        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                getApplicationContext(), 234324243, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTimeMs, AlarmManager.INTERVAL_DAY, pendingIntent);
                        /*Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, myIntent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTimeMs, AlarmManager.INTERVAL_DAY, pendingIntent);*/
                    }
                }
            }
        };

        preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);




    }
    /*




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

    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("debug", "onResume");
        activityRunning = true;

        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, getResources().getString(R.string.time));
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, getResources().getString(R.string.goal));
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, getResources().getString(R.string.alarm));

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
        //sensorManager.unregisterListener(this);
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

    //static final int CHANGE_SETTINGS_REQUEST = 1;
    public void openSettings(){
        Intent i = new Intent(this, MyPreferencesActivity.class);
        Log.i("Debug", "openSettings");
        //startActivityForResult(i, CHANGE_SETTINGS_REQUEST);
        startActivity(i);
    }



    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CHANGE_SETTINGS_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                alarmOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.alarm), true);
                alarmTime = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.time), getString(R.string.default_time));
                goal = PreferenceManager.getDefaultSharedPreferences(this).getInt(getString(R.string.goal), Integer.parseInt(getString(R.string.default_goal)));
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(alarmTime));
                calendar.set(Calendar.MINUTE, TimePreference.getMinute(alarmTime));
                calendar.set(Calendar.SECOND, 00);
                Log.i("debug", "alarmOn = " + alarmOn);
                Log.i("debug", "alarmTime = " + alarmTime);
                Log.i("debug", "goal = " + goal);
                if (alarmOn){
                    Intent myIntent = new Intent(this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                }
                // Do something with the contact here (bigger example below)
            }
        }
    }*/

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
    public static int getGoal() {
        return goal;
    }
    public static String getTime() {
        return alarmTime;
    }
    public static boolean getAlarm() {
        return alarmOn;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ALERT:
                // Create out AlterDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This will end the activity");
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", new OkOnClickListener());
                builder.setNegativeButton("Cancel", new CancelOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onCreateDialog(id);
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            //Toast.makeText(getApplicationContext(), "Activity will continue", Toast.LENGTH_LONG).show();
        }
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            //AlertExampleActivity.this.finish();
        }
    }
}
