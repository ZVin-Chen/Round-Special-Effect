package com.zvin.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImage extends ImageView {
    private int mLeftOffset = 0;
    private boolean mLeftOffsetCustom = false;
    private int mTopOffset = 0;
    private boolean mTopOffsetCustom = false;
    private int mRadius = 0;
    private boolean mRadiusCustom = false;

    public CircleImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImage(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if(!mRadiusCustom){

            mRadius = Math.min(measuredHeight, measuredWidth)/2;
        }

        if(!mLeftOffsetCustom){
            mLeftOffset = measuredWidth/2 - mRadius;
        }

        if(!mTopOffsetCustom){
            mTopOffset = measuredHeight/2 - mRadius;
        }

        Bitmap circleBitmap = Bitmap.createBitmap(mRadius * 2, mRadius * 2, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(circleBitmap);
        Paint mImgPaint = new Paint();
        mImgPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mImgPaint.setAntiAlias(true);
        c.drawCircle(mRadius, mRadius, mRadius, mImgPaint);

        Paint mOriginalPaint = ((BitmapDrawable)getDrawable()).getPaint();
        mOriginalPaint.setAntiAlias(true);
        mOriginalPaint.setColor(getContext().getResources().getColor(android.R.color.holo_blue_light));

        //set the mode
        Rect bounds = ((BitmapDrawable)getDrawable()).getBounds();
        RectF rectF = new RectF(bounds);
        int saveCount = canvas.saveLayer(rectF, null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.drawBitmap(circleBitmap, mLeftOffset, mTopOffset, mOriginalPaint);
        Xfermode oldMode = mOriginalPaint.getXfermode();
        mOriginalPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mOriginalPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //draw the above bitmap
        super.onDraw(canvas);
        mOriginalPaint.setXfermode(oldMode);
        canvas.restoreToCount(saveCount);
    }

    public void setLeftOffset(int offset){
        if(offset > getMeasuredWidth()){
            return;
        }
        this.mLeftOffset = offset;
        invalidate();
    }

    public void setTopOffset(int offset){
        if(offset > getMeasuredHeight()){
            return;
        }
        this.mTopOffset = offset;
        invalidate();
    }

    public void setRadius(int radius){
        if(radius > getSlashLen())
            return;

        this.mRadius = radius;
        invalidate();
    }

    private double getSlashLen(){
        float halfWidth = getWidth()/2;
        float halfHeight = getHeight()/2;

        return Math.sqrt(Math.pow(halfWidth, 2) + Math.pow(halfHeight, 2));
    }

    public void setRadiusCustom(boolean custom){
        this.mRadiusCustom = custom;
    }

    public void setLeftOffsetCustom(boolean custom){
        this.mLeftOffsetCustom = custom;
    }

    public void setTopOffsetCustom(boolean custom){
        this.mTopOffsetCustom = custom;
    }
}
