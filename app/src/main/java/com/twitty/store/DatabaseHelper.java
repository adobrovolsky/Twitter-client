package com.twitty.store;

import com.twitty.MainApplication;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper() {
        super(MainApplication.getContext(), DatabaseContract.DATABASE_NAME,
                null, DatabaseContract.DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        createDraftTable(db);
        createMediaTable(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP DATABASE IF EXISTS " + DatabaseContract.DATABASE_NAME);
        onCreate(db);
    }

    private void createDraftTable(SQLiteDatabase db) {
        final String sql =  "CREATE TABLE " + DatabaseContract.DraftTable.TABLE_NAME
                + "("
                + DatabaseContract.DraftTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.DraftTable.COLUMN_TEXT + " TEXT, "
                + DatabaseContract.DraftTable.COLUMN_LOCATION + " BLOB, "
                + DatabaseContract.DraftTable.COLUMN_CREATED + " DATETIME "
                + ")";

        db.execSQL(sql);
    }

    private void createMediaTable(SQLiteDatabase db) {
        final String sql = "CREATE TABLE " + DatabaseContract.MediaTable.TABLE_NAME
                + "("
                + DatabaseContract.MediaTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.MediaTable.COLUMN_PATH + " TEXT, "
                + DatabaseContract.MediaTable.COLUMN_TYPE + " TEXT, "
                + DatabaseContract.MediaTable.COLUMN_DRAFT_ID + " INTEGER "
                + ")";

        db.execSQL(sql);
    }
}
