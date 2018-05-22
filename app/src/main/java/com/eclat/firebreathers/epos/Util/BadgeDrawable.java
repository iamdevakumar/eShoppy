package com.eclat.firebreathers.epos.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * Created by AndroidPC-1 on 05-11-2016.
 */
    public class BadgeDrawable extends Drawable {
        private float mTextSize;
        private Paint mBadgePaint;
        private Paint mTextPaint;
        private Rect mTxtRect = new Rect();
        private String mCount = "";
        private boolean mWillDraw = false;
        public BadgeDrawable(Context context) {
            //mTextSize = context.getResources().getDimension(R.dimen.badge_text_size);
            mTextSize = 22F;
            mBadgePaint = new Paint();
            mBadgePaint.setColor(Color.GREEN);
            mBadgePaint.setAntiAlias(true);
            mBadgePaint.setStyle(Paint.Style.FILL);
            mTextPaint = new Paint();
            mTextPaint.setColor(Color.BLACK);
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        public void draw(Canvas canvas) {
            if (!mWillDraw) {
                return;
            }

            Rect bounds = getBounds();
            float width = bounds.right - bounds.left;
            float height = bounds.bottom - bounds.top;

            // Position the badge in the top-right quadrant of the icon.
            //float radius = ((Math.min(width, height) / 2) - 1) / 2;
            float radius1 = ((Math.min(width, height) / 2) - 1) / 1;
            float centerX = width + radius1;
            float centerY = radius1 +1;

            // Draw badge circle.
            canvas.drawCircle(centerX, centerY, radius1, mBadgePaint);

            // Draw badge count text inside the circle.
            mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
            float textHeight = mTxtRect.bottom - mTxtRect.top;
            float textY = centerY + (textHeight / 2f);
            canvas.drawText(mCount, centerX, textY, mTextPaint);
        }

        public void setCount(int count) {
            mCount = Integer.toString(count);
            mWillDraw = count > 0;
            invalidateSelf();
        }

        @Override
        public void setAlpha(int alpha) {
            // do nothing
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            // do nothing
        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }

