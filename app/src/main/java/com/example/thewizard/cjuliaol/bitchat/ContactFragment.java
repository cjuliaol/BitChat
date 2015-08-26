package com.example.thewizard.cjuliaol.bitchat;

import android.app.Activity;
import android.app.LoaderManager;
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
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Listener} interface
 * to handle interaction events.
 */
public class ContactFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ContacFragmentLog";
    private Listener mListener;
    private SimpleCursorAdapter mSimpleCursorAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null);

        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        String[] columns = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        int[] ids = {R.id.number, R.id.name};
        // CJL: Using a Content Provider is this case Contacts Content Provider
        /*Cursor cursor =getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {ContactsContract.CommonDataKinds.Phone._ID,
                                ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}
                        , null, null, null);*/

       /* cursor.moveToFirst();
        while( !cursor.isAfterLast()) {
            String number = cursor.getString(0);
            String name = cursor.getString(1);

            Log.d(TAG,name + ", " + number);
            cursor.moveToNext();    }
       cursor.close();*/

        // CJL: columns and ids must be in the same order
        //listView.setAdapter( new SimpleCursorAdapter(getActivity(),R.layout.contact_list_item,cursor,columns,ids,0));
        mSimpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.contact_list_item, null, columns, ids, 0);

        listView.setAdapter(mSimpleCursorAdapter);

        // Instead of manually querying the ContentProvider, we can use a Loader:
        //  Init the loader
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    // CJL: Retrieving the selected row
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
        cursor.moveToPosition(position);

        Log.d(TAG, "Phone number is " + cursor.getString(1) + " Name is " + cursor.getString(2));

    }

    // Instead of manually querying the ContentProvider, we can use a Loader: implementing LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
    }

    // Instead of manually querying the ContentProvider, we can use a Loader: implementing LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSimpleCursorAdapter.swapCursor(null);
    }

    // Instead of manually querying the ContentProvider, we can use a Loader: implementing LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSimpleCursorAdapter.swapCursor(data);

        Log.d(TAG, "Implementing Cursor Loader");

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

}
