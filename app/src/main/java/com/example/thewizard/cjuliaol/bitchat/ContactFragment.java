package com.example.thewizard.cjuliaol.bitchat;

import android.app.Activity;
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
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Listener} interface
 * to handle interaction events.
 */
public class ContactFragment extends Fragment {

    private static final String TAG = "ContacFragmentLog";
    private Listener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,null);

        ListView  listView= (ListView) view.findViewById(R.id.list);

        String [] columns = {    ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        int [] ids = {R.id.number,R.id.name};
        // CJL: Using a Content Provider is this case Contacts Content Provider
        Cursor cursor =getActivity().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {ContactsContract.CommonDataKinds.Phone._ID,
                                ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}
                        , null, null, null);

       /* cursor.moveToFirst();
        while( !cursor.isAfterLast()) {
            String number = cursor.getString(0);
            String name = cursor.getString(1);

            Log.d(TAG,name + ", " + number);
            cursor.moveToNext();    }
       cursor.close();*/

       listView.setAdapter( new SimpleCursorAdapter(getActivity(),R.layout.contact_list_item,cursor,columns,ids,0));


        return view;
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
