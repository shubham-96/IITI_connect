package com.kalyan0510.root.iiticonnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    EditText un,pa;
    TextView det;
    Button fp,submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        un = (EditText)findViewById(R.id.loginuname);
        pa = (EditText)findViewById(R.id.loginpwd);
        det = (TextView)findViewById(R.id.logindet);
        fp = (Button)findViewById(R.id.forgotpa);
        submit = (Button)findViewById(R.id.loginbutton);
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(un.getText().toString().trim().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter Valid username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(un.getText().toString().trim().contains("\'")||un.getText().toString().trim().contains("\"")
                        ||un.getText().toString().trim().contains("#")){
                    Toast.makeText(LoginActivity.this, "please do not enter invalid chars", Toast.LENGTH_SHORT).show();
                    return;
                }

                forgotpassword(un.getText().toString().trim());


            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = un.getText().toString().trim(),p = pa.getText().toString().trim();
                if(u.equals("")||p.equals("")){
                    Toast.makeText(LoginActivity.this, "please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(p.contains("\'")||p.contains("\"")||p.contains("#")||
                        u.contains("\'")||u.contains("\"")||u.contains("#")||u.contains("@")){
                    Toast.makeText(LoginActivity.this, "please do not enter invalid chars", Toast.LENGTH_SHORT).show();
                    return;
                }
                login(u, p);
            }
        });
        (findViewById(R.id.noacc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),SignupActivity.class);

                startActivity(it);
            }
        });
    }

    //////////////FORGOT PASSWORD//////////////////////?
    public  void forgotpassword(String username){
        new forgotptask().execute(username);
    }
     class forgotptask extends AsyncTask<String,String ,String> {
        String result;
        @Override
        protected String doInBackground(String... params) {

            try {

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, Utilities.connection.method_names.fp);
                request.addProperty("entity", "username");
                request.addProperty("value", params[0]);
                //request.addProperty("password", params[1]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + Utilities.connection.method_names.fp, envelope);
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
            if(s.equals("Success"))
            det.setText("a mail was sent to your registered mail id\n" +
                    "please check it");
        }
     }


    //////////////LOGIN//////////////////////?

    public  void login(String username, String password){
        new logintask().execute(username, password);

    }

     class logintask extends AsyncTask<String, String,String>{
        String result;
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, Utilities.connection.method_names.login);
                request.addProperty("username", params[0]);
                request.addProperty("password", params[1]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + Utilities.connection.method_names.login, envelope);
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
            if((s+"").trim().equals("Invalid/Wrong login credentials"))
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
            else {
                try{
                    SharedPreferences sp = getApplicationContext().getSharedPreferences(Utilities.SharesPresfKeys.key, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(Utilities.SharesPresfKeys.regid,Integer.parseInt(s));
                    editor.commit();
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Entering Home", Toast.LENGTH_SHORT).show();
                }

                catch(Exception e){
                    Toast.makeText(getApplicationContext(),s+" -- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
