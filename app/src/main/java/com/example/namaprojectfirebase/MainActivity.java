package com.example.namaprojectfirebase;

import static com.example.namaprojectfirebase.Login.mAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

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


public class MainActivity extends AppCompatActivity {
    //ALL PRODUCTS IN productList
    public static List<Product> productList;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    public ImageButton addToCart;
    public String typeFromDb;
    DatabaseReference dataSnapshot;
    DatabaseReference dbProducts,addToCartDb;
    public CheckBox FoodCheckBox, DrinkCheckBox, FruitsAndVegetablesCheckBox, MeatCheckBox, GrainCheckBox, DairyCheckBox;
//    public static int globalPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.allItemsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EditText editTextSearch = findViewById(R.id.editTextSearch);


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

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


        for(Product item : productList ){
            System.out.println("BEFORE FILTER");
            if(item.getNameOfProduct().toLowerCase().contains(text.toLowerCase())){
                System.out.println("PRODUCT FILTER TYPE IS " + item.getType());
                //TODO type of Product ASK VERONIKA
                if(FoodCheckBox.isChecked() && item.getType()==1)
                     filteredList.add(item);
                if(FruitsAndVegetablesCheckBox.isChecked() && item.getType() ==2)
                    filteredList.add(item);
                if(MeatCheckBox.isChecked() && item.getType() ==3)
                    filteredList.add(item);
                if(GrainCheckBox.isChecked() && item.getType() ==4)
                    filteredList.add(item);
                if(DairyCheckBox.isChecked() && item.getType() ==5)
                    filteredList.add(item);
                if(DrinkCheckBox.isChecked() && item.getType() ==6)
                    filteredList.add(item);
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
                    System.out.println("THE BEST BEFORE FROM DB " +snapshot.child("bestBefore").getValue());
                    Product product = snapshot.getValue(Product.class);
                    product.setType(Integer.parseInt(snapshot.child("typeOfProduct").getValue().toString()));
                    System.out.println("The before sending to list " + product.getBestBefore());

                    productList.add(product);


                    System.out.println("TYPE IS TO LISSS" + product.getType());

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
                    System.out.println("IUSERRR"  + snapshotUserType.child("permission").getValue());
                    if(snapshotUserType.child("email").getValue().equals(mAuth.getCurrentUser().getEmail())){
                        System.out.println("THE TYPE IS : " + snapshotUserType.child("permission").getValue() + "The user " + mAuth.getCurrentUser().getEmail());
                        Login.globalPermission = Integer.parseInt(snapshotUserType.child("permission").getValue().toString()) ;
                        System.out.println("THE permission : " + Login.globalPermission);
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
//                System.out.println("WEATHER" + response);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    public void theLogOut(MenuItem item) {
//        System.out.println("SIGNT OOUT");
////        FirebaseAuth.getInstance().signOut();
//
//    }
}

