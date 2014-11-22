package org.thomasamsler.raffleapp.fragments;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.data.RaffleContract.RaffleEntry;

public class RaffleDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AppConstants {


    private String mRaffleId;

    private static final int DETAIL_LOADER = 0;

    private static final String[] RAFFLE_COLUMNS = {

            RaffleEntry.TABLE_NAME + "." + RaffleEntry._ID,
            RaffleEntry.COLUMN_RAFFLE_NAME,
            RaffleEntry.COLUMN_RAFFLE_PIN
    };

    public static final int COL_RAFFLE_ID = 0;
    public static final int COL_RAFFLE_NAME = 1;
    public static final int COL_RAFFLE_PIN = 2;

    private TextView mRaffleName;
    private EditText mRaffleEntry;
    private Button mAddEntryButton;

    public static RaffleDetailFragment newInstance(String raffleId) {

        Bundle bundle = new Bundle();
        bundle.putString(RAFFLE_ID_KEY, raffleId);

        RaffleDetailFragment fragment = new RaffleDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public RaffleDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        mRaffleId = bundle.getString(RAFFLE_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_raffle_detail, container, false);

        mRaffleName = (TextView) rootView.findViewById(R.id.fragment_raffle_detail_name_text_view);
        mRaffleEntry = (EditText) rootView.findViewById(R.id.fragment_raffle_detail_entry_edit_text);
        mAddEntryButton = (Button) rootView.findViewById(R.id.fragment_raffle_detail_add_entry_button);
        mAddEntryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Firebase.setAndroidContext(getActivity());
                Firebase fbRef = new Firebase("https://raffle-app.firebaseio.com/raffles/" + mRaffleId + "/entries");
                fbRef.push().setValue(mRaffleEntry.getText().toString());
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {

        super.onResume();

        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                RaffleEntry.CONTENT_URI,
                RAFFLE_COLUMNS,
                "_id=?",
                new String [] { mRaffleId },
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            String raffleName = data.getString(data.getColumnIndex(RaffleEntry.COLUMN_RAFFLE_NAME));
            mRaffleName.setText(raffleName);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
