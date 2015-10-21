package com.kcs.pedometer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Kevin on 10/10/2015.
 */
public class PopupActivity extends Activity {

@Override
public void onCreate(Bundle savedInstanceState) {
    Log.i("idebug", "onCreate popup");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.popup);
}

}
