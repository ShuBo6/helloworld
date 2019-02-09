package com.example.a81418.myapp1.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.R;

public class SetUserInfo extends AppCompatActivity {
    private View include;
    private DataManager mdatamanager=new DataManager();
    private EditText usernike;
    private EditText usertelenum;
    private EditText userclassinfo;
    private SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_setting);
        usernike=findViewById(R.id.set_usernike);
        usertelenum=findViewById(R.id.set_usertelenum);
        userclassinfo=findViewById(R.id.set_userclassinfo);
        include=findViewById(R.id.setuserinfo_headbar);
        initHead();
        sp=getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        Button button=findViewById(R.id.set_tijiao);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        UpdateUserInfoToDatabase();
                        Looper.loop();
                    }
                }).start();
            }
        });
    }
    public void initHead(){
        TextView center=include.findViewById(R.id.all_order_head_title);
        center.setText("详细资料");
        View imgBack=include.findViewById(R.id.all_order_fl_right_container);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void UpdateUserInfoToDatabase(){
        boolean flag=mdatamanager.UpdateUserInfo(usernike.getText().toString().trim()
                ,usertelenum.getText().toString().trim(),userclassinfo.getText().toString().trim()
                ,sp.getString("USER_NAME", ""));
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("USER_NICK",usernike.getText().toString().trim());
        editor.putString("USER_TELENUM",usertelenum.getText().toString().trim());
        editor.putString("USER_CLASS_INFO",userclassinfo.getText().toString().trim());
        editor.apply();
//        TextView frag=findViewById(R.id.user_nike);
//        frag.setText(usernike.getText().toString().trim());
//        TextView drawer=findViewById(R.id.nav_user_name);
//        drawer.setText(usernike.getText().toString().trim());
        if (flag){
            Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();}
            else {   Toast.makeText(this,"提交失败",Toast.LENGTH_SHORT).show();      }

    }


}
