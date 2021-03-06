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
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class RoundShowImage extends ImageView {
    private int mLeftOffset = 0;
    private int mTopOffset = 0;
    private int mRadius = 1, mMaxRadius = 0;
    private boolean mIsSetMaxRadius = false;
    private int mRate;
    private int mMeasuredWidth, mMeasuredHeight, mOldMeasuredWidth, mOldMeasuredHeight;
    private boolean mIsSizeChanged = false;
    private int mSlashLen = 0;
    private boolean mStartShow = false;
    private int mOriginalX, mOriginalY;

    public RoundShowImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRate = 1;
    }

    public RoundShowImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundShowImage(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
        if(mOldMeasuredWidth != 0 && mOldMeasuredHeight != 0
                && (mMeasuredWidth != mOldMeasuredWidth || mMeasuredHeight != mOldMeasuredHeight)){
            mIsSizeChanged = true;
        }

        mOldMeasuredHeight = mMeasuredHeight;
        mOldMeasuredWidth = mMeasuredWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(!mStartShow){
            canvas.drawARGB(0, 0, 0, 0);
            return;
        }

        mOriginalX = mMeasuredWidth/2 + mLeftOffset;
        mOriginalY = mMeasuredHeight/2 + mTopOffset;
        if(!mIsSetMaxRadius){
            mMaxRadius = (int)getSlashLen();
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

        Rect bounds = ((BitmapDrawable)getDrawable()).getBounds();
        RectF rectF = new RectF(bounds);
        int saveCount = canvas.saveLayer(rectF, null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.drawBitmap(circleBitmap, mOriginalX - mRadius, mOriginalY - mRadius, mOriginalPaint);
        Xfermode oldMode = mOriginalPaint.getXfermode();
        mOriginalPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mOriginalPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //draw the image
        super.onDraw(canvas);
        mOriginalPaint.setXfermode(oldMode);
        canvas.restoreToCount(saveCount);

        Log.i(Debug.DEBUG_TAG, "on RoundShowImage onDraw mRadius=" + mRadius + ", mRate=" + mRate);
        if(mRadius < mMaxRadius){
            mRadius += mRate;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setLeftOffset(int offset){
        if(offset > getMeasuredWidth()){
            return;
        }
        this.mLeftOffset = offset;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setTopOffset(int offset){
        if(offset > getMeasuredHeight()){
            return;
        }
        this.mTopOffset = offset;
        ViewCompat.postInvalidateOnAnimation(this);;
    }

    public void setMaxRadius(int radius){
        if(radius > getSlashLen())
            return;

        this.mMaxRadius = radius;
        this.mIsSetMaxRadius = true;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setRate(int rate){
        Log.i(Debug.DEBUG_TAG, "on RoundShowImage setRate");
        if(rate < 0)
            return;
        this.mRate = rate;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private int getSlashLen(){
        if(!mIsSizeChanged && mSlashLen != 0){
            return mSlashLen;
        }

        int deltaX, deltaY;
        deltaX = deltaY = 0;
        int halfMeasuredW = mMeasuredWidth/2;
        int halfMeasuredH = mMeasuredHeight/2;
        if(mOriginalX < halfMeasuredW && mOriginalY < halfMeasuredH){
            deltaX = mMeasuredWidth - mOriginalX;
            deltaY = mMeasuredHeight - mOriginalY;
        }else if(mOriginalX < halfMeasuredW && mOriginalY >= halfMeasuredH){
            deltaX = mMeasuredWidth - mOriginalX;
            deltaY = mOriginalY;
        }else if(mOriginalX >= halfMeasuredW && mOriginalY < halfMeasuredH){
            deltaX = mOriginalX;
            deltaY = mMeasuredHeight - mOriginalY;
        }else if(mOriginalX >= halfMeasuredW && mOriginalY >= halfMeasuredH){
            deltaX = mOriginalX;
            deltaY = mOriginalY;
        }

        mSlashLen = (int)Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        return mSlashLen;
    }

    public void startShow(){
        this.mStartShow = true;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public boolean isStartShow(){
        return mStartShow;
    }

    public void stopShow(){
        this.mStartShow = false;
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
