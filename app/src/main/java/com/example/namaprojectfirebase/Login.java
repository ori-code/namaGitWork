package com.example.namaprojectfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Login extends AppCompatActivity {
    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn, Register,clientRegister;
    ProductAdapter adapter;
    public String users,email = "s",password;
    public static FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ProgressBar progressBar;
    public static String nameFromDB;
    public Query currentUser;
    DatabaseReference databaseReference;
    public static int globalPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signIn = (Button) findViewById(R.id.login);
        clientRegister = (Button) findViewById(R.id.clientRegisterBtn);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById (R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById (R.id.editTextTextPassword);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        currentUser = databaseReference.orderByChild("adressText");
        if(!email.equals("s")) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            databaseReference.addListenerForSingleValueEvent(valueEventListenerNew);
        }
    }





    ValueEventListener valueEventListenerNew = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            System.out.println("BEFORE RUNNNING" + FirebaseAuth.getInstance().getCurrentUser());
            if(!FirebaseAuth.getInstance().getCurrentUser().equals(null)) {
                System.out.println("AFTER RUNNNING" + FirebaseAuth.getInstance().getCurrentUser());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshotUserType : dataSnapshot.getChildren()) {
                        System.out.println("IUSERRR" + snapshotUserType.child("permission").getValue());
                        if (snapshotUserType.child("email").getValue().equals(mAuth.getCurrentUser().getEmail())) {
                            System.out.println("THE TYPE IS : " + snapshotUserType.child("permission").getValue() + "The user " + mAuth.getCurrentUser().getEmail());
                            globalPermission = Integer.parseInt(snapshotUserType.child("permission").getValue().toString());
                            System.out.println("THE permission : " + globalPermission);
                        }
                    }
//                adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    //login button
    public void loginFunc(View view) {

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        //System.out.println("The email is " + email + " and password " + password);

//        //System.out.println(mAuth.signInWithEmailAndPassword(email, password).isSuccessful());

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //redirect to the activity profile
                    //System.out.println("Yeeeeey we got in to the system with email !!!! " + mAuth.getCurrentUser().getEmail());
                    if(globalPermission == 5){
                        System.out.println("IM USERRR");
                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.putExtra("id", nameFromDB);
                        //System.out.println(nameFromDB);
//                    if(nameFromDB!=null) {
                        startActivity(i);
//                    }
                    }

                    Intent i = new Intent(Login.this, DrawerActivity.class);
                    i.putExtra("id", nameFromDB);
                    //System.out.println(nameFromDB);
//                    if(nameFromDB!=null) {
                        startActivity(i);
//                    }
                } else {
                    //System.out.println("Yeeeeey we DONT got in to the system with name because mAuth dont works" );
                    Toast.makeText(Login.this, "You need to try again to login", Toast.LENGTH_LONG).show();
                }


            }
        });


//        //pull the current user
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                nameFromDB = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("fullName").getValue(String.class);

                if(dataSnapshot.exists()){
                    //System.out.println("Data snap shoot work" );
                    //System.out.println("Password is " +  nameFromDB);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //System.out.println("IM in LOGIN");


    }

    public void registerFunc(View view) {
        Intent i = new Intent(Login.this, Register.class);
        i.putExtra("typeFiveUser", 5);
        startActivity(i);
    }
//    public void RegisterFunc (View view){

//    }



}

