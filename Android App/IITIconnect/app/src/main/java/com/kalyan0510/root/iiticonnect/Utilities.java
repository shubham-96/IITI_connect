package com.kalyan0510.root.iiticonnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by root on 20/2/16.
 */
public class Utilities {
    public static Context context;



    //  public static int r_id;
   // public static Uri img_uri;
    public class connection{
        public static final String NAMESPACE = "http://webService";
        public static final String exs="/RESTfulProject/services/FeedService?wsdl";
        public static final String SOAP_PREFIX = "/";
        public static  final String url= "http://192.168.";
        public static final String x = "1.5";
        public class method_names{
            public static final String signup = "signup";
            public static final String login = "login";
            public static final String changedp ="changedp";
            public static final String getdp = "getdp";
            public static final String fp="forgotpassword";
            public static final String cp="changepassword";
        }

    }
    public static class IMGARRAY{
        byte[] pic;
        IMGARRAY(byte[] array){
            pic=array;
        }
    }
    public class SharesPresfKeys{
        public static final String key = "xmotiv0510";
        public static final String regid = "reg_id";
    }

    public static boolean isNetworkAvailable(Context ctx)
    {
        ConnectivityManager ctvMngr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo aNetInfo = ctvMngr.getActiveNetworkInfo();
        return aNetInfo != null && aNetInfo.isAvailable();
    }

    public static void signup(Context c,String fn,String ln,String un, String mail){
        context  = c;
        new Signuptask().execute(fn,ln,un,mail);

    }
    public static void login(Context c,String username,String password){
        context  = c;
        new logintask().execute(username, password);

    }
    public static void setprofilepic(Context c, int regid, Uri selectedimgURI){
        context  = c;
         new setprofilepictask().execute(selectedimgURI);
    }
    public static void getdp(Context c,int reg_id) {
        context  = c;
        new getdptask().execute(reg_id);
    }


    static class getdptask extends AsyncTask<Integer,String ,String>{
        String result;
        @Override
        protected String doInBackground(Integer... params) {

            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, connection.method_names.login);
                request.addProperty("reg_id", params[0]);
                //request.addProperty("password", params[1]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + connection.method_names.login, envelope);
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
            byte[] array = Base64.decode(s.getBytes(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(array,0,array.length);

            super.onPostExecute(s);
        }
    }

    static class setprofilepictask extends AsyncTask<Uri,String,String>{
        String result;

        @Override
        protected String doInBackground(Uri... params) {

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), params[0]);
                bitmap = Bitmap.createScaledBitmap(bitmap,200,200,false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 45, stream);
                final byte[] array=stream.toByteArray();
                String Imagestr = Base64.encodeToString(array,Base64.DEFAULT);
                //converted to byte array and have to send it
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, connection.method_names.changedp);

                int x=context.getSharedPreferences(SharesPresfKeys.key,Context.MODE_PRIVATE).getInt("reg_id",0);
                request.addProperty("Reg_id", x);
                request.addProperty("x",Imagestr);

                envelope.bodyOut = request;
                envelope.setOutputSoapObject(request);
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + connection.method_names.changedp, envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage()+"xxxxxx";
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    return e.getMessage()+"yyyyyy";
                }
                result=envelope.getResponse().toString();
                if (envelope.bodyIn != null) {
                    SoapPrimitive resultSOAP = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(0);
                    result=resultSOAP.toString();
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage()+"zzzzzzz";
            }
            catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage()+"wwwwwww";
            }
            return "no result sorry";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context,  context.getSharedPreferences(SharesPresfKeys.key,Context.MODE_PRIVATE).getInt("reg_id",0)
            +"<-regid  res->"+result, Toast.LENGTH_LONG).show();
        }
    }
    static class logintask extends AsyncTask<String, String,String>{
        String result;
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, connection.method_names.login);
                request.addProperty("username", params[0]);
                request.addProperty("password", params[1]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + connection.method_names.login, envelope);
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
        protected void onPreExecute() {
            Toast.makeText(context, "Entered async", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if((s+"").trim().equals("Invalid/Wrong login credentials"))
                Toast.makeText(context,s, Toast.LENGTH_SHORT).show();
            else {
                try{
                    SharedPreferences sp = context.getSharedPreferences(SharesPresfKeys.key,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(SharesPresfKeys.regid,Integer.parseInt(s));
                    editor.commit();
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }

                catch(Exception e){
                    Toast.makeText(context,s+" -- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private static  class Signuptask extends AsyncTask<String, String, String> {

        private String resp,res;
        //int a =0,b=0;
        @Override
        protected String doInBackground(String... params) {
           // publishProgress("Loading contents...");
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE, connection.method_names.signup);
                request.addProperty("username", params[2]);
                request.addProperty("first_name", params[0]);
                request.addProperty("last_name", params[1]);
                request.addProperty("mail", params[3]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX + connection.method_names.signup, envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
                res=envelope.getResponse().toString();
                if (envelope.bodyIn != null) {
                    SoapPrimitive resultSOAP = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(0);
                    resp=resultSOAP.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result+"-res", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onPreExecute() {

        }


    }
}
