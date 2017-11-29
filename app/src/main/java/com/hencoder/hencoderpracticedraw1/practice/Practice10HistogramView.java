package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Practice10HistogramView extends View {

    private static final int TYPE_HISTOGRAM = 0;
    private static final int TYPE_LINE = 1;

    private Paint mAxisPaint;
    private Paint mHistogramPaint;
    private Paint mLinePaint;

    private int mWidth;
    private int mHeight;

    private int type = TYPE_LINE;
    private Path mLinePath;
    private boolean fromZero = true;

    private String[] types = new String[]{"Froyo", "GB", "ICS", "JB", "KitKat", "L", "M"};

    private int[] typePercents = new int[]{2, 10, 10, 50, 70, 80, 46};

    public Practice10HistogramView(Context context) {
        this(context, null);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mAxisPaint = new Paint();
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setColor(Color.WHITE);
        mAxisPaint.setStrokeWidth(4);
        mHistogramPaint = new Paint();
        mHistogramPaint.setColor(Color.GREEN);
        mHistogramPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.YELLOW);
        mLinePaint.setStrokeWidth(4);
        mLinePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        drawAxis(canvas);
        drawHistogram(canvas);
        drawLine(canvas);
//        if (type == TYPE_HISTOGRAM) {
//            drawHistogram(canvas);
//        } else {
//            drawLine(canvas);
//        }

        canvas.restore();
    }

    /**
     * 绘制坐标轴
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        //绘制X轴
        canvas.drawLine(leftAndRightMargin(), axisHeight(), mWidth - leftAndRightMargin(),
                axisHeight(), mAxisPaint);
        //绘制Y轴
        canvas.drawLine(leftAndRightMargin(), 40, leftAndRightMargin(), axisHeight(), mAxisPaint);
        
        //绘制X轴文字
        mAxisPaint.setTextAlign(Paint.Align.CENTER);
        mAxisPaint.setTextSize(25);
        for (int i = 0; i < types.length; i++) {
            canvas.drawText(types[i], leftAndRightMargin() + itemWidth()*(i + 1), axisHeight() + 25,
                    mAxisPaint);
        }
    }

    /**
     * 绘制折线
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        if (fromZero) {
            mLinePath.moveTo(leftAndRightMargin(), axisHeight());
        }
        int maxValue = getValueMax(typePercents) + 20;
        int yHeight = axisHeight() - 40;
        for (int i = 0; i < types.length; i++) {
            int centerX = leftAndRightMargin() + itemWidth()*(i + 1);
            double percent = (typePercents[i] * 1.00 / maxValue * 1.00);
            float y = (float) (yHeight * (1 - percent)) + 40;
            mLinePath.lineTo(centerX, y);
            mLinePaint.setStrokeWidth(10);
            canvas.drawPoint(centerX, y, mLinePaint);
        }
        mLinePaint.setStrokeWidth(4);
        canvas.drawPath(mLinePath, mLinePaint);
    }

    /**
     * 绘制柱子
     * @param canvas
     */
    private void drawHistogram(Canvas canvas) {
        int maxValue = getValueMax(typePercents) + 20;
        int yHeight = axisHeight() - 40;
        for (int i = 0; i < types.length; i++) {
            int centerX = leftAndRightMargin() + itemWidth()*(i + 1);
            double percent = (typePercents[i] * 1.00 / maxValue * 1.00);
            float y = (float) (yHeight * (1 - percent)) + 40;
            canvas.drawRect(centerX - (itemWidth()/2 - 10), y, centerX + (itemWidth()/2 - 10),
                    axisHeight(), mHistogramPaint);
        }

    }

    private int getValueMax(int[] values) {
        int max = 0;
        for (int i = 0; i < values.length; i++) {
            if (max < values[i]) {
                max =  values[i];
            }
        }
        return max;
    }

    /**
     * 坐标轴左右间距
     * @return
     */
    private int leftAndRightMargin() {
        return mWidth - mWidth * 9 / 10;
    }

    /**
     * 坐标X轴高度
     * @return
     */
    private int axisHeight() {
        return  40 + mHeight * 4 / 5;
    }

    /**
     * 柱状图每个柱子的宽度
     */
    private int itemWidth() {
        return (mWidth - 2*leftAndRightMargin()) / types.length - 15;
    }

    /**
     * 柱状图每个柱子实际的宽度
     */
}
