package com.cesarynga.contentproviders.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public final class TaskProvider extends ContentProvider {

    private static final int TASKS = 1;
    private static final int TASK_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASK, TASKS);
        uriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASK + "/#", TASK_ID);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;
            case TASK_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = TaskContract.Task._ID + " = " + uri.getLastPathSegment();
                } else {
                    selection += " AND " + TaskContract.Task._ID + " = " + uri.getLastPathSegment();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }

        cursor = db.query(TaskContract.Task.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TASKS:
                return TaskContract.Task.CONTENT_TYPE;
            case TASK_ID:
                return TaskContract.Task.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        final long id;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                id = db.insert(TaskContract.Task.TABLE_NAME, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        final int deletedRows;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                deletedRows = db.delete(TaskContract.Task.TABLE_NAME, selection, selectionArgs);
                break;
            case TASK_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = TaskContract.Task._ID + " = " + uri.getLastPathSegment();
                } else {
                    selection += " AND " + TaskContract.Task._ID + " = " + uri.getLastPathSegment();
                }
                deletedRows = db.delete(TaskContract.Task.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        final int updatedRows;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                updatedRows = db.update(TaskContract.Task.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TASK_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = TaskContract.Task._ID + " = " + uri.getLastPathSegment();
                } else {
                    selection += " AND " + TaskContract.Task._ID + " = " + uri.getLastPathSegment();
                }
                updatedRows = db.update(TaskContract.Task.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        return updatedRows;
    }
}
