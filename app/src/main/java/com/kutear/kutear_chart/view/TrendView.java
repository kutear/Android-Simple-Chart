package com.kutear.kutear_chart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author: kutear.guo
 * @create: 2017/3/20 20:06
 */

public class TrendView extends AbsChartView {
    private Paint mPaint;

    public TrendView(Context context) {
        this(context, null);
    }

    public TrendView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TrendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = buildPaint(Color.RED);
    }

    @Override
    protected void onDrawGraph(Canvas canvas) {
        canvas.save();
        canvas.translate(mOriginalX, mHeight - mOriginalY);
        int count = getCount();
        if (count <= 0) {
            return;
        }
        float singleWidth = getSingleWidth();
        float canDrawHeight = axisHeight();
        float baseLine = getSingleHeight() * baseLine();
        float range = getMaxValue() - getMinValue();
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            IChartContract.ChartSingleData data = getItemData(i);
            if (data == null) {
                continue;
            }
            float drawHeight = Math.abs(canDrawHeight * (data.yData) / range);
            float center = (float) (singleWidth * (i + .5));
            if (i == 0) {
                path.moveTo(center, -drawHeight);
            } else {
                path.lineTo(center, -drawHeight);
            }
        }
        canvas.drawCircle(0,0,20,mPaint);
        canvas.drawPath(path, mPaint);
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
}
