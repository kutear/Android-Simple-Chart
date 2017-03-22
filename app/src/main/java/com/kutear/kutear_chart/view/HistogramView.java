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
    private int mSelectPStartHistogramColor = -1;
    private int mSelectPEndHistogramColor = -1;
    private int mSelectNStartHistogramColor = -1;
    private int mSelectNEndHistogramColor = -1;
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
                case R.styleable.HistogramView_histogram_select_negative_bottom_color:
                    mSelectNEndHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
                case R.styleable.HistogramView_histogram_select_negative_top_color:
                    mSelectNStartHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
                case R.styleable.HistogramView_histogram_select_positive_bottom_color:
                    mSelectPEndHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
                case R.styleable.HistogramView_histogram_select_positive_top_color:
                    mSelectPStartHistogramColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
            }
        }
        ta.recycle();
        mPaintController = new PaintController();
        initDefault();
    }

    private void initDefault() {
        if (mSelectPStartHistogramColor == -1) {
            mSelectPStartHistogramColor = mPStartHistogramColor;
        }
        if (mSelectPEndHistogramColor == -1) {
            mSelectPEndHistogramColor = mPEndHistogramColor;
        }
        if (mSelectNStartHistogramColor == -1) {
            mSelectNStartHistogramColor = mNStartHistogramColor;
        }
        if (mSelectNEndHistogramColor == -1) {
            mSelectNEndHistogramColor = mNEndHistogramColor;
        }
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
                Paint paint = null;
                if (getCurrentSelectIndex() == i) {
                    mPaintController.mSelectPHistogramPaint.setShader(new LinearGradient(
                            singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                            mSelectPStartHistogramColor, mSelectPEndHistogramColor, Shader.TileMode.CLAMP
                    ));
                    paint = mPaintController.mSelectPHistogramPaint;
                } else {
                    mPaintController.mNormalPHistogramPaint.setShader(new LinearGradient(
                            singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                            mPStartHistogramColor, mPEndHistogramColor, Shader.TileMode.CLAMP
                    ));
                    paint = mPaintController.mNormalPHistogramPaint;
                }
                canvas.drawRect(singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                        paint);

            } else {
                top = 0;
                bottom = drawHeight;
                Paint paint = null;
                if (getCurrentSelectIndex() == i) {
                    mPaintController.mSelectNHistogramPaint.setShader(new LinearGradient(
                            singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                            mSelectNStartHistogramColor, mSelectNEndHistogramColor, Shader.TileMode.CLAMP
                    ));
                    paint = mPaintController.mSelectNHistogramPaint;
                } else {
                    mPaintController.mNormalNHistogramPaint.setShader(new LinearGradient(
                            singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                            mNStartHistogramColor, mNEndHistogramColor, Shader.TileMode.CLAMP
                    ));
                    paint = mPaintController.mNormalNHistogramPaint;
                }


                canvas.drawRect(singleWidth * i + space, top, singleWidth * (i + 1) - space, bottom,
                        paint);
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
        private Paint mNormalPHistogramPaint;
        /**
         * 柱状图画笔(-)
         */
        private Paint mNormalNHistogramPaint;
        /**
         * 柱状图画笔(+)
         */
        private Paint mSelectPHistogramPaint;
        /**
         * 柱状图画笔(-)
         */
        private Paint mSelectNHistogramPaint;

        PaintController() {
            mNormalPHistogramPaint = buildPaint(mPStartHistogramColor);
            mNormalNHistogramPaint = buildPaint(mNStartHistogramColor);
            mSelectPHistogramPaint = buildPaint(mPStartHistogramColor);
            mSelectNHistogramPaint = buildPaint(mNStartHistogramColor);
        }
    }
}
