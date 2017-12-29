package com.yfbx.chartdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yfbx.chartdemo.R;
import com.yfbx.chartdemo.widget.PieChart;
import com.yfbx.chartdemo.widget.PieData;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2017/12/28
 * Author:Edward
 * Description:
 */

public class PieActivity extends Activity {

    PieChart pieChart;
    PieChart pieChart2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setData(getData());

        pieChart2 = findViewById(R.id.pieChart2);
        pieChart2.setValueFormat("#.00");
        pieChart2.setChartTitle("设备运行情况", 14, 0xFF888888);
        pieChart2.setData(getData2());

    }


    private List<PieData> getData() {
        List<PieData> data = new ArrayList<>();
        data.add(new PieData("Part-1", 0.25f, 0xFFFFC152));
        data.add(new PieData("Part-2", 0.25f, 0xFF7AD876));
        data.add(new PieData("Part-3", 0.25f, 0xFF55B0FB));
        data.add(new PieData("Part-4", 0.25f, 0xFFDD5044));
        return data;
    }

    private List<PieData> getData2() {
        List<PieData> data = new ArrayList<>();
        data.add(new PieData("设备正常占比", 0.75f, 0xFF7AD876));
        data.add(new PieData("设备异常占比", 0.25f, 0xFFDD5044));
        return data;
    }
}
