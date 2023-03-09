package com.btl.musicplayer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class CircleViewGroup extends ViewGroup {

    private final int FPS = 100;
    private boolean spin = true;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (spin) {
                update();
            }
            postDelayed(this, 1000 / FPS);

        }
    };

    private void update() {
        float rotate = this.getRotation();
        if (rotate == 360) {
            rotate = 0;
        }
        this.setRotation(rotate + 1f);
        invalidate();
    }


    public CircleViewGroup(Context context) {
        super(context);
    }

    public CircleViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public CircleViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void startSpin() {
        post(runnable);
    }

    public void stopSpin() {
        spin = false;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


}
