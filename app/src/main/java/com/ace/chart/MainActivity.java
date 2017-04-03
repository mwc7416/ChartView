package com.ace.chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pieChart = (PieChart) findViewById(R.id.pie);
        List<PieChart.PieData> datas = new ArrayList<>();
        PieChart.PieData data1 = new PieChart.PieData("pie1", getColor(R.color.colorAccent), 10);
        PieChart.PieData data2 = new PieChart.PieData("pie2", getColor(R.color.red), 20);
        PieChart.PieData data3 = new PieChart.PieData("pie3", getColor(R.color.green), 30);
        PieChart.PieData data4 = new PieChart.PieData("pie4", getColor(R.color.black), 40);
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        datas.add(data4);
        pieChart.setData(datas);
    }
}
