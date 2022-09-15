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
import com.github.mikephil.charting.components.AxisBase;
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
import java.util.Locale;

public class GraphClass extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static ArrayList barArryList;
    LineChart mpLineChart;
    public static String[] datesArr = new String[500]; // array of dates for graph first date and second the value of sales
        public static String[] datesArrStrings = {"12/05/22","13/05/22"};
        public static String [] valueArrStrings = {"10","20"};
    public static String [] valuesFromDb = new String [500];
    public static String[] days = new String[500];
    public static int [] values = new int [500];
    public static long startDateInMilliseconds = 0,endDateInMilliseconds = 0;
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
    public String [] nameOfProductsInAllBuyingAndSale;

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
                if (item == "Sales and buying") {
                    System.out.println("THE SALES BUYING");
                    System.out.println(Login.anArrayOfProducts[0]);
                    runSalesAndBuyingProduct();
//                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

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

    //converting dates to epoch

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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = day + "/" + month + "/" + year;
        calendarStartTime = new GregorianCalendar(year, month, day);
        if(flagDateSet == 0){
            startDateInMilliseconds = calendarStartTime.getTimeInMillis();
            System.out.println("LONG START  " + startDateInMilliseconds);
            dates.append(calendarStartTime.getTimeInMillis());
            dates.append("|");
            flagDateSet = 1;
            System.out.println("START TIME " + calendarStartTime.getTimeInMillis());
        }
        else{
            calendarEndTime = calendarStartTime;

            endDateInMilliseconds = calendarEndTime.getTimeInMillis();
            System.out.println("LONG END  " + endDateInMilliseconds);
            dates.append(calendarEndTime.getTimeInMillis());
            dates.append("|");
            System.out.println("END TIME " + calendarEndTime.getTimeInMillis());
        }
        System.out.println("DATES : START | END " + dates);
    }

    //BUTTON SENDING
    public void showGraph(View view) {
        String textFromAutoComplete = editText.getText().toString();
        System.out.println("THE AUTOCOMPLETE " + textFromAutoComplete + " DATES " + dates);
        createSalesProductGraph(textFromAutoComplete, dates);
    }

    // GETTING SELLING VALUES AND DATES
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
                System.out.println(datesArr[0] + " <- 0 " + datesArr[1] + " <- 1 "+ datesArr[2]+  " <-2 " + datesArr[3]+  " <-3 " );
                createGraphAgainVisual(datesArr);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    //SALES OF PRODUCT GETTER
    private void runSalesProduct() {
        System.out.println("HEY IM GETTING THE PRODUCTS FOR THE LIST");
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

    private void runSalesAndBuyingProduct() {

        int count = 0;
        System.out.println("HEY IM THE SALES BUYING");
        for(int i = 0; i < Login.anArrayOfProducts.length; i++){
            System.out.println("PRODUCTS  runSalesAndBuyingProduct " + Login.anArrayOfProducts[i]);
            count ++;
        }

        valueEventListenerSalesOfProduct = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    nameOfProducts = new ArrayList<String>();


//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Product product = snapshot.getValue(Product.class);
//                        System.out.println("PURCHASES DATA : " + snapshot);
//                        String runningString = snapshot.getValue().toString();
//                        String search = new String();
//                        String sentence = runningString;
//                        for (int i = 0; i < Login.anArrayOfProducts.length - 1; i++) {
//                            if (Login.anArrayOfProducts[i] != null) {
//                                System.out.println("PRODUCT NAME RUNNING "+ Login.anArrayOfProducts[i]);
//                                if (Login.anArrayOfProducts[i].charAt(0) <'0' ||  Login.anArrayOfProducts[i].charAt(0)>'9') {
//                                    search = Login.anArrayOfProducts[i];
//                                    System.out.println("SHUGAR " + search);
//                                    if (sentence.toLowerCase().indexOf(search.toLowerCase()) != -1) {
//                                        if (nameOfProducts.toString().toLowerCase().indexOf(search.toLowerCase()) == -1) {
//                                            nameOfProducts.add(search);
//                                            System.out.println("THE LIST IS HHH" + nameOfProducts);
//                                        } else {
//                                            System.out.println("NOTHING");
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }

//                    editText = findViewById(R.id.autoComplete);
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GraphClass.this, android.R.layout.simple_list_item_1, nameOfProducts);
//                    editText.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        createGraphAgainVisualForSalesAndBuyingProduct();
//        System.out.println(nameOfProducts);
    }
    public void createGraphAgainVisualForSalesAndBuyingProduct (){
        System.out.println("createGraphAgainVisualForSalesAndBuyingProduct");
        int count = 0;
        int place = 0 ;
        System.out.println("HEY IM THE SALES BUYING");
        for(int i = 0; i < Login.anArrayOfProducts.length; i=i+2){
            System.out.println("PRODUCTS  runSalesAndBuyingProduct " + Login.anArrayOfProducts[i]);
            if(Login.anArrayOfProducts[i]!=null){
                System.out.println("THE PRODUCTS  iSSSS"  + Login.anArrayOfProducts[i]);
                count ++;
            }


        }

        String [] nameOfProductsInAllBuyingAndSale = new String [count];


        for(int i = 0; i < Login.anArrayOfProducts.length; i=i+2){
            System.out.println("PRODUCTS  runSalesAndBuyingProduct " + Login.anArrayOfProducts[i]);
            if(Login.anArrayOfProducts[i]!=null){
                nameOfProductsInAllBuyingAndSale[place] = Login.anArrayOfProducts[i];
                    System.out.println("THE PRODUCT ISssss "  + nameOfProductsInAllBuyingAndSale[place]);
                place++;

            }
        }





        BarChart mChart = (BarChart) findViewById(R.id.bar_chart);
        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<BarEntry>();
        int j = 0;
        int k = 0;
        int o = 0;
////       TODO RUN THE GRAPH NEED TO MAKE DATA OF VALUE IN Y AXIS data ARR AND THE TIME IN X AXIS days

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);

        //**add renderer**
//        BarChartCustomRenderer barChartCustomRenderer = new BarChartCustomRenderer(mChart, mChart.getAnimator(),   mChart.getViewPortHandler());
//        mChart.setRenderer(barChartCustomRenderer);



        XAxis xaxis = mChart.getXAxis();
        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(0.5f);
        xaxis.setGranularityEnabled(true);
        xaxis.setDrawLabels(true);
        xaxis.setDrawAxisLine(false);
        xaxis.setValueFormatter(new IndexAxisValueFormatter(nameOfProductsInAllBuyingAndSale));

        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setEnabled(true);

        mChart.getAxisRight().setEnabled(false);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);


//        for (int i = 0; i < days.length; i++) {
////            System.out.println(" THE datesArr it is value " +datesArr[i]  +  " and DATE in epoch " + datesArr[i+1]);
////            System.out.println(" THE valuesFromDb " + valuesFromDb[i]);
//            System.out.println(" THE days " + days[i]);
////            System.out.println(" THE datesArrStrings FAKE " + datesArrStrings[i]);
//            if(days[i]!=null) {
//                System.out.println(" THE days IN IF  " + days[i]);
//                System.out.println(" THE values IN IF  " + values[i]);
//                BarEntry entry = new BarEntry(1, 20); // x - place in array of dates y - values in array of values
//                valueSet1.add(entry);
//                if(days[i+1]==null){
//                    break;
//                }
//
//            }
//
//        }






        //red graph SELLS
        BarEntry entry1 = new BarEntry(0, 20); // x - place in array of dates y - values in array of values
        valueSet1.add(entry1);

        BarEntry entry3 = new BarEntry(1, 40); // x - place in array of dates y - values in array of values
        valueSet1.add(entry3);

        //blue graph BUYING OVERALL QUANTITY IN ADDING PLUS ALL
        BarEntry entry2 = new BarEntry(0, 10); // x - place in array of dates y - values in array of values
        valueSet2.add(entry2);

        BarEntry entry4 = new BarEntry(1, 30); // x - place in array of dates y - values in array of values
        valueSet2.add(entry4);


        List<IBarDataSet> dataSets = new ArrayList<>();

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, " ");
        barDataSet1.setColor(Color.RED);
        barDataSet1.setDrawValues(true);
        dataSets.add(barDataSet1);

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, " ");
        barDataSet2.setColor(Color.BLUE);
        barDataSet2.setDrawValues(true);
        dataSets.add(barDataSet2);


        BarData data = new BarData(barDataSet1, barDataSet2);
        data.setBarWidth(0.4f);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.invalidate();
    }


    public void createGraphAgainVisual (String arr[]){
        BarChart mChart = (BarChart) findViewById(R.id.bar_chart);
        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        int j = 0;
        int k = 0;
        int o = 0;
////       TODO RUN THE GRAPH NEED TO MAKE DATA OF VALUE IN Y AXIS data ARR AND THE TIME IN X AXIS days
        if(arr.length!=0) {
            for (int i = 0; i < arr.length - 2; i = i + 2) {
                valuesFromDb[k] = datesArr[i];
                if (datesArr[i + 1] != null) {
                    long dv = Long.valueOf(datesArr[i + 1])*1000;// its need to be in milisecond
                    System.out.println("THE DV IS " + dv);
                    System.out.println("THE EPOCH STAMP " + datesArr[i + 1]);
                    if(dv>startDateInMilliseconds && dv<endDateInMilliseconds) {
                        //find the value
                        for(int t = 0 ; t < datesArr.length; t ++){
                            if(datesArr[t]!= null) {
                                if (dv == (Long.parseLong(datesArr[t])) * 1000) {
                                    System.out.println("The time is " + datesArr[t] + "the value of selling is " + datesArr[t - 1]);
                                    values[o] = Integer.parseInt(datesArr[t-1]);
                                    o++;
                                }
                            }
                        }
                        Date df = new java.util.Date(dv);
                        String vv = new SimpleDateFormat("MM-dd-yyyy hh:mma").format(df);
                        System.out.println("VV " + vv);
                        System.out.println("THE EPOCH " + datesArr[i + 1] + " AFTER CONVERSION DATE NEW" + vv);
                        days[j] = vv;
                        System.out.println("Days in date after conversion at J place + " + j + " <-j " + days[j]);
                        j++;
                    }
                }
                if (valuesFromDb[k] != null) {
                    System.out.println("VALUES FROM DB VALUES  in k place + " + k + " <-k " + valuesFromDb[k]);
                }
                k++;
            }
        }
//

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);

        //**add renderer**
//        BarChartCustomRenderer barChartCustomRenderer = new BarChartCustomRenderer(mChart, mChart.getAnimator(),   mChart.getViewPortHandler());
//        mChart.setRenderer(barChartCustomRenderer);


        XAxis xaxis = mChart.getXAxis();
        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(0.5f);
        xaxis.setGranularityEnabled(true);
        xaxis.setDrawLabels(true);
        xaxis.setDrawAxisLine(false);
        xaxis.setValueFormatter(new IndexAxisValueFormatter(days));

        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setEnabled(true);

        mChart.getAxisRight().setEnabled(false);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        ArrayList<String> ylabels = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
//            System.out.println(" THE datesArr it is value " +datesArr[i]  +  " and DATE in epoch " + datesArr[i+1]);
//            System.out.println(" THE valuesFromDb " + valuesFromDb[i]);
            System.out.println(" THE days " + days[i]);
//            System.out.println(" THE datesArrStrings FAKE " + datesArrStrings[i]);
           if(days[i]!=null) {
               System.out.println(" THE days IN IF  " + days[i]);
               System.out.println(" THE values IN IF  " + values[i]);
               BarEntry entry = new BarEntry(i, values[i]); // x - place in array of dates y - values in array of values
               valueSet1.add(entry);
               if(days[i+1]==null){
                   break;
               }

           }

        }

        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(valueSet1, " ");
        barDataSet.setColor(Color.CYAN);
        barDataSet.setDrawValues(true);
        dataSets.add(barDataSet);


        BarData data = new BarData(dataSets);
        data.setBarWidth(0.4f);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.invalidate();
    }

    private ArrayList<BarEntry> dataValues1(){
        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        valueSet1.add(new BarEntry(1,2));
        valueSet1.add(new BarEntry(2,4));
        valueSet1.add(new BarEntry(4,8));
        return valueSet1;
    }

}

