package com.kutear.kutear_chart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kutear.kutear_chart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: kutear.guo
 * @create: 2017/3/20 10:42
 * <p>
 * 图表类
 */

public abstract class AbsChartView extends View {

    protected static final float DEFAULT_TEXT_SIZE = 10;
    protected static final float DEFAULT_PADDING = 10;
    protected static final float DEFAULT_SPACE = 4;
    protected static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    protected static final int DEFAULT_HISTOGRAM_COLOR = Color.GRAY;
    private static final int DEFAULT_SPLIT_NUM = 5;
    private IChartContract.ITipShow mShow;
    private IChartContract.IMaxMin mMaxMin;
    protected IChartContract.OnTapListener mListener;
    private IChartContract.IFormatAxis mFormatAxis;
    protected List<IChartContract.ChartSingleData> mDataLists = new ArrayList<>();
    private PaintController mPaintController;
    private Rect mRect = new Rect();
    private float mMaxValue;
    private float mMinValue;
    protected float mOriginalX;
    protected float mOriginalY;
    protected float mHeight;
    protected float mWidth;
    protected float mSpace;
    private int mAxisBorder = DEFAULT_HISTOGRAM_COLOR;
    private float mAxisWidth = .7f;
    private float mXSpace;
    private float mYSpace;
    private int mYTextColor = DEFAULT_TEXT_COLOR;
    private int mXTextColor = DEFAULT_TEXT_COLOR;
    private float mXTextSize;
    private float mYTextSize;
    private int mTipsColor = DEFAULT_TEXT_COLOR;
    private int mTipsBackground = Color.GRAY;
    private int mLineColor = DEFAULT_HISTOGRAM_COLOR;
    private float mTipsSize;
    private float mSplitNum = DEFAULT_SPLIT_NUM;
    private int mHorizontalLineColor = Color.GRAY;
    private float mSingleHeight = 0;
    private float mTipsHeight = 0;
    private boolean mShowTips = false;
    private int mIndex = -1;
    private float mTipsPadding;
    private float mTipsRadio = 0;
    private float mLastTapPointX;
    private float mLastTapPointY;


    public AbsChartView(Context context) {
        this(context, null);
    }

    public AbsChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbsChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        initDefaultValue(context);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AbsChartView, defStyleAttr, 0);
        int n = ta.getIndexCount();
        float defaultTextSize = dip2px(context, DEFAULT_TEXT_SIZE);
        float defaultPadding = dip2px(context, DEFAULT_PADDING);
        float defaultSpace = dip2px(context, DEFAULT_SPACE);
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.AbsChartView_x_color:
                    mXTextColor = ta.getColor(attr, DEFAULT_TEXT_COLOR);
                    break;
                case R.styleable.AbsChartView_x_size:
                    mXTextSize = ta.getDimension(attr, defaultTextSize);
                    break;
                case R.styleable.AbsChartView_y_color:
                    mYTextColor = ta.getColor(attr, DEFAULT_TEXT_COLOR);
                    break;
                case R.styleable.AbsChartView_y_size:
                    mYTextSize = ta.getDimension(attr, defaultTextSize);
                    break;
                case R.styleable.AbsChartView_x_space:
                    mXSpace = ta.getDimension(attr, defaultSpace);
                    break;
                case R.styleable.AbsChartView_y_space:
                    mYSpace = ta.getDimension(attr, defaultSpace);
                    break;
                case R.styleable.AbsChartView_line_color:
                    mLineColor = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
                case R.styleable.AbsChartView_tips_color:
                    mTipsColor = ta.getColor(attr, DEFAULT_TEXT_COLOR);
                    break;
                case R.styleable.AbsChartView_tips_size:
                    mTipsSize = ta.getDimension(attr, defaultTextSize);
                    break;
                case R.styleable.AbsChartView_tips_background:
                    mTipsBackground = ta.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.AbsChartView_y_horizontal_line_num:
                    mSplitNum = ta.getInt(attr, DEFAULT_SPLIT_NUM);
                    break;
                case R.styleable.AbsChartView_y_horizontal_line_color:
                    mHorizontalLineColor = ta.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.AbsChartView_axis_space:
                    mSpace = ta.getDimension(attr, defaultPadding);
                    break;
                case R.styleable.AbsChartView_tips_padding:
                    mTipsPadding = ta.getDimension(attr, defaultPadding);
                    break;
                case R.styleable.AbsChartView_tips_radio:
                    mTipsRadio = ta.getDimension(attr, 0);
                    break;
            }
        }
        ta.recycle();
        mPaintController = new PaintController();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    protected IChartContract.ChartSingleData getItemData(int index) {
        if (index >= 0 && index < getCount()) {
            return mDataLists.get(index);
        }
        return null;
    }

    private void initDefaultValue(Context ctx) {
        float defaultTextSize = dip2px(ctx, DEFAULT_TEXT_SIZE);
        float defaultPadding = dip2px(ctx, DEFAULT_PADDING);
        float defaultSpace = dip2px(ctx, DEFAULT_SPACE);
        mXTextSize = defaultTextSize;
        mYTextSize = defaultTextSize;
        mTipsSize = defaultTextSize;
        mXSpace = defaultSpace;
        mYSpace = defaultSpace;
        mSpace = defaultPadding;
        mTipsPadding = defaultPadding;
    }

    /**
     * 格式化Xy轴数据
     *
     * @param formatAxis
     */
    public void setFormatAxis(IChartContract.IFormatAxis formatAxis) {
        this.mFormatAxis = formatAxis;
    }

    /**
     * @param listener
     */
    public void setOnTapListener(IChartContract.OnTapListener listener) {
        this.mListener = listener;
    }

    /**
     * 控制Y轴最大值与最小值
     *
     * @param maxMin
     */
    public void setMaxMin(IChartContract.IMaxMin maxMin) {
        this.mMaxMin = maxMin;
    }

    /**
     * 上方指示内容
     *
     * @param show
     */
    public void setTipsShow(IChartContract.ITipShow show) {
        this.mShow = show;
    }

    protected String getTips(int position, String x, float y) {
        if (mShow != null) {
            return mShow.getTips(position, x, y);
        }
        return String.valueOf(y);
    }

    protected float getMaxValue() {
        if (mMaxMin == null) {
            return mMaxValue;
        }
        return mMaxMin.getMax(mMaxValue, mMinValue);
    }

    protected float getMinValue() {
        if (mMaxMin == null) {
            return mMinValue;
        }
        return mMaxMin.getMin(mMaxValue, mMinValue);
    }

    protected String getYbyValue(float y) {
        if (mFormatAxis == null) {
            return String.valueOf(y);
        }
        return mFormatAxis.formatY(y);
    }

    protected String getXbyValue(String str) {
        if (mFormatAxis == null) {
            return str;
        }
        return mFormatAxis.formatX(str);
    }

    /**
     * 初始化相关数据
     */
    private void initData() {
        float min = Integer.MAX_VALUE;
        float max = Integer.MIN_VALUE;
        for (IChartContract.ChartSingleData data : mDataLists) {
            if (data.yData > max) {
                max = data.yData;
            }

            if (data.yData < min) {
                min = data.yData;
            }
        }
        mMaxValue = max;
        mMinValue = min;
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<IChartContract.ChartSingleData> data) {
        this.mDataLists = data;
        initData();
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getHeight();
        mWidth = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawAxis(canvas);
        onDrawLineTips(canvas, mIndex);
        onDrawGraph(canvas);
        onDrawXText(canvas);
    }

    /**
     * 绘制XY轴 不包含数值部分
     *
     * @param canvas
     */
    private void onDrawAxis(Canvas canvas) {
        canvas.save();
        String max = getYbyValue(getMaxValue());
        String min = getYbyValue(getMinValue());
        mPaintController.mAxisYTextPaint.getTextBounds(String.valueOf(max) + "", 0, String.valueOf(max).length(), mRect);
        float maxW = mRect.width();
        mPaintController.mAxisYTextPaint.getTextBounds(String.valueOf(min), 0, String.valueOf(min).length(), mRect);
        float minW = mRect.width();
        float textHeight = mRect.height();
        float yTextWidth = maxW > minW ? maxW : minW;
        mOriginalX = yTextWidth + mYSpace + mSpace;
        mPaintController.mAxisXTextPaint.getTextBounds(String.valueOf("0"), 0, String.valueOf("0").length(), mRect);
        float axisXTextHeight = mRect.height();
        mOriginalY = axisXTextHeight + mXSpace + mSpace;
        mPaintController.mTipsTextPaint.getTextBounds("0", 0, "0".length(), mRect);
        mTipsHeight = mRect.height() * 3;

        canvas.translate(mOriginalX, mHeight - mOriginalY);
        //绘制Y轴
        canvas.drawLine(0, -axisHeight(), 0, 0, mPaintController.mAxisPaint);
        //绘制X轴
        canvas.drawLine(0, 0, axisWidth(), 0, mPaintController.mAxisPaint);
        mSingleHeight = Math.abs(axisHeight() / (mSplitNum + 1));
        onDrawHorizontalLine(canvas, mSingleHeight, textHeight);
        canvas.restore();
    }


    /**
     * 绘制横线部分
     * 绘制坐标系与展示坐标系一致
     *
     * @param canvas
     */
    protected void onDrawHorizontalLine(Canvas canvas, float singleHeight, float textHeight) {
        for (int i = 1; i <= mSplitNum; i++) {
            canvas.drawLine(0,
                    -i * singleHeight,
                    axisWidth(),
                    -i * singleHeight,
                    mPaintController.mHorizontalLinePaint);
            onDrawYText(canvas, i, -mOriginalX, -i * singleHeight + textHeight / 2);
        }
    }

    /**
     * 坐标轴的实际高度
     *
     * @return
     */
    protected float axisHeight() {
        return mHeight - mOriginalY - mTipsHeight;
    }

    /**
     * 坐标轴的实际宽度
     *
     * @return
     */
    protected float axisWidth() {
        return mWidth - mOriginalX - mSpace;
    }

    /**
     * 绘制Y轴文字部分
     *
     * @param canvas
     */
    protected void onDrawYText(Canvas canvas, int index, float startX, float startY) {
        canvas.drawText(getYHorizontalText(index), startX, startY, mPaintController.mAxisYTextPaint);
    }

    protected int getCount() {
        return mDataLists == null ? 0 : mDataLists.size();
    }


    /**
     * 绘制图形部分
     *
     * @param canvas
     */
    protected abstract void onDrawGraph(Canvas canvas);

    /**
     * 获取Y轴刻度
     *
     * @return
     */
    protected abstract String getYHorizontalText(int index);

    /**
     * 基准线
     *
     * @return
     */
    protected abstract int baseLine();

    /**
     * 水平横线的高度
     *
     * @return
     */
    protected float getSingleHeight() {
        return mSingleHeight;
    }

    /**
     * 绘制X轴文字部分
     *
     * @param canvas
     */
    protected void onDrawXText(Canvas canvas) {
        int count = getCount();
        if (count <= 0) {
            return;
        }
        canvas.translate(mOriginalX, mHeight - mOriginalY);
        float w = axisWidth() / count;
        for (int i = 0; i < count; i++) {
            IChartContract.ChartSingleData data = mDataLists.get(i);
            String showText = getXbyValue(data.xData);
            mPaintController.mAxisXTextPaint.getTextBounds(showText, 0, showText.length(), mRect);
            float start = (float) ((i + 0.5) * w - mRect.width() / 2);
            canvas.drawText(showText, start, mXSpace + mRect.height(), mPaintController.mAxisXTextPaint);

        }
    }

    private void onDrawLineTips(Canvas canvas, int index) {
        if (mShowTips && index >= 0 && index < getCount()) {
            canvas.save();
            canvas.translate(mOriginalX, mHeight - mOriginalY);
            float center = (float) ((index + 0.5) * getSingleWidth());
            canvas.drawLine(center, 0, center, -axisHeight(), mPaintController.mLinePaint);
            IChartContract.ChartSingleData node = mDataLists.get(index);
            String xValue = getTips(index, node.xData, node.yData);
            mPaintController.mTipsTextPaint.getTextBounds(xValue, 0, xValue.length(), mRect);
            float length = mRect.width() + 2 * mTipsPadding;
            float left = center - length / 2;
            float right = center + length / 2;
            float top = (float) -(axisHeight() + 2.5 * mRect.height());
            float bottom = (float) -(axisHeight() + .5 * mRect.height());
            if (left < 0) {
                left = 0;
                right = left + length;
            } else if (right > axisWidth()) {
                right = axisWidth();
                left = right - length;
            }
            canvas.drawRoundRect(
                    left,
                    top,
                    right,
                    bottom,
                    mTipsRadio,
                    mTipsRadio,
                    mPaintController.mTipsBackgroundPaint);

            Path path = new Path();
            path.moveTo(center, -axisHeight());
            path.lineTo((float) (center - .5 * mRect.height()), bottom - 5);
            path.lineTo((float) (center + .5 * mRect.height()), bottom - 5);
            canvas.drawPath(path, mPaintController.mTipsBackgroundPaint);
            canvas.drawText(xValue,
                    (left + right - mRect.width()) / 2,
                    (top + bottom + mRect.height()) / 2,
                    mPaintController.mTipsTextPaint);

            canvas.restore();
        }
    }

    protected float getSingleWidth() {
        if (getCount() != 0)
            return axisWidth() / getCount();
        return axisWidth();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mShowTips = true;
                mIndex = getIndexOfPressed(event.getX());
                mLastTapPointX = event.getX();
                mLastTapPointY = event.getY();
                invalidate();
                dispatchChange(mIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                mIndex = getIndexOfPressed(event.getX());
                float x = event.getX();
                float y = event.getY();
                if (Math.abs(x - mLastTapPointX) < Math.abs(y - mLastTapPointY)) {
                    mShowTips = false;
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {
                    mShowTips = true;
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                mLastTapPointY = y;
                mLastTapPointX = x;

                invalidate();
                dispatchChange(mIndex);
                break;
            case MotionEvent.ACTION_UP:
                mShowTips = false;
                invalidate();
                dispatchCancel();
                break;
        }
        return true;
    }

    private void dispatchChange(int index) {
        if (index >= 0 && index < getCount()) {
            if (mListener != null) {
                mListener.onTapEvent(index, mDataLists.get(index).yData);
            }
        }
    }

    private void dispatchCancel() {
        if (mListener != null) {
            mListener.onTapCancel();
        }
    }

    private int getIndexOfPressed(float x) {
        if (x < mOriginalX || x > mOriginalX + axisWidth()) {
            return -1;
        }
        float offset = Math.abs(x - mOriginalX);
        return (int) Math.floor(offset / getSingleWidth());

    }

    private class PaintController {
        /**
         * 坐标轴画笔
         */
        private Paint mAxisPaint;
        /**
         * X轴文字画笔
         */
        private Paint mAxisXTextPaint;
        /**
         * Y轴文字画笔
         */
        private Paint mAxisYTextPaint;

        /**
         * 触摸的线
         */
        private Paint mLinePaint;

        /**
         * 触摸文字
         */
        private Paint mTipsTextPaint;
        /**
         * 触摸文字的背景
         */
        private Paint mTipsBackgroundPaint;

        /**
         * 水平线
         */
        private Paint mHorizontalLinePaint;

        PaintController() {
            mHorizontalLinePaint = buildPaint(mHorizontalLineColor);
            mHorizontalLinePaint.setStyle(Paint.Style.STROKE);
            mHorizontalLinePaint.setPathEffect(new DashPathEffect(new float[]{4, 4, 4, 4}, 2));
            mAxisPaint = buildPaint(mAxisBorder);
            mAxisPaint.setStrokeWidth(mAxisWidth);
            mAxisXTextPaint = buildPaint(mXTextColor);
            mAxisXTextPaint.setTextSize(mXTextSize);
            mAxisYTextPaint = buildPaint(mYTextColor);
            mAxisYTextPaint.setTextSize(mYTextSize);
            mLinePaint = buildPaint(mLineColor);
            mTipsTextPaint = buildPaint(mTipsColor);
            mTipsTextPaint.setTextSize(mTipsSize);
            mTipsBackgroundPaint = buildPaint(mTipsBackground);
        }


    }

    protected Paint buildPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        return paint;
    }

    public static float dip2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return dpValue * density;
    }
}
