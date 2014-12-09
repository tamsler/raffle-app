package org.thomasamsler.raffleapp.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tamsler on 11/29/14.
 */
public class EntriesFragment extends ListFragment implements AppConstants {

    private ArrayAdapter<String> mAdapter;
    private List<String> mEntries;
    private String mRaffleId;

    public static EntriesFragment newInstance(String raffleId) {

        Bundle bundle = new Bundle();
        bundle.putString(RAFFLE_ID_KEY, raffleId);

        EntriesFragment fragment = new EntriesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public EntriesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        mRaffleId = bundle.getString(RAFFLE_ID_KEY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        setEmptyText(getString(R.string.entries_fragment_no_entries));

        mEntries = new ArrayList<String>();

        Firebase.setAndroidContext(getActivity());
        Firebase fbRef = new Firebase("https://raffle-app.firebaseio.com/raffles/" + mRaffleId + "/entries");

        fbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, String> entries = (Map<String, String>) snapshot.getValue();

                if(null == entries) {

                    if(null == mAdapter) {

                        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mEntries);
                        setListAdapter(mAdapter);
                    }

                    mEntries.clear();
                    mAdapter.notifyDataSetChanged();
                }
                else {

                    List<String> modifiedEntries = new ArrayList<String>();

                    for(String entry : entries.values()) {

                        modifiedEntries.add(entry.split("#")[1]);
                    }

                    mEntries.clear();
                    mEntries.addAll(modifiedEntries);

                    if(null == mAdapter) {

                        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mEntries);
                        setListAdapter(mAdapter);
                    }
                    else {

                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }
        });
    }
}
