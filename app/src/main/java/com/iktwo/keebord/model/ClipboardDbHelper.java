package com.iktwo.keebord.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClipboardDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "clipboard.db";
    private static final int DB_VERSION = 1;

    /// TODO: set a limit to how many items can be stored 50 and implement logic to replace last one
    /// consider moving items that are used recently LRU
    // UNIQUE(clip_text) ON CONFLICT REPLACE

    private static final String SQL_CREATE_CLIPBOARD = String.format("CREATE TABLE IF NOT " +
                    "EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL unique)",
            DatabaseContract.TABLE_CLIPBOARD,
            DatabaseContract.ClipboardColumns._ID,
            DatabaseContract.ClipboardColumns.CONTENT
    );

    public ClipboardDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLIPBOARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_CLIPBOARD);

        onCreate(db);
    }
}