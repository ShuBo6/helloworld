package com.example.a81418.myapp1.DataManager;

import android.graphics.Bitmap;

import java.sql.Timestamp;

public class OrderBean {
    private int id;
    private int OrderState;
    private Bitmap OrderImg;
    private String OrderBeiZhu;
    private String OrderDidian;
    private Timestamp OrderTime;
    private String OrderUserIdentify;
    private String OrderUserName;

    public OrderBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderState() {
        return OrderState;
    }

    public void setOrderState(int orderState) {
        OrderState = orderState;
    }

    public Bitmap getOrderImg() {
        return OrderImg;
    }

    public void setOrderImg(Bitmap orderImg) {
        OrderImg = orderImg;
    }

    public String getOrderBeiZhu() {
        return OrderBeiZhu;
    }

    public void setOrderBeiZhu(String orderBeiZhu) {
        OrderBeiZhu = orderBeiZhu;
    }

    public String getOrderDidian() {
        return OrderDidian;
    }

    public void setOrderDidian(String orderDidian) {
        OrderDidian = orderDidian;
    }

    public Timestamp getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        OrderTime = orderTime;
    }

    public String getOrderUserIdentify() {
        return OrderUserIdentify;
    }

    public void setOrderUserIdentify(String orderUserIdentify) {
        OrderUserIdentify = orderUserIdentify;
    }

    public String getOrderUserName() {
        return OrderUserName;
    }

    public void setOrderUserName(String orderUserName) {
        OrderUserName = orderUserName;
    }
}
