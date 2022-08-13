package com.example.namaprojectfirebase;


public class OrderForList {

    private String TimeofPlacedOrder, clientName,orderer,clientAddress,clientCommetns,clientPhone,status;
    private Long deliveryType;

    public String getTimeOfOrderStatusChange() {
        System.out.println("timeOfOrderStatusChange " + TimeofPlacedOrder);
        return TimeofPlacedOrder;
    }

    public void setTimeOfOrderStatusChange(String TimeofPlacedOrder) {
        this.TimeofPlacedOrder = TimeofPlacedOrder;
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

    public Long getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Long deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public OrderForList (String TimeofPlacedOrder, String clientName, String orderer, String clientAddress, String clientCommetns, String clientPhone, long deliveryType, String status) {
        System.out.println("IM IN FULL CONSTRUCTOR");
        this.TimeofPlacedOrder = TimeofPlacedOrder;
        this.clientName = clientName;
        this.orderer = orderer;
        this.clientAddress = clientAddress;
        this.clientCommetns = clientCommetns;
        this.clientPhone = clientPhone;
        this.deliveryType = deliveryType;
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

