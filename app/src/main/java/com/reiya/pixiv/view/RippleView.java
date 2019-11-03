package com.reiya.pixiv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import tech.yojigen.pivisionm.R;

/**
 * Created by zhengyirui on 2017/9/1.
 */

public class RippleView extends View {
    private float mX = -1;
    private float mY = -1;
    private int mRadius = 0;
    private int mMaxRadius;
    private Paint mPaint = new Paint();

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        int color = typedArray.getColor(R.styleable.RippleView_ripple_color, Color.WHITE);
        typedArray.recycle();

        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxRadius = (int) (h * 1.2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mX > -1 && mY > -1) {
            canvas.drawCircle(mX, mY, mRadius, mPaint);
            mRadius += 80;
            if (mRadius < mMaxRadius) {
                invalidate();
            }
        }
    }

    public void start(float x, float y) {
        mX = x;
        mY = y;
        invalidate();
    }

    public void reset() {
        mRadius = 0;
        mX = -1;
        mY = -1;
        invalidate();
    }
}
