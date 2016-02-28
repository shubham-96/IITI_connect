package com.kalyan0510.root.iiticonnect;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    static  Context context;
    static int reg_id;
    static TextView curLocat;
    static String Loc="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_home);
        reg_id = getSharedPreferences(Utilities.SharesPresfKeys.key,Context.MODE_PRIVATE).
                getInt(Utilities.SharesPresfKeys.regid,0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("IITI Connect");
        toolbar.setSubtitle(getSharedPreferences(Utilities.SharesPresfKeys.key, Context.MODE_PRIVATE).getString(Utilities.SharesPresfKeys.name,"subtitle here"));
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2 );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.action_profile){
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("HEY no ->" +getArguments().getInt(ARG_SECTION_NUMBER));
            return rootView;
        }
    }
    public static class WarningFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public WarningFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static WarningFragment newInstance(int sectionNumber) {

            WarningFragment fragment = new WarningFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_warning, container, false);
            final EditText descrEdt  = (EditText)rootView.findViewById(R.id.descEdt);
            final RadioGroup typeRadioGroup = (RadioGroup)rootView.findViewById(R.id.typegroup);
            typeRadioGroup.check(R.id.campusRad);
            rootView.findViewById(R.id.sendAlertBut).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(( rootView.findViewById(typeRadioGroup.getCheckedRadioButtonId()))==null){
                        Log.w("x","afakjh");
                        return;
                    }
                    if(!Utilities.isOncampusWifi(context)){
                        Toast.makeText(context, "not connected to CAMPUS WIFI", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new sendAlerttask().execute(reg_id + "", descrEdt.getText().toString().trim(),
                            ((RadioButton) rootView.findViewById(typeRadioGroup.getCheckedRadioButtonId())).getText().toString());
                    //Toast.makeText(context, ""+((RadioButton)rootView.findViewById(typeRadioGroup.getCheckedRadioButtonId())).getText(), Toast.LENGTH_SHORT).show();
                }
            });

            return rootView;
        }
    }

    public static class SOSFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public SOSFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SOSFragment newInstance(int sectionNumber) {

            SOSFragment fragment = new SOSFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_sos, container, false);
            final EditText manLoc  = (EditText)rootView.findViewById(R.id.LocManEdt);
            final RadioGroup typeRG = (RadioGroup)rootView.findViewById(R.id.helptype);
            final RadioGroup LocRG = (RadioGroup)rootView.findViewById(R.id.helpLoc);
            final TextView curloc  = curLocat = (TextView)rootView.findViewById(R.id.curLoc);
            typeRG.check(R.id.help1);
            LocRG.check(R.id.rec);
            if(Utilities.isOncampusWifi(context))
            new getLoctask().execute();
            //curloc.setText(Loc+"");
           // Toast.makeText(context, "SOS FRAGMENT LOADED", Toast.LENGTH_SHORT).show();
            rootView.findViewById(R.id.sendSOS).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Utilities.isOncampusWifi(context)){
                        Toast.makeText(context, "not connected to CAMPUS WIFI", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String Location = curloc.getText().toString().trim();
                    if(Location.equals("Not Recognised")){
                        if(manLoc.getText().toString().trim().equals("")){
                            Toast.makeText(context, "Empty Location Please enter Location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Location = manLoc.getText().toString().trim();
                    }
                    if(LocRG.getCheckedRadioButtonId()==R.id.msa){
                        Location= manLoc.getText().toString().trim();
                    }
                    new sendSOStask().execute(reg_id+"",Location,
                            ((RadioButton)rootView.findViewById(typeRG.getCheckedRadioButtonId())).getText().toString());
                    //Toast.makeText(context, ""+((RadioButton)rootView.findViewById(typeRadioGroup.getCheckedRadioButtonId())).getText(), Toast.LENGTH_SHORT).show();
                }
            });

            return rootView;
        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0)
                return WarningFragment.newInstance(1);
            else if(position==1){
                return SOSFragment.newInstance(2);
            }else
                return PlaceholderFragment.newInstance(position+1);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Warning";
                case 1:
                    return "SOS";
                case 2:
                    return "Chat";
                case 3:
                    return "Schedule";
                case 4:
                    return "map";
            }
            return null;
        }
    }









    //////////////////////////////////////////ASYNC TASKS//////////////////////////////////////

    //////////SEND ALERT///////////////////
    static class sendAlerttask extends AsyncTask<String , String,String> {
        String result;
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE,"setWarning");
                request.addProperty("Reg_id", reg_id);
                request.addProperty("message", params[1]);
                request.addProperty("type", params[2]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX +"setWarning", envelope);
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
            Toast.makeText(context, ""+s, Toast.LENGTH_SHORT).show();
        }
    }
    //////////////////SEND  SOS//////////////////////
    static class sendSOStask extends AsyncTask<String , String,String> {
        String result;
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE,"setSOS");
                request.addProperty("reg_id", reg_id);
                request.addProperty("message", params[1]);
                request.addProperty("type", params[2]);
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX +"setSOS", envelope);
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
            Toast.makeText(context, "Send SOS-> "+s, Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////GET LOCATION///////////////////////////////
    static class getLoctask extends AsyncTask<String , String,String> {
        String result;
        protected String doInBackground(String... params) {
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(Utilities.connection.NAMESPACE,"getAddress");
                request.addProperty("mac", Utilities.getwifimac(context));
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(Utilities.connection.url+Utilities.connection.x+Utilities.connection.exs);
                try {
                    transport.call(Utilities.connection.NAMESPACE + Utilities.connection.SOAP_PREFIX +"getAddress", envelope);
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
            if(s.equals("fail")){
                return;
            }
            Loc = s;
            curLocat.setText(s+"");
        }
    }


}

