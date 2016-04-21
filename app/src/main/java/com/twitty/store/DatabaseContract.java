package com.twitty.store;

import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final String DATABASE_NAME = "twitty.db";
    public static final int DATABASE_VERSION = 1;

    public static abstract class DraftTable implements BaseColumns {
        public static final String TABLE_NAME = "draft";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_CREATED = "created";
    }

    public static abstract class MediaTable implements BaseColumns {
        public static final String TABLE_NAME = "media";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DRAFT_ID = "draft_id";
    }
}
