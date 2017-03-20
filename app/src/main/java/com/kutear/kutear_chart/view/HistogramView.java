package com.kutear.kutear_chart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.kutear.kutear_chart.R;


/**
 * @author: kutear.guo
 * @create: 2017/3/16 15:07
 * 柱状图
 */

public class HistogramView extends AbsChartView {
    private static final String TAG = "GxqHistogramView";


    private int mHistogramColor = DEFAULT_HISTOGRAM_COLOR;
    private float mHistogramPercent = .5f;


    private PaintController mPaintController;
    private Rect mRect;


    private boolean mDrawLine;
    private float mTapX;
    private float mTapY;
    private float mHistogramWidth;


    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HistogramView, defStyleAttr, 0);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.HistogramView_histogram_width_percent:
                    mHistogramPercent = ta.getFloat(attr, 0.5f);
                    break;
                case R.styleable.HistogramView_histogram_color:
                    mHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
            }
        }
        ta.recycle();
        mPaintController = new PaintController();
        mRect = new Rect();
    }

    @Override
    protected void onDrawGraph(Canvas canvas) {

        float canDrawHeight = axisHeight();
        if (getCount() == 0) {
            return;
        }
        float singleWidth = getSingleWidth();
        float baseLine = getSingleHeight() * baseLine();

        canvas.save();
        canvas.translate(mOriginalX, mHeight - mOriginalY - baseLine);
        float range = getMaxValue() - getMinValue();
        if (Float.compare(range, 0f) == 0) {
            return;
        }
        float space = singleWidth * (1 - mHistogramPercent) / 2;
        for (int i = 0; i < getCount(); i++) {
            IChartContract.ChartSingleData data = getItemData(i);
            if (data == null) {
                continue;
            }
            float drawHeight = Math.abs(canDrawHeight * (data.yData) / range);
            float top = 0;
            float bottom = 0;
            if (data.yData > 0) {
                top = -drawHeight;
                bottom = 0;
            } else {
                top = 0;
                bottom = drawHeight;
            }
            canvas.drawRect(singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                    mPaintController.mHistogramPaint);
        }
        canvas.restore();
    }

    @Override
    protected String getYHorizontalText(int index) {
        return "000";
    }

    @Override
    protected int baseLine() {
        return 2;
    }


    /**
     * 画笔工具类
     */
    private class PaintController {
        /**
         * 柱状图画笔
         */
        private Paint mHistogramPaint;

        PaintController() {
            mHistogramPaint = buildPaint(mHistogramColor);
        }

        private Paint buildPaint(int color) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(color);
            return paint;
        }
    }
}
