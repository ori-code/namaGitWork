package com.example.namaprojectfirebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private Context uCtx;
    private List<User> userList;
    public static String theDeleteUserName;


    public UserAdapter(Context uCtx, List<User> userList) {
        this.uCtx = uCtx;
        this.userList = userList;
    }


    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewInUserListType,nameOfUserInList,textViewShortDescInUserList;
        public ImageView userPermisionImageInlist;
        public ImageButton deleteUserBtn;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInUserListType = itemView.findViewById(R.id.textViewInUserListType);
            nameOfUserInList = itemView.findViewById(R.id.nameOfUserInList);
            textViewShortDescInUserList = itemView.findViewById(R.id.textViewShortDescInUserList);
            userPermisionImageInlist = itemView.findViewById(R.id.userPermisionImageInlist);
            deleteUserBtn = itemView.findViewById(R.id.deleteUserBtn);





//            itemView.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                int position = getAdapterPosition();
//                                                Product product = productList.get(position);
//
//
//                                                String strNameOfProduct = productList.get(position).getNameOfProduct();
//                                                System.out.println("Send String " + strNameOfProduct);
//
//                                                Intent intentToEditProduct = new Intent(mCtx, editProduct.class);
//                                                intentToEditProduct.putExtra("keyName", strNameOfProduct);
//                                                if(Login.globalPermission == 1) {
//                                                    v.getContext().startActivity(intentToEditProduct);
//                                                }
//                                                System.out.println("THE NAME THAT SENDED " + strNameOfProduct);
//                                            }
//                                        }
//
//
//            );


            deleteUserBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int position = getAdapterPosition();
                                                User user = userList.get(position);
                                                System.out.println(user.getFullName() + "YJO");
//                                                theDeleteUser = user.getEmail();
//                                                deleteUserFunc(theDeleteUser);
                                            }
                                        }
            );
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(uCtx);
        View view = inflater.inflate(R.layout.one_user_layout, null);
        UserViewHolder holder = new UserViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewInUserListType.setText(user.getLicenseNum());
        holder.nameOfUserInList.setText(user.getFullName());
        if(user.getPermission()==1){
            holder.userPermisionImageInlist.setImageResource(R.drawable.accountant);
        }
        if(user.getPermission()==2){
            holder.userPermisionImageInlist.setImageResource(R.drawable.admin);
        }
        if(user.getPermission()==3){
            holder.userPermisionImageInlist.setImageResource(R.drawable.userclient);
        }
        if(user.getPermission()==4){
            holder.userPermisionImageInlist.setImageResource(R.drawable.deliveryman);
        }
        if(user.getPermission()==5){
            holder.userPermisionImageInlist.setImageResource(R.drawable.deliveryman);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void filteredListUsers (ArrayList<User> filteredList){
        userList = filteredList;
        notifyDataSetChanged();
    }

    public void deleteUserFunc(String emailOfUser){
        System.out.println("THE EMAIL IS " +emailOfUser );
//        UserRecycleViewClass.dataSnapshotUsers.addListenerForSingleValueEvent(valueEventListenerDeleteUser);

//       System.out.println("From  EMAIL DB " +UserRecycleViewClass.dataSnapshotUsers.child("email").child(emailOfUser).getKey());
//       deleteAccount();

    }

//    private void deleteAccount() {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    System.out.println("DELETED");//
//
//                } else {
//
//                }
//            }
//        });
//    }


//    ValueEventListener valueEventListenerDeleteUser = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            userList.clear();
//            if (dataSnapshot.exists()) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    System.out.println("USERSSSSIN ADAPTER " +snapshot.getValue());
//                    if(snapshot.getValue().equals(theDeleteUser)){
//                        System.out.println("FOUNDED!!!!");
//
//                    }
////                    User user = snapshot.getValue(User.class);
//
////                    System.out.println("USERS EMAIL" + user.getEmail());
//
//                }
////                adapter2.notifyDataSetChanged();
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };


    }
