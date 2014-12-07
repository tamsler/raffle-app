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
