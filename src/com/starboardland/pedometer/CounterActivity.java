package com.starboardland.pedometer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
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
import java.util.concurrent.TimeUnit;

import android.app.FragmentTransaction;


public class CounterActivity extends Activity implements SensorEventListener {


    ActionBar.Tab Tab1, Tab3;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab3 = new FragmentTab3();

    private SensorManager sensorManager;
    private static int steps = 0;
    private static int goal = 0;
    private static boolean activityRunning, alarmOn;
    private static String alarmTime;
    private long alarmTimeMs;
    private SharedPreferences preferences;
    private static SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private static final int DIALOG_ALERT = 10;

    private static int day = 0;
    private static int stepsBeforeToday = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("idebug", "onCreate");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //FragmentTab1 ft1 = (FragmentTab1) fragmentTab1;
        fragmentTransaction.add(R.id.fragment_container, fragmentTab1);
        fragmentTransaction.commit();


        //ActionBar actionBar = getActionBar();
        //steps = 0;

        // Hide Actionbar Icon
        //actionBar.setDisplayShowHomeEnabled(false);

        // Hide Actionbar Title
        //actionBar.setDisplayShowTitleEnabled(false);

        // Create Actionbar Tabs
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        //Tab1 = actionBar.newTab().setText("");
        //Tab3 = actionBar.newTab().setText("History");

        // Set Tab Listeners
        //Tab1.setTabListener(new TabListener(fragmentTab1));
        //Tab3.setTabListener(new TabListener(fragmentTab3));

        // Add tabs to actionbar
        //actionBar.addTab(Tab1);
        //actionBar.addTab(Tab2);
        //actionBar.addTab(Tab3);
        //Log.i("idebug", "actionBar done");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                FragmentTab1 ft1 = (FragmentTab1) fragmentTab1;
                Log.i("idebug", "onSharedPreferenceChanged key = " + key);
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
                    Log.i("idebug", "alarmTimeMs = " + alarmTimeMs);

                } else if (key.equals(getResources().getString(R.string.goal))) {
                    goal = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.goal), getString(R.string.default_goal)));
                    if (goal < 1){
                        Toast.makeText(getApplicationContext(), "Goal must be greater than 0.", Toast.LENGTH_LONG).show();
                        goal = 1;
                    }
                    ft1.updateGoal(goal);
                    Log.i("idebug", "goal = " + goal);

                } else if (key.equals(getResources().getString(R.string.alarm))) {
                    alarmOn = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.alarm), false);
                    ft1.updateAlarmOn(alarmOn);
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            getApplicationContext(), 234324243, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    Log.i("idebug", "alarmOn = " + alarmOn);
                    if (alarmOn) {
                        long millis = (alarmTimeMs - System.currentTimeMillis());
                        if (millis < 0){
                            millis = millis + TimeUnit.DAYS.toMillis(1);
                            alarmTimeMs = alarmTimeMs + TimeUnit.DAYS.toMillis(1);
                        }
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTimeMs, AlarmManager.INTERVAL_DAY, pendingIntent);
                        Log.i("idebug", "alarmTimeMs = " + millis);

                        String s = String.format("%d hours and %d minutes",
                                TimeUnit.MILLISECONDS.toHours(millis),
                                TimeUnit.MILLISECONDS.toMinutes(millis) -
                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
                        );
                        Toast.makeText(getApplicationContext(), "Alarm set for " + s + " from now.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        alarmManager.cancel(pendingIntent);
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
        Log.i("idebug", "onResume");
        activityRunning = true;

        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, getResources().getString(R.string.time));
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, getResources().getString(R.string.goal));
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, getResources().getString(R.string.alarm));

        if (sensorManager != null){

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Log.i("idebug", "countSensor");
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
        }
        else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }

        Log.i("idebug", "onResume done");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("idebug", "onPause done");
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
        Log.i("idebug", "openSettings");
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
                Log.i("idebug", "alarmOn = " + alarmOn);
                Log.i("idebug", "alarmTime = " + alarmTime);
                Log.i("idebug", "goal = " + goal);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        steps = Math.round(event.values[0]);
        Log.i("idebug", "steps = " + steps + " day = " + day + " today = " + today);

        if (day != today){
            day = today;
            stepsBeforeToday = steps;
        }

        if (activityRunning) {
        Log.i("idebug", "SensorChanged:event = " + event.toString());
        FragmentTab1 ft1 = (FragmentTab1) fragmentTab1;
        ft1.update();
        Log.i("idebug", "done");
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public static int getSteps() {
        return steps - stepsBeforeToday;
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
