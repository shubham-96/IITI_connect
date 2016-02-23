package com.kalyan0510.root.iiticonnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText mail;
    CheckBox tnc;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstName= (EditText)findViewById(R.id.fn);
        lastName= (EditText)findViewById(R.id.ln);
        username= (EditText)findViewById(R.id.un);
        mail= (EditText)findViewById(R.id.mailTv);
        tnc = (CheckBox)findViewById(R.id.tc);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn = firstName.getText().toString().trim();
                String ln = lastName.getText().toString().trim();
                String un = username.getText().toString().trim();
                String m = mail.getText().toString().trim();
                if(fn.equals("")||ln.equals("")||un.equals("")||m.equals("")){
                    Toast.makeText(SignupActivity.this, "Dont leave boxes empty", Toast.LENGTH_SHORT).show();
                }
                else if(!tnc.isChecked()){
                    Toast.makeText(SignupActivity.this, "Agree to the terms and conditions ", Toast.LENGTH_SHORT).show();
                }else {
                    Utilities.signup(getApplicationContext(),fn,ln,un,m);
                }
            }
        });
    }

}
