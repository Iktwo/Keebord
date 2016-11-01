package com.iktwo.keebord.model;

public interface ClipDelegateHandler {
    void onClipSelected(Clip clip);
    void onRemoveClipSelected(Clip clip, Integer position);
}
