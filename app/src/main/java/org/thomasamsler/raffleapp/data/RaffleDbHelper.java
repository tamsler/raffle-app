package org.thomasamsler.raffleapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.thomasamsler.raffleapp.data.RaffleContract.RaffleEntry;

/**
 * Created by tamsler on 11/20/14.
 */
public class RaffleDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "raffle.db";

    public RaffleDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_RAFFLE_TABLE = "CREATE TABLE " + RaffleEntry.TABLE_NAME + " (" +
                RaffleEntry._ID + " TEXT PRIMARY KEY," +
                RaffleEntry.COLUMN_RAFFLE_NAME + " TEXT UNIQUE NOT NULL," +
                RaffleEntry.COLUMN_RAFFLE_PIN + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_RAFFLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + RaffleEntry.TABLE_NAME);
        onCreate(db);
    }
}
