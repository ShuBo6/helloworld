package com.example.a81418.myapp1.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.DataManager.UserData;
import com.example.a81418.myapp1.R;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;

public class MainActivity extends Activity {

        private EditText MUsername;
        private EditText MPassword;

        private CheckBox rember_paswd_checkbox;
        private CheckBox auto_login_checkbox;
        private SharedPreferences login_sp;
        private Spinner identify_spinner;

        //private String identify;
        DataManager MdataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("登陆");
        //记住密码，自动登录，复选框
        rember_paswd_checkbox=findViewById(R.id.rember_paswd_checkbox);
        auto_login_checkbox=findViewById(R.id.auto_login_checkBox);
        //下拉身份选框
        identify_spinner=findViewById(R.id.identfiy_spinner);
        String [] arr={"请选择身份","教工","学生","管理员"};
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arr);
        identify_spinner.setAdapter(adapter);
        //identify=identify_spinner.getSelectedItem().toString();
        //登陆按钮
        Button MLogin=findViewById(R.id.login_btn);
        MLogin.setOnClickListener(mlistener);

        //用户名 密码输入框
        MUsername=findViewById(R.id.username);
        MPassword=findViewById(R.id.password);

        //底部文字 修改密码注册用户出口
        TextView reset_pswd_text=findViewById(R.id.resetpswd_text);
        reset_pswd_text.setClickable(true);
        reset_pswd_text.setOnClickListener(mlistener);

       TextView register_text=findViewById(R.id.register_text);
        //register_text.setClickable(true);
        register_text.setOnClickListener(mlistener);


        MdataManager=new DataManager();
        Thread thread=new Thread(runnable);
        thread.start();

        login_sp =this.getSharedPreferences("userInfo",MODE_PRIVATE);//getSharedPreferences("userInfo", MODE_PRIVATE);
        String name=login_sp.getString("USER_NAME", "");//接收 sp中保存的用户名
        String pwd =login_sp.getString("PASSWORD", "");//接收 sp中保存的 密码
        String ident=login_sp.getString("identify","");//接收 sp中保存的 身份信息

        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        if (choseAutoLogin){
            //auto_login_checkbox.setChecked(true);
            Intent intent = new Intent(MainActivity.this,DrawerMenu.class) ;    //切换Login Activity至User Activity
            startActivity(intent);
            finish();
        }
        else {
            //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
            if (choseRemember){
                rember_paswd_checkbox.setChecked(true);
                MUsername.setText(name);
                MPassword.setText(pwd);

                for (int i=0;i<adapter.getCount();i++){
                    if(ident.equals(adapter.getItem(i).toString().trim())){
                        identify_spinner.setSelection(i,true);
                        break;
                    }
                }
            }
//            new Thread(runnable).start();
//            try {
//                new Thread(runnable).join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }

    }

            View.OnClickListener mlistener= new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (v.getId()) {
                        case R.id.register_text:                           //登录界面的注册按钮
                            Intent intent_Login_to_Register = new Intent(MainActivity.this,Register.class) ;    //切换Login Activity至User Activity
                                    startActivity(intent_Login_to_Register);
                            break;
                        case R.id.login_btn:                              //登录界面的登录按钮
                           // Toast.makeText(MainActivity.this,"数据载入中,请耐心等待",Toast.LENGTH_SHORT).show();
                            new Thread(new Runnable()  {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    login();
                                    Looper.loop();
                                }
                            }).start();

                            break;
                case R.id.resetpswd_text:
                    Intent intent_login_to_reset =new Intent(MainActivity.this,ResetPaswd.class);
                    startActivity(intent_login_to_reset);
                    break;
//                case R.id.login_text_change_pwd:                             //登录界面的注销按钮
//                    Intent intent_Login_to_reset = new Intent(Login.this,Resetpwd.class) ;    //切换Login Activity至User Activity
//                    startActivity(intent_Login_to_reset);
//                    finish();
//                    break;
                    }

                }
            };



    public void login() {
                                          //登录按钮监听事件
        if (isUserNameAndPwdAndIdentifyValid()) {
            String userName = MUsername.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = MPassword.getText().toString().trim();
            String user_identify =identify_spinner.getSelectedItem().toString().trim();
            SharedPreferences.Editor editor =login_sp.edit();         //实例化一个可编辑对象
            boolean flag=MdataManager.findUserByNameAndPwdAndIdentify(userName,userPwd,user_identify);
            if(flag){                                             //返回1说明用户名和密码均正确
                //自动登录被勾选后的记录
                if (auto_login_checkbox.isChecked()){
                    editor.putBoolean("mAutologinCheck",true);
                }else{
                    editor.putBoolean("mAutologinCheck",false);
                }
                //勾选保存密码后 进行的操作
                if(rember_paswd_checkbox.isChecked()){
                    //保存是否记住密码
                    editor.putBoolean("mRememberCheck", true);

                }else{
                    editor.putBoolean("mRememberCheck", false);
                }
                //保存用户名和密码到userInfo
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);
                editor.putString("identify",user_identify);
                editor.putBoolean("ISLOGIN",true);
                UserData user=MdataManager.InitUserData(userName);
                Bitmap getHeadImg=user.getUserHeadImg();
                if (getHeadImg==null){
                    Bitmap  bmp= BitmapFactory.decodeResource(getResources(),R.mipmap.null_head_img);
                    getHeadImg=bmp;
                }
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    getHeadImg.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    String Imgstring=new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT));

                    editor.putString("imgStringStream",Imgstring);
                if (user.getUserNick()!=null){
                    editor.putString("USER_NICK",user.getUserNick());
                }else{
                    editor.putString("USER_NICK","");
                }

                editor.apply();

                    Intent intent = new Intent(MainActivity.this,DrawerMenu.class) ;    //切换Login Activity至User Activity
                    startActivity(intent);
                    finish();



            }else{

                Toast.makeText(this, "登陆信息有误请核对后再次登陆",Toast.LENGTH_SHORT).show();//登录失败提示

            }
        }
    }
    public boolean isUserNameAndPwdAndIdentifyValid() {
        if (MUsername.getText().toString().trim().equals("")) {

            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();

            return false;
        } else if (MPassword.getText().toString().trim().equals("")) {

            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();

            return false;
        }else if (identify_spinner.getSelectedItem().toString().trim().equals("请选择身份")){
            Toast.makeText(this, "请选择您的登陆身份", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
    }


    Runnable runnable=new Runnable() {
        @Override
        public void run() {
           Connection con= MdataManager.getCon();
           //testConnection(con);    //测试数据库连接

        }
//         void testConnection(Connection con1) {
//
//
//             try {
//                 String sql = "insert into log (bz) values ('admin')";        //登录日志中插入备注记录数据库连接时间
//                 Statement stmt = con1.createStatement();        //创建Statement
//                 stmt.execute(sql);
//             } catch (SQLException e) {
//                 e.printStackTrace();
//             }
//
//
//        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        //thread.
    }


}
