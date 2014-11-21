package org.thomasamsler.raffleapp.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.data.RaffleContract.RaffleEntry;
import org.thomasamsler.raffleapp.fragments.RaffleListFragment;
import org.thomasamsler.raffleapp.models.Raffle;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity implements AppConstants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, RaffleListFragment.newInstance())
                    .commit();
        }

        Raffle raffle = new Raffle("Raffle " + Long.toString(System.currentTimeMillis()), "12243");

        ContentValues raffleValues = new ContentValues();
        raffleValues.put(RaffleEntry.COLUMN_RAFFLE_NAME, raffle.getName());
        raffleValues.put(RaffleEntry.COLUMN_RAFFLE_PIN, raffle.getPin());

        Uri uri = getContentResolver().insert(RaffleEntry.CONTENT_URI, raffleValues);

        // FIXME: Experimenting with data and Firebase
        // https://www.firebase.com/docs/android/guide/saving-data.html
        Firebase.setAndroidContext(this);
        Firebase fbRef = new Firebase("https://raffle-app.firebaseio.com/raffles/" + RaffleEntry.getIdFromRaffleUri(uri));
        fbRef.setValue(raffle);

        fbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(LOG_TAG, "DEBUG: data : " + snapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
