package com.example.namaprojectfirebase;

import static com.example.namaprojectfirebase.Login.mAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;


public class Orders extends AppCompatActivity {
    //ALL ORDERS IN ordersList
    RecyclerView recyclerViewOrders;
    OrderForListAdapter adapter;
    List<OrderForList> ordersList;
    DatabaseReference dbOrdersPlaced;
    public static OrderForListAdapter adapterOrders;
    public static ImageButton showAllProducts ,cartActivity, showAllOrders, addProductActivity,allGraphs,overdueActivity,userListActivity,addUserActivity;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ordersList = new ArrayList<>();
        recyclerViewOrders = (RecyclerView) findViewById(R.id.allItemsRecyclerViewOrders);
        recyclerViewOrders.setHasFixedSize(true);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        dbOrdersPlaced = FirebaseDatabase.getInstance().getReference("orders");
        dbOrdersPlaced.addListenerForSingleValueEvent(valueEventListener);

        showAllProducts = findViewById(R.id.showAllProducts);
        cartActivity = findViewById(R.id.cartActivity);
        showAllOrders = findViewById(R.id.showAllOrders);
        addProductActivity = findViewById(R.id.addProductActivity);
        allGraphs = findViewById(R.id.allGraphs);
        overdueActivity = findViewById(R.id.overdueActivity);
        userListActivity = findViewById(R.id.userListActivity);
        addUserActivity = findViewById(R.id.addUserActivity);


        if (Login.globalPermission == 2) {
            //general worker
            allGraphs.setVisibility(View.INVISIBLE);
            addUserActivity.setVisibility(View.INVISIBLE);
            userListActivity.setVisibility(View.INVISIBLE);
        }

        if (Login.globalPermission == 3) {
            //deliveryman
            addProductActivity.setVisibility(View.INVISIBLE);
            userListActivity.setVisibility(View.INVISIBLE);
            allGraphs.setVisibility(View.INVISIBLE);
            overdueActivity.setVisibility(View.INVISIBLE);
            addUserActivity.setVisibility(View.INVISIBLE);
        }

        if (Login.globalPermission == 4) {
            //accountant
            addProductActivity.setVisibility(View.INVISIBLE);
            addUserActivity.setVisibility(View.INVISIBLE);
        }
        if(Login.globalPermission == 5) {
            //client
            addProductActivity.setVisibility(View.INVISIBLE);
            addUserActivity.setVisibility(View.INVISIBLE);
            overdueActivity.setVisibility(View.INVISIBLE);
            userListActivity.setVisibility(View.INVISIBLE);
            allGraphs.setVisibility(View.INVISIBLE);
            showAllOrders.setVisibility(View.INVISIBLE);

        }

//        ordersList.add(new OrderForList("dkl", "ldkld", "lkdl", "dlkdl", "ldklkd", "dlkd","dlkd"));
//        ordersList.add(new OrderForList(1, "hey", "Hey", 12, 13345677));
//
        //creating recyclerview adapter
        adapterOrders = new OrderForListAdapter(this, ordersList);

        //setting adapter to recyclerview
        recyclerViewOrders.setAdapter(adapterOrders);


//
        showAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Orders.this, MainActivity.class));
            }
        });
        cartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders.this, Cart.class));
            }
        });
        showAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders.this, Orders.class));
            }
        });
        addProductActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders.this, AddProduct.class));
            }
        });
        allGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders.this, GraphClass.class));
            }
        });
        overdueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders.this, ExpDateItems.class));
            }
        });
        addUserActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("TRY TO GO T REGISTER");
                startActivity(new Intent(Orders.this, Register.class));
            }
        });
        userListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders.this, UserRecycleViewClass.class));
            }
        });

    }




    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ordersList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey().equals("cartNumberFromCarts")){


                    }
                    else
                    {

                        //System.out.println("IM IN SNAP ORDERS" + snapshot.getKey());
                        OrderForList order = snapshot.getValue(OrderForList.class);
                        //System.out.println(snapshot.getValue().toString());

                        ordersList.add(order);
                        //System.out.println("THE LIST " + ordersList.get(0).getTimeOfPlacedOrder());
                    }


                }

                adapterOrders.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };




     }


//
//        recyclerView = (RecyclerView) findViewById(R.id.allItemsRecyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        EditText editTextSearch = findViewById(R.id.editTextSearch);
//        editTextSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                filter(s.toString());
//            }
//        });
//
//        adapter = new ProductAdapter(this, productList);
//        recyclerView.setAdapter(adapter);
//
//        //SELECT * FROM Products
//        dbProducts = FirebaseDatabase.getInstance().getReference("products");
//        dbProducts.addListenerForSingleValueEvent(valueEventListener);
//
//
////        dataSnapshot = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).child("permission");
////
////
//
//
//
//
//        dataSnapshot = FirebaseDatabase.getInstance().getReference().child("users");
//        dataSnapshot.addListenerForSingleValueEvent(valueEventListenerNew);
//
//
//    }
//
//
//
//
//
//
//
//
//
//
//    private void filter (String text){
//        ArrayList <Product> filteredList = new ArrayList<>();
//
//        FoodCheckBox = (CheckBox)findViewById(R.id.checkBoxFood);
//        DrinkCheckBox = (CheckBox)findViewById(R.id.checkBoxDrink);
//        FruitsAndVegetablesCheckBox = (CheckBox)findViewById(R.id.checkBoxFruitsAndVegetables);
//        MeatCheckBox = (CheckBox)findViewById(R.id.checkBoxMeat);
//        GrainCheckBox = (CheckBox)findViewById(R.id.checkBoxGrain);
//        DairyCheckBox = (CheckBox)findViewById(R.id.checkBoxDairy);
//
//
//
//
//
//
//
//
//        for(Product item : productList ){
//            //System.out.println("BEFORE FILTER");
//            if(item.getNameOfProduct().toLowerCase().contains(text.toLowerCase())){
//                //System.out.println("PRODUCT FILTER TYPE IS " + item.getType());
//                //TODO type of Product ASK VERONIKA
//                if(FoodCheckBox.isChecked() && item.getType()==1)
//                    filteredList.add(item);
//                if(FruitsAndVegetablesCheckBox.isChecked() && item.getType() ==2)
//                    filteredList.add(item);
//                if(MeatCheckBox.isChecked() && item.getType() ==3)
//                    filteredList.add(item);
//                if(GrainCheckBox.isChecked() && item.getType() ==4)
//                    filteredList.add(item);
//                if(DairyCheckBox.isChecked() && item.getType() ==5)
//                    filteredList.add(item);
//                if(DrinkCheckBox.isChecked() && item.getType() ==6)
//                    filteredList.add(item);
//
//            }
//        }
//        adapter.filteredList(filteredList);
//
//    }
//
//
//    ValueEventListener valueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            productList.clear();
//            if (dataSnapshot.exists()) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    //System.out.println("THE BEST BEFORE FROM DB " +snapshot.child("bestBefore").getValue());
//                    Product product = snapshot.getValue(Product.class);
//                    product.setType(Integer.parseInt(snapshot.child("typeOfProduct").getValue().toString()));
//                    //System.out.println("The before sending to list " + product.getBestBefore());
//
//                    productList.add(product);
//
//
//                    //System.out.println("TYPE IS TO LISSS" + product.getType());
//
//                }
//                adapter.notifyDataSetChanged();
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };
//
//
//
//    ValueEventListener valueEventListenerNew = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            if (dataSnapshot.exists()) {
//                for (DataSnapshot snapshotUserType : dataSnapshot.getChildren()) {
//                    //System.out.println("IUSERRR"  + snapshotUserType.child("permission").getValue());
//                    if(snapshotUserType.child("email").getValue().equals(mAuth.getCurrentUser().getEmail())){
//                        //System.out.println("THE TYPE IS : " + snapshotUserType.child("permission").getValue() + "The user " + mAuth.getCurrentUser().getEmail());
//                        Login.globalPermission = Integer.parseInt(snapshotUserType.child("permission").getValue().toString()) ;
//                        //System.out.println("THE permission : " + Login.globalPermission);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };
//
////    public void theLogOut(MenuItem item) {
////        //System.out.println("SIGNT OOUT");
//////        FirebaseAuth.getInstance().signOut();
////
////    }


