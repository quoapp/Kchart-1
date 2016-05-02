package com.cuimengqi.kchart;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnDrawListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CandleStickChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = (CandleStickChart) findViewById(R.id.candle_chart);
        initChart();
    }

    private void initChart() {
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
//        int highestVisibleXIndex = mChart.getHighestVisibleXIndex();
//        Log.d("highestVisibleXIndex", highestVisibleXIndex+"");

        mChart.setData(candleData);
        mChart.invalidate();

    }

    private CandleData getCandleData() {

        ArrayList<CandleEntry> candleYvals1 = new ArrayList<>();
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
        }

        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            xVals.add("" + (1990 + i));
        }

        CandleDataSet set1 = new CandleDataSet(candleYvals1, "Data Set");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);


        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(122, 242, 84));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.BLUE);


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
                break;
            case R.id.getchartmax:
                float yChartMax = mChart.getYChartMax();
                float yChartMin = mChart.getYChartMin();
                Toast.makeText(this,yChartMax +":::::::::::"+ yChartMin,Toast.LENGTH_SHORT).show();
                Log.d("getChartMax", yChartMax +":::::::::::"+ yChartMin);
                break;
            case R.id.double_tap:
                mChart.setDoubleTapToZoomEnabled(!mChart.isDoubleTapToZoomEnabled());
                break;
            default:
                break;
        }


        return true;
    }
}
