package org.thomasamsler.raffleapp.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.thomasamsler.raffleapp.AppConstants;
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

    public RaffleListFragment() {
        // Required empty public constructor
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

        Cursor cursor = mRaffleAdapter.getCursor();

        if(null != cursor && cursor.moveToPosition(position)) {

            Log.d(LOG_TAG, "DEBUG: raffle name : " + cursor.getString(COL_RAFFLE_NAME));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
}
