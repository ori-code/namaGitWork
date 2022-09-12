package com.example.namaprojectfirebase;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GraphClass extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static ArrayList barArryList;
    LineChart mpLineChart;
    public static String[] datesArr = new String[500]; // array of dates for graph first date and second the value of sales
    //    public static String[] datesArr = {"12","33"};
    public static String [] valuesFromDb = new String [500];
    public static String[] days = new String[500];

    public static String[] workArray = new String[1000];
    //public static String [] workArray = {"dlkdlkd", " dokodk"};
    public int workArrayCount = 0;
    public List<String> nameOfProducts;
    StringBuffer dates = new StringBuffer();
    ArrayList<String> purchasesForGraphs;
    DatabaseReference finalCheckPurchases;
    public static DatabaseReference refForGraphs;
    public static DatabaseReference graphRef;
    public static ValueEventListener valueEventListenerSalesOfProduct;
    public static ValueEventListener valueEventListenerSalesProductGraph;
    //Sales of product // name or id // running on sales // counting name + quantity // capturing time
    public AutoCompleteTextView editText;
    public Calendar calendarStartTime, calendarEndTime;
    public int flagDateSet = 0;
    public int flagFinishedRead = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs_layout);
        finalCheckPurchases = FirebaseDatabase.getInstance().getReference("orders");
        refForGraphs = FirebaseDatabase.getInstance().getReference("orders");
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
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerGraphs);
        spinner.setAdapter(adapter1);




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Object item = adapterView.getItemAtPosition(position);
                if (item == "Sales of product") {
                    runSalesProduct();
                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

    }





    private void runSalesProduct() {
        System.out.println("HEYYY IN INSALES RUN");
        valueEventListenerSalesOfProduct = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nameOfProducts = new ArrayList<String>();

                    System.out.println("PRODUCTS " + nameOfProducts);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        System.out.println("PURCHASES DATA : " + snapshot);
                        String runningString = snapshot.getValue().toString();
                        //System.out.println("running " +runningString);
                        String search = new String();
                        String sentence = runningString;
                        for (int i = 0; i < Login.anArrayOfProducts.length - 1; i++) {
                            if (Login.anArrayOfProducts[i] != null) {
                                System.out.println("PRODUCT NAME RUNNING "+ Login.anArrayOfProducts[i]);
                                if (Login.anArrayOfProducts[i].charAt(0) <'0' ||  Login.anArrayOfProducts[i].charAt(0)>'9') {
                                    search = Login.anArrayOfProducts[i];
                                    System.out.println("SHUGAR " + search);
                                    if (sentence.toLowerCase().indexOf(search.toLowerCase()) != -1) {

                                        if (nameOfProducts.toString().toLowerCase().indexOf(search.toLowerCase()) == -1) {
                                                 nameOfProducts.add(search);
                                             System.out.println("THE LIST IS HHH" + nameOfProducts);
                                        } else {
                                              System.out.println("NOTHING");
                                        }
                                    }


//                            if (sentence.toLowerCase().indexOf(search.toLowerCase()) != -1) {
//                                StringBuffer sbf = new StringBuffer();
//                                System.out.println("SBF 1 " + sbf);
////                                System.out.println("I found the keyword " + search + "THE VALUE IS " + sentence.charAt(sentence.toLowerCase().indexOf(search)+3));
//                                for (int z = sentence.toLowerCase().indexOf(search); z < sentence.toLowerCase().indexOf(search); z++) {
//                                            if (sentence.charAt(z) >= '0' && sentence.charAt(z) <= '9') {
//                                                System.out.println("SBF 2" + sbf);
//                                                sbf.append(sentence.charAt(z));
//                                            }
//                                        }
//
//
//                                if (nameOfProducts.toString().toLowerCase().indexOf(search.toLowerCase()) == -1) {
//                                    nameOfProducts.add(search);
//                                    System.out.println("THE LIST IS HHH" + nameOfProducts);
//                                } else {
//                                    System.out.println("NOTHING");
//                                }
//                                System.out.println("THE PRODUCT IS " + search + " VALUE " + sbf);
//                            } else {
//
//
//                            }

                                }

                            }

                        }
                    }

                    editText = findViewById(R.id.autoComplete);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GraphClass.this, android.R.layout.simple_list_item_1, nameOfProducts);
                    editText.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        System.out.println(nameOfProducts);
    }

    private void showDatePickerDialogStart() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showDatePickerDialogEnd() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void startData(View view) {
        //System.out.println("the send is clicked");
        String textFromAutoComplete = editText.getText().toString();
        System.out.println("THE AUTOCOMPLETE " + textFromAutoComplete);
        showDatePickerDialogStart();

        if (dates != null) {
            dates.delete(0, dates.length());
        }

    }

    public void endData(View view) {
        //System.out.println("the send is clicked");
        String textFromAutoComplete = editText.getText().toString();
        System.out.println("THE AUTOCOMPLETE " + textFromAutoComplete);
        showDatePickerDialogEnd();
//        if(dates!=null){
//            dates.delete(0,dates.length());
//        }

    }

    public void showGraph(View view) {

        String textFromAutoComplete = editText.getText().toString();
        System.out.println("THE AUTOCOMPLETE " + textFromAutoComplete + " DATES " + dates);
        createSalesProductGraph(textFromAutoComplete, dates);

    }

//    public void createGraphVisual(String arr[]) {
//        BarChart mChart = (BarChart) findViewById(R.id.bar_chart);
//        mChart.setDrawBarShadow(false);
//        mChart.setDrawValueAboveBar(false);
//        mChart.getDescription().setEnabled(false);
//        mChart.setDrawGridBackground(false);
//
//        //X AXIS ARR valuesFromDb
//        XAxis xaxis = mChart.getXAxis();
//        xaxis.setDrawGridLines(false);
//        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xaxis.setGranularity(1f);
//        xaxis.setDrawLabels(true);
//        xaxis.setDrawAxisLine(false);
//        xaxis.setValueFormatter(new IndexAxisValueFormatter(valuesFromDb));
//
//        //Y AXIS ARR days
//        YAxis yAxisLeft = mChart.getAxisLeft();
//        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        yAxisLeft.setDrawGridLines(false);
//        yAxisLeft.setDrawAxisLine(true);
//        yAxisLeft.setEnabled(true);
//        yAxisLeft.setValueFormatter(new IndexAxisValueFormatter(days));
//
//        mChart.getAxisRight().setEnabled(false);
//
//
//        Legend legend = mChart.getLegend();
//        legend.setEnabled(false);
//



//
//
//
//}
//
//
//
////        System.out.println("VALUE SET "  + valueSet1.get(0));
//
//
//
//        List<IBarDataSet> dataSets = new ArrayList<>();
//        BarDataSet barDataSet = new BarDataSet(valueSet1(), "");
//        barDataSet.setColor(Color.CYAN);
//        barDataSet.setDrawValues(true);
//        barDataSet.setValueTextSize(12f);
//        dataSets.add(barDataSet);
//
//        BarData data = new BarData(dataSets);
//        mChart.setData(data);
//        mChart.invalidate();
//
//    }

    public void createGraphAgainVisual (String arr[]){
        BarChart mChart = (BarChart) findViewById(R.id.bar_chart);
        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
//
//        int j = 0;
//        int k = 0;
////       TODO RUN THE GRAPH NEED TO MAKE DATA OF VALUE IN Y AXIS data ARR AND THE TIME IN X AXIS days
//        if(arr.length!=0) {
//            for (int i = 0; i < arr.length - 2; i = i + 2) {
//                valuesFromDb[k] = datesArr[i];
//                if (datesArr[i + 1] != null) {
//                    Date d = new Date(Long.parseLong(datesArr[i + 1]));
//                    String newstring = new SimpleDateFormat("yyyy-MM-dd").format(d);
//                    days[j] = newstring;
//                    System.out.println("Days in date after conversion at J place + " + j + " <-j " + days[j]);
//                    j++;
//                }
//                if (valuesFromDb[k] != null) {
//                    System.out.println("VALUES FROM DB  in k place + " + k + " <-k " + valuesFromDb[k]);
//                }
//                k++;
//            }
//
//
//            for (int i = 0; i < valuesFromDb.length; i++) {
//                j = 0;
//                k = 0;
//                System.out.println("VALUE " + valuesFromDb[k] + " DAYS " + days[j]);
//                j++;
//                k++;
//                valueSet1.add(new BarEntry(i, i * 3));
//            }
//        }
//
        BarDataSet barDataSet1 = new BarDataSet(dataValues1(), "Data Set 1");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet1);
        mChart.setData(barData);
        mChart.invalidate();
    }

    private ArrayList<BarEntry> dataValues1(){
        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        valueSet1.add(new BarEntry(33,3));
        valueSet1.add(new BarEntry(2,5));
        valueSet1.add(new BarEntry(6,9));
        return valueSet1;
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = day + "/" + month + "/" + year;
        calendarStartTime = new GregorianCalendar(year, month, day);
        if(flagDateSet == 0){
            dates.append(calendarStartTime.getTimeInMillis());
            dates.append("|");
            flagDateSet = 1;
            System.out.println("START TIME " + calendarStartTime.getTimeInMillis());
        }
        else{
            calendarEndTime = calendarStartTime;
            dates.append(calendarEndTime.getTimeInMillis());
            dates.append("|");
            System.out.println("END TIME " + calendarEndTime.getTimeInMillis());
        }
        System.out.println("DATES : START | END " + dates);
    }

    public void createSalesProductGraph (String name, StringBuffer dates) {
        System.out.println(name + " THE DATES IS " + dates);
        refForGraphs.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        flagFinishedRead = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.child(name).getValue()!=null){
                                datesArr[i] = snapshot.child(name).getValue().toString();
                                    i++;
                                datesArr[i] = snapshot.child("timeOfPlacedOrder").getValue().toString();
                                    i++;
                                System.out.println("QNTY PLEASE FROM DB" + snapshot.child(name).getValue());
                                System.out.println("DATE PLEASE " + snapshot.child("timeOfPlacedOrder").getValue());

                                for(int j = 0; j < datesArr.length; j++){
                                    if(datesArr[j]!=null){
                                        System.out.println("THE AQTUALY => " + datesArr[j]);
                                    }
                                }


                                //time and quantity
                            }
                            System.out.println("WORK PLEASE " + snapshot.child(name).getValue());

                        }
                        flagFinishedRead = 1;
                        System.out.println(datesArr[0] + " <- 0 " + datesArr[1] + " <- 1 "+ datesArr[2]+  " <-2 " );
                        createGraphAgainVisual(datesArr);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

}

