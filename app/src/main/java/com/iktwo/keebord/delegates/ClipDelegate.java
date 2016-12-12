package com.iktwo.keebord.delegates;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iktwo.binder.BindingViewHolder;
import com.iktwo.binder.ViewDelegate;
import com.iktwo.keebord.R;
import com.iktwo.keebord.databinding.DelegateClipBinding;
import com.iktwo.keebord.model.Clip;
import com.iktwo.keebord.model.ClipDelegateHandler;

public class ClipDelegate extends ViewDelegate<BindingViewHolder<DelegateClipBinding>> {
    private ClipDelegateHandler handler;

    public ClipDelegate(ClipDelegateHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getItemViewType() {
        return Delegates.CLIP;
    }

    @Override
    public BindingViewHolder<DelegateClipBinding> onCreateViewHolder(ViewGroup viewGroup) {
        DelegateClipBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.delegate_clip, viewGroup, false);

        binding.setHandler(handler);

        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<DelegateClipBinding> viewHolder, Object item, int position) {
        viewHolder.binding.setIndex(position);
        viewHolder.binding.setModel((Clip) item);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public boolean canHandleItemTypeAtPosition(Object item, int position) {
        return item instanceof Clip;
    }
}

