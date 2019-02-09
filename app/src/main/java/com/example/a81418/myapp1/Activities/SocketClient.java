package com.example.a81418.myapp1.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.R;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient extends AppCompatActivity {
    private View include;
    private Socket s = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private boolean cont = false;
    private TextView LinkState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        boolean flag=sp.getBoolean("isconnected",false);
        setContentView(R.layout.activity_socket_client);
        include=findViewById(R.id.Sockerclient_headbar);initHead();
        String Ip="服务器ip为："+"118.24.10.164";
        TextView ShowIpText=findViewById(R.id.show_ip_text);
        LinkState=findViewById(R.id.link_state_text);
        ShowIpText.setText(Ip);
        if (flag){
            LinkState.setText("校园网络正常");
        }else{ LinkState.setText("校园网络异常或失去连接");}
//        initConn();
    }
    public void initHead(){
        TextView center=include.findViewById(R.id.all_order_head_title);
        center.setText("网络状态");
        View imgBack=include.findViewById(R.id.all_order_fl_right_container);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
//    public void initConn(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                connect();
//                if (s!=null){
//                    LinkState.setText("校园网络正常");
//                    Recvmessage();
//                    sendmessage();
//                }else {
//                    LinkState.setText("校园网络异常或失去连接");
//
//                }
//
//            }
//        }).start();
//    }
//    //连接
//    public void connect() {
//
//        try {
//            s = new Socket("118.24.10.164", 8888);// 注意不要定义成Socket
//        // s,这就成了局部变量而不是成员变量了
//            dos = new DataOutputStream(s.getOutputStream());
//            dis = new DataInputStream(s.getInputStream());
//            cont = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    //接收服务器发来的消息
//    public void  Recvmessage () {
//                while (cont) {
//                    String str = null;
//                    try {
//                        str = dis.readUTF();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//    }
//// 发送消息给服务器
//    public void sendmessage() {
//            String str = "客户端登录";
//    try {
//        dos.writeUTF(str);
//        dos.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    //断开连接
//    public void disconnect() {
//        try {
//            dos.close();
//            dis.close();
//            s.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
