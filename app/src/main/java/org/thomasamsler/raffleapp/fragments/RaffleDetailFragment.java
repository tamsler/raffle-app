package org.thomasamsler.raffleapp.fragments;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.Utility;
import org.thomasamsler.raffleapp.data.RaffleContract.RaffleEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RaffleDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AppConstants {


    private String mRaffleId;

    private static final String RAFFLE_ENTRY_KEY = "entryName";

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

                String entry = mRaffleEntry.getText().toString();

                if(null == entry || "".equals(entry)) {

                    Toast.makeText(getActivity(), R.string.fragment_raffle_detail_entry_error, Toast.LENGTH_SHORT).show();
                }
                else {

                    Firebase.setAndroidContext(getActivity());
                    Firebase fbRef = new Firebase("https://raffle-app.firebaseio.com/raffles/" + mRaffleId + "/entries");
                    fbRef.push().setValue(Utility.getPreferenceUserDigest(getActivity()) + "#" + mRaffleEntry.getText().toString(), new Firebase.CompletionListener() {

                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                            if(null != firebaseError) {

                                Toast.makeText(getActivity(), R.string.fragment_raffle_detail_entry_failure, Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Toast.makeText(getActivity(), R.string.fragment_raffle_detail_entry_success, Toast.LENGTH_SHORT).show();
                                //mRaffleEntry.setText("");
                                mAddEntryButton.setEnabled(false);
                            }
                        }
                    });
                }
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
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString(RAFFLE_ENTRY_KEY, mRaffleEntry.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        if (savedInstanceState != null) {

            mRaffleEntry.setText(savedInstanceState.getString(RAFFLE_ENTRY_KEY));
        }

        /*
         * Check if user already submitted and entry
         */
        Firebase.setAndroidContext(getActivity());
        Firebase fbRef = new Firebase("https://raffle-app.firebaseio.com/raffles/" + mRaffleId + "/entries");

        fbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, String> entries = (Map<String, String>) snapshot.getValue();

                if(null != entries) {

                    String user = Utility.getPreferenceUserDigest(getActivity());

                    if(null != user) {

                        for(String entry : entries.values()) {

                            String[] tokens = entry.split("#");
                            if(user.equals(tokens[0])) {

                                mAddEntryButton.setEnabled(false);
                                mRaffleEntry.setText(tokens[1]);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }
        });
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
