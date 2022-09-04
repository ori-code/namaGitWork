package com.example.namaprojectfirebase;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GraphClass extends AppCompatActivity {
    public static ArrayList barArryList;
    LineChart mpLineChart;
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String [] days = {"1","2","3","4","5","6","7","8","9"};
    public static String [] workArray = new String[1000];
//public static String [] workArray = {"dlkdlkd", " dokodk"};
    public int workArrayCount = 0;

    ArrayList<String> purchasesForGraphs;
    DatabaseReference finalCheckPurchases;
    //Sales of product // name or id // running on sales // counting name + quantity // capturing time
    public AutoCompleteTextView editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs_layout);


        finalCheckPurchases = FirebaseDatabase.getInstance().getReference("orders");
        finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);


        purchasesForGraphs = new ArrayList<String>();

        //to fill your Spinner
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Sales of product");
        spinnerArray.add("Sales and buying");
        spinnerArray.add("Overall sales");
        spinnerArray.add("Overall shipments");
        spinnerArray.add("Specific shipments");
        spinnerArray.add("Revenue overall year/month/day");
        spinnerArray.add("Maam");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerGraphs);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Object item = adapterView.getItemAtPosition(position);
                if (item == "Sales of product") {
                   System.out.println("HEYYY");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                    Product product = snapshot.getValue(Product.class);
//                        System.out.println("PURCHASES DATA : " + snapshot);
//
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };






        //@@@@@@@@@@@




        BarChart mChart = (BarChart) findViewById(R.id.bar_chart);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);

        XAxis xaxis = mChart.getXAxis();
        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(1f);
        xaxis.setDrawLabels(true);
        xaxis.setDrawAxisLine(false);
        xaxis.setValueFormatter(new IndexAxisValueFormatter(months));


        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setEnabled(true);
        yAxisLeft.setValueFormatter(new IndexAxisValueFormatter(days));

        mChart.getAxisRight().setEnabled(false);



        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        for (int i = 0; i < 12; ++i) {
            BarEntry entry = new BarEntry(i, (i+1)*10);
            valueSet1.add(entry);
        }

        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(valueSet1, "");
        barDataSet.setColor(Color.CYAN);
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(12f);
        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);
        mChart.setData(data);
        mChart.invalidate();




//line charts

//
////        ArrayList<Entry>dataVals = new ArrayList<Entry>();
////        dataVals.add(new Entry(0,20));
////        dataVals.add(new Entry(1,30));
////        dataVals.add(new Entry(2,40));
////LINES
//        mpLineChart = (LineChart) findViewById(R.id.line_chart);
//        Description description = new Description();
//        description.setText("The new table");
//        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Jelly");
////        LineDataSet lineDataSet2 = new LineDataSet(dataValues2(), "Pomodoro");
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        lineDataSet1.setColors(ColorTemplate.PASTEL_COLORS);
//        lineDataSet1.setValueTextSize(8f);
//        lineDataSet1.setLineWidth(4f);
////        lineDataSet2.setColors(ColorTemplate.PASTEL_COLORS);
//        dataSets.add(lineDataSet1);
////        dataSets.add(lineDataSet2);
//        System.out.println("DONT GET WHAT IS GOING ON " + dataSets.toString());
//        LineData data = new LineData(dataSets);
//        System.out.println("DONT GET WHAT IS GOING ON " + dataSets.toString() + "DATA TO STRING " + data.getXMin());
//        mpLineChart.setDescription(description);
//        mpLineChart.setData(data);
//        mpLineChart.invalidate();
//    }
////LINES
//
//
////BAR CHART
////        BarChart barChart = findViewById(R.id.line_chart);
////        getData();
////        BarDataSet barDataSet = new BarDataSet(barArryList, "Sales per Date" );
////        BarData barData = new BarData(barDataSet);
////        barChart.setData(barData);
////        //color bar data set
////        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
////        //text color
////        barDataSet.setValueTextColor(Color.BLACK);
////
////        //set text size
////        barDataSet.setValueTextSize(1f);
////        barChart.getDescription().setEnabled(false);
////
////    }
////BAR CHART END
//        private ArrayList<Entry> dataValues1() {
//            ArrayList<Entry> dataVals = new ArrayList<Entry>();
//            dataVals.add(new Entry(0, 2));
//            dataVals.add(new Entry(1,4));
//      ;
//
//            return dataVals;
//        }
//
//
//
//
//
//
////    private ArrayList<Entry> dataValues2() {
////        ArrayList<Entry> dataVals = new ArrayList<Entry>();
////        dataVals.add(new Entry(0, 0));
////        dataVals.add(new Entry(1,4,15));
////        dataVals.add(new Entry(2, 20));
////        dataVals.add(new Entry(3, 10));
////        dataVals.add(new Entry(5, 30));
////        return dataVals;
////    }
//
//
//
//        private void getData ()
//        {
//            barArryList = new ArrayList();
//
////        for(int i = 0; i < 5; i++){
////            for(int j = 0; j < 5; j++){
////                System.out.println("I is : " + i + "J IS : "+ j);
////                barArryList.add(new BarEntry(i, j));
////            }
////        }
//
//            barArryList.add(new BarEntry(0f, 25));
//            barArryList.add(new BarEntry(2f, 25));
//            barArryList.add(new BarEntry(3f, 45));
//            barArryList.add(new BarEntry(4f, 55));
//
//        }
    }


    ValueEventListener valueEventListenerSalesOfProduct = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                final List <String> nameOfProducts = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    System.out.println("PURCHASES DATA : " + snapshot);
                    String runningString = snapshot.getValue().toString();
                    System.out.println("running " +runningString);
                    String search = new String();


                    String sentence = runningString;




//                    for(int j = 0; j< runningString.length(); j++){
//                        runningString.sub
//                    }



                    for(int i = 0; i < Login.anArrayOfProducts.length-1; i++){

                        if( Login.anArrayOfProducts[i]!= null){
                            search  = Login.anArrayOfProducts[i];
                        }

                        System.out.println("the search string " + search);
                        if ( sentence.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {

                            System.out.println("I found the keyword" + search);

                        } else {

                            System.out.println("not found");

                        }
                    }




                    nameOfProducts.add(runningString);
                    System.out.println("LIST ITEM " +nameOfProducts);
                    System.out.println("PRODUCTS IS " + Login.anArrayOfProducts[0]);



//                    for(int j = 0; j < Login.anArrayOfProducts.length; j+=2){
//                        if(Login.anArrayOfProducts[j]!=null) {
//                            System.out.println("HEYYYYYaaa " + Login.anArrayOfProducts[j].toString());
//                            String lkal = new String();
//                            lkal = Login.anArrayOfProducts[j].toString();
//                            System.out.println("THE NEW TRING " + lkal);
//                              workArray[workArrayCount] = lkal;
//                                System.out.println("work is : " + workArray[workArrayCount]);
//                                if(workArrayCount<999)
//                                    workArrayCount++;
//                        }
//                    }
                }


//                Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
//                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(UAdminActivity.this, android.R.layout.simple_spinner_item, areas);
//                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                areaSpinner.setAdapter(areasAdapter);


                editText = findViewById(R.id.autoComplete);
                ArrayAdapter <String> adapter = new ArrayAdapter<String>(GraphClass.this, android.R.layout.simple_list_item_1, nameOfProducts);
                editText.setAdapter(adapter);

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    public void sendData(View view) {
        System.out.println("the send is clicked");
        String textFromAutoComplete = editText.getText().toString();
        System.out.println("THE AUTOCOMPLETE " + textFromAutoComplete);
    }
}

