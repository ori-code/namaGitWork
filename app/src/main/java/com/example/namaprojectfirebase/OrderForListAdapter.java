package com.example.namaprojectfirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull OrderForListViewHolder holder, int position) {
        OrderForList order = ordersList.get(position);
        String emailOfStatus = new String();
        if(order.getTheShipper().equals("default")){
            //System.out.println("the order only placed by orderer " + order.getOrderer());
            emailOfStatus = order.getOrderer();
        }
        else{
            //System.out.println("the order status changed and now is " + order.getStatus() + " shipper "  + order.getTheShipper());
            emailOfStatus =  order.getTheShipper();
        }
        //System.out.println(order.getTheShipper() + " FROM LIST SHOWWWW");
        holder.TextViewClientDetail.setText("The client name: " + order.getClientName() + " \nAdress: " + order.getClientAddress() + "\nPhone: " + order.getClientPhone());
        String status = new String();
        String orderType = new String();
        if (order.getStatus().equals("1")) {
            status = "Order Placed";
            holder.TextViewOrderStatus.setTextColor(Color.parseColor("#FE0100"));
            //////System.out.println("Order Placed");
        }
        if (order.getStatus().equals("2")) {
            status = "Ready for Shipment";
            holder.TextViewOrderStatus.setTextColor(Color.parseColor("#007AFF"));
            ////System.out.println("Order Shipped");
        }
        if (order.getStatus().equals("3")) {
            status = "Order Shipped";
            ////System.out.println("Order Received");
            holder.TextViewOrderStatus.setTextColor(Color.parseColor("#007F00"));

        }
//        String epochDate = order.getTimeOfPlacedOrder();
//        long num = Long.parseLong(epochDate);
//
        String epochString = order.getTimeOfPlacedOrder();
        ////System.out.println("The time " + epochString);
        long epoch = Long.parseLong(epochString);
        Date expiry = new Date(epoch * 1000);
        ////System.out.println("The expiry  " + expiry);


        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.US);

        String time = df.format(expiry);

        if(order.getDeliveryType() == 1){

            orderType = "Delivery man";
        }
        if(order.getDeliveryType() == 2){

            orderType = "Drone";
        }
        if(order.getDeliveryType() == 3){

            orderType = "Pickup";
        }

        //dd-M-yyyy hh:mm:ss


             ////System.out.println("The time " + order.getTimeOfPlacedOrder());

            holder.TextViewOrderStatus.setText(status + " at "  + time + " by " + emailOfStatus);
            holder.TextViewOrderSum.setText("Shipment set by : " + orderType);
            holder.NumberOfOrder.setText("Number of Order : " + order.getNumOfOrder());


    }





    @Override
    public int getItemCount() {
       return ordersList.size();
    }

    class OrderForListViewHolder extends RecyclerView.ViewHolder {
            TextView TextViewClientDetail, TextViewOrderStatus, TextViewOrderSum,NumberOfOrder;



        public OrderForListViewHolder(@NonNull View itemView) {
            super(itemView);
            TextViewClientDetail = itemView.findViewById(R.id.textViewTitleClientDetails);
            TextViewOrderStatus = itemView.findViewById(R.id.textViewStatusOrder);
            TextViewOrderSum = itemView.findViewById(R.id.textViewTotalSum);
            NumberOfOrder = itemView.findViewById(R.id.numberOfOrder);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                     ////System.out.println("IM CLICKABLE " + ordersList.get(position).getIdOfOrder());


                     String orderForThisCard = new String();
                    orderForThisCard= ordersList.get(position).getIdOfOrder();

                    int numberOfOrder,typeOfShipment;
                    numberOfOrder= ordersList.get(position).getNumOfOrder();
                    typeOfShipment= Math.toIntExact(ordersList.get(position).getDeliveryType());
//
                    Intent intentToEditProduct = new Intent(oCtx, FinalCheck.class);
                        intentToEditProduct.putExtra("orderForThisCardInList",orderForThisCard );
                         intentToEditProduct.putExtra("orderNumber",numberOfOrder );
                         intentToEditProduct.putExtra("shipmentType", typeOfShipment);
                    v.getContext().startActivity(intentToEditProduct);


//                    if (Login.globalPermission == 1) {
//                        v.getContext().startActivity(intentToEditProduct);
//                    }

                    ////System.out.println("THE ID THAT SENDED " + orderForThisCard);
                }


            }
            );

        }
    }
}
