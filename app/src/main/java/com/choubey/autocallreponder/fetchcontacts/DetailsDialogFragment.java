package com.choubey.autocallreponder.fetchcontacts;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private OnFragmentInteractionListener mListener;
    private static final String CONTACT_ID_KEY = "contactId";
    private static final String CONTACT_KEY_KEY = "contactKey";
    private static final String CONTACT_NAME_KEY = "contactName";
    private SimpleCursorAdapter contactDetailsCursorAdaptor;
    private static final String PROJECTION[] =
            {
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.NUMBER/*,
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.LABEL*/
            };
    private static final String SORT_ORDER = ContactsContract.Data.MIMETYPE;
    private static final String SELECTION = ContactsContract.Data.LOOKUP_KEY + " = ?" +
            " AND " +
            ContactsContract.Data.MIMETYPE + " = " +
            "'" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";

    private String[] mSelectionArgs = { "" };
    private String mLookupKey;
    private final static String[] FROM_COLUMNS = {
                    ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private final static int[] TO_IDS = {
            android.R.id.text1
    };

    public static DetailsDialogFragment newInstance(long id, String key, String contactName) {
        DetailsDialogFragment fragment = new DetailsDialogFragment();
        Bundle args = new Bundle();
        args.putLong(CONTACT_ID_KEY, id);
        args.putString(CONTACT_KEY_KEY, key);
        args.putString(CONTACT_NAME_KEY, contactName);
        Log.i(DetailsDialogFragment.class.getSimpleName(), "Id = " + id + ", key = " + key + ", name = " + contactName);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Choose a number");
        Bundle args = getArguments();

        mLookupKey = String.valueOf(args.getString(CONTACT_KEY_KEY));
        final String contactName = String.valueOf(args.getString(CONTACT_NAME_KEY));

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        contactDetailsCursorAdaptor = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                null,
                FROM_COLUMNS, TO_IDS,
                0);

        final ListView listView = new ListView(getActivity());
        listView.setAdapter(contactDetailsCursorAdaptor);
        linearLayout.addView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNumber = getSelectedNumber(parent, position);
                dismiss();

                Intent data = new Intent();
                data.putExtra("number", selectedNumber);
                data.putExtra("contactName", contactName);
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        });

        Button closeButton = new Button(getActivity());
        closeButton.setText("CLOSE");
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        linearLayout.addView(closeButton);
        getLoaderManager().initLoader(0, null, this);
        return linearLayout;
    }

    public String getSelectedNumber(AdapterView<?> parent, int position)
    {
        Log.i(this.getClass().getSimpleName(), "Getting the selected number");
        Cursor cursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
        cursor.moveToPosition(position);
        String selectedNumber = cursor.getString(1);
        return selectedNumber;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(this.getClass().getSimpleName(), "On create loader");

        mSelectionArgs[0] = mLookupKey;
        // Starts the query
        CursorLoader mLoader =
                new CursorLoader(
                        getActivity(),
                        ContactsContract.Data.CONTENT_URI,
                        PROJECTION,
                        SELECTION,
                        mSelectionArgs,
                        SORT_ORDER
                );
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(this.getClass().getSimpleName(), "On load finished");
        contactDetailsCursorAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(this.getClass().getSimpleName(), "On loader reset");
        contactDetailsCursorAdaptor.swapCursor(null);
    }
}
