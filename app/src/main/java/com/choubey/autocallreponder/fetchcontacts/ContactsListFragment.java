package com.choubey.autocallreponder.fetchcontacts;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsListFragment} interface
 * to handle interaction events.
 * Use the {@link ContactsListFragment} factory method to
 * create an instance of this fragment.
 */
public class ContactsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };

    private final static int[] TO_IDS = {
            android.R.id.text1
    };

    private long mContactId;
    private String mContactKey;
    private String mContactName;
    private Uri mContactUri;
    private SimpleCursorAdapter mCursorAdapter;
    private static final String[] PROJECTION = {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };
    private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_KEY_INDEX = 1;
    private static final String SELECTION =
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" +
                    " AND " +
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'"
            ;
    private String mSearchString="";
    private String[] mSelectionArgs = {
                    mSearchString
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getSimpleName(), "OnCreate called for ContactsListFragment");

        Bundle arguments = getArguments();
        if(arguments != null) {
            String key = arguments.getString("key");
            Log.i(this.getClass().getSimpleName(), "Key = " + key);
            if(key != null) {
                mSearchString = key;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        mCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                null,
                FROM_COLUMNS, TO_IDS,
                0);
        getLoaderManager().initLoader(0, null, this);
        final ListView listView = new ListView(getActivity());
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(parent, position);
            }
        });

        linearLayout.addView(listView);

        return linearLayout;
    }

   /* @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(this.getClass().getSimpleName(), "On activity created");

        mCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                null,
                FROM_COLUMNS, TO_IDS,
                0);

        setListAdapter(mCursorAdapter);
        getLoaderManager().initLoader(0, null, this);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setItemChecked(0, true);
    }*/

    public void showDetails(AdapterView<?> parent, int position) {
        Log.i(this.getClass().getSimpleName(), "Showing details for position = " + position);
        Cursor cursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
        cursor.moveToPosition(position);
        mContactId = cursor.getLong(CONTACT_ID_INDEX);
        mContactKey = cursor.getString(CONTACT_KEY_INDEX);
        Cursor dataCursor = (Cursor)parent.getItemAtPosition(position);
        mContactName = dataCursor.getString(dataCursor.getColumnIndex("display_name"));

        DetailsDialogFragment detailsDialogFragment = DetailsDialogFragment.newInstance(mContactId, mContactKey, mContactName);
        FragmentManager fm = getFragmentManager();
        detailsDialogFragment.show(fm, "Choose a number");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(this.getClass().getSimpleName(), "Inside onOptionsItemSelected method.");
        return false;
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(this.getClass().getSimpleName(), "On create loader");
        mSelectionArgs[0] = "%" + mSearchString + "%";
        // Starts the query
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                mSelectionArgs,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
        );
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(this.getClass().getSimpleName(), "On load finished");
        mCursorAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(this.getClass().getSimpleName(), "On loader reset");
        mCursorAdapter.swapCursor(null);
    }
}
