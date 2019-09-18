package com.example.bfs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by surpr on 2017/12/27.
 */

public class CustomExpanableListView extends ExpandableListView {

    public CustomExpanableListView(Context context) {
        super(context);
    }

    public CustomExpanableListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomExpanableListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}