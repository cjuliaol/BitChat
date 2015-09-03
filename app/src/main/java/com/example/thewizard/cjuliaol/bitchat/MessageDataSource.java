package com.example.thewizard.cjuliaol.bitchat;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cjuliaol on 01-Sep-15.
 */
public class MessageDataSource {

    private static final String TAG = "MessageDataSourceLog";


    public static void sendMessage(String sender, String recipient, String text){

        ParseObject message = new ParseObject("Message");
        message.put("sender",sender);
        message.put("recipient",recipient);
        message.put("text",text);
        message.saveInBackground();


    }

    public static  void fetchMessages(String sender, String recipient, final Listener listener) {

      ParseQuery<ParseObject> mainQuery = messagesQuery(sender,recipient);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                ArrayList<Message> messages = new ArrayList<Message>();
                for (ParseObject parseObject : list) {
                    Message message = new Message(parseObject.getString("text"),  parseObject.getString("sender"));
                    message.setDate(parseObject.getCreatedAt());
                    messages.add(message);

                }
                Log.d(TAG, "query.findInBackground on MessageDataSource before  listener.onFetchedMessages");
                listener.onFetchedMessages(messages);
            }

        });

    }

    public static void fetchMessagesAfter(String sender, String recipient,Date after, final Listener listener){
        ParseQuery<ParseObject> mainQuery = messagesQuery(sender, recipient);
        mainQuery.whereGreaterThan("createdAt",after);

        Log.d(TAG,"fetchMessagesAfter Date parameter: " + after.toString());
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                ArrayList<Message> messages = new ArrayList<Message>();
                for (ParseObject parseObject : list) {
                    Message message = new Message(parseObject.getString("text"),  parseObject.getString("sender"));
                    message.setDate(parseObject.getCreatedAt());
                    Log.d(TAG, "Mensaje: " +message.getText());
                    messages.add(message);

                }
                Log.d(TAG, "query.findInBackground on MessageDataSource before  listener.onAddMessages");
                listener.onAddMessages(messages);
            }

        });


    }

    private static ParseQuery<ParseObject> messagesQuery (String sender, String recipient) {
        ParseQuery<ParseObject> querySent = ParseQuery.getQuery("Message");
        querySent.whereEqualTo("sender", sender);
        querySent.whereEqualTo("recipient", recipient);

        ParseQuery<ParseObject> queryReceived = ParseQuery.getQuery("Message");
        queryReceived.whereEqualTo("recipient", sender);
        queryReceived.whereEqualTo("sender", recipient);

        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(querySent);
        queries.add(queryReceived);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.orderByAscending("createdAt");
      return  mainQuery;
    }

    public interface Listener {
        public void onFetchedMessages(ArrayList<Message> messages);
        public void onAddMessages(ArrayList<Message> messages);
    }
}

