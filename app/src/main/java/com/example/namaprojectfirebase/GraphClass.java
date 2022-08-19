package com.example.namaprojectfirebase;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphClass extends AppCompatActivity {
    public static ArrayList barArryList;
    LineChart mpLineChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs_layout);

//        ArrayList<Entry>dataVals = new ArrayList<Entry>();
//        dataVals.add(new Entry(0,20));
//        dataVals.add(new Entry(1,30));
//        dataVals.add(new Entry(2,40));
//LINES
        mpLineChart = (LineChart) findViewById(R.id.line_chart);
        Description description = new Description();
        description.setText("The new table");
        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Jelly");
        LineDataSet lineDataSet2 = new LineDataSet(dataValues2(), "Pomodoro");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        lineDataSet1.setColors(ColorTemplate.PASTEL_COLORS);
        lineDataSet1.setValueTextSize(8f);
        lineDataSet1.setLineWidth(4f);
        lineDataSet2.setColors(ColorTemplate.PASTEL_COLORS);
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        System.out.println("DONT GET WHAT IS GOING ON " + dataSets.toString());
        LineData data = new LineData(dataSets);
        System.out.println("DONT GET WHAT IS GOING ON " + dataSets.toString() + "DATA TO STRING " + data.getXMin());
        mpLineChart.setDescription(description);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }
//LINES


//BAR CHART
//        BarChart barChart = findViewById(R.id.line_chart);
//        getData();
//        BarDataSet barDataSet = new BarDataSet(barArryList, "Sales per Date" );
//        BarData barData = new BarData(barDataSet);
//        barChart.setData(barData);
//        //color bar data set
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        //text color
//        barDataSet.setValueTextColor(Color.BLACK);
//
//        //set text size
//        barDataSet.setValueTextSize(1f);
//        barChart.getDescription().setEnabled(false);
//
//    }
//BAR CHART END
        private ArrayList<Entry> dataValues1() {
            ArrayList<Entry> dataVals = new ArrayList<Entry>();
            dataVals.add(new Entry(0, 20));
            dataVals.add(new Entry(1,4,15));
            dataVals.add(new Entry(2, 40));
            dataVals.add(new Entry(3, 10));
            dataVals.add(new Entry(5, 160));
            return dataVals;
        }




    private ArrayList<Entry> dataValues2() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, 0));
        dataVals.add(new Entry(1,4,15));
        dataVals.add(new Entry(2, 20));
        dataVals.add(new Entry(3, 10));
        dataVals.add(new Entry(5, 30));
        return dataVals;
    }



        private void getData ()
        {
            barArryList = new ArrayList();

//        for(int i = 0; i < 5; i++){
//            for(int j = 0; j < 5; j++){
//                System.out.println("I is : " + i + "J IS : "+ j);
//                barArryList.add(new BarEntry(i, j));
//            }
//        }

            barArryList.add(new BarEntry(0f, 25));
            barArryList.add(new BarEntry(2f, 25));
            barArryList.add(new BarEntry(3f, 45));
            barArryList.add(new BarEntry(4f, 55));

        }
    }

