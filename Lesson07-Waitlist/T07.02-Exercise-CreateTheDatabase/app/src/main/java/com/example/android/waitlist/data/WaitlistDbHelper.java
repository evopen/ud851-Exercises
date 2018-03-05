package com.example.android.waitlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WaitlistDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "waitlist.db";
    public static final int DATABALSE_VERSION = 1;


    public WaitlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABALSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " +
                WaitlistContract.WaitlistEntry.TABLE_NAME + " (" +
                WaitlistContract.WaitlistEntry._ID + " INTERGER PRIMERY KEY," +
                WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL," +
                WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE + " INTERGER NOT NULL," +
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL_DROP_WAITLIST_TABLE = "DROP TABLE " +
                WaitlistContract.WaitlistEntry.TABLE_NAME + ";";
        sqLiteDatabase.execSQL(SQL_DROP_WAITLIST_TABLE);
        onCreate(sqLiteDatabase);
    }

}