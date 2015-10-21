package com.kcs.pedometer;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("idebug", "onCreate MyPreferencesActivity");
        super.onCreate(savedInstanceState);
        Log.i("idebug", "getFragmentManager");
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    //static final int MENU_OK = 1;
    //static final int MENU_CANCEL = 0;

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem add = menu.add(Menu.NONE, MENU_OK, 0, R.string.ok);
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem qkAdd = menu.add(Menu.NONE, MENU_CANCEL, 1, R.string.cancel);
        qkAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("idebug", "item id = " + item.getItemId());

        switch (item.getItemId()) {
            case R.id.ok:
                setResult(RESULT_OK, new Intent());
                Log.i("idebug", "ok");
                finish();
                Log.i("idebug", "finish");
                return true;
            case R.id.cancel:
                setResult(RESULT_OK);
                Log.i("idebug", "cancel");
                finish();
                Log.i("idebug", "finish");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            Log.i("idebug", "onCreate MyPreferenceFragment");
            super.onCreate(savedInstanceState);
            Log.i("idebug", "addPreferencesFromResource");
            addPreferencesFromResource(R.xml.settings);
        }
    }

}

