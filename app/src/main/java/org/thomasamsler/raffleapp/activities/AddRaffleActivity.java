package org.thomasamsler.raffleapp.activities;

import android.app.Activity;
import android.os.Bundle;

import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.fragments.AddRaffleFragment;

public class AddRaffleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_raffle);
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, AddRaffleFragment.newInstance())
                    .commit();
        }

        setTitle(R.string.activity_add_raffle_title);
    }
}
