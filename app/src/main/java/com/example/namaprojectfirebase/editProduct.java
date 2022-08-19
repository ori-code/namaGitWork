package com.example.namaprojectfirebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.namaprojectfirebase.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;


public class editProduct extends AppCompatActivity implements View.OnClickListener {
    Product editableProduct;
    String nameOfProduct;
    DatabaseReference findProduct,deleteProduct;
    Query productQuery;
    EditText editProductName,editProductSellPrice;
    TextView editProductNameTitle,editProductQnty,editProductMinQnty;
    Button updateBtn, deleteBtn;
    public static String firstAttempQnty,theKeyOfProduct;
    DatabaseReference deleteDocument;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        System.out.println("IM HERE ");
        updateBtn = (Button) findViewById(R.id.productButtonPageEditUpdate);
        updateBtn.setOnClickListener(this);
        deleteBtn = (Button) findViewById(R.id.productButtonPageEditDelete);
        deleteBtn.setOnClickListener(this);



        Bundle extras = getIntent().getExtras();
        nameOfProduct= extras.getString("keyName");
        System.out.println("NAME IN EDIT" + nameOfProduct);
//TODO EDITABLE PRODUCT DELETE
        findProduct = FirebaseDatabase.getInstance().getReference("products");
        findProduct.addListenerForSingleValueEvent(valueEventListenerForUpdateProduct);
        productQuery = findProduct.orderByKey();
        System.out.println(findProduct);
        deleteProduct = FirebaseDatabase.getInstance().getReference();



        System.out.println(deleteDocument);

//        workWithExistFunc(nameOfProduct);




    }


    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.productButtonPageEditUpdate: {
                System.out.println("FIRST ATTEMPT STRING "  + firstAttempQnty  + " EDIT" + editProductQnty.getText().toString());
                if(!editProductQnty.getText().toString().isEmpty()){
                     System.out.println("IM NOT NULLLL" +  findProduct.child(theKeyOfProduct).child("nameOfProfuct").getKey());
                     findProduct.child(theKeyOfProduct).child("quantity").setValue(Integer.parseInt(editProductQnty.getText().toString()));
                }
                if(!editProductMinQnty.getText().toString().isEmpty()){
                    System.out.println("IM NOT MINQUANTITY" +  findProduct.child(theKeyOfProduct).child("minQty").getKey());
                    findProduct.child(theKeyOfProduct).child("minQty").setValue(Integer.parseInt(editProductMinQnty.getText().toString()));

                }
                if(!editProductSellPrice.getText().toString().isEmpty()){
                    System.out.println("IM NOT MINQUANTITY" +  findProduct.child(theKeyOfProduct).child("sellPrice").getKey());
                        findProduct.child(theKeyOfProduct).child("sellPrice").setValue(Integer.parseInt(editProductSellPrice.getText().toString()));
                }
                break;
            }

            case R.id.productButtonPageEditDelete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("This action is nonreturnable. ");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct.child("products").child(theKeyOfProduct).removeValue();
                    }
                });
                builder.show();

                break;
            }

        }
    }



//    public void workWithExistFunc (String nameSelected) {
//        productQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                List<String> products = new ArrayList();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    System.out.println("THE VALUE OF PRODUCT" + postSnapshot.getValue());
//
////                    Product product = postSnapshot.getValue(Product.class);
////                    System.out.println("THE PRODUCT IS FROM SNAP " + product);
////
////                    if(postSnapshot.child("nameOfProduct").equals(nameOfProduct)){
////                        System.out.println("THE NAME FROM DB IS " + postSnapshot.child("nameOfProduct"));
////                    }
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//    }

        ValueEventListener valueEventListenerForUpdateProduct = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            System.out.println("IN RUNNING ");
                    for (DataSnapshot snapshotRun : snapshot.getChildren()) {
                        if(nameOfProduct.equals(snapshotRun.child("nameOfProduct").getValue())){
                            System.out.println("THE KEY IS FOUNDED" + snapshotRun.getKey());
                            theKeyOfProduct = snapshotRun.getKey();
//                            editProductName = (EditText) findViewById(R.id.nameOfProductEditPage);
                            editProductSellPrice = (EditText) findViewById(R.id.sellPriceEditPage);
                            editProductNameTitle = (TextView) findViewById(R.id.nameOfProductEdit);
                            editProductQnty = (TextView) findViewById(R.id.quantityOfProductEditPage);
                            editProductMinQnty = (EditText) findViewById(R.id.minQuantityOfProductEditPage);
//                            editProductName.setHint(snapshotRun.child("nameOfProduct").getValue().toString());
                            editProductSellPrice.setHint(snapshotRun.child("sellPrice").getValue().toString());
                            editProductNameTitle.setText(snapshotRun.child("nameOfProduct").getValue().toString());
                            editProductQnty.setHint(snapshotRun.child("quantity").getValue().toString());
                            firstAttempQnty = snapshotRun.child("quantity").getValue().toString();
                            editProductMinQnty.setHint(snapshotRun.child("minQty").getValue().toString());
                        }
                        System.out.println("THE VALUE OF PRODUCT" + snapshotRun.getValue() + " AND NAME IS " + snapshotRun.child("nameOfProduct").getValue());
                    }
                }


        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };



    }




