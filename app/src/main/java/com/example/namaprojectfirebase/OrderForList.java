package com.example.namaprojectfirebase;


public class OrderForList {


    public String getTimeOfPlacedOrder() {
        return timeOfPlacedOrder;
    }

    public void setTimeOfPlacedOrder(String timeOfPlacedOrder) {
        this.timeOfPlacedOrder = timeOfPlacedOrder;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOrderer() {
        return orderer;
    }

    public void setOrderer(String orderer) {
        this.orderer = orderer;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientCommetns() {
        return clientCommetns;
    }

    public void setClientCommetns(String clientCommetns) {
        this.clientCommetns = clientCommetns;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdOfOrder() {
        return idOfOrder;
    }

    public void setIdOfOrder(String idOfOrder) {
        this.idOfOrder = idOfOrder;
    }

    public int getNumOfOrder() {
        return numOfOrder;
    }

    public void setNumOfOrder(int numOfOrder) {
        this.numOfOrder = numOfOrder;
    }

    public Long getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Long deliveryType) {
        this.deliveryType = deliveryType;
    }

    public OrderForList(String timeOfPlacedOrder, String clientName, String orderer, String clientAddress, String clientCommetns, String clientPhone, String status, String theShipper,String idOfOrder, int numOfOrder, Long deliveryType) {
        this.timeOfPlacedOrder = timeOfPlacedOrder;
        this.clientName = clientName;
        this.orderer = orderer;
        this.clientAddress = clientAddress;
        this.clientCommetns = clientCommetns;
        this.clientPhone = clientPhone;
        this.status = status;
        this.idOfOrder = idOfOrder;
        this.numOfOrder = numOfOrder;
        this.deliveryType = deliveryType;
        this.theShipper = theShipper;
    }

    private String timeOfPlacedOrder, clientName,orderer,clientAddress,clientCommetns,clientPhone,status, idOfOrder, theShipper;
    private int numOfOrder;
    private Long deliveryType;


    public String getTheShipper() {
        return theShipper;
    }

    public void setTheShipper(String theShipper) {
        this.theShipper = theShipper;
    }

    public OrderForList() {

    }

//    public int getId() {
//        return id;
//    }
//
//    public String getClientDetails() {
//        return clientDetails;
//    }
//
//    public String getStatusOfOrder() {
//        return statusOfOrder;
//    }


}

