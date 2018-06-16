package com.iktwo.keebord;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.iktwo.binder.CursorDelegateAdapter;
import com.iktwo.binder.CursorTransformer;
import com.iktwo.keebord.clipboard.ClipManagerService;
import com.iktwo.keebord.clipboard.ClipboardMonitor;
import com.iktwo.keebord.databinding.KeyboardBinding;
import com.iktwo.keebord.delegates.ClipDelegate;
import com.iktwo.keebord.model.Clip;
import com.iktwo.keebord.model.ClipDelegateHandler;
import com.iktwo.keebord.model.DatabaseContract;
import com.iktwo.keebord.ui.ListDividerItemDecoration;

public class Keebord extends InputMethodService implements KeyboardView.OnKeyboardActionListener,
        Loader.OnLoadCompleteListener<Cursor>,
        ClipDelegateHandler {
    private static final String TAG = Keebord.class.getSimpleName();
    private static final int CURSOR_LOADER = 8;

    private CursorDelegateAdapter adapter;
    private RecyclerView.AdapterDataObserver adapterDataObserver;

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.unregisterAdapterDataObserver(adapterDataObserver);
    }

    @Override
    public View onCreateInputView() {
        final KeyboardBinding binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.keyboard, null, false);

        getApplicationContext().startService(new Intent(getApplicationContext(),
                ClipboardMonitor.class));

        CursorLoader cursorLoader = new CursorLoader(this,
                DatabaseContract.CONTENT_URI, null, null, null, null);

        cursorLoader.registerListener(CURSOR_LOADER, this);
        cursorLoader.startLoading();

        binding.buttonClose.setOnClickListener(v -> goToPreviousKeyboard());

        adapter = new CursorDelegateAdapter(null, new CursorTransformer() {
            @NonNull
            @Override
            public Object transform(Cursor cursor) {
               return new Clip(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipboardColumns.CONTENT)));

            }
        });

        adapter.registerDelegate(new ClipDelegate(this));

        binding.textViewEmpty.setText(R.string.no_data);
        updateTextViewEmpty(binding.textViewEmpty);

        adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                updateTextViewEmpty(binding.textViewEmpty);
            }
        };

        adapter.registerAdapterDataObserver(adapterDataObserver);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.addItemDecoration(new ListDividerItemDecoration(getApplicationContext()));

        return binding.getRoot();
    }

    private void goToPreviousKeyboard() {
        try {
            ((InputMethodManager) Keebord.this
                    .getSystemService(INPUT_METHOD_SERVICE))
                    .switchToLastInputMethod(Keebord.this
                            .getWindow()
                            .getWindow()
                            .getAttributes()
                            .token);

        } catch (NullPointerException e) {
            Log.e(TAG, "Could not switch to previous keyboard");
        }
    }

    @Override
    public void onClipSelected(Clip clip) {
        writeText(clip.getContent());

        /// TODO: Add option to set if pasting something should go to the previous keyboard
    }

    @Override
    public void onRemoveClipSelected(Clip clip, Integer position) {
        if (adapter.getCursor().moveToPosition(position)) {
            ContentValues values = new ContentValues();
            DatabaseUtils.cursorStringToContentValues(adapter.getCursor(),
                    DatabaseContract.ClipboardColumns.CONTENT, values);

            ClipManagerService.deleteClip(Keebord.this, values);
        }
    }

    private void updateTextViewEmpty(View textViewEmpty) {
        if (adapter != null) {
            if (adapter.getItemCount() == 0) {
                textViewEmpty.setVisibility(View.VISIBLE);
            } else {
                textViewEmpty.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    private void writeText(String text) {
        if (text != null) {
            InputConnection ic = getCurrentInputConnection();
            ic.commitText(text, 1);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();

        switch (primaryCode) {
            default:
                char code = (char) primaryCode;

                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onText(CharSequence charSequence) {
        Log.d(TAG, "onText: " + charSequence);
    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }
}
