package com.cuimengqi.kchart;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CandleStickChart mChart;
    private BarChart barChart;
    private BarData barData;

    private class CoupleChartGestureListener implements OnChartGestureListener {

        private Chart srcChart;
        private Chart[] dstCharts;

        public CoupleChartGestureListener(Chart srcChart, Chart[] dstCharts) {
            this.srcChart = srcChart;
            this.dstCharts = dstCharts;
        }

        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            syncCharts();
        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {
            syncCharts();
        }

        private void syncCharts() {
            Matrix srcMatrix;
            float[] srcVals = new float[9];
            Matrix dstMatrix;
            float[] dstVals = new float[9];

            // get src chart translation matrix:
            srcMatrix = srcChart.getViewPortHandler().getMatrixTouch();
            srcMatrix.getValues(srcVals);

            // apply X axis scaling and position to dst charts:
            for (Chart dstChart : dstCharts) {
                if (dstChart.getVisibility() == View.VISIBLE) {
                    dstMatrix = dstChart.getViewPortHandler().getMatrixTouch();
                    dstMatrix.getValues(dstVals);
                    dstVals[Matrix.MSCALE_X] = srcVals[Matrix.MSCALE_X];
                    dstVals[Matrix.MTRANS_X] = srcVals[Matrix.MTRANS_X];
                    dstMatrix.setValues(dstVals);
                    dstChart.getViewPortHandler().refresh(dstMatrix, dstChart, true);
                }
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = (CandleStickChart) findViewById(R.id.candle_chart);
        barChart = (BarChart) findViewById(R.id.bar_chart);
        initCandleStickChart();
        initBarChart();
        syncCharts();//建立几个图表滑动的连接
    }

    private void syncCharts() {
        barChart.setOnChartGestureListener(new CoupleChartGestureListener(barChart, new Chart[]{mChart}));
        mChart.setOnChartGestureListener(new CoupleChartGestureListener(mChart, new Chart[]{barChart}));
    }

    private void initBarChart() {
        barChart.setDrawBarShadow(false);//背后阴影
        barChart.setDrawValueAboveBar(true);//entry数值显示位置

        barChart.setDescription("");
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);//双击放大关闭
        barChart.setScaleYEnabled(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(false);
        YAxis axisLeft = barChart.getAxisLeft();
        YAxis axisRight = barChart.getAxisRight();
        axisLeft.setEnabled(false);
        axisRight.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);


        barChart.setAutoScaleMinMaxEnabled(true);

        barChart.setData(barData);

    }

    private void initCandleStickChart() {
        mChart.setDescription("");
        mChart.setNoDataText("暂时没有数据");
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        mChart.getLegend().setEnabled(false);
        CandleData candleData = getCandleData();


        mChart.setDoubleTapToZoomEnabled(false);//双击放大关闭
        mChart.setScaleYEnabled(false);//纵向zoom关闭
        mChart.setAutoScaleMinMaxEnabled(true);
//        int highestVisibleXIndex = mChart.getHighestVisibleXIndex();
//        Log.d("highestVisibleXIndex", highestVisibleXIndex+"");

        mChart.setData(candleData);
        mChart.invalidate();

    }


    private CandleData getCandleData() {

        ArrayList<CandleEntry> candleYvals1 = new ArrayList<>();
        ArrayList<BarEntry> barYVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < 40; i++) {
            float mult = (100 + 1);
            float val = (float) (Math.random() * 40) + mult;

            float high = (float) (Math.random() * 9) + 8f;
            float low = (float) (Math.random() * 9) + 8f;

            float open = (float) (Math.random() * 6) + 1f;
            float close = (float) (Math.random() * 6) + 1f;

            boolean even = i % 2 == 0;

            candleYvals1.add(new CandleEntry(i, val + high, val - low, even ? val + open : val - open,
                    even ? val - close : val + close));
            barYVals1.add(new BarEntry(val, i));
        }

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<String> barVals = new ArrayList<String>();
        for (int i = 0; i < 40; i++) {
            xVals.add("" + (1990 + i));
            barVals.add("" + (1990 + i));
        }

        //蜡烛图数据
        CandleDataSet set1 = new CandleDataSet(candleYvals1, "");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(122, 242, 84));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.BLUE);

        //柱状图数据
        BarDataSet set2 = new BarDataSet(barYVals1, "");
        set2.setBarSpacePercent(35f);
        set2.setColors(new int[]{Color.RED, Color.GREEN});
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set2);
        barData = new BarData(xVals, dataSets);
        barData.setDrawValues(false);

        return new CandleData(xVals, set1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.candle_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtscreen:
                mChart.fitScreen();
                barChart.fitScreen();
                break;
            case R.id.getchartmax:
                float yChartMax = mChart.getYChartMax();
                float yChartMin = mChart.getYChartMin();
                Toast.makeText(this, yChartMax + ":::::::::::" + yChartMin, Toast.LENGTH_SHORT).show();
                Log.d("getChartMax", yChartMax + ":::::::::::" + yChartMin);
                break;
            case R.id.double_tap:
                mChart.setDoubleTapToZoomEnabled(!mChart.isDoubleTapToZoomEnabled());
                barChart.setDoubleTapToZoomEnabled(!barChart.isDoubleTapToZoomEnabled());
                break;
            default:
                break;
        }


        return true;
    }
}
