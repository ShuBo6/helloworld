package com.example.a81418.myapp1.DataManager;

import android.graphics.Bitmap;

public class UserData {
    private int userId;
    private String userNick;
    private String userName;                  //用户名
    private String userPwd;
    private String userTele;
    private Bitmap userHeadImg;
    private String userClassInfo;
    private String userIdentify;
public  UserData(){}
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserTele() {
        return userTele;
    }

    public void setUserTele(String userTele) {
        this.userTele = userTele;
    }

    public Bitmap getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(Bitmap userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getUserClassInfo() {
        return userClassInfo;
    }

    public void setUserClassInfo(String userClassInfo) {
        this.userClassInfo = userClassInfo;
    }

    public String getUserIdentify() {
        return userIdentify;
    }

    public void setUserIdentify(String userIdentify) {
        this.userIdentify = userIdentify;
    }

    public UserData(int userId, String userNick, String userName, String userPwd, String userTele, Bitmap userHeadImg, String userClassInfo, String userIdentify) {
        this.userId = userId;
        this.userNick = userNick;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userTele = userTele;
        this.userHeadImg = userHeadImg;
        this.userClassInfo = userClassInfo;
        this.userIdentify = userIdentify;
    }
}
