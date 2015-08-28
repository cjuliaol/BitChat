package com.example.thewizard.cjuliaol.bitchat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivityLog";
    private EditText mUserNumber;
    private EditText mUserPassword;
    private EditText mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        mUserNumber = (EditText) findViewById(R.id.user_number);
        mUserPassword = (EditText) findViewById(R.id.user_password);
        mUserName = (EditText) findViewById(R.id.user_name);


        TelephonyManager telephonyManager = (TelephonyManager) this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telephonyManager.getLine1Number();
        String subscriberId = telephonyManager.getSubscriberId();
        // Log.d(TAG, "Phone number is " + phoneNumber);
        // Log.d(TAG, "Subscriber id is " + subscriberId);

        mUserNumber.setText(phoneNumber);


        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mUserNumber.getText().toString();
                String password = mUserPassword.getText().toString();
                ParseUser user = new ParseUser();

                user.setUsername(phoneNumber);
                user.setPassword(password);

                user.put("name", mUserName.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            SignInActivity.this.finish();
                        } else {
                            Log.d(TAG, "SIGN UP FAILED");
                        }
                    }
                });
            }
        });

        Button logInButton = (Button) findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logInInBackground(mUserNumber.getText().toString(), mUserPassword.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {
                                    SignInActivity.this.finish();
                                } else {
                                    Log.d(TAG, "SIGN UP FAILED");
                                }
                            }
                        });

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
