package com.example.thewizard.cjuliaol.bitchat;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjuliaol on 28-Aug-15.
 */
public class ContactDataSource implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ContactDataSourceLog";
    private Context mContext;
    private Listener mListener;

    ContactDataSource(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }


    // Instead of manually querying the ContentProvider, we can use a Loader: implementing LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
    }


    // Instead of manually querying the ContentProvider, we can use a Loader: implementing LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        List<String> numbers = new ArrayList<>();

        // CJL: recorriendo el cursor con los contactos del telefono
        data.moveToFirst();
        while (!data.isAfterLast()) {
            String phoneNumber = data.getString(1);
            phoneNumber.replaceAll("-", "");
            phoneNumber.replaceAll(" ", "");

            numbers.add(phoneNumber);
            data.moveToNext();
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("username", numbers);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    ArrayList<Contact> contacts = new ArrayList<Contact>();
                    for (ParseUser parseUser : list) {
                        Contact contact = new Contact();
                        contact.setName((String) parseUser.get("name"));
                        contact.setPhoneNumber(parseUser.getUsername());

                        contacts.add(contact);
                    }

                    if (mListener != null) {
                        mListener.onFetchedContacts(contacts);
                    }

                    // CJL: contactos que son usuarios de BitChat en Parse.com
                    for (Contact contact : contacts) {
                        Log.d(TAG, contact.getName() + ", " + contact.getPhoneNumber());
                    }


                } else {

                }
            }
        });

    }

    // Instead of manually querying the ContentProvider, we can use a Loader: implementing LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }

    public interface Listener {

        public void onFetchedContacts(ArrayList<Contact> contacts);

    }

}
