package com.example.a81418.myapp1.DataManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {


    public Connection getCon() {
        Connection con=null;
        String driver = "com.mysql.jdbc.Driver";//MySQL 驱动
        String url = "jdbc:mysql://118.24.10.164:3306/wangkehan";//MYSQL数据库连接Url
        String user = "root";//用户名
        String password = "w@ng123";//密码
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
              con =    DriverManager.getConnection(url,user,password);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return con;
    }



//    public Connection getCon() {
//        return con;
//    }
//
//    public void setCon(Connection con) {
//        this.con = con;
//    }

    public boolean insertUserData(String username,String Paswd,String Identify) {
        PreparedStatement psm=null;
        Connection cn=getCon();

        boolean flag=false;

        String sql="insert into user (user_name,user_paswd,user_identify) values (?,?,?)";

        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1,username);
            psm.setString(2,Paswd);
            psm.setString(3,Identify);
            flag=psm.executeUpdate()>0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }



        return flag;

    }
    //更新用户信息，如修改密码
    public boolean updateUserData(String username,String newpaswd) {
        PreparedStatement psm=null;
        Connection cn=getCon();
        boolean flag=false;
        String sql="update user set user_paswd=? where user_name=?";

        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1,newpaswd);
            psm.setString(2,username);
            flag=psm.executeUpdate()>0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }

        return flag;
    }
    //
//    public boolean deleteUserDatabyname(String name) {
//        PreparedStatement psm=null;
//
//        boolean flag=false;
//        String sql="delete from user where user_name=?";
//
//        try {
//            psm=con.prepareStatement(sql);
//            psm.setString(1, name);
//            flag=psm.executeUpdate()>0;
//            con.close();
//            psm.close();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//
//
//        return flag;
//    }
    //
    public int findUserByName(String userName) {
        PreparedStatement psm=null;
        ResultSet rs = null;
        Connection cn=getCon();
        int result=0;
        String sql="select * from user where user_name =?";
        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1,userName);

            rs=psm.executeQuery();
            while(rs.next()) {
                //String p=rs.getString("user_paswd");
                result++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
        }

        return result;
    }

    //根据用户名、密码和身份找用户，用于登录
    public boolean findUserByNameAndPwdAndIdentify(String userName,String pwd,String id){
        PreparedStatement psm=null;
        ResultSet rs = null;
        Connection cn=getCon();
        boolean flag=false;
        String sql="select * from user where user_name = ? ";
        String p=null;
        String identify1=null;
        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1, userName);
            rs=psm.executeQuery();
            if(rs!=null) {
                while(rs.next())
                {
                    p=rs.getString("user_paswd");
                    identify1=rs.getString("user_identify");

                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
        }

        if(pwd.equals(p)&&id.equals(identify1)) {
            flag=true;
        }

        return flag;
    }
    public boolean findUserByNameAndPwd(String userName,String pwd){
        PreparedStatement psm=null;
        ResultSet rs = null;
        Connection cn=getCon();
       // con=getCon();
        boolean flag=false;
        String sql="select * from user where user_name = ? ";
        String p=null;
        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1, userName);
            rs=psm.executeQuery();
            if(rs!=null) {
                while(rs.next())
                {
                    p=rs.getString("user_paswd");
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
        }
        if(pwd.equals(p)) {
            flag=true;
        }
        return flag;
    }
    public boolean SetUserImg(String username, Bitmap bmp) {
        PreparedStatement psm=null;
        Connection cn=getCon();
        boolean flag=false;
        String sql="update user set user_headimg= ? where user_name= ? ";
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        InputStream in=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            psm=cn.prepareStatement(sql);
            try {
                psm.setBinaryStream(1,in,in.available());
            } catch (IOException e) {
                e.printStackTrace();
            }
            psm.setString(2,username);
            flag=psm.executeUpdate()>0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){   try {cn.close();   } catch (SQLException e) { e.printStackTrace(); } }
        }
        return flag;
    }
//    public Bitmap getUserImg(String username){
//        Bitmap bmp = null;
//        PreparedStatement psm=null;
//        ResultSet rs=null;
//        Connection cn =getCon();
//
//        String sql="select * from user where user_name= ?";
//        try {
//            psm=cn.prepareStatement(sql);
//            psm.setString(1,username);
//            rs=psm.executeQuery();
//            while (rs.next()){
//                InputStream in=rs.getBinaryStream("user_headimg");
//                bmp = BitmapFactory.decodeStream(in);
//            }
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }finally {
//            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
//            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
//            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
//        }
//        return bmp;
//    }
    public boolean UpdateUserInfo(String usernick,String userTelenum,String userclassinfo,String username){
       PreparedStatement psm=null;
       boolean flag=false;
       Connection cn=getCon();
       String sql="update user set user_nick=? ,user_telenum=? ,user_class_info=? where user_name= ? ";
        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1,usernick);
            psm.setString(2,userTelenum);
            psm.setString(3,userclassinfo);
            psm.setString(4,username);
            flag=psm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return flag;
    }
    public UserData InitUserData (String username){
        UserData user=new UserData();
        user.setUserName(username);
        PreparedStatement psm=null;
        ResultSet rs=null;
        Connection cn=getCon();
        String sql="select * from user where user_name= ?";
        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1,username);
            rs=psm.executeQuery();
            while (rs.next()){
                InputStream in=rs.getBinaryStream("user_headimg");
                user.setUserHeadImg(BitmapFactory.decodeStream(in));
                user.setUserIdentify(rs.getString("user_identify"));
                user.setUserNick(rs.getString("user_nick"));
                user.setUserTele(rs.getString("user_telenum"));
                user.setUserClassInfo(rs.getString("user_class_info"));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
        }
        return user;
    }
    public List <OrderBean> getOrderData(String username){
        OrderBean bean;
        List <OrderBean> list=new ArrayList<>();
        int count=0;
        PreparedStatement psm=null;
        Connection cn=getCon();
        ResultSet rs=null;
        String sql="select * from baoxiu where order_user_name = ?";
        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1,username);
            rs=psm.executeQuery();
            while (rs.next()){
                count++;
                bean=new OrderBean();
                InputStream in=rs.getBinaryStream("order_img");
                bean.setId(rs.getInt("order_id"));
                bean.setOrderUserName(rs.getString("order_user_name"));
                bean.setOrderImg(BitmapFactory.decodeStream(in));
                bean.setOrderUserIdentify(rs.getString("order_user_identity"));
                bean.setOrderState(rs.getInt("order_state"));
                bean.setOrderBeiZhu(rs.getString("order_text"));
                bean.setOrderDidian(rs.getString("order_didian"));
                bean.setOrderTime(rs.getTimestamp("order_time"));
                list.add(count-1,bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
        }
        return list;
    }
    public boolean InsertOrder(OrderBean bean){
        PreparedStatement psm=null;
        Connection cn=getCon();
        boolean flag=false;
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bean.getOrderImg().compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        InputStream in=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        String sql="insert into baoxiu (order_state,order_img,order_text,order_didian,order_user_identity,order_user_name) values (?,?,?,?,?,?)";

        try {
            psm=cn.prepareStatement(sql);
            psm.setInt(1,bean.getOrderState());
            try {
                psm.setBinaryStream(2,in,in.available());
            } catch (IOException e) {
                e.printStackTrace();
            }
            psm.setString(3,bean.getOrderBeiZhu());
            psm.setString(4,bean.getOrderDidian());
            psm.setString(5,bean.getOrderUserIdentify());
            psm.setString(6,bean.getOrderUserName());
            flag=psm.executeUpdate()>0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return flag;
    }
//    public int getCountNum(){
//        int flag=0;
//        PreparedStatement psm=null;
//        Connection cn=getCon();
//        ResultSet rs=null;
//        String sql="select count(order_id) from baoxiu";
//        try {
//            psm=cn.prepareStatement(sql);
//            rs=psm.executeQuery();
//            while (rs.next()){
//                flag=rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
//            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
//            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
//        }
//        return flag;
//    }
    public boolean DeleteOrderbyId(int id){
        PreparedStatement psm=null;
        Connection cn=getCon();
        boolean flag=false;
        String sql="delete from baoxiu where order_id =?";
        try {
            psm=cn.prepareStatement(sql);
            psm.setInt(1,id);
            flag=psm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
        if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
    }
        return flag;
    }
    public boolean StartOrderbyId(int id){
        PreparedStatement psm=null;
        Connection cn=getCon();
        boolean flag=false;
        String sql="update baoxiu set order_state=? where order_id= ? ";
        try {
            psm=cn.prepareStatement(sql);
            psm.setInt(1,2);
            psm.setInt(2,id);
            flag=psm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }

        return flag;
    }
    public boolean FinishOrderbyId(int id){
        PreparedStatement psm=null;
        Connection cn=getCon();
        boolean flag=false;
        String sql="update baoxiu set order_state=? where order_id= ? ";
        try {
            psm=cn.prepareStatement(sql);
            psm.setInt(1,3);
            psm.setInt(2,id);
            flag=psm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }

        return flag;
    }
    public List <OrderBean> getAllOrderData(){
        OrderBean bean;
        List <OrderBean> list=new ArrayList<>();
        int count=0;
        PreparedStatement psm=null;
        Connection cn=getCon();
        ResultSet rs=null;
        String sql="select * from baoxiu ";
        try {
            psm=cn.prepareStatement(sql);

            rs=psm.executeQuery();
            while (rs.next()){
                count++;
                bean=new OrderBean();
                bean.setOrderUserName(rs.getString("order_user_name"));
                InputStream in=rs.getBinaryStream("order_img");
                bean.setId(rs.getInt("order_id"));
                bean.setOrderUserName(rs.getString("order_user_name"));
                bean.setOrderImg(BitmapFactory.decodeStream(in));
                bean.setOrderUserIdentify(rs.getString("order_user_identity"));
                bean.setOrderState(rs.getInt("order_state"));
                bean.setOrderBeiZhu(rs.getString("order_text"));
                bean.setOrderDidian(rs.getString("order_didian"));
                bean.setOrderTime(rs.getTimestamp("order_time"));
                list.add(count-1,bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
        }
        return list;
    }
    public boolean Studentqiandao( String name){
        PreparedStatement psm=null;
        Connection cn=getCon();
        boolean flag=false;
        String sql="update qiandao set qiandao_state=1 where qiandao_user_name= ?";
        try {
            psm=cn.prepareStatement(sql);
            psm.setString(1,name);
            flag=psm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return flag;
    }
    public List<CheckInMingDan> getMingdan(){
        CheckInMingDan mingdan;
        List<CheckInMingDan> mingDans=new ArrayList<>();
        int count=0;
        PreparedStatement psm=null;
        Connection cn=getCon();
        ResultSet rs=null;
        String sql="select user_name,user_class_info,qiandao_state from user,qiandao where user.user_name=qiandao.qiandao_user_name";
        try {
            psm=cn.prepareStatement(sql);
            rs=psm.executeQuery();
            while (rs.next()){
                mingdan=new CheckInMingDan();
                count++;
                mingdan.setBanji(rs.getString("user_class_info"));
                mingdan.setName(rs.getString("user_name"));
                mingdan.setState(rs.getInt("qiandao_state"));
                mingDans.add(count-1,mingdan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (rs!=null){  try {rs.close(); } catch (SQLException e) { e.printStackTrace(); }}
        }

        return mingDans;
    }
    public boolean Initqiandao(){
        PreparedStatement psm=null;
        Connection cn=getCon();
        boolean flag=false;
        String sql="update qiandao set qiandao_state=0";
        try {
            psm=cn.prepareStatement(sql);
            flag=psm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm!=null){  try { psm.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (cn!=null){  try {cn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return flag;
    }
}
