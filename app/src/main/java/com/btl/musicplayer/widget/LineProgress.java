package com.btl.musicplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class LineProgress extends ProgressBar {

    public LineProgress(Context context) {
        super(context);
        init();
    }

    public LineProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LineProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }
}
