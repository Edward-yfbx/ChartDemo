package com.yfbx.chartdemo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.yfbx.chartdemo.R;
import com.yfbx.chartdemo.widget.BarChart;
import com.yfbx.chartdemo.widget.CharData;
import com.yfbx.chartdemo.widget.LineChart;

import java.util.ArrayList;
import java.util.List;


public class LineActivity extends Activity {

    LineChart mChart;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        mChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);

        mChart.setRangeY(100, 5);
        mChart.setData(getData());

        barChart.setRangeY(100, 5);
        barChart.setData(getData());


    }

    private List<CharData> getData() {
        List<CharData> data = new ArrayList<>();
        data.add(new CharData("12/20", 25));
        data.add(new CharData("12/21", 77));
        data.add(new CharData("12/22", 44));
        data.add(new CharData("12/23", 88));
        data.add(new CharData("12/24", 66));
        data.add(new CharData("12/25", 33));
        data.add(new CharData("12/26", 98));
        return data;
    }


}
