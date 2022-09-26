package com.example.namaprojectfirebase;

import static com.example.namaprojectfirebase.Login.mAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.namaprojectfirebase.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class Cart extends AppCompatActivity {
    public static RecyclerView recyclerView;
    CartProductAdapter adapter;
    List<Product> productList;
    static DatabaseReference dbProducts,dbProductsInCart;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    Button removeOrderBtn,placeOrderBtn;
    public static int sum, inCartFlag, orderPlaced=0,createdNewCart=0;
    public TextView sumTotal;
    public DataSnapshot snapshotAllProducts;
    DatabaseReference orderDbSnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        productList = new ArrayList<>();
        orderPlaced=0;
        //////System.out.println("At the create orderPlaced flag " + orderPlaced);

        recyclerView = (RecyclerView) findViewById(R.id.allItemsRecyclerViewCart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button b = (Button) findViewById(R.id.placeOrderBtn);

//        imageView = findViewById(R.id.imageView);
//        Glide.with(this).load("").into(imageView);

        adapter = new CartProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);


        //SELECT * FROM Carts
        dbProducts = FirebaseDatabase.getInstance().getReference("carts").child(HomeFragment.uniqueOfCartID);
        dbProducts.addListenerForSingleValueEvent(valueEventListener);



        dbProductsInCart = FirebaseDatabase.getInstance().getReference("carts").child(HomeFragment.uniqueOfCartID);
        dbProductsInCart.addValueEventListener(valueEventListenerForUpdateGlobalQuantity);


        //DELETE CART * From Products

        //////System.out.println("THE CARD IS NUM" + HomeFragment.uniqueOfCartID);
        removeOrderBtn = (Button) findViewById(R.id.removeOrderBtn);
        placeOrderBtn = (Button) findViewById(R.id.placeOrderBtn);
        sumTotal = findViewById(R.id.sumTotal);
        removeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////System.out.println("HEYYY REMOVE");
                //////System.out.println("TRY TO REMOVE" + dbProducts.child("orderPlaced").setValue(2));

                // create cart
                HomeFragment.createCartFuncUnique(mAuth.getCurrentUser().getEmail());
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////System.out.println("HEYYY I PLACE THE ORDER");
//                //////System.out.println("TRY TO PLACE" + dbProducts.child("orderPlaced").setValue(1));


                startActivity(new Intent(Cart.this, Order.class));



                //////System.out.println(orderPlaced + " THIS IS ORDER PLACED FLAG BEFORE");
                orderPlaced = 1;
                //////System.out.println(orderPlaced + " THIS IS ORDER PLACED FLAG AFTER");



            }
        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            productList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshotAllProducts : dataSnapshot.getChildren()) {
                    inCartFlag = 0;
                    //////System.out.println("THE PRODUCTS" + snapshotAllProducts);
                    if (snapshotAllProducts.getKey().equals("currentUserEmail") || snapshotAllProducts.getKey().equals("orderPlaced")){
                        ////System.out.println("IT IS THE WRONG KEY");
                    }
                    else{
                    Product product = snapshotAllProducts.getValue(Product.class);
//UPDATE QUANTITY
                    for(int i = 0; i < productList.size(); i ++){
                        ////System.out.println("RUN ON " +   productList.get(i).getNameOfProduct());
                        if (productList.get(i).getNameOfProduct().equals(product.getNameOfProduct())){
                                productList.get(i).setQuantity(productList.get(i).getQuantity() + ProductAdapter.valueQnty);
                                ////System.out.println("THE NAME IS SAME ");
//                                product.setQuantity(productList.get(i).getQuantity() + ProductAdapter.valueQnty);
                            inCartFlag = 1;
                        }
                    }
                    if(inCartFlag == 0){
                        ////System.out.println("I add product to list!!!");
                        productList.add(product);
                    }
                    else {
//                        product.setQuantity(productList.get(i).getQuantity() + ProductAdapter.valueQnty);
                        ////System.out.println("Quantity Updated " + ProductAdapter.valueQnty);
                    }
//                    ////System.out.println(productList.get(0).getNameOfProduct());

                    ////System.out.println(" PRODUCTS LIST " + product.getNameOfProduct());
                    }
                }
            // THE TOTAL
                sum = 0;
                for(int i = 0; i < productList.size(); i ++){
                    ////System.out.println("Product name: " + productList.get(i).getNameOfProduct() + " The sum price of this product " + productList.get(i).getQuantity()*productList.get(i).getBuyPrice());
                    sum += productList.get(i).getQuantity()*productList.get(i).getBuyPrice();
                    ////System.out.println(sum + "the sum");
                    sumTotal.setText("TOTAL FOR THIS ORDER: " + sum);
                }

                adapter.notifyDataSetChanged();

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void createNewCart(){
        ////System.out.println("Creating new CART FROM CART AFTER orderPlaced 1");
        HomeFragment.createCartFuncUnique(mAuth.getCurrentUser().getEmail());
        orderPlaced = 1;
        finish();
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }





    ValueEventListener valueEventListenerForUpdateGlobalQuantity = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(orderPlaced==1) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshotRun : snapshot.getChildren()) {

                        if (snapshotRun.getKey().contains("currentUserEmail") || snapshotRun.getKey().contains("orderPlaced")) {
                            ////System.out.println("THE PRODUCTS NEW SNAPSHOOT" + snapshotRun.getKey());
                        } else {

                            ////System.out.println("IM PRODUCT" + snapshotRun.child("nameOfProduct").getValue() + "THE QUANTITY " + snapshotRun.child("quantity").getValue());

                        }
                    }
                    ////System.out.println("Before order placed FLAG" + orderPlaced);

                    adapter.notifyDataSetChanged();
                }
                createNewCart();
            }

            }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }

    };


    public void resetGraph(){

    }




}

