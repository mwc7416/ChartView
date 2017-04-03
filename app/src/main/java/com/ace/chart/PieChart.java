package com.ace.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * PieChart sample
 * Created by Ace on 2017/4/3.
 */

public class PieChart extends View {
    private static final String TAG = PieChart.class.getSimpleName();

    private float circleR = 300f;//圆饼半径
    private boolean isShowValue = true;//是否显示具体的值
    private boolean isShowComment = true;//是否显示颜色对应值
    private int width;
    private int height;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //get the user's setting
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //calculate the height and width
        int height = CalculateWidth(widthMode, widthSize);
        int width = CalculateHeight(heightMode, heightSize);

        //set the width and height
        setMeasuredDimension(width, height);
    }

    /**
     * calculate the height of View
     *
     * @param heightMode
     * @param heightSize
     * @return
     */
    private int CalculateHeight(int heightMode, int heightSize) {
        int height = 0;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                int desired = (int) (getPaddingTop() + circleR * 2 + getPaddingBottom());
                height = desired;
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
        }
        return height;
    }

    /**
     * calculate the width of View
     *
     * @param widthMode
     * @param widthSize
     * @return
     */
    private int CalculateWidth(int widthMode, int widthSize) {
        int width = 0;
        switch (widthMode) {
            case MeasureSpec.AT_MOST://表示子布局限制在一个最大值内，一般为WARP_CONTENT
                int desired = (int) (getPaddingLeft() + circleR * 2 + getPaddingRight());
                width = desired;
                break;
            case MeasureSpec.EXACTLY://一般是设置了明确的值或者是MATCH_PARENT
                width = widthSize;
                break;
        }
        return width;
    }

    private List<PieData> datas; //数据源

    public void setData(List<PieData> datas) {
        this.datas = datas;
    }

    /**
     * 设置圆饼半径
     *
     * @param circleR
     */
    public void setCircleR(int circleR) {
        this.circleR = circleR;
    }

    public void setShowValue(boolean showValue) {
        isShowValue = showValue;
    }

    public void setShowComment(boolean showComment) {
        isShowComment = showComment;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();//获取View宽度
        height = getHeight();//获取View高度
        drawChart(canvas);//绘制圆饼表
        drawValue(canvas);//绘制具体值
        drawComment(canvas);
    }

    /**
     * 绘制注释颜色对应区，匹配数据源个素的小圆点
     *
     * @param canvas
     */
    private void drawComment(Canvas canvas) {

    }

    /**
     * 绘制具体的值
     *
     * @param canvas
     */
    private void drawValue(Canvas canvas) {

    }

    /**
     * 绘制基本的表
     *
     * @param canvas
     */
    private void drawChart(Canvas canvas) {
        RectF rectF = new RectF(width / 2 - circleR, height / 2 - circleR, width / 2 + circleR, height / 2 + circleR);
        if (datas == null)
            return;
        if (chartThread != null && !chartThread.isThreadRunning()) {
            chartThread.start();
        }
        int startA = 0;//临时存储已经完成动画的绘制起点
        for (PieData data : datas) {
            if (data.completeStatus)//该data绘制动画已经完成，可以直接绘制出整体
            {
                Log.i(TAG, "                 ");
                Log.i(TAG, "**********************************************************");
                Log.i(TAG, "data.completeStatus true " + data.name);
                Log.i(TAG, "data.completeStatus true startAngle " + startAngle);
                Log.i(TAG, "**********************************************************");
                Log.i(TAG, "                  ");
                canvas.drawArc(rectF, startA, data.getValue(), true, data.paint);
                startA += data.getValue();
            } else {
                Log.i(TAG, "                  ");
                Log.i(TAG, "**********************************************************");
                Log.i(TAG, "data.completeStatus false " + data.name);
                Log.i(TAG, "data.completeStatus false startAngle" + startAngle);
                Log.i(TAG, "data.completeStatus false sweepAngle" + sweepAngle);
                Log.i(TAG, "**********************************************************");
                Log.i(TAG, "                  ");
                canvas.drawArc(rectF, startAngle, sweepAngle, true, data.paint);
                break;
            }

        }
    }

    private ChartThread chartThread = new ChartThread();

    private int startAngle = 0;//圆弧每次开始的角度
    private int sweepAngle = 0;//圆弧实际角度


    /**
     * 展开动画控制线程
     */
    private class ChartThread extends Thread {

        private boolean isThreadRunning = false;

        public ChartThread() {

        }

        @Override
        public synchronized void start() {
            super.start();
            isThreadRunning = true;
        }

        @Override
        public void run() {
            super.run();
            for (PieData data : datas) {
                int angle = data.getValue();
                while (angle-- > 0) {
                    sweepAngle++;
                    postInvalidate();
                    try {
                        Thread.sleep(10);//设置刷新速度
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                data.completeStatus = true;//该部分绘制动画完成
                startAngle += data.getValue();//计算下一起点位置
                sweepAngle = 0;//重置当前相对起点的实际位置
            }
        }

        public boolean isThreadRunning() {
            return isThreadRunning;
        }
    }

    public static class PieData {
        public PieData(String name, int color, int value) {
            this.name = name;
            this.value = value;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color);
            paint.setTextSize(20f);
        }

        String name;
        int value;
        Paint paint;
        boolean completeStatus = false;//记录该部分是否已经绘制完成

        public int getValue() {
            return 360 * value / 100;
        }

        public void drawSelf(Canvas canvas) {

        }
    }
}
