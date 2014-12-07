package org.thomasamsler.raffleapp.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

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
