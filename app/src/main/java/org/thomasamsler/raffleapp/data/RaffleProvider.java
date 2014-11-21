package org.thomasamsler.raffleapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by tamsler on 11/20/14.
 */
public class RaffleProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RaffleDbHelper mOpenHelper;

    private static final int RAFFLE = 100;

    private static final SQLiteQueryBuilder sRaffleQueryBuilder;

    static {

        sRaffleQueryBuilder = new SQLiteQueryBuilder();
        sRaffleQueryBuilder.setTables(
                RaffleContract.RaffleEntry.TABLE_NAME
        );
    }

    @Override
    public boolean onCreate() {

        mOpenHelper = new RaffleDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch(sUriMatcher.match(uri)) {

            case RAFFLE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RaffleContract.RaffleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch(match) {

            case RAFFLE:
                return RaffleContract.RaffleEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match) {

            case RAFFLE:
                long _id = db.insert(RaffleContract.RaffleEntry.TABLE_NAME, null, values);

                if(_id > 0) {

                    returnUri = RaffleContract.RaffleEntry.buildRaffleUri(_id);
                }
                else {

                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch(match) {

            case RAFFLE:
                rowsDeleted = db.delete(RaffleContract.RaffleEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // NOTE: null deletes all rows
        if (selection == null || rowsDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch(match) {

            case RAFFLE:

                rowsUpdated = db.update(RaffleContract.RaffleEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RaffleContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RaffleContract.PATH_RAFFLE, RAFFLE);

        return matcher;
    }
}
