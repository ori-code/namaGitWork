package com.example.namaprojectfirebase;

import static com.example.namaprojectfirebase.Login.anArrayOfProducts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FinalCheck extends Activity implements AdapterView.OnItemSelectedListener {
//    public static ArrayList <String> finalCheckList;
    DatabaseReference finalCheckSnapShot,finalCheckAllProducts;
    TextView nameOfProductInTotal;
    public String idOfCart;
    public static TableLayout table;
    public static TextView orderFinalDetails,sumTotalShipping;
    public static int orderNumber;
    public static int shipmentTypeUpdate;
    public static int sumForOrder = 0,shipmentType = 0, shipmentFee=0;
    public Button shipmentTypeUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_order);
        Intent intent = getIntent();
        int orderNumber = 0,shipmentType = 0;
        orderFinalDetails = findViewById(R.id.orderFinalDetails);
        shipmentTypeUpdateBtn = findViewById(R.id.shipmentTypeUpdateBtn);


        idOfCart = intent.getStringExtra("orderForThisCardInList");
        orderNumber = intent.getIntExtra("orderNumber", orderNumber);
        shipmentType = intent.getIntExtra("shipmentType",shipmentType);

        //System.out.println( "WHY hhh" + shipmentType);
        if(shipmentType == 1){
            shipmentFee =100;
        }
        if(shipmentType == 2){
            shipmentFee =200;
        }
        if(shipmentType == 3){
            shipmentFee =0;
        }
        //System.out.println("FEE" + shipmentFee);
        int tmpInt = orderNumber;
        String tmpStr10 = String.valueOf(tmpInt);

        orderFinalDetails.setText("SUMMARY DETAILS\n ORDER NUMBER : "  + tmpStr10);

        finalCheckSnapShot = FirebaseDatabase.getInstance().getReference("orders").child(idOfCart);
        finalCheckSnapShot.addListenerForSingleValueEvent(valueEventListener);

        table = (TableLayout) findViewById(R.id.myTableLayout);
        sumTotalShipping = (TextView) findViewById(R.id.sumTotalForThisOrder);

//        nameOfProductInTotal = findViewById(R.id.nameOfProductInTotal);



        //spinner example
        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.statusOfOrder, android.R.layout.simple_spinner_item );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(this);


    }

    ArrayList<String> finalCheckList = new ArrayList<String>();
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            finalCheckList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //System.out.println("FINAL CHECK "  + snapshot.getKey());
                    try {
                        for(int i = 0; i < anArrayOfProducts.length; i++ ) {
                           //System.out.println("PRODUCTS FROM LIST :" + anArrayOfProducts[i].toString());
                            if(anArrayOfProducts[i].toString().equals(snapshot.getKey())){
                                //System.out.println("FOUNDED!!!! " + anArrayOfProducts[i].toString());
                                    //System.out.println("HEYY " + anArrayOfProducts[i].toString() + " : " + snapshot.getValue().toString());
                                    finalCheckList.add(anArrayOfProducts[i].toString());
                                    finalCheckList.add(snapshot.getValue().toString());
                                    finalCheckList.add(anArrayOfProducts[i+1]);
                                    i++;
                                    //System.out.println("LIST STRING" + finalCheckList);
                            }
                        }
                    }
                    catch(Exception e) {
                        //System.out.println(e);
                    }


//                    nameOfProductInTotal.setText("HEYY");

                }






                }

            for(int i=0;i<finalCheckList.size()-2;i++)
            {
                TableRow row=new TableRow(FinalCheck.this);
                String debt = "\n\n" + "   "+ finalCheckList.get(i) + "\n"+"\n"+"   "+ finalCheckList.get(i+1) +" x " + finalCheckList.get(i+2);
                TextView tvDebt=new TextView(FinalCheck.this);
                tvDebt.setTextSize(14);
                tvDebt.setTextColor(Color.BLACK);
                tvDebt.setText(""+debt);
                row.addView(tvDebt);
                table.addView(row);
                int qnty, price;
                qnty = Integer.parseInt(finalCheckList.get(i+1));
                price = Integer.parseInt(finalCheckList.get(i+2));

                sumForOrder = (sumForOrder + (qnty * price)) + shipmentFee ;


                sumTotalShipping.setText("SHIPPING : "+ shipmentFee + "\nTOTAL IS : " + String.valueOf(sumForOrder));

                //System.out.println("SUM IS IN THIS ORDER " +sumForOrder );
                i++;
                i++;

            }


            ViewCompat.setNestedScrollingEnabled(table, true);
        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }


    };



    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Received")) {
            shipmentTypeUpdate=1;

        }
        if (text.equals("Packed and ready for shipment")) {
            shipmentTypeUpdate=2;
        }
        if (text.equals("Shipped")) {
            shipmentTypeUpdate=3;
        }
//        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

    }


    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void updateStatusOfOrder(View view) {
        //System.out.println("SHIPMENT UPDATE" + shipmentTypeUpdate);
        String forUpdateShipmentType = String.valueOf(shipmentTypeUpdate);
        finalCheckSnapShot.child("status").setValue(forUpdateShipmentType);
        finalCheckSnapShot.child("theShipper").setValue(Login.mAuth.getCurrentUser().getEmail());
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        finalCheckSnapShot.child("timeOfPlacedOrder").setValue(ts);
    }
}