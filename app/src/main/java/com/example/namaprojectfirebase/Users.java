package com.example.namaprojectfirebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Users extends AppCompatActivity {


    ArrayList<User> list;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_recycle);
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        userAdapter = new UserAdapter(this, list);
        recyclerView.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(valueEventListener);



        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    System.out.println("The siiiiss" + snapshot.getValue());
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            list.clear();
            if(snapshot.exists()){
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    System.out.println("user from DB" +snapshot1.child("fullName").getValue());
                    User user = snapshot1.getValue(User.class);
                    System.out.println("trtrtrtrtrtrtr"+user.getAddress());
                    list.add(user);
                    System.out.println("ytytytty       "+list.toArray());
                }
                userAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}
