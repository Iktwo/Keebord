package com.iktwo.keebord.utils;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class BindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final T binding;

    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

