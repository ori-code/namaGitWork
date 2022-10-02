package com.example.namaprojectfirebase;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
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
    public static ArrayList deliveryMansList;
    LineChart mpLineChart;
    public static String[] datesArr = new String[500]; // array of dates for graph first date and second the value of sales
    public static String[] datesArrStrings = {"12/05/22","13/05/22"};
    public static String [] valueArrStrings = {"10","20"};
    public static String [] valuesFromDb = new String [500];
    public static String[] days = new String[500];
    public static int [] values = new int [500];
    public static long startDateInMilliseconds = 0,endDateInMilliseconds = 0;
    public static String [] valuesOfProductsInAllBuyingAndSale;
    public static int [] valuesOfProductsSales;
    public static String[] workArray = new String[1000];
//    public static String [] workArray1 = {"dlkdlkd", " dokodk"};
    public static String [] workArray1 = new String[100];
    public int workArrayCount = 0;
    public List<String> nameOfProducts;
    StringBuffer dates = new StringBuffer();
    public static List <String> checkArray = new ArrayList<>();
    ArrayList<String> purchasesForGraphs;
    public static int finishedGetDataAllShipments;
    DatabaseReference finalCheckPurchases;
    DatabaseReference refAllShipments;
    public static DatabaseReference refForGraphs, salesAndBuyingProducts,specificShipmentsUsers;
    public static DatabaseReference graphRef;
    public static ValueEventListener valueEventListenerSalesOfProduct;
    public static ValueEventListener valueEventListenerAllShipments;
    public static String[] nameOfProductsInAllBuyingAndSale;
    public static ValueEventListener valueEventListenerSalesProductGraph;
    //Sales of product // name or id // running on sales // counting name + quantity // capturing time
    public AutoCompleteTextView editText;
    public Calendar calendarStartTime, calendarEndTime;
    public static int revenueProductsArr [];
    public int flagDateSet = 0;
    public int flagFinishedRead = 0;
    public int salesFinished;
    public PieChart pieChart;
    public BarChart barChart;
    public LineChart lineChart;
    public TextView pieChartTotalRevenue;
    public static Query shipmentsQuery,specificShipments;
    public int graphSelected = 0;
    public static String shipmentsDates [];
    public static int shipmentsCount [];
//    private int mFillColorAll = Color.argb(150,51,181,229)
public static ImageButton showAllProducts ,cartActivity, showAllOrders, addProductActivity,allGraphs,overdueActivity,userListActivity,addUserActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs_layout);
        finalCheckPurchases = FirebaseDatabase.getInstance().getReference("orders");
        refAllShipments = FirebaseDatabase.getInstance().getReference("orders");
        refForGraphs = FirebaseDatabase.getInstance().getReference("orders");
        salesAndBuyingProducts = FirebaseDatabase.getInstance().getReference("products");
        specificShipmentsUsers = FirebaseDatabase.getInstance().getReference("users");

        shipmentsQuery = refAllShipments.orderByChild("timeOfPlacedOrder");


        specificShipments = specificShipmentsUsers.orderByKey();

        purchasesForGraphs = new ArrayList<String>();
        pieChart = findViewById(R.id.pie_chart);
        barChart = findViewById(R.id.bar_chart);
        lineChart = findViewById(R.id.line_chart);

        XAxis xAxis = lineChart.getXAxis();
        YAxis leftAxis = lineChart.getAxisLeft();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        lineChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("Date");
        description.setTextSize(15f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(workArray1));


        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setGridBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(true);
        


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
                    graphSelected = 0;
                    barChart.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                    lineChart.setVisibility(View.GONE);
                    runSalesProduct();
                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
                }
                if (item == "Sales and buying") {
                    graphSelected = 1;
                    barChart.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                    lineChart.setVisibility(View.GONE);
                    ////System.out.println("THE SALES BUYING");
                    ////System.out.println(Login.anArrayOfProducts[0]);
                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
                    runSalesAndBuyingProduct();
//                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
                }
                if (item == "Overall sales") {
                    graphSelected = 2;
                    ////System.out.println("THE SALES BUYING");
                    ////System.out.println(Login.anArrayOfProducts[0]);
//                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
//                    runSalesAndBuyingProduct();
//                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
                    barChart.setVisibility(View.GONE);
                    lineChart.setVisibility(View.GONE);
                    pieChart.setVisibility(View.VISIBLE);
                    setupPieChart();
                    getPieChartData();

                }
                if (item == "Overall shipments") {
                    graphSelected = 3;
                    ////System.out.println("THE SALES BUYING");
                    ////System.out.println(Login.anArrayOfProducts[0]);
//                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
//                    runSalesAndBuyingProduct();
//                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
                    barChart.setVisibility(View.GONE);
                    pieChart.setVisibility(View.GONE);
                    lineChart.setVisibility(View.VISIBLE);


                    runLineAllShipments();
//                    getLineChart();
                }
                if (item == "Specific shipments") {
                    graphSelected = 4;
                    ////System.out.println("THE SALES BUYING");
                    ////System.out.println(Login.anArrayOfProducts[0]);
//                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
//                    runSalesAndBuyingProduct();
//                    finalCheckPurchases.addListenerForSingleValueEvent(valueEventListenerSalesOfProduct);
                    barChart.setVisibility(View.GONE);
                    pieChart.setVisibility(View.GONE);
                    lineChart.setVisibility(View.VISIBLE);


                    runSpecificShipments();
//                    getLineChart();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });



        showAllProducts = findViewById(R.id.showAllProducts);
        cartActivity = findViewById(R.id.cartActivity);
        showAllOrders = findViewById(R.id.showAllOrders);
        addProductActivity = findViewById(R.id.addProductActivity);
        allGraphs = findViewById(R.id.allGraphs);
        overdueActivity = findViewById(R.id.overdueActivity);
        userListActivity = findViewById(R.id.userListActivity);
        addUserActivity = findViewById(R.id.addUserActivity);


        showAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphClass.this, MainActivity.class));
            }
        });
        cartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphClass.this, Cart.class));
            }
        });
        showAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphClass.this, Orders.class));
            }
        });
        addProductActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphClass.this, AddProduct.class));
            }
        });
        allGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphClass.this, GraphClass.class));
            }
        });
        overdueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphClass.this, ExpDateItems.class));
            }
        });
        addUserActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("TRY TO GO T REGISTER");
                startActivity(new Intent(GraphClass.this, Register.class));
            }
        });
        userListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphClass.this, UserRecycleViewClass.class));
            }
        });


    }

//CUSTOM X LINE FORMATTER

//    public class ClaimsXAxisValueFormatter extends ValueFormatter {
//
//        List<String> datesList;
//
//        public ClaimsXAxisValueFormatter(List<String> arrayOfDates) {
//            this.datesList = arrayOfDates;
//        }
//
//
//        @Override
//        public String getAxisLabel(float value, AxisBase axis) {
///*
//Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
//*/
//            Integer position = Math.round(value);
//            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
//
////            if (value > 1 && value < 2) {
////                position = 0;
////            } else if (value > 2 && value < 3) {
////                position = 1;
////            } else if (value > 3 && value < 4) {
////                position = 2;
////            } else if (value > 4 && value <= 5) {
////                position = 3;
////            }
////            if (position < datesList.size())
////                for(int i = 0; i < checkArray.size(); i++){
////                    return checkArray.get(i);
////                }
//
//            return "123";
//        }
//    }





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
        //////System.out.println("the send is clicked");
        String textFromAutoComplete = editText.getText().toString();
        ////System.out.println("THE AUTOCOMPLETE " + textFromAutoComplete);
            showDatePickerDialogStart();




        if (dates != null) {
            dates.delete(0, dates.length());
        }

    }

    public void endData(View view) {
        //////System.out.println("the send is clicked");
        String textFromAutoComplete = editText.getText().toString();
        ////System.out.println("THE AUTOCOMPLETE " + textFromAutoComplete);
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
            ////System.out.println("LONG START  " + startDateInMilliseconds);
            dates.append(calendarStartTime.getTimeInMillis());
            dates.append("|");
            flagDateSet = 1;
            ////System.out.println("START TIME " + calendarStartTime.getTimeInMillis());
        }
        else{
            calendarEndTime = calendarStartTime;

            endDateInMilliseconds = calendarEndTime.getTimeInMillis();
            ////System.out.println("LONG END  " + endDateInMilliseconds);
            dates.append(calendarEndTime.getTimeInMillis());
            dates.append("|");
            ////System.out.println("END TIME " + calendarEndTime.getTimeInMillis());
        }
        ////System.out.println("DATES : START | END " + dates);
    }

    //BUTTON SENDING
    public void showGraph(View view) {
        String textFromAutoComplete = editText.getText().toString();
        ////System.out.println("THE AUTOCOMPLETE " + textFromAutoComplete + " DATES " + dates);
        if(graphSelected == 0){
            createSalesProductGraph(textFromAutoComplete, dates);
        }
        if(graphSelected == 1) {
            System.out.println("NOTHING IN DATES");
        }
        if(graphSelected == 2) {
            if(dates!=null){
                System.out.println("I GET DATESSS");
                getPieChartData();
            }
        }
        if(graphSelected == 3) {
            if(dates!=null){
                System.out.println("OVERALL shipments");
                runLineAllShipments();
            }
            else{
                runLineAllShipments();
            }
        }
        if(graphSelected == 4) {
            if(dates!=null){
                System.out.println("Specific shipments");
//                runSpecificShipments();
            }
            else{
                System.out.println("Specific shipments");
//                runSpecificShipments();
            }
        }
    }

    //GETTING SELLING VALUES AND DATES
    public void createSalesProductGraph (String name, StringBuffer dates) {
        ////System.out.println(name + " THE DATES IS " + dates);
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
                        ////System.out.println("QNTY PLEASE FROM DB" + snapshot.child(name).getValue());
                        ////System.out.println("DATE PLEASE " + snapshot.child("timeOfPlacedOrder").getValue());
                        for(int j = 0; j < datesArr.length; j++){
                            if(datesArr[j]!=null){
                                ////System.out.println("THE AQTUALY => " + datesArr[j]);
                            }
                        }


                        //time and quantity
                    }
                    ////System.out.println("WORK PLEASE " + snapshot.child(name).getValue());

                }
                flagFinishedRead = 1;
                ////System.out.println(datesArr[0] + " <- 0 " + datesArr[1] + " <- 1 "+ datesArr[2]+  " <-2 " + datesArr[3]+  " <-3 " );
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
        ////System.out.println("HEY IM GETTING THE PRODUCTS FOR THE LIST");
        valueEventListenerSalesOfProduct = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nameOfProducts = new ArrayList<String>();
                    ////System.out.println("PRODUCTS " + nameOfProducts);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        ////System.out.println("PURCHASES DATA : " + snapshot);
                        String runningString = snapshot.getValue().toString();
                        String search = new String();
                        String sentence = runningString;
                        for (int i = 0; i < Login.anArrayOfProducts.length - 1; i++) {
                            if (Login.anArrayOfProducts[i] != null) {
                                ////System.out.println("PRODUCT NAME RUNNING "+ Login.anArrayOfProducts[i]);
                                if (Login.anArrayOfProducts[i].charAt(0) <'0' ||  Login.anArrayOfProducts[i].charAt(0)>'9') {
                                    search = Login.anArrayOfProducts[i];
                                    ////System.out.println("SHUGAR " + search);
                                    if (sentence.toLowerCase().indexOf(search.toLowerCase()) != -1) {
                                        if (nameOfProducts.toString().toLowerCase().indexOf(search.toLowerCase()) == -1) {
                                                 nameOfProducts.add(search);
                                             ////System.out.println("THE LIST IS HHH" + nameOfProducts);
                                        } else {
                                              ////System.out.println("NOTHING");
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

    }

    private void runSalesAndBuyingProduct() {
        int count = 0;
        int place = 0 ;
        int checkPlace = 0;
        ////System.out.println("HEY IM THE SALES BUYING");
        for(int i = 0; i < Login.anArrayOfProducts.length; i=i+2){
            ////System.out.println("PRODUCTS  runSalesAndBuyingProduct " + Login.anArrayOfProducts[i]);
            if(Login.anArrayOfProducts[i]!=null){
//                System.out.println("THE PRODUCTS  iSSSS"  + Login.anArrayOfProducts[i]);
                count ++;
            }
        }


       nameOfProductsInAllBuyingAndSale = new String[count];

        for (int i = 0; i < Login.anArrayOfProducts.length; i = i + 2) {
            ////System.out.println("PRODUCTS  runSalesAndBuyingProduct " + Login.anArrayOfProducts[i]);
            if (Login.anArrayOfProducts[i] != null) {
                nameOfProductsInAllBuyingAndSale[place] = Login.anArrayOfProducts[i];
                System.out.println("THE PRODUCT ISssss " + nameOfProductsInAllBuyingAndSale[place]);
                place++;
            }
        }



        valuesOfProductsInAllBuyingAndSale = new String[count];

//        for (int f = 0; f < nameOfProductsInAllBuyingAndSale.length; f++) {
            //products ref counting all added FROM DB
            salesAndBuyingProducts.addListenerForSingleValueEvent(new ValueEventListener() {
                public int placceForValues = 0;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //TODO need to count from the string all values and
                        ////System.out.println("RUNNING IN PRODUCTSsss " + " THE NAME IS " + snapshot.child("nameOfProduct").getValue() + " DATA OF ADDING " + snapshot.child("dataOfAdding").getValue());
                        String arrrayOfAdding = new String();
                        arrrayOfAdding = snapshot.child("dataOfAdding").getValue().toString();
                        String[] words = arrrayOfAdding.split("\\s+");
                        long sumOfValues = 0;
                        //convert string to words of each product
                        for (int i = 0; i < words.length; i++) {
                            // You may want to check for a non-word character before blindly
                            // performing a replacement
                            // It may also be necessary to adjust the character class
                            words[i] = words[i].replaceAll("[^\\w]", "");
                            try {
                                if (!words[i].equals(null)) {
                                    ////System.out.println("THE WORDS IS " + words[i]);
                                    if (Long.parseLong(words[i]) < 160000000000L) {
                                        ////System.out.println("THE WORDS IS AFTER IF" + words[i]);
                                        sumOfValues = Long.parseLong(words[i]) + sumOfValues;
                                        ////System.out.println("THE SUM IS AFTER IF" + sumOfValues);

                                    }
//                                    valuesOfProductsInAllBuyingAndSale[0] = String.valueOf(sumOfValues);

                                }
                            } catch (Exception e) {
                                ////System.out.println("THE VALUE OF E " + e);
                            }
                        }
                        valuesOfProductsInAllBuyingAndSale[placceForValues] = String.valueOf(sumOfValues);
                        System.out.println("THE SUM IN STRING " + valuesOfProductsInAllBuyingAndSale[placceForValues]);
                        System.out.println("PLACE OF VALUES IS " + placceForValues + " NAME : " + nameOfProductsInAllBuyingAndSale[placceForValues] + " GOING TO NEXT PRODUCT ON SNAPSHOOT RUN and VALUES is " + valuesOfProductsInAllBuyingAndSale[placceForValues]);
                        placceForValues++;

                        if(placceForValues == nameOfProductsInAllBuyingAndSale.length){
                            System.out.println("I FINISHED RUNNN ");
                            createSalesAndBuyingGraph();

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }

            });

            refForGraphs.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    salesFinished = 0;
                    valuesOfProductsSales = new int [nameOfProductsInAllBuyingAndSale.length];
                    for (int j = 0; j < nameOfProductsInAllBuyingAndSale.length; j ++) {
                        int sum = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            for (int j = 0; j < nameOfProductsInAllBuyingAndSale.length; j++) {
                                System.out.println("NAME IS " + nameOfProductsInAllBuyingAndSale[j] + " VALUE FROM DB " + snapshot.child(nameOfProductsInAllBuyingAndSale[j]).getValue());
                                if(snapshot.child(nameOfProductsInAllBuyingAndSale[j]).getValue()!=null) {
                                    sum = Integer.parseInt((String) snapshot.child(nameOfProductsInAllBuyingAndSale[j]).getValue()) + sum;
                                    System.out.println("SALES SUM " + sum);
                                 }
                        }
                        valuesOfProductsSales[j] = sum;
                    }
                    salesFinished = 1;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }

            });
          if(salesFinished==1 ){
              System.out.println("IT IS DONE ");
          }
          else{
              System.out.println("NOT FINISHED YET");
          }

        }
    //timefilter
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
                    ////System.out.println("THE DV IS " + dv);
                    ////System.out.println("THE EPOCH STAMP " + datesArr[i + 1]);
                    if(dv>startDateInMilliseconds && dv<endDateInMilliseconds) {
                        //find the value
                        for(int t = 0 ; t < datesArr.length; t ++){
                            if(datesArr[t]!= null) {
                                if (dv == (Long.parseLong(datesArr[t])) * 1000) {
                                    ////System.out.println("The time is " + datesArr[t] + "the value of selling is " + datesArr[t - 1]);
                                    values[o] = Integer.parseInt(datesArr[t-1]);
                                    o++;
                                }
                            }
                        }
                        Date df = new java.util.Date(dv);
                        String vv = new SimpleDateFormat("MM-dd-yyyy hh:mma").format(df);
                        ////System.out.println("VV " + vv);
                        ////System.out.println("THE EPOCH " + datesArr[i + 1] + " AFTER CONVERSION DATE NEW" + vv);
                        days[j] = vv;
                        ////System.out.println("Days in date after conversion at J place + " + j + " <-j " + days[j]);
                        j++;
                    }
                }
                if (valuesFromDb[k] != null) {
                    ////System.out.println("VALUES FROM DB VALUES  in k place + " + k + " <-k " + valuesFromDb[k]);
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
//            ////System.out.println(" THE datesArr it is value " +datesArr[i]  +  " and DATE in epoch " + datesArr[i+1]);
//            ////System.out.println(" THE valuesFromDb " + valuesFromDb[i]);
            ////System.out.println(" THE days " + days[i]);
//            ////System.out.println(" THE datesArrStrings FAKE " + datesArrStrings[i]);
           if(days[i]!=null) {
               ////System.out.println(" THE days IN IF  " + days[i]);
               ////System.out.println(" THE values IN IF  " + values[i]);
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

    public void createSalesAndBuyingGraph(){

System.out.println("CREATE GRAPH OF SALES AND BUYING!!!!" + nameOfProductsInAllBuyingAndSale[0] + " all ADDS "  + valuesOfProductsSales[0] + " VALUES " + valuesOfProductsInAllBuyingAndSale[0]);
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

        //adding values for the graphs for all products that run with names nameOfProductsInAllBuyingAndSale




//runSalesAndBuyingProduct

        for(int u = 0; u < nameOfProductsInAllBuyingAndSale.length; u++ ){
            ////System.out.println("TRY TO RUN RED GRAPH NAME IS " + "ON U " + u + "AND NAME IS " +   nameOfProductsInAllBuyingAndSale[u] +  " THE VALUE IN THIS PLACE " + valuesOfProductsInAllBuyingAndSale[u]);
            //            red all added products
            BarEntry entry1 = new BarEntry(u, Integer.parseInt(valuesOfProductsInAllBuyingAndSale[u])); // x - place in array of dates y - values in array of values
            valueSet1.add(entry1);

            //blue all sold products
            BarEntry entry2 = new BarEntry(u, valuesOfProductsSales[u]); // x - place in array of dates y - values in array of values
            valueSet2.add(entry2);
        }

        List<IBarDataSet> dataSets = new ArrayList<>();

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, " ");
        barDataSet1.setColor(Color.RED);
        barDataSet1.setDrawValues(true);
        dataSets.add(barDataSet1);

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, " ");
        barDataSet2.setColor(Color.CYAN);
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

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Sales by products");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void getPieChartData(){
        int count = 0;
        int place = 0 ;
        int checkPlace = 0;
        ////System.out.println("HEY IM THE SALES BUYING");
        for(int i = 0; i < Login.anArrayOfProducts.length; i=i+2){
            ////System.out.println("PRODUCTS  runSalesAndBuyingProduct " + Login.anArrayOfProducts[i]);
            if(Login.anArrayOfProducts[i]!=null){
//                System.out.println("THE PRODUCTS  iSSSS"  + Login.anArrayOfProducts[i]);
                count ++;
            }
        }


        nameOfProductsInAllBuyingAndSale = new String[count];
        for (int i = 0; i < Login.anArrayOfProducts.length; i = i + 2) {
            ////System.out.println("PRODUCTS  runSalesAndBuyingProduct " + Login.anArrayOfProducts[i]);
            if (Login.anArrayOfProducts[i] != null) {
                nameOfProductsInAllBuyingAndSale[place] = Login.anArrayOfProducts[i];
                System.out.println("THE PRODUCT ISssss " + nameOfProductsInAllBuyingAndSale[place]);
                place++;
            }
        }




        valuesOfProductsInAllBuyingAndSale = new String[count];
//        revenueProductsArr = new int [23];
        revenueProductsArr = new int [nameOfProductsInAllBuyingAndSale.length];
//        for (int f = 0; f < nameOfProductsInAllBuyingAndSale.length; f++) {
        //products ref counting all added FROM DB
        salesAndBuyingProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            public int placceForValues = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //TODO need to count from the string all values and
                    ////System.out.println("RUNNING IN PRODUCTSsss " + " THE NAME IS " + snapshot.child("nameOfProduct").getValue() + " DATA OF ADDING " + snapshot.child("dataOfAdding").getValue());
                    String arrrayOfAdding = new String();
                    String priceOfBuying = new String();
                    String priceOfSale = new String();
                    int revenueResult = 0;
                    arrrayOfAdding = snapshot.child("dataOfAdding").getValue().toString();
                    priceOfBuying = snapshot.child("buyPrice").getValue().toString();
                    priceOfSale = snapshot.child("sellPrice").getValue().toString();

                    revenueResult = Integer.parseInt(priceOfSale) - Integer.parseInt(priceOfBuying);
                    revenueProductsArr[placceForValues] = revenueResult;
                    System.out.println("REVENUE " + revenueProductsArr[placceForValues]);
                    String[] words = arrrayOfAdding.split("\\s+");
                    long sumOfValues = 0;
                    //convert string to words of each product
                    for (int i = 0; i < words.length; i++) {
                        // You may want to check for a non-word character before blindly
                        // performing a replacement
                        // It may also be necessary to adjust the character class
                        words[i] = words[i].replaceAll("[^\\w]", "");
                        try {
                            if (!words[i].equals(null)) {
                                ////System.out.println("THE WORDS IS " + words[i]);
                                if (Long.parseLong(words[i]) < 160000000000L) {
                                    ////System.out.println("THE WORDS IS AFTER IF" + words[i]);
                                    sumOfValues = Long.parseLong(words[i]) + sumOfValues;
                                    ////System.out.println("THE SUM IS AFTER IF" + sumOfValues);

                                }
//                                    valuesOfProductsInAllBuyingAndSale[0] = String.valueOf(sumOfValues);

                            }
                        } catch (Exception e) {
                            ////System.out.println("THE VALUE OF E " + e);
                        }
                    }
                    valuesOfProductsInAllBuyingAndSale[placceForValues] = String.valueOf(sumOfValues);
                    System.out.println("THE SUM IN STRING " + valuesOfProductsInAllBuyingAndSale[placceForValues]);
                    System.out.println("PLACE OF VALUES IS " + placceForValues + " NAME : " + nameOfProductsInAllBuyingAndSale[placceForValues] + " GOING TO NEXT PRODUCT ON SNAPSHOOT RUN and VALUES is " + valuesOfProductsInAllBuyingAndSale[placceForValues]);
                    placceForValues++;

                    if(placceForValues == nameOfProductsInAllBuyingAndSale.length){
                        System.out.println("I FINISHED RUNNN ");
//                        createSalesAndBuyingGraph();

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }

        });

        refForGraphs.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                salesFinished = 0;
                valuesOfProductsSales = new int [nameOfProductsInAllBuyingAndSale.length];
                long placedOrder = 0;
                for (int j = 0; j < nameOfProductsInAllBuyingAndSale.length; j ++) {
                    int sum = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            for (int j = 0; j < nameOfProductsInAllBuyingAndSale.length; j++) {

                        if(snapshot.child("timeOfPlacedOrder").getValue()!=null) {
                            System.out.println(snapshot.child("timeOfPlacedOrder").getValue().toString()  + "JJJ");
                            placedOrder = Long.valueOf(snapshot.child("timeOfPlacedOrder").getValue().toString());
                        }
                            if (placedOrder * 1000 > startDateInMilliseconds && placedOrder * 1000 < endDateInMilliseconds) {
                                System.out.println("NAME IS PIE " + nameOfProductsInAllBuyingAndSale[j] + " VALUE FROM DB " + snapshot.child(nameOfProductsInAllBuyingAndSale[j]).getValue());
                                if (snapshot.child(nameOfProductsInAllBuyingAndSale[j]).getValue() != null)
                                {
                                    sum = Integer.parseInt((String) snapshot.child(nameOfProductsInAllBuyingAndSale[j]).getValue()) + sum;
                                    System.out.println("SALES SUM " + sum);
                                }

                            }
                        }
//
                    valuesOfProductsSales[j] = sum;
                }
                salesFinished = 1;
                loadPieChartData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }

        });


        if(salesFinished==1 ){
            System.out.println("IT IS DONE ");
        }
        else{
            System.out.println("NOT FINISHED YET");
        }


    }



    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        int summaryRev = 0;

//        nameOfProductsInAllBuyingAndSale // <= names
//        valuesOfProductsSales // <= allsalescounting
//        revenueProductsArr // <= revenue

        pieChartTotalRevenue = findViewById(R.id.pie_chart_total_revenue);

        for(int i = 0; i < nameOfProductsInAllBuyingAndSale.length; i++){
            System.out.println("THE TOTAl" + revenueProductsArr[i]  + " HEY " + Integer.parseInt(String.valueOf(valuesOfProductsSales[i])));
            int total = revenueProductsArr[i]*Integer.parseInt(String.valueOf(valuesOfProductsSales[i]));
            System.out.println("THE TOTAl" + total);
            entries.add(new PieEntry(total, nameOfProductsInAllBuyingAndSale[i] + " | " + valuesOfProductsSales[i] + " | " + total));
            summaryRev = total + summaryRev;
        }
        pieChartTotalRevenue.setText("THE TOTAL REVENUE IS :" + summaryRev);


        //percentage in value
//        entries.add(new PieEntry(20, "Food & Dining 2344"));
//        entries.add(new PieEntry(2, "Medical"));
//        entries.add(new PieEntry(3, "Entertainment"));
//        entries.add(new PieEntry(4, "Electricity and Gas"));
//        entries.add(new PieEntry(5, "Housing"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void setupLineChart(){
        LineDataSet lineDataSet1 = new LineDataSet(dataValues(), "Shipments per date");
        System.out.println("THE CHART NEED TO BE PRINTED");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }


    private void runLineAllShipments() {
        finishedGetDataAllShipments = 0;
        shipmentsCount  = new int [100];
        shipmentsDates = new String[100];
        ////System.out.println("HEY IM GETTING THE PRODUCTS FOR THE LIST");
        shipmentsQuery.addValueEventListener(new ValueEventListener() {
            int index = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                shipmentsDates[0]="start";
                for (DataSnapshot shipmentSnapshot : dataSnapshot.getChildren()) {
                    try {
                    System.out.println("HHH" + shipmentSnapshot.child("timeOfPlacedOrder").getValue().toString());
                    if(index==0){
                        System.out.println("The first time");
                        System.out.println("THE DATA IS " + shipmentSnapshot.child("timeOfPlacedOrder").getValue().toString());
                        shipmentsDates[index] = (shipmentSnapshot.child("timeOfPlacedOrder").getValue().toString());
                        shipmentsCount[index] = 1;
                        System.out.println("DATE " +  shipmentsDates[index] + " COUNT " +  shipmentsCount[index]);
                        index++;
                    }
                    else {
                        if (shipmentsDates[index - 1].equals(shipmentSnapshot.child("timeOfPlacedOrder").getValue().toString()) ) {
                            System.out.println("The same time ");
                            shipmentsCount[index - 1] = 1 + shipmentsCount[index - 1];
                            System.out.println("DATE " + shipmentsDates[index - 1] + " COUNT " + shipmentsCount[index - 1]);
                        } else {
                            System.out.println("The new time");
                            shipmentsDates[index] = (shipmentSnapshot.child("timeOfPlacedOrder").getValue().toString());
                            shipmentsCount[index] = 1;
                            System.out.println("DATE " + shipmentsDates[index] + " COUNT " + shipmentsCount[index]);
                            index++;
                        }
                    }
                    }
                    catch (Exception e){
                        System.out.println("tttt" + e);
                    }
                }


                for(int j = 0; j<shipmentsDates.length; j++){
                    if(shipmentsDates[j]!=null) {
                        System.out.println("DATE ARR " + shipmentsDates[j] + " COUNT " + shipmentsCount[j]);
                        String epochString = shipmentsDates[j];
                        long epoch = Long.parseLong(epochString);
                        Date expiry = new Date(epoch * 1000);
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM", Locale.US);
                        //dd-MM-yyyy
                        String time = df.format(expiry);
                        workArray1[j] = time;
                    }
                    else{
                        finishedGetDataAllShipments = 1;
                    }

                }

                System.out.println("run setup line chart");
                setupLineChart();


//                System.out.println("BEFORE FINISHED finishedGetDataAllShipments");
//                if(finishedGetDataAllShipments == 1) {
//                    System.out.println("INSIDE FINISHED finishedGetDataAllShipments");
//                    for (int j = 0; j < shipmentsDates.length; j++) {
//                        if (shipmentsDates[j] != null) {
//                            System.out.println("DATE ARR1 " + shipmentsDates[j] + " COUNT " + shipmentsCount[j]);
//                        }
////                        setupLineChart();
//                    }
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }




    private ArrayList<Entry> dataValues()
    {
        int count = 0;
        ArrayList<Entry> dataVals = new ArrayList<Entry>();


//        for(int j = 0; j<shipmentsDates.length; j++){
//            try {
//                if (!shipmentsDates[j].equals(null)) {
//                    count++;
//                    System.out.println("COUNT IS SSS " + count + " AND DATES ARR " + shipmentsDates[j] + " AND J IS " + j);
//                }
//            }catch(Exception e) {
////            System.out.println("THE PROBLEM CATCHED"  + e);
//            }
//        }
//
//        workArray1 = new String [count];
//
//        for(int j = 0; j<shipmentsDates.length; j++){
//            try {
//                if (!shipmentsDates[j].equals(null)) {
//                    workArray1[j] = shipmentsDates[j];
//                    System.out.println("COUNT IS SSS " + count + " AND DATES ARR " + shipmentsDates[j] + " AND J IS " + j);
//                }
//            }catch(Exception e) {
////            System.out.println("THE PROBLEM CATCHED"  + e);
//            }
//        }



//        for(int a = 0; a < workArray1.length; a ++ ) {
//            System.out.println("INISDE DATA VALUES " + shipmentsCount[a] + "THE DATES IS " + shipmentsDates[a]);
//            if(workArray1[a]!=null){
//                System.out.println("THE DATE IN WORK ARRAY" + workArray1[a]);
//                dataVals.add(new Entry(a, shipmentsCount[a]));
//            }
//        }
//@
//        dataVals.add(new Entry(workArray1.length,shipmentsCount[count]));

        for(int g = 0; g < workArray1.length; g++){
         if (workArray1[g] != null) {
             System.out.println(" HYE " + g + " || " + workArray1[g] + " || " + shipmentsCount[g]);
             dataVals.add(new Entry(g, shipmentsCount[g]));
         }
     }

//     int last = workArray1.length+1;
//        dataVals.add(new Entry(workArray1.length,4f));
//        dataVals.add(new Entry(1,2f));
//        dataVals.add(new Entry(2,2f));
        return dataVals;
    }


//Specific shipments
public void runSpecificShipments()
{
    {
        deliveryMansList = new ArrayList();
        System.out.println("SPECIFIC SHIPMENTS");
        specificShipments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot shipmentSnapshot : dataSnapshot.getChildren()) {
                    User deliveryMan = shipmentSnapshot.getValue(User.class);
                    if(deliveryMan.getPermission() == 3){
                        System.out.println("HE IS DELIVERY MAN " + deliveryMan.getEmail());
                        deliveryMansList.add(deliveryMan.getEmail());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
}




}



