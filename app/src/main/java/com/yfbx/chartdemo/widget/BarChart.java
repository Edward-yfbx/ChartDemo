package com.yfbx.chartdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:2017/12/26
 * Author:Edward
 * Description:
 */

public class BarChart extends View {

    private int defaultColor = 0xFF999999;
    private float width;
    private float height;
    private int textColor = defaultColor;
    private int axesColor = defaultColor;
    private int lineColor = 0xFF55B0FB;

    private float perX = 128;//X轴每一格宽度
    private float perY = 128;//Y轴每一格高度
    private float textSize = 24;
    private float textW = 72;
    private float textH = 72;
    private float chartW;
    private float chartH;

    private Paint paint;

    private Context context;
    private int maxY = 100;
    private int countY = 5;
    private List<CharData> data;


    public BarChart(Context context) {
        this(context, null);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        this.context = context;

        //初始化尺寸
        perX = dp2px(32);
        perY = dp2px(32);
        textW = dp2px(24);
        textH = dp2px(24);
        textSize = sp2px(12);

        //初始化画笔
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        initData();

    }

    /**
     * 初始化数据，让数据不为空
     */
    private void initData() {
        data = new ArrayList<>();
        data.add(new CharData("1", 0));
        data.add(new CharData("2", 0));
        data.add(new CharData("3", 0));
        data.add(new CharData("4", 0));
        data.add(new CharData("5", 0));
        data.add(new CharData("6", 0));
        data.add(new CharData("7", 0));
    }

    /**
     * 设置Y 轴范围
     */
    public void setRangeY(int maxY, int countY) {
        this.maxY = maxY;
        this.countY = countY;
    }

    /**
     * 设置数据(其他参数设置好，最后设置数据，这样只需要重绘一次)
     */
    public void setData(List<CharData> data) {
        this.data = data;
        requestLayout();
        invalidate();
    }

    /**
     * 表格每一格的距离
     */
    public void setChartSize(float perX, float perY) {
        this.perX = dp2px(perX);
        this.perY = dp2px(perY);
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);

        if (modeH == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            chartH = height - textH * 2;
            perY = chartH / countY;
        } else {
            chartH = countY * perY;
            height = chartH + textH * 2;
        }

        if (modeW == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            chartW = width - textW * 2;
            perX = chartW / data.size();
        } else {
            chartW = data.size() * perX;
            width = chartW + textW * 2;
        }
        setMeasuredDimension((int) width + 1, (int) height + 1);//存在精度损失
    }


    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        drawY(canvas);
        drawX(canvas);
    }

    private void drawY(Canvas canvas) {

        canvas.translate(textW, textH);
        paint.setColor(axesColor);
        canvas.drawLine(0, -textH, 0, chartH, paint);


        for (int i = 0; i <= countY; i++) {
            paint.setColor(axesColor);
            canvas.drawLine(0, i * perY, chartW, i * perY, paint);

            String text = String.valueOf(maxY - maxY / countY * i);
            float textX = -textW;
            float textY = i * perY;
            canvas.drawText(text, textX, textY, paint);
        }

    }


    private void drawX(Canvas canvas) {
        canvas.translate(textW / 2, chartH);
        Rect textSize = getTextSize(String.valueOf(data.get(0).key));
        Rect valueSize = getTextSize(String.valueOf(data.get(0).value));

        for (int i = 0; i < data.size(); i++) {
            //X轴
            String text = data.get(i).key;
            paint.setColor(textColor);
            canvas.drawText(text, i * perX, textH, paint);

            Number value = data.get(i).value;
            String num = String.valueOf(value);
            //点
            paint.setColor(lineColor);
            float pointY = -chartH * Float.valueOf(num) / maxY;
            float pointX = i * perX + textSize.width() / 2;

            //值
            float textX = pointX - valueSize.width() / 2;
            float textY = pointY - valueSize.height() / 2;
            canvas.drawText(num, textX, textY, paint);

            RectF rectF = new RectF(textX, pointY, pointX + valueSize.width() / 2, 0);
            canvas.drawRect(rectF, paint);
        }

    }

    /**
     * 测量文字尺寸
     */
    private Rect getTextSize(String text) {
        Rect rect = new Rect();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * 坐标轴颜色
     */
    public void setAxesColor(int axesColor) {
        this.axesColor = axesColor;
    }

    /**
     * 折线颜色
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * 文字颜色
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /**
     * 文字尺寸
     */
    public void setTextSize(float textSize) {
        this.textSize = SizeTranslator.sp2px(context, textSize);
    }

    /**
     * 重绘
     */
    public void drawChart() {
        requestLayout();
        invalidate();
    }

    /**
     * dp转换成px
     */
    private float dp2px(float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    /**
     * sp转换成px
     */
    public float sp2px(float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

}
