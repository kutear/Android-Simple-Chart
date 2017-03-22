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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
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
    private float mTipsHeight = 0;
    private int mIndex = -1;
    private float mTipsPadding;
    private float mTipsRadio = 0;
    private float mZeroLineWidth;
    private int mZeroLineColor = DEFAULT_TEXT_COLOR;
    private float mLastTapPointX;
    private float mLastTapPointY;
    private float mOffset = .5f; //触摸线偏移一个单元格的比例
    private Drawable mTapUpDrawable;
    private Drawable mTapDonwDrawable;


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
                case R.styleable.AbsChartView_axis_color:
                    mAxisBorder = ta.getColor(attr, DEFAULT_HISTOGRAM_COLOR);
                    break;
                case R.styleable.AbsChartView_axis_width:
                    mAxisWidth = ta.getDimension(attr, 0.7f);
                    break;
                case R.styleable.AbsChartView_zero_line_color:
                    mZeroLineColor = ta.getColor(attr, DEFAULT_TEXT_COLOR);
                    break;
                case R.styleable.AbsChartView_zero_line_size:
                    mZeroLineWidth = ta.getDimension(attr, 0.7f);
                    break;
                case R.styleable.AbsChartView_tap_up_bg:
                    mTapUpDrawable = ta.getDrawable(attr);
                    break;
                case R.styleable.AbsChartView_tap_down_bg:
                    mTapDonwDrawable = ta.getDrawable(attr);
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

    protected Drawable getTapBitmap() {
        if (mTapUpDrawable != null) {
            return mTapUpDrawable;
        }
        return null;
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

    protected String getXbyValue(String str, int i) {
        if (mFormatAxis == null) {
            return str;
        }
        return mFormatAxis.formatX(str, i);
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
        if (Float.compare(max, min) == 0) {
            mMaxValue = Math.abs(max);
            mMinValue = -Math.abs(max);
        } else {
            mMaxValue = max;
            mMinValue = min;
        }
        if (Float.compare(max, min) == 0 && Float.compare(max, 0) == 0) {
            mMaxValue = 1;
            mMinValue = 0;
        }
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<IChartContract.ChartSingleData> data) {
        this.mDataLists = data;
        initData();
        refresh();
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
        onDrawTapDrawable(canvas);
    }

    protected void onDrawTapDrawable(Canvas canvas) {
        if (mTapUpDrawable == null && mTapDonwDrawable == null
                && mIndex < 0 && mIndex >= getCount()) {
            return;
        }
        canvas.save();
        canvas.translate(mOriginalX, mHeight - mOriginalY);
        float where = whereIs(mIndex);
        float height = getHeightOfIndex(mIndex);

        Drawable drawable = getTapDrawable(mIndex);
        if (drawable != null) {
            float w = drawable.getIntrinsicWidth();
            drawable.setBounds(
                    (int) (where - w / 2),
                    (int) (-height - w / 2),
                    (int) (where + w / 2),
                    (int) (-height + w / 2)
            );
            drawable.draw(canvas);
        }
        canvas.restore();
    }

    private Drawable getTapDrawable(int index) {
        IChartContract.ChartSingleData data = getItemData(index);
        if (mTapDonwDrawable == null) {
            mTapDonwDrawable = mTapUpDrawable;
        }
        if (mTapUpDrawable == null) {
            mTapUpDrawable = mTapDonwDrawable;
        }
        if (data != null) {
            if (data.yData < 0) {
                return mTapDonwDrawable;
            }
            return mTapUpDrawable;
        }
        return mTapUpDrawable;
    }

    public void setTapDrawable(Drawable drawable) {
        this.mTapUpDrawable = drawable;
        refresh();
    }

    /**
     * 绘制XY轴 不包含数值部分
     *
     * @param canvas
     */
    private void onDrawAxis(Canvas canvas) {
        canvas.save();
        float yTextHeight = getYMaxTextHeight();
        float yTextWidth = getYMaxTextLength();
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
        float singleLineHeight = Math.abs(axisHeight() / (mSplitNum + 1));
        onDrawHorizontalLine(canvas, singleLineHeight, yTextHeight, yTextWidth);
        canvas.restore();
    }

    private float getYMaxTextLength() {
        String max = getYbyValue(getMaxValue());
        String min = getYbyValue(getMinValue());
        mPaintController.mAxisYTextPaint.getTextBounds(max, 0, max.length(), mRect);
        float maxW = mRect.width();
        mPaintController.mAxisYTextPaint.getTextBounds(min, 0, min.length(), mRect);
        float minW = mRect.width();
        float resW = maxW > minW ? maxW : minW;
        return resW;
    }

    private float getYMaxTextHeight() {
        String min = getYbyValue(getMinValue());
        mPaintController.mAxisYTextPaint.getTextBounds(min, 0, min.length(), mRect);
        return mRect.height();
    }

    protected void setOffset(float offset) {
        this.mOffset = offset;
    }


    /**
     * 绘制横线部分
     * 绘制坐标系与展示坐标系一致
     *
     * @param canvas
     */
    protected void onDrawHorizontalLine(Canvas canvas, float singleHeight, float textHeight, float textWidth) {
        for (int i = 1; i <= mSplitNum; i++) {
            canvas.drawLine(0,
                    -i * singleHeight,
                    axisWidth(),
                    -i * singleHeight,
                    mPaintController.mHorizontalLinePaint);
            onDrawYText(canvas, i, -mOriginalX, -i * singleHeight + textHeight / 2, textWidth);
        }
        if (getMaxValue() > 0 && getMinValue() < 0) {
            float zeroLine = -getZeroLine();
            canvas.drawLine(0, zeroLine, axisWidth(), zeroLine, mPaintController.mZeroLinePaint);
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
    protected void onDrawYText(Canvas canvas, int index, float startX, float startY, float textW) {
        String text = getYHorizontalText(index);
        mPaintController.mAxisYTextPaint.getTextBounds(text, 0, text.length(), mRect);
        float w = mRect.width();
        float offset = textW - w;
        canvas.drawText(text, startX + offset, startY, mPaintController.mAxisYTextPaint);
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
    protected String getYHorizontalText(int index) {
        float min = getMinValue();
        float max = getMaxValue();
        float range = max - min;
        float single = range / (mSplitNum + 1);
        float currentValue = min + single * index;
        return getYbyValue(currentValue);
    }

    /**
     * 基准线
     *
     * @return
     */
    protected float getZeroLine() {
        float range = getMaxValue() - getMinValue();
        if (Float.compare(range, 0f) == 0) {
            return 0;
        }
        if (getMinValue() > 0) {
            return 0;
        }
        return axisHeight() * Math.abs(getMinValue()) / range;
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
        canvas.save();
        canvas.translate(mOriginalX, mHeight - mOriginalY);
        float w = axisWidth() / count;
        float axisLength = axisWidth();
        for (int i = 0; i < count; i++) {
            IChartContract.ChartSingleData data = mDataLists.get(i);
            String showText = getXbyValue(data.xData, i);
            mPaintController.mAxisXTextPaint.getTextBounds(showText, 0, showText.length(), mRect);
            float start = ((i + mOffset) * w - mRect.width() / 2);
            if (start + mRect.width() > axisLength) {
                start = axisLength - mRect.width();
            }
            if (start < 0) {
                start = 0;
            }
            canvas.drawText(showText, start, mXSpace + mRect.height(), mPaintController.mAxisXTextPaint);
        }
        canvas.restore();
    }

    /**
     * 计算index对应单元的中心位置
     *
     * @param index
     * @return
     */
    protected float whereIs(int index) {
        float cellW = getCellWidth();
        return cellW * (index + mOffset);
    }

    protected float getHeightPercent(int index) {
        float min = getMinValue();
        float max = getMaxValue();
        float range = Math.abs(max - min);
        if (Float.compare(range, 0) == 0) {
            return 1;
        }
        if (index >= 0 && index < mDataLists.size()) {
            float currentValue = mDataLists.get(index).yData;
            return Math.abs(currentValue - min) / range;
        }
        return 0;
    }

    protected float getHeightOfIndex(int index) {
        return getHeightPercent(index) * axisHeight();
    }

    private void onDrawLineTips(Canvas canvas, int index) {
        if (index >= 0 && index < getCount()) {
            canvas.save();
            canvas.translate(mOriginalX, mHeight - mOriginalY);
            float center = ((index + mOffset) * getCellWidth());
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
            //三角形指示器
            Path path = new Path();
            path.moveTo(center, -axisHeight());
            float pLeft = (float) (center - .5 * mRect.height());
            if (pLeft < 0) {
                pLeft = 0;
            }
            float pRight = (float) (center + .5 * mRect.height());
            if (pRight > axisWidth()) {
                pRight = axisWidth();
            }
            path.lineTo(pLeft, bottom - 5);
            path.lineTo(pRight, bottom - 5);
            canvas.drawPath(path, mPaintController.mTipsBackgroundPaint);
            canvas.drawText(xValue,
                    (left + right - mRect.width()) / 2,
                    (top + bottom + mRect.height()) / 2,
                    mPaintController.mTipsTextPaint);

            canvas.restore();
        }
    }

    /**
     * 一个单元格的宽度
     *
     * @return
     */
    protected float getCellWidth() {
        if (getCount() != 0) {
            if (Float.compare(mOffset, 0) == 0 && getCount() != 1) {
                return axisWidth() / (getCount() - 1);
            }
            return axisWidth() / getCount();
        }
        return axisWidth();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIndex = getIndexOfPressed(event.getX());
                mLastTapPointX = event.getX();
                mLastTapPointY = event.getY();
                invalidate();
                dispatchChange(mIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int index = getIndexOfPressed(event.getX());
                float x = event.getX();
                float y = event.getY();
                if (Math.abs(x - mLastTapPointX) < Math.abs(y - mLastTapPointY)) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                mLastTapPointY = y;
                mLastTapPointX = x;
                if (index != mIndex) {
                    mIndex = index;
                    invalidate();
                    dispatchChange(mIndex);
                }
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                dispatchCancel();
                break;
        }
        return true;
    }

    private void refresh() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setDefaultSelected(int index) {
        this.mIndex = index;
        refresh();
    }

    protected int getCurrentSelectIndex() {
        return mIndex;
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
            return mIndex;
        }
        float offset = (float) Math.abs(x - mOriginalX + (0.5 - mOffset) * getCellWidth());
        return (int) Math.floor(offset / getCellWidth());
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
        /**
         * 0基准水平线
         */
        private Paint mZeroLinePaint;

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
            mZeroLinePaint = buildPaint(mZeroLineColor);
            mZeroLinePaint.setStrokeWidth(mZeroLineWidth);
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
