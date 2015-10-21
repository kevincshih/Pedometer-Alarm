package com.kcs.pedometer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        int steps = CounterActivity.getSteps();
        Log.i("idebug", "steps = " + steps);

        if (steps < CounterActivity.getGoal()){

            NotificationManager notificationManager = (NotificationManager) arg0.getSystemService(arg0.NOTIFICATION_SERVICE);

            Intent intent = new Intent(arg0, CounterActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
            PendingIntent pIntent = PendingIntent.getActivity(arg0, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
            Notification n  = new Notification.Builder(arg0)
                    .setContentTitle("Pedometer Alarm")
                    .setContentText("Time to go for a walk!")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true).build();
                    //.addAction(R.drawable.ic_launcher, "Call", pIntent)
                    //.addAction(R.drawable.ic_launcher, "More", pIntent)
                    //.addAction(R.drawable.ic_launcher, "And more", pIntent).build();

            notificationManager.notify(0, n);

            Toast.makeText(arg0, "Time to go for a walk!",
                    Toast.LENGTH_LONG).show();

            //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //MediaPlayer mp = MediaPlayer.create(arg0.getApplicationContext(), notification);
            //mp.setVolume(1.0f, 1.0f);
            //mp.start();

            /*try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Ringtone r = RingtoneManager.getRingtone(arg0.getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            // Vibrate the mobile phone
            Vibrator vibrator = (Vibrator) arg0
                    .getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);

            /*Intent newIntent = new Intent(arg0, PopupActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            arg0.startActivity(newIntent);*/



        }

        /*Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (steps < 10000 * hour / 24){
            String toastString = "Go for a walk, you are falling behind on your goal!";
            Toast.makeText(arg0, toastString, Toast.LENGTH_SHORT).show();
        }*/
    }

}
