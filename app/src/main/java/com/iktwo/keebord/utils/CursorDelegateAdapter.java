package com.iktwo.keebord.utils;

import android.database.Cursor;
import android.support.annotation.NonNull;

public class CursorDelegateAdapter extends DelegateAdapter {
    private Cursor cursor;
    private CursorTransformer cursorTransformer;

    public CursorDelegateAdapter(@NonNull Cursor cursor, CursorTransformer cursorTransformer) {
        this.cursor = cursor;
        this.cursorTransformer = cursorTransformer;
    }

    public CursorDelegateAdapter(@NonNull CursorTransformer cursorTransformer) {
        this.cursorTransformer = cursorTransformer;
    }

    public void swapCursor(Cursor cursor) {
        if (this.cursor != null) {
            this.cursor.close();
        }

        this.cursor = cursor;

        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        if (cursor.moveToPosition(position)) {
            return cursorTransformer.transform(cursor);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
