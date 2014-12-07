package org.thomasamsler.raffleapp;

import android.app.Application;
import android.content.ContentValues;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.thomasamsler.raffleapp.data.RaffleContract;

import java.util.Map;

/**
 * Created by tamsler on 11/29/14.
 */
public class MainApplication extends Application implements AppConstants {

    @Override
    public void onCreate() {

        super.onCreate();

        Firebase.setAndroidContext(this);
        Firebase fbRef = new Firebase("https://raffle-app.firebaseio.com/raffles");

        fbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, Map<String, Object>> data = (Map<String, Map<String, Object>>) snapshot.getValue();

                if(null != data) {

                    for(String key : data.keySet()) {

                        Map<String, Object> raffle = data.get(key);

                        ContentValues raffleValues = new ContentValues();
                        raffleValues.put(RaffleContract.RaffleEntry.COLUMN_RAFFLE_ID, (String) raffle.get("id"));
                        raffleValues.put(RaffleContract.RaffleEntry.COLUMN_RAFFLE_NAME, (String) raffle.get("name"));
                        raffleValues.put(RaffleContract.RaffleEntry.COLUMN_RAFFLE_PIN, (String) raffle.get("pin"));

                        getContentResolver().insert(RaffleContract.RaffleEntry.CONTENT_URI, raffleValues);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }

        });
    }
}
