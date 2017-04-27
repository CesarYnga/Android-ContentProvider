package com.cesarynga.contentproviders.db;

import android.provider.BaseColumns;

public final class TaskContract {

    private TaskContract() {
    }

    public static class Task implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";
    }
}
