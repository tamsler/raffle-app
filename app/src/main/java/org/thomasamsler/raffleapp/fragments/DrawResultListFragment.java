package org.thomasamsler.raffleapp.fragments;


import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DrawResultListFragment extends ListFragment implements AppConstants {

    private ArrayAdapter<String> mAdapter;
    private List<String> mWinners;
    private String mRaffleId;

    public static DrawResultListFragment newInstance(String raffleId) {

        Bundle bundle = new Bundle();
        bundle.putString(RAFFLE_ID_KEY, raffleId);

        DrawResultListFragment fragment = new DrawResultListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public DrawResultListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        mRaffleId = bundle.getString(RAFFLE_ID_KEY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        setEmptyText(getString(R.string.entries_fragment_no_winners));

        mWinners = new ArrayList<String>();

        Firebase.setAndroidContext(getActivity());
        Firebase fbRef = new Firebase("https://raffle-app.firebaseio.com/raffles/" + mRaffleId + "/entries");

        fbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, String> entries = (Map<String, String>) snapshot.getValue();

                if(null == entries) {

                    mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mWinners);
                    setListAdapter(mAdapter);
                }
                else {

                    List<String> modifiedEntries = new ArrayList<String>();

                    for(String entry : entries.values()) {

                        modifiedEntries.add(entry.split("#")[1]);
                    }

                    mWinners.clear();
                    Collections.shuffle(modifiedEntries);
                    mWinners.addAll(modifiedEntries);

                    if(null == mAdapter) {

                        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mWinners);
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
