package com.iktwo.keebord.clipboard;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.iktwo.keebord.model.DatabaseContract;

public class ClipManagerService extends IntentService {
    private static final String TAG = ClipManagerService.class.getSimpleName();

    public static final String ACTION_INSERT = TAG + ".INSERT";
    public static final String ACTION_DELETE = TAG + ".DELETE";
    public static final String EXTRA_VALUES = TAG + ".ContentValues";
    private static final String WHERE_CONTENT = "content = ?";

    public ClipManagerService() {
        super(TAG);
    }

    public static void insertClip(Context context, ContentValues values) {
        Intent intent = new Intent(context, ClipManagerService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void deleteClip(Context context, ContentValues values) {
        Intent intent = new Intent(context, ClipManagerService.class);
        intent.setAction(ACTION_DELETE);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_INSERT.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsert(values);
        } else if (ACTION_DELETE.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performDelete(values);
        }
    }

    private void performInsert(ContentValues values) {
        if (getContentResolver().insert(DatabaseContract.CONTENT_URI, values) == null) {
            Log.e(TAG, "Error inserting new task");
        }
    }

    private void performDelete(ContentValues values) {
        String[] args = new String[] {values.getAsString(DatabaseContract.ClipboardColumns.CONTENT)};

        getContentResolver().delete(DatabaseContract.CONTENT_URI, WHERE_CONTENT, args);
    }
}
