package com.example.a81418.myapp1.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.R;

public class ResetPaswd extends Activity {
    private View include;
    private EditText InputUserName;
    private EditText InputPaswd;
    private EditText InputNewPaswd;
    private EditText ReInputNewPaswd;
    private DataManager dataManager=new DataManager();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_paswd);
//        setTitle("修改密码");
        include=findViewById(R.id.resetpswd_headbar);
        InputUserName=findViewById(R.id.reset_username);
        InputPaswd=findViewById(R.id.reset_paswd);
        InputNewPaswd=findViewById(R.id.reset_new_paswd);
        ReInputNewPaswd=findViewById(R.id.reset_new_repaswd);
        initHead();
        Button tijiao_btn=findViewById(R.id.tijiao);
        tijiao_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    resetPaswd();
                    Looper.loop();
                }
            }).start();
            }
        });
    }
    public void resetPaswd(){
        String username=InputUserName.getText().toString().trim(),
                paswd=InputPaswd.getText().toString().trim(),
                NewPaswd=InputNewPaswd.getText().toString().trim();
        boolean flag=dataManager.findUserByNameAndPwd(username,paswd);
        if (UsernameAndPaswdAndNewpaswdAndReNewPaswd()){
            if (flag){
                boolean flag1=dataManager.updateUserData(username,NewPaswd);
                if (flag1){
                    Toast.makeText(this,"密码修改成功",Toast.LENGTH_SHORT).show();
                }else {Toast.makeText(this,"密码修改失败",Toast.LENGTH_SHORT).show();}
            }else{Toast.makeText(this,"用户名或密码有误，请重试",Toast.LENGTH_SHORT).show();}

        }

    }
    public void initHead(){
        TextView center=include.findViewById(R.id.all_order_head_title);
        center.setText("修改密码");
        View imgBack=include.findViewById(R.id.all_order_fl_right_container);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public boolean UsernameAndPaswdAndNewpaswdAndReNewPaswd(){
        if (InputUserName.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
            return false;
        }else if(InputPaswd.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(InputNewPaswd.getText().toString().trim().equals("")){
            Toast.makeText(this,"请输入一个新的密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if (!InputNewPaswd.getText().toString().trim().equals(ReInputNewPaswd.getText().toString().trim())){
            Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
