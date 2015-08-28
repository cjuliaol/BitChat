package com.example.thewizard.cjuliaol.bitchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseUser;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "x8BgPbIuaIXb63oRI9vu3pGLjVc9VeTqnL1buNik", "iGtUt4kHlI6jp7Jv1muxfw2wl4zKdQLVjnUgVd4Q");

        // CJL: If you're not log in start Sign in activity
        if (ParseUser.getCurrentUser() == null) {
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
