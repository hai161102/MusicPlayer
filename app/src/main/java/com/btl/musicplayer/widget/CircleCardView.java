package com.btl.musicplayer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class CircleCardView extends CardView {

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


    public CircleCardView(Context context) {
        super(context);
    }

    public CircleCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public CircleCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(){
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
