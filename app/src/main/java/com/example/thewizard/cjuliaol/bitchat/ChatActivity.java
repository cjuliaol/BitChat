package com.example.thewizard.cjuliaol.bitchat;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, MessageDataSource.Listener {

    private static final String TAG = "ChatActivityLog";
    private ArrayList<Message> mMessages;
    private MessageAdapter mMessageAdapter;
    public static final String CONTACT_NUMBER = "CONTACTNUMBER";
    private String mRecipient;
    private ListView mMessageListView;
    private Date mLastMessageDate = new Date();
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
           // Log.d(TAG, "Fetched messages with Runnable");
            MessageDataSource.fetchMessagesAfter(
                    ContactDataSource.getCurrentUser().getPhoneNumber(),
                    mRecipient,
                    mLastMessageDate,
                    ChatActivity.this);
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));

        ExceptionReporting.sendCrashReportFile(this);


        setContentView(R.layout.activity_chat);

        mMessages =
                new ArrayList<Message>();


        mRecipient = getIntent().getStringExtra(CONTACT_NUMBER);
        setTitle(mRecipient);

        mMessageAdapter = new MessageAdapter(mMessages);
        mMessageListView = (ListView) findViewById(R.id.message_list);
        mMessageListView.setAdapter(mMessageAdapter);

        Button sendMessage = (Button) findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);

        Log.d(TAG, "onCreate on ChatActivity before  MessageDataSource.fetchMessage");
        MessageDataSource.fetchMessages(ContactDataSource.getCurrentUser().getPhoneNumber(), mRecipient, this);
        Log.d(TAG, "onCreate on ChatActivity after  MessageDataSource.fetchMessage");

    }

    @Override
    public void onClick(View v) {
        EditText newMessageView = (EditText) findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        Message message = new Message(newMessage, ContactDataSource.getCurrentUser().getPhoneNumber());

       Log.d(TAG, "Current User Phone Number :" + ContactDataSource.getCurrentUser().getPhoneNumber() );

        // CJL: Comment it because I poll the message from the backend
       // mMessages.add(message);
        mMessageAdapter.notifyDataSetChanged();
        newMessageView.setText("");
        MessageDataSource.sendMessage(message.getSender(), mRecipient, message.getText());


    }

    @Override
    public void onFetchedMessages(ArrayList<Message> messages) {
        Log.d(TAG, "onFetchedMessages on ChatActivity before mMessageAdapter.notifyDataSetChanged");
        mMessages.clear();
        addMessages(messages);
        mHandler.postDelayed(mRunnable, 1000);

    }

    @Override
    public void onAddMessages(ArrayList<Message> messages) {
      addMessages(messages);
    }

    private void addMessages(ArrayList<Message> messages) {
        mMessages.addAll(messages);
        mMessageAdapter.notifyDataSetChanged();


        if (mMessages.size() > 0) {
            mMessageListView.setSelection(mMessages.size() - 1);
            Message message = mMessages.get(mMessages.size() - 1);
            mLastMessageDate = message.getDate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // CJL: stop calling runnable
        mHandler.removeCallbacks(mRunnable);
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
            super(ChatActivity.this, R.layout.message_list_item, R.id.message, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView messageText = (TextView) convertView.findViewById(R.id.message);
            messageText.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) messageText.getLayoutParams();
              int sdk = Build.VERSION.SDK_INT;



            if (message.getSender().equals(ContactDataSource.getCurrentUser().getPhoneNumber())) {

                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    messageText.setBackground(getDrawable(R.drawable.bubble_right_green));
                }
                else {
                    messageText.setBackgroundDrawable(getDrawable(R.drawable.bubble_right_green));
                }

                layoutParams.gravity = Gravity.RIGHT;
            } else {
                messageText.setBackground(getDrawable(R.drawable.bubble_left_gray));
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    messageText.setBackground(getDrawable(R.drawable.bubble_left_gray));
                }
                else {
                    messageText.setBackgroundDrawable(getDrawable(R.drawable.bubble_left_gray));
                }

                layoutParams.gravity = Gravity.LEFT;
            }

            messageText.setLayoutParams(layoutParams);

            return convertView;
        }
    }
}
