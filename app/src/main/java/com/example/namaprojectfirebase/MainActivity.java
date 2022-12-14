package com.example.namaprojectfirebase;

import static com.example.namaprojectfirebase.Login.mAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    //ALL PRODUCTS IN productList
    public static List<Product> productList;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    public ImageButton addToCart;
    public String typeFromDb;
    DatabaseReference dataSnapshot;
    DatabaseReference dbProducts,addToCartDb;
    public CheckBox expCheckBox, FoodCheckBox, DrinkCheckBox, FruitsAndVegetablesCheckBox, MeatCheckBox, GrainCheckBox, DairyCheckBox;
    public static ImageButton showAllProducts ,cartActivity, showAllOrders, addProductActivity,allGraphs,overdueActivity,userListActivity,addUserActivity;
    //    public static int globalPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAllProducts = findViewById(R.id.showAllProducts);
        cartActivity = findViewById(R.id.cartActivity);
        showAllOrders = findViewById(R.id.showAllOrders);
        addProductActivity = findViewById(R.id.addProductActivity);
        allGraphs = findViewById(R.id.allGraphs);
        overdueActivity = findViewById(R.id.overdueActivity);
        userListActivity = findViewById(R.id.userListActivity);
        addUserActivity = findViewById(R.id.addUserActivity);


        productList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.allItemsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EditText editTextSearch = findViewById(R.id.editTextSearch);


        showAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        cartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Cart.class));
            }
        });
        showAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Orders.class));
            }
        });
        addProductActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddProduct.class));
            }
        });
        allGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GraphClass.class));
            }
        });
        overdueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExpDateItems.class));
            }
        });
        addUserActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("TRY TO GO T REGISTER");
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
        userListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserRecycleViewClass.class));
            }
        });


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
////
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        //SELECT * FROM Products
        dbProducts = FirebaseDatabase.getInstance().getReference("products");
        dbProducts.addListenerForSingleValueEvent(valueEventListener);


//        dataSnapshot = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).child("permission");
//
//




        dataSnapshot = FirebaseDatabase.getInstance().getReference().child("users");
        dataSnapshot.addListenerForSingleValueEvent(valueEventListenerNew);


    }

    private void filter (String text){
        ArrayList <Product> filteredList = new ArrayList<>();
        FoodCheckBox = (CheckBox)findViewById(R.id.checkBoxFood);
        DrinkCheckBox = (CheckBox)findViewById(R.id.checkBoxDrink);
        FruitsAndVegetablesCheckBox = (CheckBox)findViewById(R.id.checkBoxFruitsAndVegetables);
        MeatCheckBox = (CheckBox)findViewById(R.id.checkBoxMeat);
        GrainCheckBox = (CheckBox)findViewById(R.id.checkBoxGrain);
        DairyCheckBox = (CheckBox)findViewById(R.id.checkBoxDairy);
//        expCheckBox = (CheckBox)findViewById(R.id.checkBoxDairy);


        for(Product item : productList ){
            ////System.out.println("BEFORE FILTER");
            if(item.getNameOfProduct().toLowerCase().contains(text.toLowerCase())){
                ////System.out.println("PRODUCT FILTER TYPE IS " + item.getType());
                //TODO type of Product ASK VERONIKA
                if(FoodCheckBox.isChecked() && item.getType()==1)
                     filteredList.add(item);
                if(FruitsAndVegetablesCheckBox.isChecked() && item.getType() ==6)
                    filteredList.add(item);
                if(MeatCheckBox.isChecked() && item.getType() ==3)
                    filteredList.add(item);
                if(GrainCheckBox.isChecked() && item.getType() ==4)
                    filteredList.add(item);
                if(DairyCheckBox.isChecked() && item.getType() ==5)
                    filteredList.add(item);
                if(DrinkCheckBox.isChecked() && item.getType() ==2)
                    filteredList.add(item);
//                if(expCheckBox.isChecked() && item.getType() ==6)
//                    filteredList.add(item);
            }
        }
        adapter.filteredList(filteredList);

    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            productList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ////System.out.println("THE BEST BEFORE FROM DB " +snapshot.child("bestBefore").getValue());
                        ////System.out.println("NEW VALUES " + snapshot.getValue());

                    Product product = snapshot.getValue(Product.class);
                    String arr = snapshot.child("dataOfAdding").getValue().toString();
                    ////System.out.println("THE ARR FROM DATABASE " + arr);


                    int count = 0;
                    long dataOfAdding [] = new long[1000];
                    Matcher matcher = Pattern.compile("\\d+").matcher(arr);


                        while (matcher.find()) {
                            ////System.out.println("FOUNDED" + Long.valueOf(matcher.group()));
                            dataOfAdding[count] = Long.valueOf(matcher.group());
                            count++;
                        }

                    for(int i = 0; i < arr.length(); i++){
                        ////System.out.println("!!!" + dataOfAdding[i]);
                    }

                    product.setAddingDate(dataOfAdding);

                    ////System.out.println("LOOKING FOR VALUE OF DATA ADDING " + snapshot + "AFTER BUILDER " + product.getAddingDate().toString());

                    product.setType(Integer.parseInt(snapshot.child("typeOfProduct").getValue().toString()));
                    ////System.out.println("The before sending to list " + product.getBestBefore());
                    productList.add(product);

                    ////System.out.println("TYPE IS TO LISSS" + product.getType());

                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    ValueEventListener valueEventListenerNew = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshotUserType : dataSnapshot.getChildren()) {
                    ////System.out.println("IUSERRR"  + snapshotUserType.child("permission").getValue());
                    if(snapshotUserType.child("email").getValue().equals(mAuth.getCurrentUser().getEmail())){
                        ////System.out.println("THE TYPE IS : " + snapshotUserType.child("permission").getValue() + "The user " + mAuth.getCurrentUser().getEmail());
                        Login.globalPermission = Integer.parseInt(snapshotUserType.child("permission").getValue().toString()) ;
                        ////System.out.println("THE permission : " + Login.globalPermission);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };




//TODO WEATHER

//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=35.5&lon=-78.5")
//                .get()
//                .addHeader("Master API Key", "b5d00ae48e384ca1b56afcf1316a9778")
//                .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
//                .build();
//
//        Response response;
//
//        {
//            try {
//                response = client.newCall(request).execute();
//                ////System.out.println("WEATHER" + response);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    public void theLogOut(MenuItem item) {
//        ////System.out.println("SIGNT OOUT");
////        FirebaseAuth.getInstance().signOut();
//
//    }
}

