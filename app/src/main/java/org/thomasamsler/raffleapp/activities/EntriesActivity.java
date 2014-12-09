package org.thomasamsler.raffleapp.activities;

import android.app.Activity;
import android.os.Bundle;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.fragments.EntriesFragment;

public class EntriesActivity extends Activity implements AppConstants {

    private String mRaffleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entries);

        mRaffleId = getIntent().getExtras().getString(RAFFLE_ID_KEY);

        if(savedInstanceState == null) {

            getFragmentManager().beginTransaction()
                    .add(R.id.container, EntriesFragment.newInstance(mRaffleId))
                    .commit();
        }

        setTitle(R.string.activity_raffle_entries_title);
    }
}
