package com.kutear.kutear_chart.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.kutear.kutear_chart.R;


/**
 * @author: kutear.guo
 * @create: 2017/3/20 20:06
 */

public class TrendView extends AbsChartView {
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private Paint mLinePaint;
    private Paint mFillPaint;
    private Path mLinePath;
    private Path mFillPath;
    private int mStartColor;
    private int mEndColor;
    private int mFillStartColor = DEFAULT_FILL_COLOR;
    private int mFillEndColor = DEFAULT_FILL_COLOR;
    private static final float DEFAULT_LINE_WIDTH = 0.3f;

    public TrendView(Context context) {
        this(context, null);
    }

    public TrendView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public TrendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TrendView, defStyleAttr, 0);
        int n = ta.getIndexCount();
        float defaultLineSize = dip2px(context, DEFAULT_LINE_WIDTH);
        mStartColor = Color.GRAY;
        mEndColor = Color.GRAY;
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.TrendView_trend_line_start_color:
                    mStartColor = ta.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.TrendView_trend_line_end_color:
                    mEndColor = ta.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.TrendView_trend_line_size:
                    defaultLineSize = ta.getDimension(attr, defaultLineSize);
                    break;
                case R.styleable.TrendView_trend_fill_start_color:
                    mFillStartColor = ta.getColor(attr, DEFAULT_FILL_COLOR);
                    break;
                case R.styleable.TrendView_trend_fill_end_color:
                    mFillEndColor = ta.getColor(attr, DEFAULT_FILL_COLOR);
                    break;
            }
        }
        ta.recycle();
        mLinePaint = buildPaint(mStartColor);
        mLinePaint.setStrokeWidth(defaultLineSize);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mFillPaint = buildPaint(mFillStartColor);
        mLinePath = new Path();
        mFillPath = new Path();
        setOffset(0);
    }


    @Override
    protected void onDrawGraph(Canvas canvas) {
        mLinePaint.setShader(new LinearGradient(0, 0, axisWidth(), 0, mStartColor, mEndColor, Shader.TileMode.MIRROR));
        LinearGradient fillColor = new LinearGradient(0, 0, 0, -axisHeight(), mFillStartColor, mFillEndColor, Shader.TileMode.CLAMP);
        mFillPaint.setShader(fillColor);
        canvas.save();
        canvas.translate(mOriginalX, mHeight - mOriginalY);
        int count = getCount();
        if (count <= 0) {
            return;
        }
        float singleWidth = getCellWidth();
        mFillPath.reset();
        mLinePath.reset();
        mFillPath.moveTo(0, 0);
        for (int i = 0; i < count; i++) {
            float drawHeight = getHeightOfIndex(i);
            float center = singleWidth * i;
            if (i == 0) {
                mLinePath.moveTo(center, -drawHeight);
            } else {
                mLinePath.lineTo(center, -drawHeight);
            }
            mFillPath.lineTo(center, -drawHeight);
            if (i == count - 1) {
                mFillPath.lineTo(center, 0);
                mFillPath.lineTo(0, 0);
            }
        }
        canvas.drawPath(mLinePath, mLinePaint);
        if (mFillEndColor != Color.TRANSPARENT || mFillStartColor != Color.TRANSPARENT) {
            canvas.drawPath(mFillPath, mFillPaint);
        }
        canvas.restore();
    }

}
