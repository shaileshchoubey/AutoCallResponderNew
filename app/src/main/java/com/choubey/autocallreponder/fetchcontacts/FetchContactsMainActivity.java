package com.choubey.autocallreponder.fetchcontacts;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.choubey.autocallreponder.R;

public class FetchContactsMainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getSimpleName(), "Called FetchContactsMainActivity");

        refreshUIContent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(this.getClass().getSimpleName(), "Received new intent");
        handleIntent(intent);
    }

    public String getSearchKey(Intent intent)
    {
        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            return query;
        }
        return null;
    }

    private void handleIntent(Intent intent) {
        Log.i(this.getClass().getSimpleName(), "The intent action is " + intent.getAction());
        refreshUIContent(intent);
    }

    private void refreshUIContent(Intent intent)
    {
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.contactsListFrame);
        setContentView(frame, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        String query = getSearchKey(intent);
        showSearchResultForQuery(query);
    }

    private void showSearchResultForQuery(String query)
    {
        Log.i(this.getClass().getSimpleName(), "The received query is = " + query);
        Fragment newFragment = new ContactsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", query);
        newFragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.contactsListFrame, newFragment, "contactsList").commit();
    }
}
