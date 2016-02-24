package com.kalyan0510.root.iiticonnect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by root on 24/2/16.
 */
public class WifiConnectionReceiver  extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intentx) {
        //Toast.makeText(context, "Wifi state changed", Toast.LENGTH_SHORT).show();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 1);

        Intent intent = new Intent(context, RecursiveReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context,
                        1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), pendingIntent);
            Toast.makeText(context,
                    "Broadcast Started",
                    Toast.LENGTH_LONG).show();
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), pendingIntent);
        }
    }
}
