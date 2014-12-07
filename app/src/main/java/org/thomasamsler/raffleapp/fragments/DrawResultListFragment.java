package org.thomasamsler.raffleapp.fragments;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ShareActionProvider;

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

    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent = new Intent(Intent.ACTION_SEND);

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

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();

        mRaffleId = bundle.getString(RAFFLE_ID_KEY);

        mShareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        mShareIntent.setType("text/plain");
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

                    String winners = "";

                    for(String winner : modifiedEntries) {

                        winners += winner + "\n";
                    }

                    mShareIntent.putExtra(Intent.EXTRA_TEXT, winners);
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_draw_result, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) menuItem.getActionProvider();

        if(null != mShareActionProvider) {

            mShareActionProvider.setShareIntent(mShareIntent);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }
}
