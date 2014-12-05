package org.thomasamsler.raffleapp.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.fragments.RaffleDetailFragment;

public class RaffleDetailActivity extends Activity implements AppConstants {

    private String mRaffleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_raffle_detail);

        mRaffleId = getIntent().getExtras().getString(RAFFLE_ID_KEY);

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, RaffleDetailFragment.newInstance(mRaffleId))
                    .commit();
        }

        setTitle(R.string.activity_raffle_detail_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_raffle_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        int id = item.getItemId();

        switch(id) {

            case R.id.action_entries:
                intent = new Intent(this, EntriesActivity.class);
                intent.putExtra(RAFFLE_ID_KEY, mRaffleId);
                startActivity(intent);
                break;

            case R.id.action_draw:
                intent = new Intent(this, DrawResultActivity.class);
                intent.putExtra(RAFFLE_ID_KEY, mRaffleId);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
