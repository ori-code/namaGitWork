package com.example.namaprojectfirebase.ui.home;

import static com.example.namaprojectfirebase.Login.mAuth;
//import static com.example.namaprojectfirebase.MainActivity.globalPermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.namaprojectfirebase.AddProduct;
import com.example.namaprojectfirebase.Cart;
import com.example.namaprojectfirebase.Login;
import com.example.namaprojectfirebase.MainActivity;
import com.example.namaprojectfirebase.ProductAdapter;
import com.example.namaprojectfirebase.R;
import com.example.namaprojectfirebase.Register;
import com.example.namaprojectfirebase.Users;
import com.example.namaprojectfirebase.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeFragment<puiblic> extends Fragment {
    final String chief = "chief@nama.com";
    private FragmentHomeBinding binding;
    public TextView activeUserNameHomeFragment;
    public ImageButton btnPLus,btnTable, btnAdd,  ordrButton, userControl, grafBtn, overdueBtn,ordrButton2;
    public TextView iRemember;
/*    public EditText iForget;
    public Button rememberMe, rememberYou;*/
    public Switch switch1;
    public static String uniqueOfCartID;
    public String text;
    private boolean switchOnOff;
    DatabaseReference dataSnapshot;

    public String nameFromDB;
/*    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWITCH1 = "switch1";*/
    public static int cartFlag = 0;

    DatabaseReference dbCarts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dbCarts = FirebaseDatabase.getInstance().getReference().child("carts");




        if(dbCarts != null) {
            dbCarts = FirebaseDatabase.getInstance().getReference().child("carts");
            dbCarts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    String ma = datasnapshot.getValue().toString();
                    //System.out.println("THE ALL DATA OF CARTS" + ma);
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        String ma1 = snapshot.child("currentUserEmail").getValue().toString();
                        String ma2 = snapshot.child("orderPlaced").getValue().toString();
                        //System.out.println(ma1);
                        //System.out.println(ma2);

///                        NEW TRY
                        if (ma1.equals(mAuth.getCurrentUser().getEmail())) {
                            //System.out.println("IT IS THE SAME USER");
                            if(ma2.equals("0")){
                                //System.out.println("OPEN EXIST CARD");
                                uniqueOfCartID = snapshot.getKey();
                                cartFlag = 1;
                                break;
                            }
                        }
                    }

                    if(cartFlag==0){
                        //System.out.println("Creating CART with FUNC");
                        uniqueOfCartID = UUID.randomUUID().toString();
                        createCartFunc(mAuth.getCurrentUser().getEmail());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
//            uniqueOfCartID = UUID.randomUUID().toString();
        }

        //System.out.println("DB CARTS"  + dbCarts);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        final String admin = "f@f.com";

        final String currentUser = mAuth.getCurrentUser().getEmail();
        //System.out.println("THE USER IS " + currentUser);


        View root = binding.getRoot();
        //System.out.println("Home Fragment name " + Login.nameFromDB);
        btnTable = (ImageButton) root.findViewById(R.id.tableButton);
        btnPLus = (ImageButton) root.findViewById(R.id.plusButton);
        btnAdd = (ImageButton) root.findViewById(R.id.addUser);
        ordrButton = (ImageButton) root.findViewById(R.id.orderButton);
        ordrButton2 = (ImageButton) root.findViewById(R.id.orderButton2);

        userControl = (ImageButton) root.findViewById(R.id.controlUser);
        grafBtn = (ImageButton) root.findViewById(R.id.grafBtn);
        overdueBtn = (ImageButton) root.findViewById(R.id.overdueBtn);



        System.out.println("global permission" + Login.globalPermission);
        if (Login.globalPermission == 2) {
            //general worker
            btnAdd.setVisibility(View.INVISIBLE);
            userControl.setVisibility(View.INVISIBLE);
            grafBtn.setVisibility(View.INVISIBLE);
        }

        if (Login.globalPermission == 3) {
            //deliveryman
            System.out.println("Welcome deliveryman");
            btnAdd.setVisibility(View.INVISIBLE);
            userControl.setVisibility(View.INVISIBLE);
            grafBtn.setVisibility(View.INVISIBLE);
            btnPLus.setVisibility(View.INVISIBLE);
            grafBtn.setVisibility(View.INVISIBLE);
            overdueBtn.setVisibility(View.INVISIBLE);
            ordrButton.setVisibility(View.INVISIBLE);
        }

        if (Login.globalPermission == 4) {
            //accountant
            btnAdd.setVisibility(View.INVISIBLE);
            btnPLus.setVisibility(View.INVISIBLE);
            overdueBtn.setVisibility(View.INVISIBLE);
            ordrButton.setVisibility(View.INVISIBLE);
        }
        if(Login.globalPermission == 5) {
            //client

            btnPLus.setVisibility(View.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            userControl.setVisibility(View.INVISIBLE);
            grafBtn.setVisibility(View.INVISIBLE);
            overdueBtn.setVisibility(View.INVISIBLE);
            ordrButton2.setVisibility(View.INVISIBLE);
        }


       /* if (globalPermission != 1 && currentUser.matches(chief)){
            btnAdd.setVisibility(View.INVISIBLE);
            userControl.setVisibility(View.INVISIBLE);

        }
        if (!currentUser.matches(admin) && !currentUser.matches(chief)){
            btnPLus.setVisibility(View.INVISIBLE);

        }*/


        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //System.out.println("Going to Register");
                Intent i = new Intent(getActivity(), Register.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });
        ordrButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //System.out.println("Going to Cart");
                Intent i = new Intent(getActivity(), Cart.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        btnPLus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //System.out.println("Going to new Product");
                Intent i = new Intent(getActivity(), AddProduct.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        btnTable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //System.out.println("Going to Table");
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });
        userControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Users.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0,0);

            }
        });
//        activeUserNameHomeFragment = root.findViewById(R.id.activeUserNameHomeFragment);
//        activeUserNameHomeFragment.setText(Login.nameFromDB);



        return root;
    }


/*
    public void rememberData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, iRemember.getText().toString());
        editor.putBoolean(SWITCH1, switch1.isChecked());
        editor.apply();
    }
    public void uploadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);

    }
*/

    public void updateViews(){
        iRemember.setText(text);
        switch1.setChecked(switchOnOff);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void createCartFunc (String currentUser){
        Map<String, Object> dataOfCart = new HashMap<>();
        dataOfCart.put("currentUserEmail", currentUser);
        dataOfCart.put("orderPlaced", 0);
        FirebaseDatabase.getInstance().getReference("carts")
                .child(HomeFragment.uniqueOfCartID)
                .setValue(dataOfCart).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //System.out.println("The cart has been added " + HomeFragment.uniqueOfCartID);
                } else {

                }
            }
        });
    }

    public static void createCartFuncUnique (String currentUser){
        Map<String, Object> dataOfCart = new HashMap<>();
        dataOfCart.put("currentUserEmail", currentUser);
        dataOfCart.put("orderPlaced", 0);
        uniqueOfCartID = UUID.randomUUID().toString();
        FirebaseDatabase.getInstance().getReference("carts")
                .child(HomeFragment.uniqueOfCartID)
                .setValue(dataOfCart).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //System.out.println("The cart has been added " + HomeFragment.uniqueOfCartID);
                        } else {

                        }
                    }
                });
    }
}