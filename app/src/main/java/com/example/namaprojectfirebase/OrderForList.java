package com.example.namaprojectfirebase;

public class OrderForList {

    private String timeOfOrderStatusChange,clientName,orderer,clientAddress,clientCommetns,clientPhone,status;

    public String getTimeOfOrderStatusChange() {
        return timeOfOrderStatusChange;
    }

    public void setTimeOfOrderStatusChange(String timeOfOrderStatusChange) {
        this.timeOfOrderStatusChange = timeOfOrderStatusChange;
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

    public OrderForList(String timeOfOrderStatusChange, String clientName, String orderer, String clientAddress, String clientCommetns, String clientPhone, String status) {
        System.out.println("IM IN FULL CONSTRUCTOR");
        this.timeOfOrderStatusChange = timeOfOrderStatusChange;
        this.clientName = clientName;
        this.orderer = orderer;
        this.clientAddress = clientAddress;
        this.clientCommetns = clientCommetns;
        this.clientPhone = clientPhone;
        this.status = status;

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

