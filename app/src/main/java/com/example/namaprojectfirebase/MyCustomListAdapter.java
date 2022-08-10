package com.example.namaprojectfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyCustomListAdapter extends ArrayAdapter <Product> {

    Context mCtx;
    int resource;
    List<Product> productList;

    public MyCustomListAdapter(@NonNull Context mCtx, int resource, @NonNull List<Product> productList) {
        super(mCtx, resource, productList);
        this.mCtx = mCtx;
        this.resource = resource;
        this.productList = productList;
    }
}
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
////        return super.getView(position, convertView, parent);
//        LayoutInflater inflater = LayoutInflater.from(mCtx);
//        View view = inflater.inflate(R.layout.my_list_item, null);
//
//
//        TextView nameOfProduct = view.findViewById(R.id.nameOfProductCart);
//
////        TextView categoryOfProduct = view.findViewById(R.id.categoryOfProduct);
////        ImageView productImage = view.findViewById(R.id.imageProduct);
//
//
//        Product product = productList.get(position);
////        nameOfProduct.setText(product.getName());
////        imageOfProduct.setImageResource();
//
//        //System.out.println("name of product " + nameOfProduct);
////        categoryOfProduct.setText(product.getType());
////        productImage.setImageDrawable(mCtx.getResources().getDrawable(product.getImage), null);
//
////        view.findViewById(R.id.addToCard).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                //System.out.println("ADD TO CARD !!!");
////            }
////        });
//        return view;
//        }
//    }