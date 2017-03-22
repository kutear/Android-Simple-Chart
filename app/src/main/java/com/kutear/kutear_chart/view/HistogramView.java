package com.kutear.kutear_chart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
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
    private int mPStartHistogramColor = DEFAULT_HISTOGRAM_COLOR;
    private int mPEndHistogramColor = DEFAULT_HISTOGRAM_COLOR;
    private int mNStartHistogramColor = DEFAULT_HISTOGRAM_COLOR;
    private int mNEndHistogramColor = DEFAULT_HISTOGRAM_COLOR;
    private float mHistogramPercent = .5f;
    private PaintController mPaintController;

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
                case R.styleable.HistogramView_histogram_positive_bottom_color:
                    mPEndHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
                case R.styleable.HistogramView_histogram_positive_top_color:
                    mPStartHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
                case R.styleable.HistogramView_histogram_negative_bottom_color:
                    mNEndHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
                case R.styleable.HistogramView_histogram_negative_top_color:
                    mNStartHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
            }
        }
        ta.recycle();
        mPaintController = new PaintController();
    }

    @Override
    protected void onDrawGraph(Canvas canvas) {
        float singleWidth = getCellWidth();
        float baseLine = getZeroLine();
        canvas.save();
        canvas.translate(mOriginalX, mHeight - mOriginalY - baseLine);
        float space = singleWidth * (1 - mHistogramPercent) / 2;
        for (int i = 0; i < getCount(); i++) {
            IChartContract.ChartSingleData data = getItemData(i);
            if (data == null) {
                continue;
            }
            float drawHeight = -(getHeightOfIndex(i) - baseLine);
            float top = 0;
            float bottom = 0;
            if (data.yData >= 0) {
                top = drawHeight;
                bottom = 0;
                mPaintController.mPHistogramPaint.setShader(new LinearGradient(
                        singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                        mPStartHistogramColor,mPEndHistogramColor, Shader.TileMode.CLAMP
                ));
                canvas.drawRect(singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                        mPaintController.mPHistogramPaint);
            } else {
                top = 0;
                bottom = drawHeight;
                mPaintController.mNHistogramPaint.setShader(new LinearGradient(
                        singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                        mNStartHistogramColor,mNEndHistogramColor, Shader.TileMode.CLAMP
                ));
                canvas.drawRect(singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                        mPaintController.mNHistogramPaint);
            }
        }
        canvas.restore();
    }

    /**
     * 画笔工具类
     */
    private class PaintController {
        /**
         * 柱状图画笔(+)
         */
        private Paint mPHistogramPaint;
        /**
         * 柱状图画笔(-)
         */
        private Paint mNHistogramPaint;

        PaintController() {
            mPHistogramPaint = buildPaint(mPStartHistogramColor);
            mNHistogramPaint = buildPaint(mNStartHistogramColor);
        }

        private Paint buildPaint(int color) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(color);
            return paint;
        }
    }
}
