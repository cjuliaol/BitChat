package com.example.thewizard.cjuliaol.bitchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseUser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ContactsActivity extends AppCompatActivity implements ContactFragment.Listener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));


       ExceptionReporting.sendCrashReportFile(this);

        setContentView(R.layout.activity_contacts);


       // CJL: I move Parse initialization to another class

        // CJL: If you're not log in start Sign in activity
        if (ContactDataSource.getCurrentUser() == null) {
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
        }



        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ContactFragment())
                    .commit();
        }
    }

    @Override
    public void onContactSelected(Contact contact) {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra(ChatActivity.CONTACT_NUMBER, contact.getPhoneNumber());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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
