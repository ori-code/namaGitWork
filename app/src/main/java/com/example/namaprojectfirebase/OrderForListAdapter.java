package com.example.namaprojectfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class OrderForListAdapter extends RecyclerView.Adapter<OrderForListAdapter.OrderForListViewHolder> {
    private Context oCtx;
    private List<OrderForList> ordersList;

    public OrderForListAdapter(Context oCtx, List<OrderForList> ordersList) {
        this.oCtx = oCtx;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderForListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(oCtx);
        View view = inflater.inflate(R.layout.order_layout, null);
        OrderForListViewHolder holder = new OrderForListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderForListViewHolder holder, int position) {
            OrderForList order = ordersList.get(position);
//            System.out.println("Print from LIST: " + order.getId() + " CLIENT DETAILS " +order.getClientDetails() + " time of order " + order.getTimeOfOrder()+"status ");

            holder.TextViewClientDetail.setText(order.getClientAddress());
            holder.TextViewOrderStatus.setText(order.getStatus());
            holder.TextViewOrderSum.setText(order.getClientPhone());
    }

    @Override
    public int getItemCount() {
       return ordersList.size();
    }

    class OrderForListViewHolder extends RecyclerView.ViewHolder {
            TextView TextViewClientDetail, TextViewOrderStatus, TextViewOrderSum;



        public OrderForListViewHolder(@NonNull View itemView) {
            super(itemView);
            TextViewClientDetail = itemView.findViewById(R.id.textViewTitleClientDetails);
            TextViewOrderStatus = itemView.findViewById(R.id.textViewStatusOrder);
            TextViewOrderSum = itemView.findViewById(R.id.textViewTotalSum);
        }
    }
}
