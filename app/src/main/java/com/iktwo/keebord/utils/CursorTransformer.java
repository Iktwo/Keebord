package com.iktwo.keebord.utils;

import android.database.Cursor;

public interface CursorTransformer {
    Object transform(Cursor cursor);
}
