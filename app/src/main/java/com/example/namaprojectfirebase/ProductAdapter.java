package com.example.namaprojectfirebase;


import static com.example.namaprojectfirebase.R.id.*;

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
    DatabaseReference cartDb;
    Query cartQuery;
    public CardView cardForRecycle;
    public ImageView imageArrow;
    public static ArrayList <Product> overdueProdList = null;

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
        //TODO PERMISSION SETUP
        if(Login.globalPermission != 1) {
            imageArrow.setVisibility(View.GONE);

        }
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        //System.out.println("Product before creation "+ productList.get(position).getBestBefore());
        Product product = productList.get(position);
        //System.out.println("Product xfksnfnfksdm "+ productList.get(position).getAddingDate());
        //EPOCH TO STRING
        long bestBefore = productList.get(position).getBestBefore();
        Date date = new Date(bestBefore);
        long addingDate = productList.get(position).getAddingDate();

        long epochExp = date.getTime();



        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String text = format.format(date);
        //current time
        Date currentTime = Calendar.getInstance().getTime();
        long epochCurrent = currentTime.getTime();
        
        long toOrder = bestBefore - 86400 * 3;

        if(epochCurrent >= toOrder )
        {
//
            //System.out.println("TO ORDERRRR" + toOrder);
            System.out.println("CURRENT " + epochCurrent);
            holder.expDateInList.setTextColor(Color.parseColor("#FE0100"));
            holder.expDateInList.setTypeface(holder.expDateInList.getTypeface(), Typeface.BOLD);

            //overdueProdList.add(product);
            //System.out.println("overdue name'" + product.getNameOfProduct() + "sfssfs" + overdueProdList);

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



            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int position = getAdapterPosition();
                                                Product product = productList.get(position);
                                                System.out.println("WTFFFF"  +  product.getAddingDate());

                                                String strNameOfProduct = productList.get(position).getNameOfProduct();
                                                System.out.println("Send String " + strNameOfProduct);

                                                Intent intentToEditProduct = new Intent(mCtx, editProduct.class);
                                                intentToEditProduct.putExtra("keyName", strNameOfProduct);
                                                if(Login.globalPermission == 1) {
                                                    v.getContext().startActivity(intentToEditProduct);
                                                }
                                                System.out.println("THE NAME THAT SENDED " + strNameOfProduct);
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
                                //System.out.println("The product added to cart " + HomeFragment.uniqueOfCartID);
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


