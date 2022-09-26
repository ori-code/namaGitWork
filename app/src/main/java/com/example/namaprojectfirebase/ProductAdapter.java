package com.example.namaprojectfirebase;


import static com.example.namaprojectfirebase.R.id.*;
import static com.example.namaprojectfirebase.R.id.overallCart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namaprojectfirebase.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    ImageView imageView;
    public Context mCtx;
    public List<Product> productList;
    public ImageView imageDB;
    public int foundedProductFlag,finishedSnapRun = 0,theFoundedQuantity=0 ;
    public String theFoundedProductKey;
    public static int valueQnty;
    DatabaseReference cartDb,updateProducts;
    Query cartQuery, productUpdateQuery;
    public CardView cardForRecycle;
    public ImageView imageArrow;
    public static ArrayList <Product> overdueProdList = null;
    public LinearLayout overallCart;
    public long [] dataCounting = new long[1000];
    public long allCountItem;
    public int flagGreenBckg;
    public long theLastQntyAdded;
    public long theLastTimeAdded;
    public long theNowQuantity1;
    public static String theKeyOfProduct1;
    public int readingDataCount = 0;
    public static long lastAddingDate = 0;
    public static long lassAddingCount = 0;
    public static long minQuantity = 0;
    public static ArrayList dateOfAdding1;


    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recyclerlist, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        cartDb = FirebaseDatabase.getInstance().getReference("carts").child(HomeFragment.uniqueOfCartID);
        cartQuery = cartDb.orderByKey();
        updateProducts = FirebaseDatabase.getInstance().getReference("products");
        productUpdateQuery = updateProducts.orderByKey();

        //TODO PERMISSION SETUP
        if(Login.globalPermission != 1) {
            imageArrow.setVisibility(View.GONE);

        }


        return holder;

    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        //////System.out.println("Product before creation "+ productList.get(position).getBestBefore());
        Product product = productList.get(position);
        //////System.out.println("Product xfksnfnfksdm "+ productList.get(position).getAddingDate());
        //EPOCH TO STRING
        long bestBefore = productList.get(position).getBestBefore();
        Date date = new Date(bestBefore);
        long addingDate [] = productList.get(position).getAddingDate();

        long epochExp = date.getTime();





        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String text = format.format(date);
        //current time
        Date currentTime = Calendar.getInstance().getTime();
        long epochCurrent = currentTime.getTime();
        long toOrder = bestBefore - 86400 * 3;

//TODO changeBackground
        ////System.out.println("HEYYY the list in adapter" +productList.get(position).getAddingDate() + "the name " + productList.get(position).getNameOfProduct());
        dataCounting = productList.get(position).getAddingDate();
        for(int i = 0; i < dataCounting.length; i++ ){
            if(i%2!=0){
                allCountItem = dataCounting[i] + allCountItem;
                ////System.out.println("ALL COUNT ITEM " + allCountItem);
                if(dataCounting[i]!=0){
                    theLastQntyAdded = dataCounting[i];
                    theLastTimeAdded = dataCounting[i-1];
                    ////System.out.println("LAST QUABTITY " + theLastQntyAdded*0.5 + " the real wuanityt "+  productList.get(position).getQuantity());


                }
            }
            else{
                ////System.out.println(" THE RECORDS IN THIS IS " + dataCounting[i]);
            }
            }



//GREEN PRODUCT
        if(theLastQntyAdded*0.5 > productList.get(position).getQuantity()){
            ////System.out.println("Im THE GREEN PRODUCT " + theLastTimeAdded);
            if(epochCurrent - theLastTimeAdded < 1662508800){
                ////System.out.println(" THE TIME LOWER THAN WEEK" + (epochCurrent - theLastTimeAdded));
                overallCart.setBackgroundColor(Color.parseColor("#007F00"));
            }

        }

//RED PRODUCT
        if(theLastQntyAdded*0.8 < productList.get(position).getQuantity()){
            ////System.out.println("Im THE RED PRODUCT " + theLastTimeAdded);
            if(epochCurrent - theLastTimeAdded > 1662508800){
//                227238713
                overallCart.setBackgroundColor(Color.parseColor("#FF0000"));
                ////System.out.println(" THE TIME LOWER THAN WEEK" + (epochCurrent - theLastTimeAdded));
            }

        }




        if(readingDataCount < productList.size()){

            System.out.println("MIN QNTY " + productList.get(position).getMinQty());
            System.out.println("Reading Count  " + readingDataCount); // products count in list
        //last adding and min quantity algorithm


            System.out.println("THE NAME OF PRODUCT " + productList.get(position).getNameOfProduct());
            System.out.println("THE LAST TIME ADDED " + theLastTimeAdded+ " epochCurrent " +epochCurrent+ " THE RESULT " + (epochCurrent - theLastTimeAdded)/1000);

        if(productList.get(position).getMinQty() >= productList.get(position).getQuantity()){
            //less than 1 week reached
            if((epochCurrent - theLastTimeAdded)/1000 < 604800){
                //less than week
                System.out.println("THE NAME OF PRODUCT AFTER IF " + productList.get(position).getNameOfProduct());
                System.out.println("THE LAST TIME ADDED " + theLastTimeAdded+ " epochCurrent " +epochCurrent+ " THE RESULT " + (epochCurrent - theLastTimeAdded));
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                builder1.setMessage("The " + productList.get(position).getNameOfProduct() + " reached the minimum quantity parameter in LESS THAN WEEK, are you want to order 150% of quantity from the last adding? ");
                builder1.setCancelable(true);
                long[] longAddingDateArr;// = new long[productList.get(position).getAddingDate().toString().length()];
                longAddingDateArr = productList.get(position).getAddingDate();

                for(int i = 0; i < longAddingDateArr.length; i++){
                    if(longAddingDateArr[i] == 0){
                        lastAddingDate = longAddingDateArr[i-2];
                        lassAddingCount = longAddingDateArr[i-1];
                        System.out.println("HHHH in place: " + i + " DATA=> " +longAddingDateArr[i-1] + " and " + longAddingDateArr[i-2]);
                        System.out.println("GGGG in place: " + i + " DATA=> " +lassAddingCount + " and " + lastAddingDate);
                        break;
                    }

                }


                builder1.setPositiveButton(
                        "ORDER 150% AMOUNT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                productUpdateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // dataSnapshot is the "issue" node with all children with id 0
                                            for (DataSnapshot product : dataSnapshot.getChildren()) {
                                                if(product.child("nameOfProduct").getValue().equals(productList.get(position).getNameOfProduct())){

                                                    dateOfAdding1 = (ArrayList) product.child("dataOfAdding").getValue();
                                                    theNowQuantity1 =  Long.parseLong(product.child("quantity").getValue().toString());

                                                    long theLastCountAdding = (long) dateOfAdding1.get(dateOfAdding1.size()-1);

                                                    System.out.println("THE LAST count of added qnty " + theLastCountAdding);
                                                    theKeyOfProduct1 = product.getKey();
                                                    long lassAddingCount1 = (long) (theLastCountAdding*1.5);
                                                    long updatedQnty = theNowQuantity1 + lassAddingCount1;
                                                    int sizeAndLastPLace = dateOfAdding1.size();
                                                    long time= System.currentTimeMillis();
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("dataOfAdding")
                                                            .child(String.valueOf(sizeAndLastPLace+1))
                                                            .setValue(time)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("dataOfAdding")
                                                            .child(String.valueOf(sizeAndLastPLace+2))
                                                            .setValue(lassAddingCount1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("quantity")
                                                            .setValue(updatedQnty)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
//
                                                }

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "DONT ORDER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder1.setNeutralButton(
                        "ORDER THE SAME AMOUNT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                productUpdateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // dataSnapshot is the "issue" node with all children with id 0
                                            for (DataSnapshot product : dataSnapshot.getChildren()) {
                                                if(product.child("nameOfProduct").getValue().equals(productList.get(position).getNameOfProduct())){

                                                    dateOfAdding1 = (ArrayList) product.child("dataOfAdding").getValue();
                                                    theNowQuantity1 =  Long.parseLong(product.child("quantity").getValue().toString());

                                                    long theLastCountAdding = (long) dateOfAdding1.get(dateOfAdding1.size()-1);

                                                    System.out.println("THE LAST count of added qnty " + theLastCountAdding);
                                                    theKeyOfProduct1 = product.getKey();
                                                    long lassAddingCount1 = (long) (theLastCountAdding*1.0);
                                                    long updatedQnty = theNowQuantity1 + lassAddingCount1;
                                                    int sizeAndLastPLace = dateOfAdding1.size();
                                                    long time= System.currentTimeMillis();
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("dataOfAdding")
                                                            .child(String.valueOf(sizeAndLastPLace+1))
                                                            .setValue(time)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("dataOfAdding")
                                                            .child(String.valueOf(sizeAndLastPLace+2))
                                                            .setValue(lassAddingCount1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("quantity")
                                                            .setValue(updatedQnty)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
//
                                                }

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else{

                System.out.println("THE NAME OF PRODUCT AFTER IF " + productList.get(position).getNameOfProduct());
                System.out.println("THE LAST TIME ADDED " + theLastTimeAdded+ " epochCurrent " +epochCurrent+ " THE RESULT " + (epochCurrent - theLastTimeAdded));
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                builder1.setMessage("The " + productList.get(position).getNameOfProduct() + " reached the minimum quantity parameter, are you want to order 100% of quantity from the last adding? ");
                builder1.setCancelable(true);
                long[] longAddingDateArr;// = new long[productList.get(position).getAddingDate().toString().length()];
                longAddingDateArr = productList.get(position).getAddingDate();

                for(int i = 0; i < longAddingDateArr.length; i++){
                    if(longAddingDateArr[i] == 0){
                        lastAddingDate = longAddingDateArr[i-2];
                        lassAddingCount = longAddingDateArr[i-1];
                        System.out.println("HHHH in place: " + i + " DATA=> " +longAddingDateArr[i-1] + " and " + longAddingDateArr[i-2]);
                        System.out.println("GGGG in place: " + i + " DATA=> " +lassAddingCount + " and " + lastAddingDate);
                        break;
                    }

                }
                //not less than week
                System.out.println("Another alerts and movements");
                builder1.setPositiveButton(
                        "ORDER 100% OF THE LAST ADDING AMOUNT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                productUpdateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // dataSnapshot is the "issue" node with all children with id 0
                                            for (DataSnapshot product : dataSnapshot.getChildren()) {
                                                if(product.child("nameOfProduct").getValue().equals(productList.get(position).getNameOfProduct())){

                                                    dateOfAdding1 = (ArrayList) product.child("dataOfAdding").getValue();
                                                    theNowQuantity1 =  Long.parseLong(product.child("quantity").getValue().toString());

                                                    long theLastCountAdding = (long) dateOfAdding1.get(dateOfAdding1.size()-1);

                                                    System.out.println("THE LAST count of added qnty " + theLastCountAdding);
                                                    theKeyOfProduct1 = product.getKey();
                                                    long lassAddingCount1 = (long) (theLastCountAdding*1.0);
                                                    long updatedQnty = theNowQuantity1 + lassAddingCount1;
                                                    int sizeAndLastPLace = dateOfAdding1.size();
                                                    long time= System.currentTimeMillis();
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("dataOfAdding")
                                                            .child(String.valueOf(sizeAndLastPLace+1))
                                                            .setValue(time)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("dataOfAdding")
                                                            .child(String.valueOf(sizeAndLastPLace+2))
                                                            .setValue(lassAddingCount1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("products")
                                                            .child(theKeyOfProduct1)
                                                            .child("quantity")
                                                            .setValue(updatedQnty)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                                                                    } else {

                                                                    }

                                                                }


                                                            });
//
                                                }

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "DONT ORDER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


//                builder1.setNeutralButton(
//                        "ORDER THE SAME AMOUNT",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                productUpdateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.exists()) {
//                                            // dataSnapshot is the "issue" node with all children with id 0
//                                            for (DataSnapshot product : dataSnapshot.getChildren()) {
//                                                if(product.child("nameOfProduct").getValue().equals(productList.get(position).getNameOfProduct())){
//
//                                                    dateOfAdding1 = (ArrayList) product.child("dataOfAdding").getValue();
//                                                    theNowQuantity1 =  Long.parseLong(product.child("quantity").getValue().toString());
//
//                                                    long theLastCountAdding = (long) dateOfAdding1.get(dateOfAdding1.size()-1);
//
//                                                    System.out.println("THE LAST count of added qnty " + theLastCountAdding);
//                                                    theKeyOfProduct1 = product.getKey();
//                                                    long lassAddingCount1 = (long) (theLastCountAdding*1.0);
//                                                    long updatedQnty = theNowQuantity1 + lassAddingCount1;
//                                                    int sizeAndLastPLace = dateOfAdding1.size();
//                                                    long time= System.currentTimeMillis();
//                                                    FirebaseDatabase.getInstance()
//                                                            .getReference("products")
//                                                            .child(theKeyOfProduct1)
//                                                            .child("dataOfAdding")
//                                                            .child(String.valueOf(sizeAndLastPLace+1))
//                                                            .setValue(time)
//                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                public void onComplete(@NonNull Task<Void> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);
//
//                                                                    } else {
//
//                                                                    }
//
//                                                                }
//
//
//                                                            });
//                                                    FirebaseDatabase.getInstance()
//                                                            .getReference("products")
//                                                            .child(theKeyOfProduct1)
//                                                            .child("dataOfAdding")
//                                                            .child(String.valueOf(sizeAndLastPLace+2))
//                                                            .setValue(lassAddingCount1)
//                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                public void onComplete(@NonNull Task<Void> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);
//
//                                                                    } else {
//
//                                                                    }
//
//                                                                }
//
//
//                                                            });
//                                                    FirebaseDatabase.getInstance()
//                                                            .getReference("products")
//                                                            .child(theKeyOfProduct1)
//                                                            .child("quantity")
//                                                            .setValue(updatedQnty)
//                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                public void onComplete(@NonNull Task<Void> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);
//
//                                                                    } else {
//
//                                                                    }
//
//                                                                }
//
//
//                                                            });
////
//                                                }
//
//                                            }
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//
//
//
//
//                                dialog.cancel();
//                            }
//                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();





            }
        }
            readingDataCount++;
        }








        if(epochCurrent >= toOrder )
        {
            //////System.out.println("TO ORDERRRR" + toOrder);
            ////System.out.println("CURRENT " + epochCurrent);
            holder.expDateInList.setTextColor(Color.parseColor("#FE0100"));
            holder.expDateInList.setTypeface(holder.expDateInList.getTypeface(), Typeface.BOLD);


            //overdueProdList.add(product);
            //////System.out.println("overdue name'" + product.getNameOfProduct() + "sfssfs" + overdueProdList);
        }


        Picasso.get().load(product.getImageUrl()).into(imageDB);

        holder.expDateInList.setText("Exp. date: " +  text);
        holder.textViewTitle.setText(product.getNameOfProduct());
        holder.textViewDesc.setText(product.getDescription());
        holder.textViewPrice.setText("Price: " + String.valueOf(product.getSellPrice()));
        holder.textViewRating.setText("Available Quantity: " + String.valueOf((int) product.getQuantity()));



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void filteredList (ArrayList<Product> filteredList){
        productList = filteredList;
        notifyDataSetChanged();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewRating, textViewPrice, expDateInList, counter;
        ImageButton addToCardRecycle;



        //        View view = inflater.inflate(R.layout.my_list_item, null);


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDB = itemView.findViewById(R.id.imageDB);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewDesc = itemView.findViewById(textViewShortDesc);
            addToCardRecycle = itemView.findViewById(R.id.addToCardRecycle);
            expDateInList = itemView.findViewById(R.id.expDateInList);
            imageArrow = itemView.findViewById(R.id.imageArrow);
            cardForRecycle = itemView.findViewById(R.id.cardForRecycle);


            overallCart = itemView.findViewById(R.id.overallCart);







            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int position = getAdapterPosition();
                                                Product product = productList.get(position);
                                                ////System.out.println("THE ADDING DATE LONG LIST  "  +  product.getAddingDate());

                                                String strNameOfProduct = productList.get(position).getNameOfProduct();
                                                ////System.out.println("Send String " + strNameOfProduct);

                                                Intent intentToEditProduct = new Intent(mCtx, editProduct.class);
                                                intentToEditProduct.putExtra("keyName", strNameOfProduct);





                                                if(Login.globalPermission == 1) {
                                                    v.getContext().startActivity(intentToEditProduct);
                                                }
                                                ////System.out.println("THE NAME THAT SENDED " + strNameOfProduct);
                                            }
                                        }




            );


            // ADD CARD BUTTON WITH QUANTITY
            itemView.findViewById(R.id.addToCardRecycle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Product product = productList.get(position);
                    EditText text = (EditText) itemView.findViewById(textViewQuantity);
                    String value = text.getText().toString();
                    valueQnty = Integer.parseInt(value);

                    //Start running on exist card
                    if (valueQnty > 0) {
                        dataReadFunc(position);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if(foundedProductFlag == 0){
                                    purchaseFunc(product.getNameOfProduct(), product.getBuyPrice(), valueQnty, product.getNameOfProduct());

                                }
                                else{
                                    purchaseFuncUpdateQuantity(product.getNameOfProduct(), product.getBuyPrice(), valueQnty);
                                }


                            }
                        }, 1000);


                        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                        builder.setCancelable(true);
                        builder.setTitle("You added to cart " + valueQnty + " of " + product.getNameOfProduct() + " units");
                        builder.setMessage("For any help you can talk to the supervisor.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                        builder.setCancelable(true);
                        builder.setTitle("You need to add more than 0 items to cart");
                        builder.setMessage("For any help you can talk to the supervisor.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    }
                }
            });
        }


        public int dataReadFunc(int position) {
            cartQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    foundedProductFlag = 0;
                    List<String> products = new ArrayList<String>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.child("nameOfProduct").getValue() == null){

                            break;
                        }
                        if(postSnapshot.child("nameOfProduct").getValue().equals(productList.get(position).getNameOfProduct())){
                            foundedProductFlag = 1;


                            theFoundedProductKey = postSnapshot.getKey();

                            Long k = (Long) postSnapshot.child("quantity").getValue();
                            theFoundedQuantity = Math.toIntExact(k);


                            break;

                        }
                        products.add(postSnapshot.getValue().toString());

                        if(foundedProductFlag == 1){
                            break;
                        }
                    }

                    finishedSnapRun = 1;

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
//
            return foundedProductFlag;
        }




        public void purchaseFunc(String productName, double price, double quantity, String keyFromProducts ) {
            Map<String, Object> dataOfCart = new HashMap<>();
            dataOfCart.put("URL", "hey");
            dataOfCart.put("id", "444");
            dataOfCart.put("nameOfProduct", productName);
            dataOfCart.put("buyPrice", price);
            dataOfCart.put("quantity", quantity);
            dataOfCart.put("sum", "sokf");
            FirebaseDatabase.getInstance()
                    .getReference("carts")
                    .child(HomeFragment.uniqueOfCartID)
                    .child(keyFromProducts).setValue(dataOfCart)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);
                            } else {

                            }

                        }

                    });

        }

        public void purchaseFuncUpdateQuantity(String productName, double price, double quantity) {
            Map<String, Object> dataOfCart = new HashMap<>();
            dataOfCart.put("URL", "hey");
            dataOfCart.put("id", "444");
            dataOfCart.put("nameOfProduct", productName);
            dataOfCart.put("buyPrice", price);
            dataOfCart.put("quantity", theFoundedQuantity + valueQnty);
            dataOfCart.put("sum", "sokf");
            FirebaseDatabase.getInstance()
                    .getReference("carts")
                    .child(HomeFragment.uniqueOfCartID).child(theFoundedProductKey)
                    .setValue(dataOfCart)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {

                            }

                        }


                    });

        }

    }
}


