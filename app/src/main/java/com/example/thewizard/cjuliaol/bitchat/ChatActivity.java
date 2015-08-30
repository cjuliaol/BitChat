package com.example.thewizard.cjuliaol.bitchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ArrayList<Message> messages =  new ArrayList<Message>();
        messages.add( new Message("Hello","18095557777"));




        ListView messageListView = (ListView) findViewById(R.id.message_list);
        messageListView.setAdapter( new MessageAdapter(messages));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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

    private class MessageAdapter extends ArrayAdapter<Message> {

        MessageAdapter(ArrayList<Message> messages) {
            super(ChatActivity.this, R.layout.message_list_item,R.id.message, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

           TextView messageText = (TextView) convertView.findViewById(R.id.message);
            messageText.setText( message.getText());

            return convertView;
        }
    }
}
