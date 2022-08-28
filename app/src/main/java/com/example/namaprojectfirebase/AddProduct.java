package com.example.namaprojectfirebase;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class AddProduct extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public TextView addProduct, DateAdding, BestBefore, getProduct;
    public EditText editID, editName, editBuyPrice, editSellPrice, editQuantity , editDescription;
    private DatePickerDialog.OnDateSetListener AddingDateListener, BestBeforeListener;
    public int Type;
    public static String uniqueOfProducID;
    public double buyPr, sellPr;
    private DatabaseReference rootDataBase;
    private StorageReference mStorageRef;
    private ImageView imImage;
    public Uri uploadUri;
    public String UriStr;
    public  Calendar calendarExp, calendarAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        rootDataBase = FirebaseDatabase.getInstance().getReference().child("products");
//        rootDataBase.addListenerForSingleValueEvent(rootDataBase.addValueEventListener());
        mStorageRef = FirebaseStorage.getInstance().getReference("ImageDB");

        DateAdding = (TextView) findViewById(R.id.editDateAdd);
        BestBefore = (TextView) findViewById(R.id.editBestBefore);



        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typeProduct, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        DateAdding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar addDate = Calendar.getInstance();
                int year = addDate.get(Calendar.YEAR);
                int month = addDate.get(Calendar.MONTH);
                int day = addDate.get(Calendar.DAY_OF_MONTH);

                addDate.set(year,month,day);
                Long dateInMillies = addDate.getTimeInMillis();
                String date = Long.toString(dateInMillies);
                System.out.println("date is DATE" + date);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddProduct.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        AddingDateListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });



        BestBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar addDate = Calendar.getInstance();
                int year = addDate.get(Calendar.YEAR);
                int month = addDate.get(Calendar.MONTH);
                int day = addDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddProduct.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        BestBeforeListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                //System.out.println("date out");
            }
        });




        AddingDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //System.out.println("the date is " + day + "/" + month + "/" + year + "/");

                String date = day + "/" + month + "/" + year;
                calendarAdd = new GregorianCalendar(year, month, day);
                calendarAdd.getTimeInMillis();
                System.out.println(calendarAdd.getTimeInMillis());
                DateAdding.setText(date);
            }
        };


        BestBeforeListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //System.out.println("the date is " + day + "/" + month + "/" + year + "/");
                String date = day + "/" + month + "/" + year;

                calendarExp = new GregorianCalendar(year, month, day);
                calendarExp.getTimeInMillis();
                System.out.println(calendarExp.getTimeInMillis());
                BestBefore.setText(date);

            }
        };

        addProduct = (Button) findViewById(R.id.addProductButton);
        addProduct.setOnClickListener(this);

        editID = (EditText) findViewById(R.id.editID);
        editName = (EditText) findViewById(R.id.editName);
        editBuyPrice = (EditText) findViewById(R.id.editBuyPrice);
        editSellPrice = (EditText) findViewById(R.id.editCellPrice);
        editQuantity = (EditText) findViewById(R.id.quantity);
        editDescription = (EditText) findViewById(R.id.editDescription);
        imImage = (ImageView) findViewById(R.id.imageView);
    }





    public static double StoNum (@NonNull String s) throws ParsingException {
        try {
            double num = Double.parseDouble(s.trim());
            //System.out.println("the num is " + num);
            return num;
        }
        catch (NumberFormatException nfe) {
            throw new ParsingException("NumberFormatException: " + nfe.getMessage());
        }
    }




//
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addProductButton:
                //System.out.println("Product is added");
                addProduct();
                break;
//            case R.id.backButton:
//                startActivity(new Intent(this, MainActivity.class));
//                break;

        }
    }
    //Getting image for uploading from internal storage
    public void onClickChooseImage(View view){
        getImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null){
            if (resultCode == RESULT_OK){
                Log.d("My log", "image URI: " + data.getData());
                imImage.setImageURI(data.getData());

            }
        }
    }
    private void uploadImage (){
        Bitmap bitmap = ((BitmapDrawable) imImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos );
        byte [] byteArray = baos.toByteArray();

        final StorageReference mRef = mStorageRef.child(System.currentTimeMillis()+ "my_image");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult(); //tyt hranitsa ssilka
                SaveProduct();
                //System.out.println("Heyy, the pic is uploaded  " + uploadUri);
            }
        });
    }

    private void getImage(){
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Food")) {
            Type = 1;
            //System.out.println("The type is " + Type);
        }
        if (text.equals("Drink")) {
            Type = 2;
            //System.out.println("The type is " + Type);
        }
        if (text.equals("Meat")) {
            Type = 3;
            //System.out.println("The type is " + Type);
        }
        if (text.equals("Grain")) {
            Type = 4;
            //System.out.println("The type is " + Type);
        }
        if (text.equals("Dairy")) {
            Type = 5;
            //System.out.println("The type is " + Type);
        }
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void SaveProduct () {

        String ID = editID.getText().toString().trim();
        String Name = editName.getText().toString().trim();
        String BuyPrice = editBuyPrice.getText().toString().trim();
        String CellPrice = editSellPrice.getText().toString().trim();

        String quantityString= editQuantity.getText().toString();
        double quantity = Integer.parseInt(quantityString);

        double minQty = quantity*0.1;
        int minQuantity = (int) minQty;

        //System.out.println("QUANTITY" + quantity);

        String addingDate = DateAdding.getText().toString().trim();
        String bestBefore = BestBefore.getText().toString().trim();



        String URL = uploadUri.toString();
        String description = editDescription.getText().toString().trim();

        if (ID.isEmpty()) {
            editID.setError("ID is required!");
            editID.requestFocus();
            return;
        }
        if (Name.isEmpty()) {
            editName.setError("Name is required!");
            editName.requestFocus();
            return;
        }
        if(BuyPrice.isEmpty()){
            editBuyPrice.setError("Purchase Price is required!");
            editBuyPrice.requestFocus ();
            return;
        }
         if(CellPrice.isEmpty()) {
             editSellPrice.setError("Cell price is required");
             editSellPrice.requestFocus();
             return;
         }

        try {
            buyPr = StoNum(BuyPrice);
        } catch (ParsingException e) {
            //System.out.println(e.getMessage());
        }

        try {
            sellPr = StoNum(CellPrice);
        } catch (ParsingException e) {
            //System.out.println(e.getMessage());
        }


        //System.out.println("tytytyty     " + UriStr);
        uniqueOfProducID = UUID.randomUUID().toString();
        Map<String, Object> dataOfProduct = new HashMap<>();
        dataOfProduct.put("id", ID);
        dataOfProduct.put("nameOfProduct", Name);
        dataOfProduct.put("URL", URL);
        dataOfProduct.put("buyPrice", buyPr);
        dataOfProduct.put("sellPrice", sellPr);
        dataOfProduct.put("quantity", quantity);
        dataOfProduct.put("minQty", minQuantity);
        dataOfProduct.put("dataOfAdding", "");
        dataOfProduct.put("bestBefore", calendarExp.getTimeInMillis());
        dataOfProduct.put("typeOfProduct", Type);
        dataOfProduct.put("description", description);

        //System.out.println("After builder new product");

        FirebaseDatabase.getInstance().getReference("products")
                .child(uniqueOfProducID)
                .setValue(dataOfProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("The product has been added with UNIQUE ID " + uniqueOfProducID);
                } else {
//                            Toast.makeText(Register.this, " Failed to register! Try again!", Toast.LENGTH_LONG).show();

                }
            }
        });


        FirebaseDatabase.getInstance()
                .getReference("products")
                .child(uniqueOfProducID)
                .child("dataOfAdding")
                .child("0")
                .setValue(calendarAdd.getTimeInMillis())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                        } else {

                        }

                    }


                });


        FirebaseDatabase.getInstance()
                .getReference("products")
                .child(uniqueOfProducID)
                .child("dataOfAdding")
                .child("1")
                .setValue(quantity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);

                        } else {

                        }

                    }


                });


    }
    private void addProduct() {
        uploadImage();
    }


}

