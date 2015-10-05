package com.starboardland.pedometer;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Debug", "onCreate MyPreferencesActivity");
        super.onCreate(savedInstanceState);
        Log.i("Debug", "getFragmentManager");
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            Log.i("Debug", "onCreate MyPreferenceFragment");
            super.onCreate(savedInstanceState);
            Log.i("Debug", "addPreferencesFromResource");
            addPreferencesFromResource(R.xml.settings);
        }
    }

}

