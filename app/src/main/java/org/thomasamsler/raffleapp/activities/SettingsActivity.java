package org.thomasamsler.raffleapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import org.thomasamsler.raffleapp.AppConstants;
import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.Utility;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, AppConstants {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onResume() {

        super.onResume();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        Preference userPreference = getPreferenceScreen().findPreference(getString(R.string.pref_user_key));

        userPreference.setSummary(Utility.getPreferenceUser(this));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.pref_user_key))) {

            Preference userPreference = getPreferenceScreen().findPreference(key);

            userPreference.setSummary(Utility.getPreferenceUser(this));
        }
    }

    @Override
    public void onPause() {

        super.onPause();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
