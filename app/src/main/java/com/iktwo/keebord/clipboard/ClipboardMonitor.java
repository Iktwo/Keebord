package com.iktwo.keebord.clipboard;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.iktwo.keebord.model.DatabaseContract;

public class ClipboardMonitor extends Service {
    private static final String TAG = ClipboardMonitor.class.getSimpleName();
    private static String lastCopiedString;
    private ClipboardManager clipboardManager;
    private boolean isListenerRegistered;
    private ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener;

    @Override
    public void onCreate() {
        super.onCreate();

        onPrimaryClipChangedListener = () -> {
            ClipData clipData = clipboardManager.getPrimaryClip();

            // Log.d(TAG, "clipData size: " + clipData.getItemCount());

            if (clipData.getDescription().getMimeType(0).equals(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                    clipData.getDescription().getMimeType(0).equals(ClipDescription.MIMETYPE_TEXT_HTML)) {

                if (clipData.getItemAt(0).getText() != null) {
                    String text = clipData.getItemAt(0).getText().toString();

                    if (lastCopiedString == null || !lastCopiedString.equals(text)) {
                        lastCopiedString = text;

                        ContentValues values = new ContentValues(1);
                        values.put(DatabaseContract.ClipboardColumns.CONTENT, text);

                        ClipManagerService.insertClip(ClipboardMonitor.this, values);

                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        if (!isListenerRegistered) {
            clipboardManager.addPrimaryClipChangedListener(onPrimaryClipChangedListener);
            isListenerRegistered = true;
        }

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isListenerRegistered) {
            clipboardManager.removePrimaryClipChangedListener(onPrimaryClipChangedListener);
            isListenerRegistered = false;
        }
    }
}
