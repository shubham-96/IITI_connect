package com.kalyan0510.root.iiticonnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class ChangePwdActivity extends AppCompatActivity {
    EditText op,np,rp;
    TextView tdet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        op= (EditText)findViewById(R.id.oldp);
        np = (EditText)findViewById(R.id.newp);
        rp = (EditText)findViewById(R.id.conp);
        tdet = (TextView)findViewById(R.id.cpdet);
        findViewById(R.id.cpcp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String o=op.getText().toString().trim(), p = np.getText().toString().trim(), c =rp.getText().toString().trim();
                if(o.equals("")||p.equals("")||c.equals("")){
                    tdet.setText("Don't leave Empty boxes");
                    return;
                }
                if(o.contains("\'")||o.contains("\"")||o.contains("#")||p.contains("\'")||p.contains("\"")||p.contains("#")
                        ||c.contains("\'")||c.contains("\"")||c.contains("#")){
                    tdet.setText("Don't Enter Invalid characters like \' or \" or ` or #");
                    return;
                }
                SharedPreferences sp = getApplicationContext().getSharedPreferences(Utilities.SharesPresfKeys.key, Context.MODE_PRIVATE);
                int i=sp.getInt(Utilities.SharesPresfKeys.regid,0);
                if(!c.equals(p)){
                    tdet.setText("Passwords did not match");
                    return;
                }
                new changepasswd().execute("" + i, o, p);
            }
        });
    }

    class changepasswd extends AsyncTask<String,String,String>{
        String result;
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, Utilities.connection.method_names.cp);
                request.addProperty("reg_id", Integer.parseInt(params[0]));
                request.addProperty("oldpass", params[1]);
                request.addProperty("newpass", params[2]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + Utilities.connection.method_names.cp, envelope);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tdet.setText(s+"");
        }
    }
}
