package com.kalyan0510.root.iiticonnect;

/**
 * Created by root on 13/2/16.
 */

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Created by root on 13/2/16.
 */
public class RecursiveReceiver extends WakefulBroadcastReceiver {

    Context contextx;
    @Override
    public void onReceive(Context context, Intent intent) {
        contextx = context;
        //WakeLocker.acquire(context);

        new checkWarning().execute();
        recursiveBroadcast(context);
        //WakeLocker.release();
    }

    void recursiveBroadcast(Context context){
        Context bc = context;
        Context c = context;
        int interval=15;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, interval);
        Intent xtent = new Intent(bc, RecursiveReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(bc,
                        1, xtent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager =
                (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);


        alarmManager.set(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), pendingIntent);
    }

    class checkWarning extends AsyncTask<String,String,String> {
        String result;
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, "isWarnignGenerated");

                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + "isWarnignGenerated", envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
                result=envelope.getResponse().toString();
                if (envelope.bodyIn != null) {
                    SoapPrimitive resultSOAP = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(0);
                    result=resultSOAP.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage();
            }
            if(result.equals("no"))
                return result;
            else{
                //get the Alert details
                try{
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, "getWarning");
                    envelope.bodyOut = request;
                    HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                    try {
                        transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + "getWarning", envelope);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return e.getMessage();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                        return e.getMessage();
                    }
                    result=envelope.getResponse().toString();
                    if (envelope.bodyIn != null) {
                        SoapPrimitive resultSOAP = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(0);
                        result=resultSOAP.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = e.getMessage();
                }
                return result;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s+"\n");
            if(!s.equals("no")){
                WakeLocker.acquire(contextx);
                Toast.makeText(contextx, "Move your ass out of this building", Toast.LENGTH_SHORT).show();
                String[] x = s.split("`");
                setcall(contextx,x[1],x[2]+" ",x[0],4);

                WakeLocker.release();

            }
        }
    }
    public void setcall(Context c,String descr,String type,String user,int id) {
        // Toast.makeText(this,"call", Toast.LENGTH_SHORT).show();

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(c)
                .setSmallIcon(R.mipmap.editw)
                .setContentTitle("Warning from "+user)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        notificationBuilder.setStyle(inboxStyle);
        inboxStyle.setBigContentTitle("from "+user);
        inboxStyle.addLine("type - "+type);
        inboxStyle.setSummaryText(descr);
        notificationBuilder.setStyle(inboxStyle);

        NotificationManager notificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        int NOTIFICATION_ID = 100+id;
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


}