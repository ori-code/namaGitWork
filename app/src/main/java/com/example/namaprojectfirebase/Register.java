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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    public TextView banner, registeruser,loginBtn;

    private EditText editTextFullName, licenseNum, editTextEmail, editTextPassword,
                    phoneNum, editAddress, editSalary;
//    private ProgressBar progressBar;
    public static int permission,salary;
    public static Bundle extrasFromRegister;

    public RadioGroup buttonsGroupJava;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registeruser = (Button) findViewById(R.id.registerUser);
        registeruser.setOnClickListener(this);

        extrasFromRegister = getIntent().getExtras();

        if(extrasFromRegister!=null){
            int newInt;
            newInt = extrasFromRegister.getInt("typeFiveUser");

            permission = newInt;
            System.out.println(newInt + "PERMISSION");
            System.out.println(permission + "THE REAL PERMISSION");

        }
        editTextFullName = (EditText) findViewById(R.id.fulLName);
        licenseNum = (EditText) findViewById(R.id.licenseNum);
        editTextEmail = (EditText) findViewById(R.id.email);
        phoneNum = (EditText) findViewById(R.id.phoneBtn);
        editAddress = (EditText) findViewById(R.id.adressBtn);
        editSalary = (EditText) findViewById(R.id.salaryBtn);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonsGroupJava = (RadioGroup) findViewById(R.id.buttonsGroup);
        mAuth = FirebaseAuth.getInstance();

        if(permission == 5){
            editSalary.setVisibility(View.GONE);
            buttonsGroupJava.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                //System.out.println("Register blah");
                registeruser();
                break;
            case R.id.loginButton:
                startActivity(new Intent(this, Login.class));
                break;

        }
    }

    public void onRadioButtonClickedAdmin(View view) {
        permission = 1;
        System.out.println("Admin");
    }
    public void onRadioButtonClickedWorker(View view) {
        permission = 2;
        System.out.println("Worker");
    }
    public void onRadioButtonClickedCourier(View view) {
        permission = 3;
        System.out.println("Courier");
    }
    public void onRadioButtonClickedAccountant(View view) {
        permission = 4;
        System.out.println("Accountant");
    }

    private void registeruser() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String license = licenseNum.getText().toString().trim();
        String phone = phoneNum.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        if(permission==5){
            salary = 0;
        }
        else
        {
            String salaryStr = editSalary.getText().toString().trim();
            salary = Integer.parseInt(salaryStr);
            System.out.println("kfjnikdfmmsmdwsf   " + salary);
        }





        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if (license.isEmpty()) {
            licenseNum.setError("Age is required!");
            licenseNum.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus ();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
//

//        if(permission > 0 && permission <5) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            System.out.println("After task new user, the permission is   " + permission);

                            if (task.isSuccessful()) {
                                User user = new User(fullName, license, email,phone, address, salary, permission);
                                System.out.println("After builder new user" + permission);
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, " User has been registered", Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(Register.this, " failed", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }
                            else{
                                Toast.makeText(Register.this, " Failed to register! This email is already exist", Toast.LENGTH_LONG).show();
                            }
                        }

                    });
        }
//        else
//            Toast.makeText(this, "You need to select the permission", Toast.LENGTH_LONG).show();

//        }


    public void tologinpage() {

    }



}