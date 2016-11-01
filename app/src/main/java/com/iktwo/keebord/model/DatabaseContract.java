package com.iktwo.keebord.model;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_CLIPBOARD = "clipboard";
    public static final String CONTENT_AUTHORITY = "com.iktwo.keebord";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_CLIPBOARD)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static final class ClipboardColumns implements BaseColumns {
        public static final String CONTENT = "content";
    }
}