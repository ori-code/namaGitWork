package com.example.namaprojectfirebase;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRecycleViewClass extends AppCompatActivity {
        RecyclerView recyclerViewUsers;
        UserAdapter adapter2;
        List <User> userList;
        public static DatabaseReference dataSnapshotUsers;
        public CheckBox adminCheckBox, workerCheckBox, courierCheckBox, accountantCheckBox, userClientCheckBox;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        dataSnapshotUsers = FirebaseDatabase.getInstance().getReference().child("users");
        dataSnapshotUsers.addListenerForSingleValueEvent(valueEventListenerNewUsers);

        userList = new ArrayList<>();
        recyclerViewUsers = (RecyclerView) findViewById(R.id.usersRecycleView);
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        EditText editTextSearch = findViewById(R.id.editTextSearchUser);


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterUserList(s.toString());
            }

        });


        adapter2 = new UserAdapter(this,userList);
        recyclerViewUsers.setAdapter(adapter2);


    }

    private void filterUserList (String text){
        ArrayList <User> filteredUserList = new ArrayList<>();
        adminCheckBox = (CheckBox)findViewById(R.id.adminCheckBox);
        workerCheckBox = (CheckBox)findViewById(R.id.workerCheckBox);
        courierCheckBox = (CheckBox)findViewById(R.id.courierCheckBox);
        accountantCheckBox = (CheckBox)findViewById(R.id.accountantCheckBox);
        userClientCheckBox = (CheckBox)findViewById(R.id.userClientCheckBox);
            ////System.out.println("Admin");
            ////System.out.println("Worker");
            ////System.out.println("Courier");
            ////System.out.println("Accountant");

        for(User item : userList ){
            ////System.out.println("BEFORE FILTER");
            if(item.getFullName().toLowerCase().contains(text.toLowerCase())){
                //System.out.println("PRODUCT FILTER TYPE IS " + item.getPermission());
                //TODO type of Product ASK VERONIKA
                if(adminCheckBox.isChecked() && item.getPermission()==1)
                    filteredUserList.add(item);
                if(workerCheckBox.isChecked() && item.getPermission()==2)
                    filteredUserList.add(item);
                if(courierCheckBox.isChecked() && item.getPermission()==3)
                    filteredUserList.add(item);
                if(accountantCheckBox.isChecked() && item.getPermission()==4)
                    filteredUserList.add(item);
                if(userClientCheckBox.isChecked() && item.getPermission()==5)
                    filteredUserList.add(item);

            }
        }
        adapter2.filteredListUsers(filteredUserList);
    }


    ValueEventListener valueEventListenerNewUsers = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //System.out.println("USERSSSS " +snapshot.getValue());
                    User user = snapshot.getValue(User.class);

                    userList.add(user);
//                    //System.out.println("USERS EMAIL" + user.getEmail());

                }
                adapter2.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };




}


