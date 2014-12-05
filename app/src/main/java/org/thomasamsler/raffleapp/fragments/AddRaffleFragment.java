package org.thomasamsler.raffleapp.fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.data.RaffleContract;
import org.thomasamsler.raffleapp.data.RaffleContract.RaffleEntry;
import org.thomasamsler.raffleapp.models.Raffle;

import java.util.UUID;

/**
 * Created by tamsler on 11/21/14.
 */
public class AddRaffleFragment extends Fragment implements AppConstants {

    private EditText mRaffleNameEditText;
    private EditText mRafflePinEditText;
    private Button mCreateRaffleButton;

    public static AddRaffleFragment newInstance() {

        AddRaffleFragment fragment = new AddRaffleFragment();
        return fragment;
    }

    public AddRaffleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_raffle, container, false);

        mRaffleNameEditText = (EditText) rootView.findViewById(R.id.fragment_add_raffle_name_edit_text);
        mRafflePinEditText = (EditText) rootView.findViewById(R.id.fragment_add_raffle_pin_edit_text);
        mCreateRaffleButton = (Button) rootView.findViewById(R.id.fragment_add_raffle_button);
        mCreateRaffleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String raffleId = UUID.randomUUID().toString();

                Raffle raffle = new Raffle(
                        raffleId,
                        mRaffleNameEditText.getText().toString(),
                        mRafflePinEditText.getText().toString());

                ContentValues raffleValues = new ContentValues();
                raffleValues.put(RaffleEntry.COLUMN_RAFFLE_ID, raffle.getId());
                raffleValues.put(RaffleEntry.COLUMN_RAFFLE_NAME, raffle.getName());
                raffleValues.put(RaffleEntry.COLUMN_RAFFLE_PIN, raffle.getPin());

                getActivity().getContentResolver().insert(RaffleEntry.CONTENT_URI, raffleValues);

                // https://www.firebase.com/docs/android/guide/saving-data.html
                Firebase.setAndroidContext(getActivity());
                Firebase fbRef = new Firebase("https://raffle-app.firebaseio.com/raffles/" + raffle.getId());
                fbRef.setValue(raffle, new Firebase.CompletionListener() {

                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                        Firebase ref = new Firebase("https://raffle-app.firebaseio.com/raffles/");
                        if (firebaseError != null) {

                            Log.d(LOG_TAG, "Data could not be saved. " + firebaseError.getMessage());
                        }
                        else {

                            Log.d(LOG_TAG, "Data saved successfully.");
                            getActivity().finish();
                        }
                    }
                });
            }
        });

        return rootView;
    }
}
