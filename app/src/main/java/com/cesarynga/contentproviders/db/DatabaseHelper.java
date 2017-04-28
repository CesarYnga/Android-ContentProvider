package com.cesarynga.contentproviders.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tasks_list.db";
    private static final int DB_VERSION = 1;

    private static final String SQL_CREATE_TASKS =
            "CREATE TABLE " + TaskContract.Task.TABLE_NAME + " (" +
                    TaskContract.Task._ID + " INTEGER PRIMARY KEY," +
                    TaskContract.Task.COLUMN_NAME_TITLE + " TEXT," +
                    TaskContract.Task.COLUMN_NAME_DATE_TIME + " TEXT)";

    private static final String SQL_DELETE_TASKS =
            "DROP TABLE IF EXISTS " + TaskContract.Task.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TASKS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
