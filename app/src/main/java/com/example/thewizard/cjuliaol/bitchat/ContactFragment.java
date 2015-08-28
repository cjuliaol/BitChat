package com.example.thewizard.cjuliaol.bitchat;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Listener} interface
 * to handle interaction events.
 */
public class ContactFragment extends Fragment implements AdapterView.OnItemClickListener, ContactDataSource.Listener {

    private static final String TAG = "ContactFragmentLog";
    private Listener mListener;

    private ArrayList<Contact> mContacts = new ArrayList<>();
    private ContactAdapter mContactAdapter;


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null);

        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);


        ContactDataSource dataSource = new ContactDataSource(getActivity(), this);
        mContactAdapter = new ContactAdapter(mContacts);
        listView.setAdapter(mContactAdapter);

        // Instead of manually querying the ContentProvider, we can use a Loader:
        //  Init the loader
        getLoaderManager().initLoader(0, null, dataSource);
        return view;
    }

    // CJL: Retrieving the selected row
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onFetchedContacts(ArrayList<Contact> contacts) {
        mContacts.clear();
        mContacts.addAll(contacts);
        mContactAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //    mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Listener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class ContactAdapter extends ArrayAdapter<Contact> {
        public ContactAdapter(ArrayList<Contact> contacts) {
            super(getActivity(), R.layout.contact_list_item, R.id.name, contacts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Contact contact = getItem(position);
            TextView nameView = (TextView) convertView.findViewById(R.id.name);
            nameView.setText(contact.getName());

            return convertView;
        }
    }

}
