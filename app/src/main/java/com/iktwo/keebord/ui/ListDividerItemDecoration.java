package com.iktwo.keebord.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.iktwo.keebord.R;

public class ListDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;

    public ListDividerItemDecoration(Context context) {
        divider = ResourcesCompat.getDrawable(context.getResources(), R.drawable.list_divider,
                context.getTheme());
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            if (parent.getChildAdapterPosition(child) == (parent.getAdapter().getItemCount() - 1)) {
                bottom = top;
            }

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}