package com.example.namaprojectfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FinalCheck extends Activity {
    public static ArrayList<Product> finalCheckList;
    DatabaseReference finalCheckSnapShot;
    TextView nameOfProductInTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_order);

        finalCheckSnapShot = FirebaseDatabase.getInstance().getReference("products");
        finalCheckSnapShot.addListenerForSingleValueEvent(valueEventListener);

//        nameOfProductInTotal = findViewById(R.id.nameOfProductInTotal);


    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            finalCheckList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("FINAL CHECK " + snapshot.child("nameOfProduct").getValue());
//                    nameOfProductInTotal.setText("HEYY");
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}