package com.btl.musicplayer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class CircleImageView extends androidx.appcompat.widget.AppCompatImageView {

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
        invalidate();
    }


    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        float rotate = this.getRotation();
        if (rotate == 360) {
            rotate = 0;
        }
        this.setRotation(rotate + 1f);
    }


}
