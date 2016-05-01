package com.cuimengqi.kchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by treycc on 2016/5/1.
 */
public class Chart extends View {

    private static Paint paint;
    private static Path path;
    private Context context;
    private float width;
    private float height;
    private float chartPadding;
    private float valuePadding;
    private float timePadding;
    private float Xpadding;
    private float leftMargin;

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        valuePadding = DenstyUtils.dip2px(context, 4);
        timePadding = DenstyUtils.dip2px(context, 12);
        chartPadding = DenstyUtils.dip2px(context, 12);
        leftMargin = DenstyUtils.dip2px(context, 64);
    }

    public Chart(Context context) {
        super(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        canvas.translate(0, DenstyUtils.dip2px(context, 150));
        width = canvas.getWidth();
        height = canvas.getHeight();
        Xpadding = (width - leftMargin - 2 * chartPadding) / 4;

        //坐标系
        //X轴加粗
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(DenstyUtils.dip2px(context, 0.8f));
        canvas.drawLine(
                0,
                DenstyUtils.dip2px(context, 0),
                width - leftMargin,
                DenstyUtils.dip2px(context, 0),
                paint);

        paint.setStrokeWidth(DenstyUtils.dip2px(context, 0.1f));
        canvas.drawLine(
                0,
                DenstyUtils.dip2px(context, -50),
                width - leftMargin,
                DenstyUtils.dip2px(context, -50),
                paint);
        canvas.drawLine(
                0,
                DenstyUtils.dip2px(context, -100),
                width - leftMargin,
                DenstyUtils.dip2px(context, -100),
                paint);
//        canvas.drawLine(
//                0,
//                DenstyUtils.dip2px(context, -150),
//                width - leftMargin,
//                DenstyUtils.dip2px(context, -150),
//                paint);
        //坐标值
        //待增加sp-px转换
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(48);

        //横坐标值
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("2016-4-29", 0, 48 + timePadding, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("2016-5-10", (width - leftMargin) / 2, 48 + timePadding, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("2016-5-28", width - leftMargin, 48 + timePadding, paint);

        //纵坐标收益
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("00.00%", width - leftMargin + valuePadding, 0, paint);
        canvas.drawText("50.00%", width - leftMargin + valuePadding, DenstyUtils.dip2px(context, -50), paint);
        canvas.drawText("100.00%", width - leftMargin + valuePadding, DenstyUtils.dip2px(context, -100), paint);

        //绘制折线
        //初始化值
        List<Float> integers = new ArrayList<>();
        integers.add((float) -10);
        integers.add((float) -110);
        integers.add((float) -98);
        integers.add((float) -20);
        integers.add((float) -130);
        //纵坐标
        List<Float> yvalues = FormYvalues(integers);
        //横坐标
        float Xvalues[] = {
                chartPadding,
                chartPadding + Xpadding,
                chartPadding + Xpadding * 2,
                chartPadding + Xpadding * 3,
                chartPadding + Xpadding * 4};

        //绘制路径
        path = new Path();
        //绘制透明线填充
        Chart.paint.setColor(Color.TRANSPARENT);
        Chart.paint.setStyle(Paint.Style.STROKE);
        Chart.path.moveTo(Xvalues[0], 0);
        Chart.path.moveTo(Xvalues[0], yvalues.get(0));
        canvas.drawPath(Chart.path, Chart.paint);

        //绘制曲线
        Chart.paint.setStrokeWidth(DenstyUtils.dip2px(context, 2));
        Chart.paint.setStyle(Paint.Style.STROKE);
        Chart.paint.setColor(Color.WHITE);
        Chart.path.lineTo(Xvalues[1], yvalues.get(1));
        Chart.path.lineTo(Xvalues[2], yvalues.get(2));
        Chart.path.lineTo(Xvalues[3], yvalues.get(3));
        Chart.path.lineTo(Xvalues[4], yvalues.get(4));
        canvas.drawPath(Chart.path, Chart.paint);

        //绘制透明线填充
        Chart.paint.setColor(Color.TRANSPARENT);
        Chart.path.lineTo(Xvalues[4], 0);
        Chart.path.lineTo(Xvalues[0], 0);
        canvas.drawPath(Chart.path, Chart.paint);

        //填充曲线到X轴
        Chart.paint.setStyle(Paint.Style.FILL);
        Chart.paint.setColor(Color.WHITE);
        Chart.paint.setAlpha(120);
        canvas.drawPath(Chart.path, Chart.paint);

    }

    private List<Float> FormYvalues(List<Float> integers) {
        List<Float> yvalues = new ArrayList<>();
        for (float value : integers) {
            int i = DenstyUtils.dip2px(context, value);
            yvalues.add((float) i);
        }
        return yvalues;
    }
}
