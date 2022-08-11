package com.example.namaprojectfirebase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.namaprojectfirebase.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Order extends Activity {
    public static DatabaseReference orderDbSnap,allProducts,ordersReference;
    public int count = 0,flagRunningCart =0,valueUpdated=0;
    public EditText editClientName, editAddress, editPhone, editComments ;
    public Button addPurchases;
    public static int deliveryType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        Button getOrder = (Button) findViewById(R.id.markOrderRecieved);
        System.out.println("IM IN ORDER CARD ");
        Query orderQueryCopyAllToOrders, updateQuantityFromGlobal,ordersQuery;

        orderDbSnap = FirebaseDatabase.getInstance().getReference("carts").child(HomeFragment.uniqueOfCartID);
        orderQueryCopyAllToOrders = orderDbSnap.orderByKey();


        allProducts = FirebaseDatabase.getInstance().getReference("products");
        updateQuantityFromGlobal = allProducts.orderByKey();


        //ORDERS QUERY
        ordersReference = FirebaseDatabase.getInstance().getReference("orders");
        ordersQuery = ordersReference.orderByKey();




        editClientName = (EditText) findViewById(R.id.nameOfTheClient);
        editAddress = (EditText) findViewById(R.id.editAddressClient);
        editPhone = (EditText) findViewById(R.id.phoneOfClient);
        editComments = (EditText) findViewById(R.id.editComments);






        List productsInCartNameQuantity = new ArrayList();







        getOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               String editCName = editClientName.getText().toString().trim();
                String editCAddress = editAddress.getText().toString().trim();
                String editCPhone =  editPhone.getText().toString().trim();
               String editCComments =   editComments.getText().toString().trim();



                if (editCName.isEmpty()) {
                    editClientName.setError("Full name is required!");
                    editClientName.requestFocus();
                    return;
                }
                if (editCAddress.isEmpty()) {
                    editAddress.setError("Address is required!");
                    editAddress.requestFocus();
                    return;
                }

                if (editCPhone.isEmpty()) {
                    editPhone.setError("Phone is required!");
                    editPhone.requestFocus();
                    return;
                }

                if (editCComments.isEmpty()) {
                    editComments.setError("You dont wnat to comment something ??! No problem ... ");
                    editComments.setText("No comments");
                    return;
                }





                count = 0;
                productsInCartNameQuantity.clear();
                flagRunningCart = 0;
                orderQueryCopyAllToOrders.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        System.out.println("CLICK BUTTON START RUNNING FUNC BEFORE FOR");
                    if(flagRunningCart==0){
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //LIST FROM CART
                            productsInCartNameQuantity.add(postSnapshot.getKey());
                            productsInCartNameQuantity.add(String.valueOf(postSnapshot.child("quantity").getValue()));
//                            System.out.println("The values in list" + productsInCartNameQuantity);
//                            System.out.println("COUNT IS" + count);



                            count++;
//                            System.out.println("COUNT IS : "+ count);
                        }
                        flagRunningCart = 1;



                        //ADDING TO ORDER
                        Map<String, Object> dataOfCart = new HashMap<>();
                        dataOfCart.put("orderer", Login.mAuth.getCurrentUser().getEmail());
                        for(int z = 0; z < productsInCartNameQuantity.size()-2; z=z+2 ){
                            System.out.println("THE PRODUCT IN CART" + productsInCartNameQuantity.get(z));
                            if(productsInCartNameQuantity.get(z).equals("currentUserEmail")){
                                System.out.println("IM IN BREAKKK!!!");
                                break;
                            }
//                            if(!productsInCartNameQuantity.get(z).getClass().equals(Number.class)){
                                System.out.println("CLASS IS" + productsInCartNameQuantity.get(z).getClass());
                                dataOfCart.put(productsInCartNameQuantity.get(z).toString(), productsInCartNameQuantity.get(z+1));
//                            }
                        }

                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();


                        dataOfCart.put("status", "1");
                        dataOfCart.put("Time of Placed Order" , ts);
                        dataOfCart.put("clientName",editCName);
                        dataOfCart.put("clientAddress",editCAddress);
                        dataOfCart.put("clientPhone",editCPhone);
                        dataOfCart.put("clientCommetns",editCComments);
                        dataOfCart.put("deliveryType",  deliveryType);
                        FirebaseDatabase.getInstance()
                                .getReference("orders")
                                .child(HomeFragment.uniqueOfCartID)
                                .setValue(dataOfCart)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                        } else {

                                        }

                                    }


                                });
                    }
                    else{
//                        System.out.println("the flagRunningCart is : " +flagRunningCart);
                    }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

//                if(count > 2){
                    Cart.dbProducts.child("orderPlaced").setValue(1);

//                }
//                else
//                {
//                    AlertDialog alertDialog = new AlertDialog.Builder(Order.this).create();
//                    alertDialog.setTitle("Attention");
//                    alertDialog.setMessage("You need to add some products");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK, I WILL DO IT",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();
//                }

//COMMENT START NEEDED FUNC OF RUNNING OVERALL PRODUCTS
//RUNNING ON ALL PRODUCTS
                valueUpdated = 0;
                updateQuantityFromGlobal.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(valueUpdated==0){
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

//                            System.out.println("Name from snap allProducts " + postSnapshot.child("nameOfProduct").getValue());
                            for(int i = 0; i < productsInCartNameQuantity.size(); i++){
//                               System.out.println("Data in list on i place :" +i +" "+productsInCartNameQuantity.get(i).toString());
                               if (postSnapshot.child("nameOfProduct").getValue().equals(productsInCartNameQuantity.get(i).toString())) {
//                                        System.out.println("the names the same in if");
                                        int overallQnty = Integer.parseInt(postSnapshot.child("quantity").getValue().toString());
                                        int productQnty = Integer.parseInt(productsInCartNameQuantity.get(i+1).toString());
//                                            System.out.println("OVERAL QNTY FROM DATABASE INT CASTED " + overallQnty);
//                                            System.out.println("QNTY OF PRODUCT FROM LIST INT CASTED " + productQnty);
//                                            System.out.println("NAME IN LIST ON THIS I PLACE IN IF " + productsInCartNameQuantity.get(i).toString());
//                                            System.out.println("QUANTITY IN LIST ON THIS I+1 PLACE IN IF " + productsInCartNameQuantity.get(i+1).toString());
                                            int result = overallQnty -productQnty;
//                                            System.out.println("RESULT" + result);


                                            postSnapshot.getRef().child("quantity").setValue(result);

                                   for(int j = 0; j < productsInCartNameQuantity.size(); j ++ ){
                                       System.out.println("THE PRODUCT IN CART" + productsInCartNameQuantity.get(j));
                                   }
                                }

                            }
                            }
                        }
                        valueUpdated = 1;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });











                //CREATE AND UPDATE ORDER
                ///////@@@@@@@@@@/////////
                ordersReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                   System.out.println(     "The value of list" + postSnapshot.getValue());


                            }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }



        });








        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.95) , (int) (height*.8));

    }




    public void radioTypeButtonDeliveryMan(View view) {
        deliveryType=1;
        System.out.println("radioTypeButtonDeliveryMan");
    }
    public void radioTypeDrone(View view) {
        deliveryType=2;
        System.out.println("radioTypeDrone");
    }
    public void radioTypePickup(View view) {
        deliveryType=3;
        System.out.println("radioTypePickup");
    }

}
