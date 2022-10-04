package com.example.namaprojectfirebase;

import static com.example.namaprojectfirebase.Login.mAuth;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserEdit extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference userRef;

    public EditText nameOfUserEditPage,userIdEditPageLicense,userEditPhone, userEditAddress;
    public TextView userEditPageEmail, salaryEditUser, permissionUserEdit;
    public static String currentUserEmail;
    public static User userToEdit;
    Button updateBtn, deleteBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_show);
        nameOfUserEditPage = findViewById(R.id.nameOfUserEditPage);
        userIdEditPageLicense = findViewById(R.id.userIdEditPageLicense);
        userEditPhone = findViewById(R.id.userEditPhone);
        userEditAddress = findViewById(R.id.userEditAddress);

        userEditPageEmail = findViewById(R.id.userEditPageEmail);
        salaryEditUser = findViewById(R.id.salaryEditUser);
        permissionUserEdit = findViewById(R.id.permissionUserEdit);



        updateBtn = (Button) findViewById(R.id.productButtonPageEditUpdate);
        updateBtn.setOnClickListener(this);
        deleteBtn = (Button) findViewById(R.id.productButtonPageEditDelete);
        deleteBtn.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();

        userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addListenerForSingleValueEvent(valueEventListenerForUpdateUser);



//        userQuery = userQuery.orderByKey();

        currentUserEmail = Login.mAuth.getCurrentUser().getEmail();

        findUser(currentUserEmail);

    }



    public void findUser (String emailOfUser) {





    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }



    //LIST  OF ADDING PRODUCTS
    ValueEventListener valueEventListenerForUpdateUser = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            System.out.println("IN RUNNING ");
            for (DataSnapshot snapshotRun : snapshot.getChildren()) {
                if(snapshotRun.child("email").getValue().equals(mAuth.getCurrentUser().getEmail()))
                {
                    String permission = new String();
                    userToEdit = snapshotRun.getValue(User.class);
                    System.out.println("IN IF " + userToEdit.getEmail());
                    nameOfUserEditPage.setText(userToEdit.getFullName());
                    userIdEditPageLicense.setText(userToEdit.getLicenseNum());
                    userEditPageEmail.setText(userToEdit.getEmail());
                    userEditPhone.setText(userToEdit.getPhoneNum());
                    userEditAddress.setText(userToEdit.getAddress());
                    salaryEditUser.setText(String.valueOf(userToEdit.getSalary()));
                    if (userToEdit.getPermission() == 1){
                        permission = "Admin";
                    }
                    if(userToEdit.getPermission() == 2){
                        permission = "Worker";
                    }
                    if(userToEdit.getPermission() == 3){
                        permission = "Delivery Man";
                    }
                    if(userToEdit.getPermission() == 4){
                        permission = "Account Manager";
                    }
                    if(userToEdit.getPermission() == 5){
                        permission = "Client";
                    }

                    permissionUserEdit.setText(permission);


                }
                System.out.println("THE VALUE OF USER" +   snapshotRun.child("email").getValue());
//                System.out.println("THE VALUE OF USER from phone is " + userToEdit.getEmail() );
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

//clickers
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case  R.id.productButtonPageEditUpdate: {
//                //System.out.println("FIRST ATTEMPT STRING "  + firstAttempQnty  + " EDIT" + editProductQnty.getText().toString());
//                if(!editProductQnty.getText().toString().isEmpty()){
//                    //System.out.println("IM NOT NULLLL" +  findProduct.child(theKeyOfProduct).child("nameOfProfuct").getKey() + " REAL QUANTITY " +   findProduct.child(theKeyOfProduct).child("quantity").getKey());
//                    theNowQuantity = theNowQuantity + Integer.parseInt(editProductQnty.getText().toString());
//                    findProduct.child(theKeyOfProduct).child("quantity").setValue(Integer.parseInt(String.valueOf(theNowQuantity)));
////                     findProduct.child(theKeyOfProduct).child("quantity").setValue(Integer.parseInt(editProductQnty.getText().toString() + theNowQuantity));
//
//                    Integer.parseInt(editProductQnty.getText().toString());
//
//                    int sizeAndLastPLace = dateOfAdding.size();
//
//                    String.valueOf(sizeAndLastPLace+1);
//
//                    long time= System.currentTimeMillis();
//
//                    FirebaseDatabase.getInstance()
//                            .getReference("products")
//                            .child(theKeyOfProduct)
//                            .child("dataOfAdding")
//                            .child(String.valueOf(sizeAndLastPLace+1))
//                            .setValue(time)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);
//
//                                    } else {
//
//                                    }
//
//                                }
//
//
//                            });
//                    FirebaseDatabase.getInstance()
//                            .getReference("products")
//                            .child(theKeyOfProduct)
//                            .child("dataOfAdding")
//                            .child(String.valueOf(sizeAndLastPLace+2))
//                            .setValue(Integer.parseInt(editProductQnty.getText().toString()))
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        ////System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);
//
//                                    } else {
//
//                                    }
//
//                                }
//
//
//                            });
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
//
//
//
//
//
//                }
//                if(!editProductMinQnty.getText().toString().isEmpty()){
//                    //System.out.println("IM NOT MINQUANTITY" +  findProduct.child(theKeyOfProduct).child("minQty").getKey());
//                    findProduct.child(theKeyOfProduct).child("minQty").setValue(Integer.parseInt(editProductMinQnty.getText().toString()));
//
//                }
//                if(!editProductSellPrice.getText().toString().isEmpty()){
//                    //System.out.println("IM NOT MINQUANTITY" +  findProduct.child(theKeyOfProduct).child("sellPrice").getKey());
//                    findProduct.child(theKeyOfProduct).child("sellPrice").setValue(Integer.parseInt(editProductSellPrice.getText().toString()));
//                }
//                break;
//            }
//
//            case R.id.productButtonPageEditDelete: {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setCancelable(true);
//                builder.setTitle("Are you sure?");
//                builder.setMessage("This action is nonreturnable. ");
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteProduct.child("products").child(theKeyOfProduct).removeValue();
//                    }
//                });
//                builder.show();
//
//                break;
//            }
//
//        }
//    }



//    public void workWithExistFunc (String nameSelected) {
//        productQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                List<String> products = new ArrayList();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    //System.out.println("THE VALUE OF PRODUCT" + postSnapshot.getValue());
//
////                    Product product = postSnapshot.getValue(Product.class);
////                    //System.out.println("THE PRODUCT IS FROM SNAP " + product);
////
////                    if(postSnapshot.child("nameOfProduct").equals(nameOfProduct)){
////                        //System.out.println("THE NAME FROM DB IS " + postSnapshot.child("nameOfProduct"));
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

//    ValueEventListener valueEventListenerForUpdateProduct = new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            //System.out.println("IN RUNNING ");
//            for (DataSnapshot snapshotRun : snapshot.getChildren()) {
//                if(nameOfProduct.equals(snapshotRun.child("nameOfProduct").getValue())){
//                    //System.out.println("THE KEY IS FOUNDED" + snapshotRun.getKey());
//                    theKeyOfProduct = snapshotRun.getKey();
////                            editProductName = (EditText) findViewById(R.id.nameOfProductEditPage);
//                    editProductSellPrice = (EditText) findViewById(R.id.sellPriceEditPage);
//                    editProductNameTitle = (TextView) findViewById(R.id.nameOfProductEdit);
//                    editProductQnty = (TextView) findViewById(R.id.quantityOfProductEditPage);
//                    editProductMinQnty = (EditText) findViewById(R.id.minQuantityOfProductEditPage);
////                            editProductName.setHint(snapshotRun.child("nameOfProduct").getValue().toString());
//                    editProductSellPrice.setHint(snapshotRun.child("sellPrice").getValue().toString());
//                    editProductNameTitle.setText(snapshotRun.child("nameOfProduct").getValue().toString());
//                    editProductQnty.setHint(snapshotRun.child("quantity").getValue().toString());
//                    firstAttempQnty = snapshotRun.child("quantity").getValue().toString();
//                    editProductMinQnty.setHint(snapshotRun.child("minQty").getValue().toString());
//                }
//                //System.out.println("THE VALUE OF PRODUCT" + snapshotRun.getValue() + " AND NAME IS " + snapshotRun.child("nameOfProduct").getValue());
//            }
//        }
//
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    };






}




