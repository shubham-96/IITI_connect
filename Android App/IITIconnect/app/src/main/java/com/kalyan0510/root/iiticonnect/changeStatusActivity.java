package com.kalyan0510.root.iiticonnect;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class changeStatusActivity extends AppCompatActivity {
    EditText stEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!Utilities.isOncampusWifi(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "not connected to CAMPUS WIFI", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);
        stEdt = ((EditText)findViewById(R.id.statusEdtxt));
        stEdt.setText(getIntent().getStringExtra("status"));
        findViewById(R.id.setstatusBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new changeStatus().execute(stEdt.getText().toString().trim());
            }
        });

    }






    ////////////////////////////CHANGE STATUS////////////////////////////////////////////////////////////////
    class changeStatus extends AsyncTask<String, String,String> {
        String result;
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE,"changeStatus");
                int x = getSharedPreferences(Utilities.SharesPresfKeys.key, Context.MODE_PRIVATE).getInt(Utilities.SharesPresfKeys.regid,0);
                request.addProperty("reg_id", x);
                request.addProperty("status", params[0]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX +"changeStatus", envelope);
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


        @Override
        protected void onPostExecute(String  s) {
            if(s.equals("error")){
                Toast.makeText(getApplicationContext(), "Error in changing status", Toast.LENGTH_SHORT).show();
                return;
            }
            finish();

        }
    }

}
