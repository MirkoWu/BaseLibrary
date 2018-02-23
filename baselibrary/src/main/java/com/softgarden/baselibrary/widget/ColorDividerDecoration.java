package com.softgarden.baselibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Lightwave on 2016/7/5.
 */
public class ColorDividerDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = OrientationHelper.VERTICAL;

    public static final int BEGINNING_MIDDLE = 0;
    public static final int MIDDLE = 1;
    public static final int MIDDLE_END = 2;

    private Context mContext;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 方向 默认垂直方向
     */
    private int mOrientation = VERTICAL;
    /**
     * 分割线颜色
     */
    private int mDividerColor;
    /**
     * 分割线高度
     */
    private int mDividerHeight;
    /**
     * 分割线显示模式 头部和中间  仅显示中间  中间和底部
     */
    private int mShowDividers;

//    private int mPadding;//
//    private int mLeftPadding;//
//    private int mRightPadding;//


    public ColorDividerDecoration(Context mContext) {
        this(mContext, VERTICAL, Color.parseColor("#CCC"), 1, BEGINNING_MIDDLE);
    }
    public ColorDividerDecoration(Context mContext, @ColorInt int mDividerColor) {
        this(mContext, VERTICAL, mDividerColor, 1, BEGINNING_MIDDLE);
    }
     public ColorDividerDecoration(Context mContext, @ColorInt int mDividerColor, int mDividerHeight) {
        this(mContext, VERTICAL, mDividerColor, mDividerHeight, BEGINNING_MIDDLE);
    }
    public ColorDividerDecoration(Context mContext, int mOrientation, @ColorInt int mDividerColor, int mDividerHeight) {
        this(mContext, mOrientation, mDividerColor, mDividerHeight, BEGINNING_MIDDLE);
    }

 public ColorDividerDecoration(Context mContext, int mOrientation, @ColorInt int mDividerColor, int mDividerHeight, int mShowDividers) {
        this.mContext = mContext;
        this.mOrientation = mOrientation;
        this.mDividerColor = mDividerColor;
        this.mDividerHeight = mDividerHeight;
        this.mShowDividers = mShowDividers;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mDividerColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * @param c
     * @param parent
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        childCount = mShowDividers == MIDDLE ? childCount - 1 : childCount;//不画最后一个

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    /**
     * @param c
     * @param parent
     */

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        childCount = mShowDividers == MIDDLE ? childCount - 1 : childCount;//不画最后一个

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            if (mShowDividers == BEGINNING_MIDDLE) {
                outRect.set(0, mDividerHeight, 0, 0);
            } else {
                outRect.set(0, 0, 0, mDividerHeight);
            }
        } else {
            if (mShowDividers == BEGINNING_MIDDLE) {
                outRect.set(mDividerHeight, 0, 0, 0);
            } else {
                outRect.set(0, 0, mDividerHeight, 0);
            }
        }
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }
}
