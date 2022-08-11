package com.example.namaprojectfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {


    Context context;

    ArrayList<User> userList;
    public CardView cardForUsers;


    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.userList = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = userList.get(position);

        holder.fullName.setText(user.getFullName());
        holder.idNum.setText(user.getLicenseNum());
        holder.email.setText(user.getEmail());
        holder.telNum.setText(user.getPhoneNum());
        holder.salary.setText(user.getSalary());
        holder.address.setText(user.getAddress());
        holder.position.setText(user.getPermission());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fullName, email, address, position, telNum, idNum, salary;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            fullName = itemView.findViewById(R.id.theFullName);
            email = itemView.findViewById(R.id.theEmail);
            address = itemView.findViewById(R.id.theAddress);
            position = itemView.findViewById(R.id.thePermission);
            telNum = itemView.findViewById(R.id.thePhoneNum);
            idNum = itemView.findViewById(R.id.theID);
            salary = itemView.findViewById(R.id.theSalary);
            //cardForUsers = itemView.findViewById(R.id.myUserList);


        }
    }

}
