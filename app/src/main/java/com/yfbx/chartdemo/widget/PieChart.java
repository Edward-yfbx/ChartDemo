package com.yfbx.chartdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Date:2017/12/26
 * Author:Edward
 * Description:
 */

public class PieChart extends View {

    private int defColor = 0xFF888888;
    private float width;
    private float height;

    private Paint paint;

    private Context context;
    private List<PieData> data;
    private String title;
    private float titleSize;
    private int titleColor;
    private float arcWidth;
    private float descTextSize;
    private float r;//半径
    private float space = 16;
    private DecimalFormat decimalFormat;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        this.context = context;
        //初始化尺寸
        arcWidth = dp2px(24);
        descTextSize = sp2px(10);
        decimalFormat = new DecimalFormat("#.00");

        //初始化画笔
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(arcWidth);
        paint.setAntiAlias(true);

        initData();

    }

    /**
     * 初始化数据，让数据不为空
     */
    private void initData() {
        data = new ArrayList<>();
        data.add(new PieData("Part1", 0.25f, 0xFFFFC152));
        data.add(new PieData("Part2", 0.25f, 0xFF7AD876));
        data.add(new PieData("Part3", 0.25f, 0xFF55B0FB));
        data.add(new PieData("Part4", 0.25f, 0xFFDD5044));
    }


    /**
     * 设置数据(其他参数设置好，最后设置数据，这样只需要重绘一次)
     */
    public void setData(List<PieData> data) {
        this.data = data;
        requestLayout();
        invalidate();
    }


    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 宽高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(width / 2, height / 2);  //中心点
        float d = Math.min(width, height) - arcWidth * 4;//直径
        r = d / 2;
        RectF rectF = new RectF(-r, -r, r, r);

        float sumAngle = 0;
        for (int i = 0; i < data.size(); i++) {
            PieData pieData = data.get(i);
            paint.setColor(pieData.color);
            paint.setStrokeWidth(arcWidth);
            paint.setStyle(Paint.Style.STROKE);
            float angle = 360 * pieData.percent;
            canvas.drawArc(rectF, sumAngle, angle, false, paint);

            sumAngle += angle;
            //绘制说明文字
            drawText(canvas, pieData, angle, sumAngle);

        }

        //绘制标题
        drawTitle(canvas);
    }


    /**
     * 绘制说明文字
     */
    private void drawText(Canvas canvas, PieData data, float arcAngle, float sumAngle) {
        double ang = getAngle(arcAngle);            //角度
        double sin = Math.sin(ang);                 //正弦值
        double cos = Math.cos(ang);                 //余弦值
        float x = (float) (sin * r);                //弧中点在X坐标上的长度
        float y = (float) (cos * r);                //弧中点在Y坐标上的长度

        if (sumAngle > 90 && sumAngle <= 270) {
            x = -x;
        }
        if (sumAngle > 180) {
            y = -y;
        }

        Rect textSize = getDescTextSize(data.title);

        //线
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(defColor);
        paint.setStrokeWidth(1);
        float lineEnd = x > 0 ? x + textSize.width() + arcWidth + space : x - textSize.width() - arcWidth - space;
        canvas.drawLine(x, y, lineEnd, y, paint);

        //文字
        paint.setColor(defColor);
        float textLeft = x > 0 ? x + arcWidth + space : x - textSize.width() - arcWidth - space;
        float textTop = y + textSize.height();
        canvas.drawText(data.title, textLeft, textTop, paint);

        //百分比值
        String value = decimalFormat.format(data.percent * 100) + "%";
        float vWidth = getDescTextSize(value).width();
        paint.setColor(data.color);
        float vLeft = x > 0 ? lineEnd - vWidth : lineEnd;
        float vTop = y - textSize.height() / 2;
        canvas.drawText(value, vLeft, vTop, paint);

    }

    /**
     * 绘制标题
     */
    private void drawTitle(Canvas canvas) {
        if (!TextUtils.isEmpty(title)) {
            Rect textSize = getTextSize(title);
            canvas.drawText(title, -textSize.width() / 2, textSize.height() / 2, paint);
        }
    }

    /**
     * 测量标题文字尺寸
     */
    private Rect getTextSize(String text) {
        Rect rect = new Rect();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(titleSize);
        paint.setColor(titleColor);
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * 测量描述文字尺寸
     */
    private Rect getDescTextSize(String text) {
        Rect rect = new Rect();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(descTextSize);
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * 设置标题(图表中间的文字)
     */
    public void setChartTitle(String title, float titleSize, int titleColor) {
        this.title = title;
        this.titleSize = SizeTranslator.sp2px(context, titleSize);
        this.titleColor = titleColor;
    }

    /**
     * 描述文字尺寸
     */
    public void setDescTextSize(float descTextSize) {
        this.descTextSize = sp2px(descTextSize);
    }

    /**
     * 圆弧宽度
     */
    public void setArcWidth(float arcWidth) {
        this.arcWidth = SizeTranslator.dp2px(context, arcWidth);
    }

    /**
     * 通过弧度计算角度
     */
    private double getAngle(float arc) {
        return Math.PI * arc / 360;
    }


    /**
     * 数值格式
     * "#.00"
     */
    public void setValueFormat(String format) {
        decimalFormat = new DecimalFormat(format);
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
