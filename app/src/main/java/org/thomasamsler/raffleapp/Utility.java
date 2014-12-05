package org.thomasamsler.raffleapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;

/**
 * Created by tamsler on 12/4/14.
 */
public class Utility {

    public static String getPreferenceUser(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_user_key), null);
    }

    public static String getPreferenceUserDigest(Context context) {

        String user = getPreferenceUser(context);

        return new String(Hex.encodeHex(DigestUtils.md5(user)));
    }

    public static void setPreferenceUser(Context context, String user) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_user_key), user);
        editor.commit();
    }
}
