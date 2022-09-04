package com.example.namaprojectfirebase;

import android.widget.ImageView;

public class Product {

    public String id, nameOfProduct, description;
    public int type;
    public double buyPrice, sellPrice, quantity, minQty;
    public long bestBeforeList;
    public long addingDateList [] ;
//     = new long[1000]
    public String URL;
    public String imageID;
    ImageView imageView;


//    public Product(String id, String nameOfProduct, String description, int type, double buyPrice, double sellPrice, long addingDate, long bestBefore) {
//        this.id = id;
//        this.nameOfProduct = nameOfProduct;
//        this.description = description;
//        this.type = type;
//        this.buyPrice = buyPrice;
//        this.sellPrice = sellPrice;
//        AddingDate = addingDate;
//        BestBefore = bestBefore;
//    }

//    public Product(String id, String nameOfProduct, String description, String imageUrl, int type, double buyPrice, double sellPrice, long addingDate, long bestBefore) {
//
//    }
//
//    public Product (String bestBefore, int buyPrice, String dataOfAdding, String description, String id, String nameOfProduct, double sellPrice, int typeOfProduct){
//
//    }


    public void setAddingDate(long[] addingDate) {

        ////System.out.println(" IN SEETTER " + addingDate);
        addingDateList = addingDate;
        for(int i = 0; i < addingDate.length; i ++ ){
            ////System.out.println( addingDate[i]);
        }
    }

    public void setBestBefore(long bestBefore) {
        bestBeforeList = bestBefore;
    }

    public void setQuantity(double quantity) {
        ////System.out.println("I SET QUANTITY");
        this.quantity = quantity;
    }

    public void setType(int type) {
        ////System.out.println("I SET TYPE ");
        this.type = type;
    }

    public void setMinQty ( double minQty) {
        this.minQty = minQty;
    }

    public String getNameOfProduct() {
        return nameOfProduct;
    }

    public String getDescription() {
        return description;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() { return sellPrice; }

    public double getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return URL;
    }

    public long[] getAddingDate() {
        ////System.out.println(" IM IN ADDING DATE GETTT " + addingDateList[0]);
        return addingDateList; }

    public long getBestBefore() { return bestBeforeList; }

    public String getImageID() { return imageID; }

    public int getType() {return type;}

    /*   public Product (String nameOfProduct, String description, double buyPrice, double quantity, String imageUrl){
        this.nameOfProduct = nameOfProduct;
        this.description = description;
        this.buyPrice = buyPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;

    }*/

    public Product (){

    }


    ///THE STRUCTURE

//
//    public TextView addProduct, DateAdding, BestBefore, getProduct;
//    private EditText editID, editName, editBuyPrice, editCellPrice, editDescription;
//    private DatePickerDialog.OnDateSetListener AddingDateListener, BestBeforeListener;
//    public int Type;
//    public static String uniqueOfProducID;
//    public double buyPr, cellPr;

//    public Product (double sellPrice, String description, String id, String nameOfProduct ){
//        this.sellPrice = sellPrice;
//        this.description = description;
//        this.id = id;
//        this.nameOfProduct = nameOfProduct;
//    }


}


