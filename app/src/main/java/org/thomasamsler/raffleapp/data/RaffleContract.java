package org.thomasamsler.raffleapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tamsler on 11/20/14.
 */
public class RaffleContract {

    public static final String CONTENT_AUTHORITY = "org.thomasamsler.raffleapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RAFFLE = "raffle";

    public static final class RaffleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RAFFLE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_RAFFLE;

        // Table name
        public static final String TABLE_NAME = "raffle";

        public static final String COLUMN_RAFFLE_ID = RaffleEntry._ID;

        public static final String COLUMN_RAFFLE_NAME = "name";

        public static final String COLUMN_RAFFLE_PIN = "pin";

        public static Uri buildRaffleUri(long id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIdFromRaffleUri(Uri uri) {

            return uri.getPathSegments().get(1);
        }
    }
}
