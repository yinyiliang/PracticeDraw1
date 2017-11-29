package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hencoder.hencoderpracticedraw1.PieChartData;

import java.util.ArrayList;
import java.util.List;

public class Practice11PieChartView extends View {

    private Paint mPiePaint;
    private Paint mTextPaint;

    private List<PieChartData> mDataList;
    private RectF mRectF;
    private List<PointInfo> mPointList;

    private int mWidth;
    private int mHeight;
    private float dataNumSum = 0;
    private float maxNum = 0;

    private int radus = 300;
    private float textSize = 40;

    private float startAngle = 0;
    private float endAngle;
    private float lineAngle;

    private float lineStartX;
    private float lineStartY;
    private float lineEndX;
    private float lineEndY;

    public Practice11PieChartView(Context context) {
        this(context, null);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPiePaint = new Paint();
        mPiePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(textSize);

        mRectF = new RectF(-300, -300, 300, 300);

        initData();
    }

    private void initData() {
        mDataList = new ArrayList<>();

        mDataList.add(new PieChartData("Gingerbread", 2.0f, Color.WHITE));
        mDataList.add(new PieChartData("Ice Cream Sandwich", 93.0f, Color.MAGENTA));
        mDataList.add(new PieChartData("Jelly Bean", 39.0f, Color.GRAY));
        mDataList.add(new PieChartData("KitKat", 25.0f, Color.GREEN));
        mDataList.add(new PieChartData("Lollipop", 36.0f, Color.BLUE));
        mDataList.add(new PieChartData("Marshmallow", 6.0f, Color.RED));
        mDataList.add(new PieChartData("Nougat", 10.5f, Color.YELLOW));
        mDataList.add(new PieChartData("Lolita", 140, Color.LTGRAY));

        for (PieChartData data : mDataList) {
            dataNumSum += data.getNumber();
            if (data.getNumber() > maxNum) {
                maxNum = data.getNumber();
            }
        }

        mPointList = new ArrayList<>();
    }

    PointInfo mPointInfo = new PointInfo();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 将画布(0，0)坐标点移到画布的中心
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        startAngle = 0f;

        for (PieChartData chartData : mDataList) {
            //通过集合中数值的总和来计算在圆中所占比例的角度
            endAngle = chartData.getNumber() / dataNumSum * 360;
            //获取该圆弧中间角度
            lineAngle = startAngle + endAngle / 2;
            //圆弧中间点的X、Y坐标
            lineStartX = (float) Math.cos(lineAngle / 180 * Math.PI) * radus;
            lineStartY = (float) Math.sin(lineAngle / 180 * Math.PI) * radus;
            lineEndX = (radus + 50) * (float) Math.cos(lineAngle / 180 * Math.PI);
            lineEndY = (radus + 50) * (float) Math.sin(lineAngle / 180 * Math.PI);
            //设置画笔颜色
            mPiePaint.setColor(chartData.getColor());
            //当当前数值为最大的一个数值，保存画布的状态，将画布偏移一定距离 绘制完成后再取出前面的状态，避免后面的圆弧也发生偏移
            if (chartData.getNumber() == maxNum) {
                canvas.save();
                canvas.translate(lineStartX * 0.1f,lineStartY * 0.1f);
                canvas.drawArc(mRectF, startAngle, endAngle, true, mPiePaint);
            } else {
                canvas.drawArc(mRectF, startAngle, endAngle - 2f, true, mPiePaint);
            }

            canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, mTextPaint);
            //当扇形中间角度在圆的右半边时，字体往右边
            if (lineAngle >= 270 || lineAngle <= 90) {
                mTextPaint.setTextAlign(Paint.Align.LEFT);

                if (mPointList.size() > 0) {
                    if ((lineEndY - textSize) <=  mPointList.get(mPointList.size()-1).getyValue()) {
                        canvas.drawLine(lineEndX, lineEndY, lineEndX + 6, lineEndY + textSize, mTextPaint);
                        lineEndY = lineEndY + textSize;
                        lineEndX = lineEndX + 6;
                    }
                }
                canvas.drawLine(lineEndX, lineEndY, lineEndX + 30, lineEndY, mTextPaint);
                canvas.drawText(chartData.getName(), lineEndX + 34, lineEndY, mTextPaint);
                mPointInfo.setyValue(lineEndY);
                mPointInfo.setAngle(lineAngle);
                mPointList.add(mPointInfo);
            } else {

                if (mPointList.size() > 0) {
                    if (oppositeSigns((int)lineEndY,
                            (int)mPointList.get(mPointList.size()-1).getyValue())) {
                        if ((lineEndY + textSize) <=  mPointList.get(mPointList.size()-1).getyValue()) {
                            //当上一个数不在圆的右边才改变Y轴数值
                            if (!isLeft(mPointList.get(mPointList.size()-1).getAngle())) {
                                canvas.drawLine(lineEndX, lineEndY, lineEndX - 6, lineEndY +
                                        textSize, mTextPaint);
                                lineEndY = lineEndY + textSize;
                                lineEndX = lineEndX - 6;
                            }
                        }
                    }
                }
                mTextPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawLine(lineEndX, lineEndY, lineEndX - 30, lineEndY, mTextPaint);
                canvas.drawText(chartData.getName(), lineEndX - 34, lineEndY, mTextPaint);
                mPointInfo.setyValue(lineEndY);
                mPointInfo.setAngle(lineAngle);
                mPointList.add(mPointInfo);
            }
            if (chartData.getNumber() == maxNum) {
                canvas.restore();
            }
            startAngle += endAngle;
        }

    }

    /**
     * 判断扇形的中间角度是在圆形的左边还是右边
     * @param angle
     * @return
     */
    private boolean isLeft(float angle) {
        if (angle >= 270 || angle <= 90) {
            return true;
        }
        return false;
    }

    /**
     * 判断两个数是否符号相反
     * @param x
     * @param y
     * @return
     */
    private boolean oppositeSigns(int x, int y) {
        if (((x ^ y) >> 31) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public class PointInfo {
        private float yValue;
        private float angle;

        public PointInfo() {
        }

        public PointInfo(float yValue, float angle) {
            this.yValue = yValue;
            this.angle = angle;
        }

        public float getyValue() {
            return yValue;
        }

        public void setyValue(float yValue) {
            this.yValue = yValue;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }
    }
}
