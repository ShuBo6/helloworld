package com.example.a81418.myapp1.Activities;

import android.app.Activity;
import android.os.Bundle;

import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.R;


public class Register extends Activity {
    private EditText reg_username;
    private EditText reg_paswd;
    private EditText reg_repaswd;
    private View HeadBar;
    private Spinner reg_spinner;
   // private Connection con;
    private DataManager dataManager=new DataManager();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        reg_username=findViewById(R.id.reg_username);
        reg_paswd=findViewById(R.id.reg_paswd);
        reg_repaswd=findViewById(R.id.reg_repaswd);
        HeadBar=findViewById(R.id.register_bar);initHead();
        reg_spinner=findViewById(R.id.reg_spinner);
        String [] arr={"请选择身份","教工","学生","管理员"};
        ArrayAdapter<String> adapter =new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,arr);
        reg_spinner.setAdapter(adapter);

       Button register_btn=findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        register();
                        Looper.loop();
                    }
                }).start();

            }
        });
    }
    public void initHead(){
        TextView center=HeadBar.findViewById(R.id.all_order_head_title);
        center.setText("注册账号");
        View imgBack=HeadBar.findViewById(R.id.all_order_fl_right_container);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void register(){
        String reg_u=reg_username.getText().toString().trim(),
                reg_p=reg_paswd.getText().toString().trim(),
                reg_i=reg_spinner.getSelectedItem().toString().trim();
        if(UsernameAndPaswdAndRepaswdAndIdentifyValid()){
            int result=dataManager.findUserByName(reg_u);
            if (result>0){
                Toast.makeText(this,"该用户名已经存在",Toast.LENGTH_SHORT).show();
            }else{
                boolean flag= dataManager.insertUserData(reg_u,reg_p,reg_i);
                if(flag){
                    Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                }else{Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show();}
            }

        }
    }
    public boolean UsernameAndPaswdAndRepaswdAndIdentifyValid(){
        if (reg_username.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入一个新的用户名",Toast.LENGTH_SHORT).show();
            return false;
        }else if(reg_paswd.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(reg_repaswd.getText().toString().trim().equals("")){
            Toast.makeText(this,"请再次输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if (!reg_paswd.getText().toString().trim().equals(reg_repaswd.getText().toString().trim())){
            Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
            return false;
        }else if (reg_spinner.getSelectedItem().toString().trim().equals("请选择身份")){
            Toast.makeText(this,"请选择身份",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
