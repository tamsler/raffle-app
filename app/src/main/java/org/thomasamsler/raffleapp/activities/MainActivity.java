package org.thomasamsler.raffleapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.fragments.RaffleDetailFragment;
import org.thomasamsler.raffleapp.fragments.RaffleListFragment;


public class MainActivity extends Activity implements RaffleListFragment.Callback, AppConstants {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setTitle(R.string.activity_main_title);

        if (findViewById(R.id.raffle_detail_container) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {

                getFragmentManager().beginTransaction()
                    .add(R.id.raffle_detail_container, RaffleDetailFragment.newInstance(""))
                    .commit();
            }
        }
        else {

            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String raffleId) {

        if(mTwoPane) {

            getFragmentManager().beginTransaction()
                    .replace(R.id.raffle_detail_container, RaffleDetailFragment.newInstance(raffleId))
                    .commit();
        }
        else {

            Intent intent = new Intent(this, RaffleDetailActivity.class);
            intent.putExtra(RAFFLE_ID_KEY, raffleId);
            startActivity(intent);
        }
    }
}
