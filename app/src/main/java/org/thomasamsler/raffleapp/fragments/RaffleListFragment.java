package org.thomasamsler.raffleapp.fragments;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.Utility;
import org.thomasamsler.raffleapp.activities.AddRaffleActivity;
import org.thomasamsler.raffleapp.activities.RaffleDetailActivity;
import org.thomasamsler.raffleapp.adapters.RaffleAdapter;
import org.thomasamsler.raffleapp.data.RaffleContract.RaffleEntry;


public class RaffleListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, AppConstants {

    private RaffleAdapter mRaffleAdapter;

    private static final int RAFFLE_LOADER = 0;

    private static final String[] RAFFLE_COLUMNS = {

            RaffleEntry.TABLE_NAME + "." + RaffleEntry._ID,
            RaffleEntry.COLUMN_RAFFLE_NAME,
            RaffleEntry.COLUMN_RAFFLE_PIN
    };

    public static final int COL_RAFFLE_ID = 0;
    public static final int COL_RAFFLE_NAME = 1;
    public static final int COL_RAFFLE_PIN = 2;

    public static RaffleListFragment newInstance() {

        RaffleListFragment fragment = new RaffleListFragment();
        return fragment;
    }

    public RaffleListFragment() { }

    public interface Callback {

        public void onItemSelected(String raffleId);
    }

    @Override
    public void onResume() {

        super.onResume();

        getLoaderManager().restartLoader(RAFFLE_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mRaffleAdapter = new RaffleAdapter(getActivity(), null, 0);

        setListAdapter(mRaffleAdapter);

        getLoaderManager().initLoader(RAFFLE_LOADER, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String user = Utility.getPreferenceUser(getActivity());

        if(null == user || "".equals(user)) {

            startActivityForResult(AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null), 1000);
            Toast.makeText(getActivity(), R.string.fragment_raffle_list_missing_user, Toast.LENGTH_SHORT).show();
        }
        else {

            Cursor cursor = mRaffleAdapter.getCursor();

            if(null != cursor && cursor.moveToPosition(position)) {

                ((Callback)getActivity()).onItemSelected(cursor.getString(COL_RAFFLE_ID));
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        String user = Utility.getPreferenceUser(getActivity());

        if(null == user || "".equals(user)) {

            startActivityForResult(AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null), 1000);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                RaffleEntry.CONTENT_URI,
                RAFFLE_COLUMNS,
                null,
                null,
                RaffleEntry.COLUMN_RAFFLE_NAME + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mRaffleAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mRaffleAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_raffle, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add) {

            Intent intent = new Intent(getActivity(), AddRaffleActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {

            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            if(null != accountName && !"".equals(accountName)) {

                Utility.setPreferenceUser(getActivity(), accountName);
            }
            else {

                Log.e(LOG_TAG, "ERROR: Did not get valid accountName");
            }
        }
    }
}
