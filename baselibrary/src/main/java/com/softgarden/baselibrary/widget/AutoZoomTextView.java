package com.softgarden.baselibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;


/**
 * @author by DELL
 * @date on 2017/9/20
 * @describe
 */

public class AutoZoomTextView extends AppCompatTextView {
    private int maxWidth;
    private float defaultTextSize;

    public AutoZoomTextView(Context context) {
        this(context, null);
    }

    public AutoZoomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoZoomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            maxWidth = android.R.attr.maxWidth;
            maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        if (defaultTextSize == 0.0f) {
            defaultTextSize = getTextSize();
        }
        float textSize = defaultTextSize;
        paint.setTextSize(textSize);
        /***support zoom*/
        if (maxWidth == 0)
            maxWidth = getWidth();
        float textViewWidth = maxWidth - getPaddingLeft() - getPaddingRight();//不包含左右padding的空间宽度
        float textViewWidth1 = textViewWidth - paint.getFontSpacing() * 2;//不包含左右字体空间
        String text = getText().toString();
        float textWidth = paint.measureText(text);
        while (textWidth > textViewWidth1) {
            textSize--;
            paint.setTextSize(textSize);
            textWidth = paint.measureText(text);
            textViewWidth1 = textViewWidth - paint.getFontSpacing() * 2;
        }
        super.onDraw(canvas);
    }
}
